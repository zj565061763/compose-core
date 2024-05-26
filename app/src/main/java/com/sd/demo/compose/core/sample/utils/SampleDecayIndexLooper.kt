package com.sd.demo.compose.core.sample.utils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sd.demo.compose.core.logMsg
import com.sd.demo.compose.core.theme.AppTheme
import com.sd.lib.compose.core.utils.FDecayIndexLooper

private const val SIZE = 5

class SampleDecayIndexLooper : ComponentActivity() {
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
    val coroutineScope = rememberCoroutineScope()
    val looper = remember(coroutineScope) {
        FDecayIndexLooper(coroutineScope)
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ListView(
            selectedIndex = looper.currentIndex,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Button(onClick = {
                val startLoop = looper.startLoop(SIZE)
                logMsg { "click startLoop:${startLoop}" }
            }) {
                Text(text = "开始循环")
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(onClick = {
                val startDecay = looper.startDecay(3)
                logMsg { "click startDecay:${startDecay}" }
            }) {
                Text(text = "开始减速")
            }
        }
    }
}

@Composable
private fun ListView(
    modifier: Modifier = Modifier,
    selectedIndex: Int?,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(SIZE) { index ->
            Button(
                onClick = { },
                enabled = index == selectedIndex,
            ) {
                Text(text = index.toString())
            }
        }
    }
}