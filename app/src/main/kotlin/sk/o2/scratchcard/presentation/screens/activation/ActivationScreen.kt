package sk.o2.scratchcard.presentation.screens.activation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import sk.o2.scratchcard.R
import sk.o2.scratchcard.domain.model.ScratchCardState
import sk.o2.scratchcard.presentation.components.O2BackButton
import sk.o2.scratchcard.presentation.components.O2BrandedBackground
import sk.o2.scratchcard.presentation.components.O2Card
import sk.o2.scratchcard.presentation.components.O2ContentCard
import sk.o2.scratchcard.presentation.components.O2ErrorDialog
import sk.o2.scratchcard.presentation.components.O2LoadingIndicator
import sk.o2.scratchcard.presentation.components.O2PrimaryButton
import sk.o2.scratchcard.presentation.theme.O2Colors
import sk.o2.scratchcard.presentation.theme.O2Spacing
import sk.o2.scratchcard.presentation.theme.O2Theme

/**
 * Activation Screen - Activate scratch card via API.
 *
 * Features:
 * - Calls O2 API to validate scratch code (Stories 1.5, 1.6)
 * - Non-cancellable operation (continues even if user navigates away - FR014)
 * - Error categorization with appropriate messages (Story 1.7, FR017)
 * - Success feedback when activation completes
 * - Loading indicator during API call
 *
 * Error Handling:
 * - Validation Failed: android version at or below threshold
 * - Network Errors: No connection, timeout
 * - Server Errors: 4xx, 5xx responses
 * - Parsing Errors: Malformed JSON
 *
 * @param onNavigateBack Callback to return to previous screen
 * @param viewModel ActivationViewModel injected via Hilt
 */
@Composable
fun ActivationScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ActivationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cardState by viewModel.cardState.collectAsStateWithLifecycle()

    ActivationScreenContent(
        uiState = uiState,
        cardState = cardState,
        onActivateCard = { code -> viewModel.activateCard(code) },
        onRetryActivation = { code -> viewModel.retryActivation(code) },
        onClearError = { viewModel.clearError() },
        onNavigateBack = onNavigateBack,
        modifier = modifier,
    )
}

/**
 * Activation Screen content composable (stateless).
 *
 * Separated from ActivationScreen for easier testing and preview.
 * Contains only UI logic with no ViewModel dependency.
 *
 * @param uiState Current UI state (Idle/Loading/Success/Error)
 * @param cardState Current scratch card state
 * @param onActivateCard Callback to activate card with code
 * @param onRetryActivation Callback to retry activation with code
 * @param onClearError Callback to clear error state
 * @param onNavigateBack Callback to return to previous screen
 * @param modifier Optional modifier
 */
@Composable
private fun ActivationScreenContent(
    uiState: ActivationUiState,
    cardState: ScratchCardState,
    onActivateCard: (String) -> Unit,
    onRetryActivation: (String) -> Unit,
    onClearError: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Show error modal if in error state (Story 1.7)
    if (uiState is ActivationUiState.Error) {
        val errorType = uiState.errorType

        O2ErrorDialog(
            title = stringResource(errorType.titleRes),
            message = stringResource(errorType.messageRes),
            onRetry =
                if (cardState is ScratchCardState.Scratched) {
                    { onRetryActivation(cardState.code) }
                } else {
                    null
                },
            onDismiss = onClearError,
        )
    }

    O2BrandedBackground(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            O2ContentCard(
                modifier =
                    Modifier
                        .padding(bottom = O2Spacing.xxl), // Space from bottom bar
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(O2Spacing.md),
                ) {
                    // Back button at top-left
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = O2Spacing.sm),
                    ) {
                        O2BackButton(
                            onClick = onNavigateBack,
                            modifier = Modifier.align(Alignment.TopStart),
                        )
                    }
                    when (uiState) {
                        is ActivationUiState.Success -> {
                            // Success feedback (Story 1.6)
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = stringResource(R.string.msg_activation_success),
                                tint = O2Colors.Success,
                                modifier = Modifier.size(80.dp),
                            )

                            Spacer(modifier = Modifier.height(O2Spacing.lg))

                            Text(
                                text = stringResource(R.string.msg_activation_success),
                                style = MaterialTheme.typography.headlineMedium,
                                color = O2Colors.Success,
                                textAlign = TextAlign.Center,
                            )

                            Spacer(modifier = Modifier.height(O2Spacing.md))

                            if (cardState is ScratchCardState.Activated) {
                                O2Card {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = stringResource(R.string.msg_code_activated),
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )
                                        Spacer(modifier = Modifier.height(O2Spacing.sm))
                                        Text(
                                            text = (cardState as ScratchCardState.Activated).code,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textAlign = TextAlign.Center,
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(O2Spacing.xl))

                            O2PrimaryButton(
                                text = stringResource(R.string.btn_back_to_main),
                                onClick = onNavigateBack,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }

                        is ActivationUiState.Loading -> {
                            // Loading state during API call
                            O2LoadingIndicator() // Indeterminate for API call

                            Spacer(modifier = Modifier.height(O2Spacing.md))

                            Text(
                                text = stringResource(R.string.msg_activating),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                            )

                            Spacer(modifier = Modifier.height(O2Spacing.sm))

                            Text(
                                text = stringResource(R.string.msg_please_wait),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }

                        is ActivationUiState.Idle, is ActivationUiState.Error -> {
                            // Show activation UI if card is Scratched
                            when (val state = cardState) {
                                is ScratchCardState.Scratched -> {
                                    O2Card {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = stringResource(R.string.msg_activate_your_card),
                                                style = MaterialTheme.typography.headlineMedium,
                                                color = MaterialTheme.colorScheme.onSurface,
                                            )

                                            Spacer(modifier = Modifier.height(O2Spacing.md))

                                            Text(
                                                text = stringResource(R.string.msg_your_scratch_code),
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            )

                                            Spacer(modifier = Modifier.height(O2Spacing.sm))

                                            Text(
                                                text = state.code,
                                                style = MaterialTheme.typography.titleLarge,
                                                color = MaterialTheme.colorScheme.primary,
                                                textAlign = TextAlign.Center,
                                            )

                                            Spacer(modifier = Modifier.height(O2Spacing.md))

                                            Text(
                                                text = stringResource(R.string.msg_tap_to_activate),
                                                style = MaterialTheme.typography.bodyMedium,
                                                textAlign = TextAlign.Center,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(O2Spacing.xl))

                                    O2PrimaryButton(
                                        text = stringResource(R.string.btn_activate),
                                        onClick = { onActivateCard(state.code) },
                                        modifier = Modifier.fillMaxWidth(),
                                    )
                                }

                                is ScratchCardState.Activated -> {
                                    // Already activated - show message
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = stringResource(R.string.cd_already_activated),
                                        tint = O2Colors.Success,
                                        modifier = Modifier.size(64.dp),
                                    )

                                    Spacer(modifier = Modifier.height(O2Spacing.md))

                                    Text(
                                        text = stringResource(R.string.msg_card_already_activated),
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = O2Colors.Success,
                                    )

                                    Spacer(modifier = Modifier.height(O2Spacing.xl))

                                    O2PrimaryButton(
                                        text = stringResource(R.string.btn_back_to_main),
                                        onClick = onNavigateBack,
                                        modifier = Modifier.fillMaxWidth(),
                                    )
                                }

                                is ScratchCardState.Unscratched -> {
                                    // No code to activate - should not reach here via navigation
                                    Text(
                                        text = stringResource(R.string.msg_no_scratch_code),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.error,
                                    )

                                    Spacer(modifier = Modifier.height(O2Spacing.lg))

                                    O2PrimaryButton(
                                        text = stringResource(R.string.btn_back_to_main),
                                        onClick = onNavigateBack,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════════

@Preview(name = "Activation Screen - Idle", showBackground = true)
@Preview(name = "Activation Screen - Idle (Dark)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ActivationScreenIdlePreview() {
    O2Theme {
        ActivationScreenContent(
            uiState = ActivationUiState.Idle,
            cardState = ScratchCardState.Scratched("550e8400-e29b-41d4-a716-446655440000"),
            onActivateCard = {},
            onRetryActivation = {},
            onClearError = {},
            onNavigateBack = {},
        )
    }
}

@Preview(name = "Activation Screen - Loading", showBackground = true)
@Preview(name = "Activation Screen - Loading (Dark)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ActivationScreenLoadingPreview() {
    O2Theme {
        ActivationScreenContent(
            uiState = ActivationUiState.Loading,
            cardState = ScratchCardState.Scratched("550e8400-e29b-41d4-a716-446655440000"),
            onActivateCard = {},
            onRetryActivation = {},
            onClearError = {},
            onNavigateBack = {},
        )
    }
}

@Preview(name = "Activation Screen - Success", showBackground = true)
@Preview(name = "Activation Screen - Success (Dark)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ActivationScreenSuccessPreview() {
    O2Theme {
        ActivationScreenContent(
            uiState = ActivationUiState.Success,
            cardState = ScratchCardState.Activated("550e8400-e29b-41d4-a716-446655440000"),
            onActivateCard = {},
            onRetryActivation = {},
            onClearError = {},
            onNavigateBack = {},
        )
    }
}

@Preview(name = "Activation Screen - Already Activated", showBackground = true)
@Preview(
    name = "Activation Screen - Already Activated (Dark)",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun ActivationScreenAlreadyActivatedPreview() {
    O2Theme {
        ActivationScreenContent(
            uiState = ActivationUiState.Idle,
            cardState = ScratchCardState.Activated("550e8400-e29b-41d4-a716-446655440000"),
            onActivateCard = {},
            onRetryActivation = {},
            onClearError = {},
            onNavigateBack = {},
        )
    }
}
