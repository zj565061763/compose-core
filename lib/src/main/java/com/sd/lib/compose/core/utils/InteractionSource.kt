package com.sd.lib.compose.core.utils

import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * 是否触摸的状态
 */
@Composable
fun InteractionSource.fCollectIsTouchedAsState(): State<Boolean> {
    val isTouched = remember { mutableStateOf(false) }
    LaunchedEffect(this) {
        val listInteraction = mutableListOf<Interaction>()
        interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> listInteraction.add(interaction)
                is PressInteraction.Release -> listInteraction.remove(interaction.press)
                is PressInteraction.Cancel -> listInteraction.remove(interaction.press)

                is DragInteraction.Start -> listInteraction.add(interaction)
                is DragInteraction.Stop -> listInteraction.remove(interaction.start)
                is DragInteraction.Cancel -> listInteraction.remove(interaction.start)
            }
            isTouched.value = listInteraction.isNotEmpty()
        }
    }
    return isTouched
}