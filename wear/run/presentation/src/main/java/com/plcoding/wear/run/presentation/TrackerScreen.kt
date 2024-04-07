package com.plcoding.wear.run.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.FilledTonalIconButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedIconButton
import androidx.wear.compose.material3.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.plcoding.core.presentation.designsystem.ExclamationMarkIcon
import com.plcoding.core.presentation.designsystem.FinishIcon
import com.plcoding.core.presentation.designsystem.PauseIcon
import com.plcoding.core.presentation.designsystem.StartIcon
import com.plcoding.core.presentation.designsystem_wear.RuniqueTheme
import com.plcoding.core.presentation.ui.formatted
import com.plcoding.core.presentation.ui.toFormattedHeartRate
import com.plcoding.core.presentation.ui.toFormattedKm
import com.plcoding.wear.run.presentation.components.RunDataCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrackerScreenRoot(
    viewModel: TrackerViewModel = koinViewModel(),
) {
    TrackerScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun TrackerScreen(
    state: TrackerState,
    onAction: (TrackerAction) -> Unit
) {
    if (state.isConnectedPhoneNearby) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                RunDataCard(
                    title = stringResource(id = R.string.heart_rate),
                    value = if (state.canTrackHeartRate) {
                        state.heartRate.toFormattedHeartRate()
                    } else {
                        stringResource(id = R.string.unsupported)
                    },
                    valueTextColor = if (state.canTrackHeartRate) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                RunDataCard(
                    title = stringResource(id = R.string.distance),
                    value = (state.distanceMeters / 1000.0).toFormattedKm(),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.elapsedDuration.formatted(),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.isTrackable) {
                    ToggleRunButton(
                        isRunActive = state.isRunActive,
                        onClick = {
                            onAction(TrackerAction.OnToggleRunClick)
                        }
                    )
                    if (!state.isRunActive && state.hasStartedRunning) {
                        FilledTonalIconButton(
                            onClick = {
                                onAction(TrackerAction.OnFinishRunClick)
                            },
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onBackground
                            )
                        ) {
                            Icon(
                                imageVector = FinishIcon,
                                contentDescription = stringResource(id = R.string.finish_run)
                            )
                        }
                    }
                } else {
                    Text(
                        text = stringResource(id = R.string.open_active_run_screen),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ExclamationMarkIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.connect_your_phone),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ToggleRunButton(
    isRunActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedIconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        if (isRunActive) {
            Icon(
                imageVector = PauseIcon,
                contentDescription = stringResource(id = R.string.pause_run),
                tint = MaterialTheme.colorScheme.onBackground
            )
        } else {
            Icon(
                imageVector = StartIcon,
                contentDescription = stringResource(id = R.string.start_run),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@WearPreviewDevices
@Composable
private fun TrackerScreenPreview() {
    RuniqueTheme {
        TrackerScreen(
            state = TrackerState(
                isConnectedPhoneNearby = true,
                isRunActive = false,
                isTrackable = true,
                hasStartedRunning = true,
                canTrackHeartRate = true,
                heartRate = 150
            ),
            onAction = {}
        )
    }
}