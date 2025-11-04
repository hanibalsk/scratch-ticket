package sk.o2.scratchcard.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.o2.scratchcard.presentation.theme.O2Colors
import sk.o2.scratchcard.presentation.theme.O2Spacing
import sk.o2.scratchcard.presentation.theme.O2Theme

/**
 * O2 Error Dialog - Modal for error display with retry capability.
 *
 * Visual Specs (FR017):
 * - Title color: O2 Error Red (#E53935)
 * - Icon: Warning icon in Error Red
 * - Corner radius: 24dp (large shape for dialogs)
 * - Primary action: "Retry" button (O2 Blue) if retryable
 * - Secondary action: "Dismiss" text button (O2 Blue text)
 *
 * Error Types:
 * - Validation Failed: "Activation Failed" title
 * - Network Error: "Connection Error" title
 * - Server/Parsing Error: "Service Error" title
 *
 * @param title Error modal title
 * @param message User-friendly error description
 * @param onRetry Optional retry callback (null for non-retryable errors)
 * @param onDismiss Callback when modal is dismissed
 * @param modifier Optional modifier
 */
@Composable
fun O2ErrorDialog(
    title: String,
    message: String,
    onRetry: (() -> Unit)? = null,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = O2Colors.Error,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = O2Colors.Error,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            if (onRetry != null) {
                O2PrimaryButton(
                    text = "Retry",
                    onClick = {
                        onRetry()
                        onDismiss()
                    }
                )
            }
        },
        dismissButton = {
            if (onRetry != null) {
                // Show "Dismiss" if retry is available
                TextButton(onClick = onDismiss) {
                    Text(
                        text = "Dismiss",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                // Show "OK" if no retry available
                O2PrimaryButton(
                    text = "OK",
                    onClick = onDismiss
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(24.dp) // Large shape for dialogs
    )
}

// ═══════════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════════

@Preview(name = "Error Dialog - Validation Failed", showBackground = true)
@Composable
fun O2ErrorDialogValidationPreview() {
    O2Theme {
        O2ErrorDialog(
            title = "Activation Failed",
            message = "This scratch card could not be activated. Please try again or contact support.",
            onRetry = {},
            onDismiss = {}
        )
    }
}

@Preview(name = "Error Dialog - Network Error", showBackground = true)
@Composable
fun O2ErrorDialogNetworkPreview() {
    O2Theme {
        O2ErrorDialog(
            title = "Connection Error",
            message = "No internet connection",
            onRetry = {},
            onDismiss = {}
        )
    }
}

@Preview(name = "Error Dialog - Server Error", showBackground = true)
@Composable
fun O2ErrorDialogServerPreview() {
    O2Theme {
        O2ErrorDialog(
            title = "Service Error",
            message = "Service temporarily unavailable. Please try again later.",
            onRetry = {},
            onDismiss = {}
        )
    }
}

@Preview(name = "Error Dialog - Dark Mode", showBackground = true)
@Composable
fun O2ErrorDialogDarkPreview() {
    O2Theme(darkTheme = true) {
        O2ErrorDialog(
            title = "Connection Error",
            message = "Request timed out",
            onRetry = {},
            onDismiss = {}
        )
    }
}

@Preview(name = "Error Dialog - No Retry", showBackground = true)
@Composable
fun O2ErrorDialogNoRetryPreview() {
    O2Theme {
        O2ErrorDialog(
            title = "Error",
            message = "An unexpected error occurred.",
            onRetry = null, // No retry option
            onDismiss = {}
        )
    }
}
