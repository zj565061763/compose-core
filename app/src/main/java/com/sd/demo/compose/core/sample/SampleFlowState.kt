package com.sd.demo.compose.core.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sd.demo.compose.core.theme.AppTheme
import com.sd.lib.compose.core.fFlowStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class SampleFlowState : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Content()
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
) {
    val countdown by fFlowStateWithLifecycle(100) { scope ->
        var seconds = 100
        flow {
            while (seconds > 0) {
                delay(1_000)
                seconds--
                emit(seconds)
            }
        }.stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = seconds,
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = countdown.toString())
    }
}