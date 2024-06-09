package com.sd.demo.compose.core.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.sd.demo.compose.core.logMsg
import com.sd.demo.compose.core.theme.AppTheme
import com.sd.lib.compose.core.fAppLifecycleScope
import com.sd.lib.compose.core.fAwait
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SampleLifecycle : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Content()
            }
        }

        fAppLifecycleScope.launch {
            logMsg { "fAppLifecycleScope launched" }
        }
    }
}

@Composable
private fun Content() {
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        logMsg { "ON_START" }
    }
    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        logMsg { "ON_STOP" }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        while (true) {
            lifecycleOwner.fAwait()
            logMsg { "lifecycle" }
            delay(1_000)
        }
    }
}