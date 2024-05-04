package com.sd.demo.compose.core.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.lifecycleScope
import com.sd.demo.compose.core.logMsg
import com.sd.demo.compose.core.theme.AppTheme
import com.sd.lib.compose.core.fAppIsStartedFlow
import kotlinx.coroutines.launch

class SampleLifecycle : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Content()
            }
        }

        lifecycleScope.launch {
            fAppIsStartedFlow.collect {
                logMsg { "App started:$it" }
            }
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
}