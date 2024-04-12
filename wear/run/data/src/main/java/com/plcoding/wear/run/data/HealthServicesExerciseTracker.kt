package com.plcoding.wear.run.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.health.services.client.ExerciseUpdateCallback
import androidx.health.services.client.HealthServices
import androidx.health.services.client.HealthServicesException
import androidx.health.services.client.clearUpdateCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.ExerciseConfig
import androidx.health.services.client.data.ExerciseLapSummary
import androidx.health.services.client.data.ExerciseTrackedStatus
import androidx.health.services.client.data.ExerciseType
import androidx.health.services.client.data.ExerciseUpdate
import androidx.health.services.client.data.WarmUpConfig
import androidx.health.services.client.endExercise
import androidx.health.services.client.getCapabilities
import androidx.health.services.client.getCurrentExerciseInfo
import androidx.health.services.client.pauseExercise
import androidx.health.services.client.prepareExercise
import androidx.health.services.client.resumeExercise
import androidx.health.services.client.startExercise
import com.plcoding.core.domain.util.EmptyResult
import com.plcoding.core.domain.util.Result
import com.plcoding.wear.run.domain.ExerciseError
import com.plcoding.wear.run.domain.ExerciseTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt

class HealthServicesExerciseTracker(
    private val context: Context
): ExerciseTracker {

    private val client = HealthServices.getClient(context).exerciseClient

    override val heartRate: Flow<Int>
        get() = callbackFlow {
            val callback = object : ExerciseUpdateCallback {
                override fun onAvailabilityChanged(
                    dataType: DataType<*, *>,
                    availability: Availability
                ) = Unit

                override fun onExerciseUpdateReceived(update: ExerciseUpdate) {
                    val heartRates = update.latestMetrics.getData(DataType.HEART_RATE_BPM)
                    val currentHeartRate = heartRates.firstOrNull()?.value

                    currentHeartRate?.let {
                        trySend(currentHeartRate.roundToInt())
                    }
                }

                override fun onLapSummaryReceived(lapSummary: ExerciseLapSummary) = Unit

                override fun onRegistered() = Unit

                override fun onRegistrationFailed(throwable: Throwable) {
                    if(BuildConfig.DEBUG) {
                        throwable.printStackTrace()
                    }
                }
            }

            client.setUpdateCallback(callback)

            awaitClose {
                runBlocking {
                    client.clearUpdateCallback(callback)
                }
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun isHeartRateTrackingSupported(): Boolean {
        return hasBodySensorsPermission() && runCatching {
            val capabilities = client.getCapabilities()
            val supportedDataTypes = capabilities
                .typeToCapabilities[ExerciseType.RUNNING]
                ?.supportedDataTypes ?: setOf()

            DataType.HEART_RATE_BPM in supportedDataTypes
        }.getOrDefault(false)
    }

    override suspend fun prepareExercise(): EmptyResult<ExerciseError> {
        if(!isHeartRateTrackingSupported()) {
            return Result.Error(ExerciseError.TRACKING_NOT_SUPPORTED)
        }

        val result = getActiveExerciseInfo()
        if(result is Result.Error) {
            return result
        }

        val config = WarmUpConfig(
            exerciseType = ExerciseType.RUNNING,
            dataTypes = setOf(DataType.HEART_RATE_BPM)
        )
        client.prepareExercise(config)

        return Result.Success(Unit)
    }

    override suspend fun startExercise(): EmptyResult<ExerciseError> {
        if(!isHeartRateTrackingSupported()) {
            return Result.Error(ExerciseError.TRACKING_NOT_SUPPORTED)
        }

        val result = getActiveExerciseInfo()
        if(result is Result.Error) {
            return result
        }

        val config = ExerciseConfig.builder(ExerciseType.RUNNING)
            .setDataTypes(setOf(DataType.HEART_RATE_BPM))
            .setIsAutoPauseAndResumeEnabled(false)
            .build()
        client.startExercise(config)

        return Result.Success(Unit)
    }

    override suspend fun resumeExercise(): EmptyResult<ExerciseError> {
        if(!isHeartRateTrackingSupported()) {
            return Result.Error(ExerciseError.TRACKING_NOT_SUPPORTED)
        }

        val result = getActiveExerciseInfo()
        if(result is Result.Error && result.error == ExerciseError.ONGOING_OTHER_EXERCISE) {
            return result
        }

        return try {
            client.resumeExercise()
            Result.Success(Unit)
        } catch (e: HealthServicesException) {
            Result.Error(ExerciseError.EXERCISE_ALREADY_ENDED)
        }
    }

    override suspend fun pauseExercise(): EmptyResult<ExerciseError> {
        if(!isHeartRateTrackingSupported()) {
            return Result.Error(ExerciseError.TRACKING_NOT_SUPPORTED)
        }

        val result = getActiveExerciseInfo()
        if(result is Result.Error && result.error == ExerciseError.ONGOING_OTHER_EXERCISE) {
            return result
        }

        return try {
            client.pauseExercise()
            Result.Success(Unit)
        } catch (e: HealthServicesException) {
            Result.Error(ExerciseError.EXERCISE_ALREADY_ENDED)
        }
    }

    override suspend fun stopExercise(): EmptyResult<ExerciseError> {
        if(!isHeartRateTrackingSupported()) {
            return Result.Error(ExerciseError.TRACKING_NOT_SUPPORTED)
        }

        val result = getActiveExerciseInfo()
        if(result is Result.Error && result.error == ExerciseError.ONGOING_OTHER_EXERCISE) {
            return result
        }

        return try {
            client.endExercise()
            Result.Success(Unit)
        } catch (e: HealthServicesException) {
            Result.Error(ExerciseError.EXERCISE_ALREADY_ENDED)
        }
    }

    @SuppressLint("RestrictedApi")
    private suspend fun getActiveExerciseInfo(): EmptyResult<ExerciseError> {
        val info = client.getCurrentExerciseInfo()
        return when(info.exerciseTrackedStatus) {
            ExerciseTrackedStatus.NO_EXERCISE_IN_PROGRESS -> Result.Success(Unit)
            ExerciseTrackedStatus.OWNED_EXERCISE_IN_PROGRESS -> {
                Result.Error(ExerciseError.ONGOING_OWN_EXERCISE)
            }
            ExerciseTrackedStatus.OTHER_APP_IN_PROGRESS -> {
                Result.Error(ExerciseError.ONGOING_OTHER_EXERCISE)
            }
            else -> {
                Result.Error(ExerciseError.UNKNOWN)
            }
        }
    }

    private fun hasBodySensorsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BODY_SENSORS
        ) == PackageManager.PERMISSION_GRANTED
    }
}