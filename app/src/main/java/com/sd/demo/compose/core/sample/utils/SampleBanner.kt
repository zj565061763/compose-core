package com.sd.demo.compose.core.sample.utils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sd.demo.compose.core.theme.AppTheme
import com.sd.lib.compose.core.utils.fLoopPlay
import java.util.UUID

class SampleBanner : ComponentActivity() {
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
    val list = remember { mutableStateListOf(UUID.randomUUID().toString()) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = list.size.toString())

        Button(onClick = {
            list.add(UUID.randomUUID().toString())
        }) {
            Text(text = "Add")
        }

        Button(onClick = {
            if (list.isNotEmpty()) {
                list.removeLastOrNull()
            }
        }) {
            Text(text = "Remove")
        }

        BannerView(
            list = list,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BannerView(
    modifier: Modifier = Modifier,
    list: List<String>,
) {
    val state = rememberPagerState { list.size }
    state.fLoopPlay()

    HorizontalPager(
        modifier = modifier,
        state = state,
    ) { index ->
        list.getOrNull(index)?.let { item ->
            ItemView(
                index = index,
                item = item,
            )
        }
    }
}

@Composable
private fun ItemView(
    modifier: Modifier = Modifier,
    index: Int,
    item: String,
) {
    val color = if (index % 2 == 0) Color.Red else Color.Blue
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(color),
    ) {
        Text(text = "$index $item")
    }
}