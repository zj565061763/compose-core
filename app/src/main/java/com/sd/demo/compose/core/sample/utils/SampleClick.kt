package com.sd.demo.compose.core.sample.utils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sd.demo.compose.core.logMsg
import com.sd.demo.compose.core.theme.AppTheme
import com.sd.lib.compose.core.utils.fClick
import com.sd.lib.compose.core.utils.fCombinedClick

class SampleClick : ComponentActivity() {
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
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ClickView()
        CombinedClickView()
    }
}

@Composable
private fun ClickView(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "fClick",
            modifier = Modifier
                .size(100.dp)
                .border(1.dp, Color.Red)
                .fClick { logMsg { "fClick" } }
        )

        Text(
            text = "clickable",
            modifier = Modifier
                .size(100.dp)
                .border(1.dp, Color.Red)
                .clickable { logMsg { "clickable" } }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CombinedClickView(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "fCombinedClick",
            modifier = Modifier
                .size(100.dp)
                .border(1.dp, Color.Red)
                .fCombinedClick(
                    onLongClick = { logMsg { "fCombinedClick onLongClick" } },
                    onDoubleClick = { logMsg { "fCombinedClick onDoubleClick" } },
                ) {
                    logMsg { "fCombinedClick onClick" }
                }
        )

        Text(
            text = "combinedClickable",
            modifier = Modifier
                .size(100.dp)
                .border(1.dp, Color.Red)
                .combinedClickable(
                    onLongClick = { logMsg { "combinedClickable onLongClick" } },
                    onDoubleClick = { logMsg { "combinedClickable onDoubleClick" } },
                ) {
                    logMsg { "combinedClickable onClick" }
                }
        )
    }
}