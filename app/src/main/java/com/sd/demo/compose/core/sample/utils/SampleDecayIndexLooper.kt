package com.sd.demo.compose.core.sample.utils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sd.demo.compose.core.theme.AppTheme
import com.sd.lib.compose.core.utils.FDecayIndexLooper
import kotlinx.coroutines.launch

private const val COUNT = 5

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
    val looper = remember { FDecayIndexLooper() }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ListView(
            selectedIndex = looper.currentIndex,
            modifier = Modifier.weight(1f)
        )

        Row {
            Button(onClick = {
                coroutineScope.launch { looper.startLoop(5) }
            }) {
                Text(text = "开始循环")
            }

            Button(onClick = {
                coroutineScope.launch { looper.startDecay(2) }
            }) {
                Text(text = "开始减速")
            }
        }
    }
}

@Composable
private fun ListView(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(COUNT) { index ->
            Button(
                onClick = { },
                enabled = index == selectedIndex,
            ) {
                Text(text = index.toString())
            }
        }
    }
}