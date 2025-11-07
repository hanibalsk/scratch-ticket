package sk.o2.scratchcard.presentation.screens.scratch

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import sk.o2.scratchcard.R
import sk.o2.scratchcard.domain.model.ScratchCardState
import sk.o2.scratchcard.presentation.components.O2BackButton
import sk.o2.scratchcard.presentation.components.O2BrandedBackground
import sk.o2.scratchcard.presentation.components.O2Card
import sk.o2.scratchcard.presentation.components.O2ContentCard
import sk.o2.scratchcard.presentation.components.O2LoadingIndicator
import sk.o2.scratchcard.presentation.components.O2PrimaryButton
import sk.o2.scratchcard.presentation.theme.O2Spacing
import sk.o2.scratchcard.presentation.theme.O2Theme

/**
 * Scratch Screen - Perform scratch operation.
 *
 * Features:
 * - Initiates 2-second scratch operation with progress indicator
 * - Displays revealed UUID code after completion
 * - Handles back navigation cancellation automatically via viewModelScope
 *
 * Implementation Details:
 * - Uses viewModelScope for cancellable operation (FR010)
 * - Progress updates every 20ms for smooth animation
 * - BackHandler allows navigation away during scratch
 * - Operation cancels automatically if user navigates back
 *
 * @param onNavigateBack Callback to return to previous screen
 * @param viewModel ViewModel injected via Hilt
 */
@Composable
fun ScratchScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScratchViewModel = hiltViewModel(),
) {
    val cardState by viewModel.cardState.collectAsStateWithLifecycle()
    val isScratching by viewModel.isScratching.collectAsStateWithLifecycle()
    val scratchProgress by viewModel.scratchProgress.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    // Allow back navigation during scratch - operation will cancel
    BackHandler {
        onNavigateBack()
    }

    ScratchScreenContent(
        cardState = cardState,
        isScratching = isScratching,
        scratchProgress = scratchProgress,
        errorMessage = errorMessage,
        onStartScratch = { viewModel.startScratch() },
        onNavigateBack = onNavigateBack,
        modifier = modifier,
    )
}

/**
 * Scratch Screen content composable (stateless).
 *
 * Separated from ScratchScreen for easier testing and preview.
 * Contains only UI logic with no ViewModel dependency.
 *
 * @param cardState Current scratch card state
 * @param isScratching Whether scratch operation is in progress
 * @param scratchProgress Progress value from 0.0 to 1.0
 * @param errorMessage Error message to display, if any
 * @param onStartScratch Callback to start scratch operation
 * @param onNavigateBack Callback to return to previous screen
 * @param modifier Optional modifier
 */
@Composable
private fun ScratchScreenContent(
    cardState: ScratchCardState,
    isScratching: Boolean,
    scratchProgress: Float,
    errorMessage: String?,
    onStartScratch: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
                    when {
                        // Show loading with progress during scratch
                        isScratching -> {
                            O2LoadingIndicator(progress = scratchProgress)

                            Spacer(modifier = Modifier.height(O2Spacing.md))

                            Text(
                                text =
                                    stringResource(
                                        R.string.msg_scratching_progress,
                                        (scratchProgress * 100).toInt(),
                                    ),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }

                        // Show revealed code after scratch
                        cardState is ScratchCardState.Scratched -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = stringResource(R.string.cd_scratch_complete),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(64.dp),
                            )

                            Spacer(modifier = Modifier.height(O2Spacing.md))

                            Text(
                                text = stringResource(R.string.msg_code_revealed),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )

                            Spacer(modifier = Modifier.height(O2Spacing.lg))

                            O2Card {
                                Text(
                                    text = (cardState as ScratchCardState.Scratched).code,
                                    style = MaterialTheme.typography.headlineMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }

                            Spacer(modifier = Modifier.height(O2Spacing.xl))

                            O2PrimaryButton(
                                text = stringResource(R.string.btn_back_to_main),
                                onClick = onNavigateBack,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }

                        // Show scratch button (initial state)
                        else -> {
                            O2Card {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(O2Spacing.lg),
                                ) {
                                    Text(
                                        text = stringResource(R.string.msg_scratch_your_card),
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )

                                    Spacer(modifier = Modifier.height(O2Spacing.md))

                                    Text(
                                        text = stringResource(R.string.msg_tap_to_reveal_code),
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(O2Spacing.xl))

                            O2PrimaryButton(
                                text = stringResource(R.string.btn_scratch_card),
                                onClick = onStartScratch,
                                modifier = Modifier.fillMaxWidth(),
                            )

                            // Show error message if scratch failed
                            errorMessage?.let { error ->
                                Spacer(modifier = Modifier.height(O2Spacing.md))
                                Text(
                                    text = error,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error,
                                )
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

@Preview(name = "Scratch Screen - Initial", showBackground = true)
@Preview(name = "Scratch Screen - Initial (Dark)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ScratchScreenInitialPreview() {
    O2Theme {
        ScratchScreenContent(
            cardState = ScratchCardState.Unscratched,
            isScratching = false,
            scratchProgress = 0f,
            errorMessage = null,
            onStartScratch = {},
            onNavigateBack = {},
        )
    }
}

@Preview(name = "Scratch Screen - Scratching", showBackground = true)
@Preview(name = "Scratch Screen - Scratching (Dark)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ScratchScreenScratchingPreview() {
    O2Theme {
        ScratchScreenContent(
            cardState = ScratchCardState.Unscratched,
            isScratching = true,
            scratchProgress = 0.65f,
            errorMessage = null,
            onStartScratch = {},
            onNavigateBack = {},
        )
    }
}

@Preview(name = "Scratch Screen - Revealed", showBackground = true)
@Preview(name = "Scratch Screen - Revealed (Dark)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ScratchScreenRevealedPreview() {
    O2Theme {
        ScratchScreenContent(
            cardState = ScratchCardState.Scratched("550e8400-e29b-41d4-a716-446655440000"),
            isScratching = false,
            scratchProgress = 1f,
            errorMessage = null,
            onStartScratch = {},
            onNavigateBack = {},
        )
    }
}
