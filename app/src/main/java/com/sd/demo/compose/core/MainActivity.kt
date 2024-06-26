package com.sd.demo.compose.core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sd.demo.compose.core.sample.SampleActive
import com.sd.demo.compose.core.sample.SampleFlowState
import com.sd.demo.compose.core.sample.SampleLifecycle
import com.sd.demo.compose.core.sample.SampleTabContainer
import com.sd.demo.compose.core.sample.SampleViewModelScope
import com.sd.demo.compose.core.sample.utils.SampleAnnotatedTarget
import com.sd.demo.compose.core.sample.utils.SampleBanner
import com.sd.demo.compose.core.sample.utils.SampleCarousel
import com.sd.demo.compose.core.sample.utils.SampleClick
import com.sd.demo.compose.core.sample.utils.SampleConstrainLayout
import com.sd.demo.compose.core.sample.utils.SampleDecayIndexLooper
import com.sd.demo.compose.core.sample.utils.SampleFlicker
import com.sd.demo.compose.core.sample.utils.SamplePager
import com.sd.demo.compose.core.sample.utils.SampleString
import com.sd.demo.compose.core.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Content(
                    listActivity = listOf(
                        SampleActive::class.java,
                        SampleFlowState::class.java,
                        SampleLifecycle::class.java,
                        SampleTabContainer::class.java,
                        SampleViewModelScope::class.java,

                        SampleAnnotatedTarget::class.java,
                        SampleBanner::class.java,
                        SampleCarousel::class.java,
                        SampleClick::class.java,
                        SampleConstrainLayout::class.java,
                        SampleDecayIndexLooper::class.java,
                        SampleFlicker::class.java,
                        SamplePager::class.java,
                        SampleString::class.java,
                    ),
                    onClickActivity = {
                        startActivity(Intent(this, it))
                    },
                )
            }
        }
    }
}

@Composable
private fun Content(
    listActivity: List<Class<out Activity>>,
    onClickActivity: (Class<out Activity>) -> Unit,
) {
    val onClickActivityUpdated by rememberUpdatedState(onClickActivity)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(
            items = listActivity,
            key = { it },
        ) { item ->
            Button(
                onClick = { onClickActivityUpdated(item) }
            ) {
                Text(text = item.simpleName)
            }
        }
    }
}

inline fun logMsg(block: () -> Any?) {
    Log.i("compose-core-demo", block().toString())
}