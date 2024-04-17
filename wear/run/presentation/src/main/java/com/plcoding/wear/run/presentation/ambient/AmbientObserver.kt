package com.plcoding.wear.run.presentation.ambient

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.wear.ambient.AmbientLifecycleObserver

@Composable
fun AmbientObserver(
    onEnterAmbient: (AmbientLifecycleObserver.AmbientDetails) -> Unit,
    onExitAmbient: () -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(key1 = lifecycle) {
        val callback = object : AmbientLifecycleObserver.AmbientLifecycleCallback {
            override fun onEnterAmbient(ambientDetails: AmbientLifecycleObserver.AmbientDetails) {
                super.onEnterAmbient(ambientDetails)
                onEnterAmbient(ambientDetails)
            }

            override fun onExitAmbient() {
                super.onExitAmbient()
                onExitAmbient()
            }
        }
        val observer = AmbientLifecycleObserver(context as ComponentActivity, callback)

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}