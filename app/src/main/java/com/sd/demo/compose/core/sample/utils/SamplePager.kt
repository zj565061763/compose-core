package com.sd.demo.compose.core.sample.utils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sd.demo.compose.core.logMsg
import com.sd.demo.compose.core.theme.AppTheme
import com.sd.lib.compose.core.utils.fAnimateScrollToPage
import com.sd.lib.compose.core.utils.fCurrentPage
import com.sd.lib.compose.core.utils.fSettledPage

class SamplePager : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Content()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
) {
    val state = rememberPagerState {
        10
    }

    var index by remember { mutableIntStateOf(0) }

    state.fAnimateScrollToPage(page = index)

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ButtonsRow(count = 10) {
            index = it
        }

        PagerView(
            modifier = Modifier.weight(1f),
            state = state,
        )
    }
}

@Composable
private fun ButtonsRow(
    modifier: Modifier = Modifier,
    count: Int,
    onClick: (Int) -> Unit,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(count) { index ->
            Button(onClick = { onClick(index) }) {
                Text(text = index.toString())
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PagerView(
    modifier: Modifier = Modifier,
    state: PagerState,
) {
    state.fCurrentPage {
        logMsg { "fCurrentPage:$it" }
    }

    state.fSettledPage {
        logMsg { "fSettledPage:$it" }
    }

    HorizontalPager(
        state = state,
        modifier = modifier.fillMaxSize(),
    ) { index ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = index.toString())
        }
    }
}