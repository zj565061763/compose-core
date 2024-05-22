package com.sd.lib.compose.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy

@Composable
fun FDialog(
    dismissOnBackPress: Boolean = false,
    dismissOnClickOutside: Boolean = false,
    securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
    usePlatformDefaultWidth: Boolean = false,
    decorFitsSystemWindows: Boolean = true,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            securePolicy = securePolicy,
            usePlatformDefaultWidth = usePlatformDefaultWidth,
            decorFitsSystemWindows = decorFitsSystemWindows,
        ),
        content = content,
    )
}