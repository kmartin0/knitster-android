package nl.kmartin.knitster.feature.projectdetail

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailLastSaved
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailNotes
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailRowCounter
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailTitleSection
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailTopAppBar
import nl.kmartin.knitster.theme.KnitsterDimensions
import java.time.Instant

/**
 * Displays the project detail screen and connects it to the [ProjectDetailViewModel].
 *
 * @param modifier Modifier to be applied to the root layout.
 * @param viewModel ViewModel providing the screen state and handling user interactions.
 * @param onNavigateBack Called when the screen should navigate back.
 */
@Composable
fun ProjectDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ProjectDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Consume the one-time navigation event.
    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            onNavigateBack()
            viewModel.onNavigateBackHandled()
        }
    }

    ProjectDetailContent(
        modifier = modifier,
        uiState = uiState,
        onBackClick = viewModel::onBackClicked,
        onIncrementClick = viewModel::onIncrementRowCounter,
        onDecrementClick = viewModel::onDecrementRowCounter,
        projectNameState = viewModel.projectNameState,
        projectNotesState = viewModel.projectNotesState,
    )
}

/**
 * Displays the content of the project detail screen.
 *
 * @param modifier Modifier to be applied to the root layout.
 * @param uiState Current UI state of the screen.
 * @param onBackClick Called when the back button is clicked.
 * @param onIncrementClick Called when the row counter is incremented.
 * @param onDecrementClick Called when the row counter is decremented.
 * @param projectNameState State backing the project name text field.
 * @param projectNotesState State backing the project notes text field.
 */
@Composable
fun ProjectDetailContent(
    modifier: Modifier = Modifier,
    uiState: ProjectDetailUiState,
    onBackClick: () -> Unit,
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit,
    projectNameState: TextFieldState,
    projectNotesState: TextFieldState
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        modifier = modifier
            .fillMaxSize()
            // Clear focus when tapping outside a text field to dismiss the keyboard.
            .pointerInput(focusManager) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            },
        topBar = {
            ProjectDetailTopAppBar(
                onBackClick = onBackClick,
                isLoading = uiState.isLoadingProject
            )
        }
    ) { innerPadding ->
        if (!uiState.isLoadingProject && uiState.project != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(all = KnitsterDimensions.ScreenPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProjectDetailTitleSection(
                    projectNameState = projectNameState,
                    onProjectIconClick = {/* TODO: Open bottom sheet with icon picker */ }
                )
                ProjectDetailNotes(
                    projectNotesState = projectNotesState,
                    modifier = Modifier.weight(0.9f)
                )
                Spacer(
                    modifier = Modifier.weight(0.05f)
                )
                ProjectDetailRowCounter(
                    rowCount = uiState.project.rowCount,
                    onIncrementClick = onIncrementClick,
                    onDecrementClick = onDecrementClick,
                )
                Spacer(
                    modifier = Modifier.weight(0.05f)
                )
                ProjectDetailLastSaved(
                    lastSavedAt = uiState.project.lastSavedAt
                )
            }
        }
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun ProjectDetailContentPreview() {
    ProjectDetailContent(
        uiState = ProjectDetailUiState(
            isLoadingProject = false,
            navigateBack = false,
            project = Project(
                id = 1,
                name = "Blue winter sweater",
                notes = "Using 4 mm needles. Repeat the cable pattern every eight rows.",
                rowCount = 24,
                lastSavedAt = Instant.parse("2026-07-23T14:35:00Z"),
                createdAt = Instant.parse("2026-07-23T14:30:00Z")
            ),
        ),
        onBackClick = {},
        onIncrementClick = {},
        onDecrementClick = {},
        projectNameState = rememberTextFieldState(
            initialText = "Blue winter sweater",
        ),
        projectNotesState = rememberTextFieldState(
            initialText = "Using 4 mm needles. Repeat the cable pattern every eight rows.",
        ),
    )
}