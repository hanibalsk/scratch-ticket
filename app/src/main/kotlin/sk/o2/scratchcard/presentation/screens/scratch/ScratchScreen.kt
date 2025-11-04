package sk.o2.scratchcard.presentation.screens.scratch

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import sk.o2.scratchcard.domain.model.ScratchCardState
import sk.o2.scratchcard.presentation.components.O2Card
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
    viewModel: ScratchViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val cardState by viewModel.cardState.collectAsState()
    val isScratching by viewModel.isScratching.collectAsState()
    val scratchProgress by viewModel.scratchProgress.collectAsState()
    val revealedCode by viewModel.revealedCode.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Allow back navigation during scratch - operation will cancel
    BackHandler {
        onNavigateBack()
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(O2Spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        when {
            // Show loading with progress during scratch
            isScratching -> {
                O2LoadingIndicator(progress = scratchProgress)

                Spacer(modifier = Modifier.height(O2Spacing.md))

                Text(
                    text = "Scratching... ${(scratchProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            // Show revealed code after scratch
            cardState is ScratchCardState.Scratched -> {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Scratch complete",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp),
                )

                Spacer(modifier = Modifier.height(O2Spacing.md))

                Text(
                    text = "Code Revealed!",
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
                    text = "Back to Main",
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
                            text = "Scratch Your Card",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        Spacer(modifier = Modifier.height(O2Spacing.md))

                        Text(
                            text = "Tap the button below to reveal your unique code",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(O2Spacing.xl))

                O2PrimaryButton(
                    text = "Scratch Card",
                    onClick = { viewModel.startScratch() },
                    enabled = !isScratching,
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

@Preview(showBackground = true)
@Composable
fun ScratchScreenPreview() {
    O2Theme {
        ScratchScreen(onNavigateBack = {})
    }
}
