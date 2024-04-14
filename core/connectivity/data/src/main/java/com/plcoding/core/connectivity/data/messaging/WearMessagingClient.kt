package com.plcoding.core.connectivity.data.messaging

import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.plcoding.core.connectivity.domain.messaging.MessagingAction
import com.plcoding.core.connectivity.domain.messaging.MessagingClient
import com.plcoding.core.connectivity.domain.messaging.MessagingError
import com.plcoding.core.domain.util.EmptyResult
import com.plcoding.core.domain.util.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WearMessagingClient(
    context: Context
) : MessagingClient {

    private val client = Wearable.getMessageClient(context)

    private val messageQueue = mutableListOf<MessagingAction>()
    private var connectedNodeId: String? = null

    override fun connectToNode(nodeId: String): Flow<MessagingAction> {
        connectedNodeId = nodeId

        return callbackFlow {
            val listener: (MessageEvent) -> Unit = { event ->
                if(event.path.startsWith(BASE_PATH_MESSAGING_ACTION)) {
                    val json = event.data.decodeToString()
                    val action = Json.decodeFromString<MessagingActionDto>(json)
                    trySend(action.toMessagingAction())
                }
            }

            client.addListener(listener)

            messageQueue.forEach {
                sendOrQueueAction(it)
            }
            messageQueue.clear()

            awaitClose {
                client.removeListener(listener)
            }
        }
    }

    override suspend fun sendOrQueueAction(action: MessagingAction): EmptyResult<MessagingError> {
        return connectedNodeId?.let { id ->
            try {
                val json = Json.encodeToString(action.toMessagingActionDto())
                client.sendMessage(id, BASE_PATH_MESSAGING_ACTION, json.encodeToByteArray()).await()
                Result.Success(Unit)
            } catch (e: ApiException) {
                Result.Error(
                    if(e.status.isInterrupted) {
                        MessagingError.CONNECTION_INTERRUPTED
                    } else MessagingError.UNKNOWN
                )
            }
        } ?: run {
            messageQueue.add(action)
            Result.Error(MessagingError.DISCONNECTED)
        }
    }

    companion object {
        private const val BASE_PATH_MESSAGING_ACTION = "runique/messaging_action"
    }
}