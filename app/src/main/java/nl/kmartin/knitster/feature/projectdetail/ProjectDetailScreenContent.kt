package nl.kmartin.knitster.feature.projectdetail

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.data.model.RowCounter
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailLastSaved
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailNotes
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailRowCounterSection
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailTitleSection
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailTopAppBar
import nl.kmartin.knitster.theme.Dimensions
import java.time.Instant

private val minimumRegularContentHeight = 310.dp

/**
 * Displays the content of the project detail screen.
 *
 * @param modifier Modifier to be applied to the root layout.
 * @param uiState Current UI state of the screen.
 * @param snackbarHostState State used to display snackbar message.
 * @param onBackClick Called when the back button is clicked.
 * @param onDeleteClick Called when the delete button is clicked.
 * @param onResetCounterClick Called when the reset button is clicked.
 * @param onIncrementClick Called when the row counter is incremented.
 * @param onDecrementClick Called when the row counter is decremented.
 * @param projectNameState State backing the project name text field.
 * @param projectNotesState State backing the project notes text field.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProjectDetailScreenContent(
    modifier: Modifier = Modifier,
    uiState: ProjectDetailUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onResetCounterClick: (Long) -> Unit,
    onEditCounterClick: (Long) -> Unit,
    onDeleteCounterClick: (Long) -> Unit,
    onIncrementClick: (Long) -> Unit,
    onDecrementClick: (Long) -> Unit,
    onAddRowCounterClick: () -> Unit,
    onProjectIconClick: () -> Unit,
    projectNameState: TextFieldState,
    projectNotesState: TextFieldState
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
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
                onDeleteClick = onDeleteClick,
                isLoading = uiState.isLoadingProject
            )
        },
        bottomBar = {
            uiState.project?.lastSavedAt?.let { lastSavedAt ->
                ProjectDetailLastSaved(
                    modifier = Modifier.navigationBarsPadding(),
                    lastSavedAt = lastSavedAt
                )
            }
        }
    ) { innerPadding ->
        if (!uiState.isLoadingProject && uiState.project != null) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(Dimensions.ScreenPadding)
            ) {
                val isCompact = maxHeight < minimumRegularContentHeight

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProjectDetailTitleSection(
                        projectNameState = projectNameState,
                        onProjectIconClick = onProjectIconClick,
                        projectIcon = uiState.project.icon
                    )
                    ProjectDetailNotes(
                        modifier = Modifier,
                        projectNotesState = projectNotesState,
                        minHeightInLines = if (isCompact) 1 else 4,
                        maxHeightInLines = if (isCompact) 3 else 8
                    )
                    ProjectDetailRowCounterSection(
                        rowCounters = uiState.project.rowCounters,
                        onIncrementClick = onIncrementClick,
                        onDecrementClick = onDecrementClick,
                        onResetClick = onResetCounterClick,
                        onEditClick = onEditCounterClick,
                        onDeleteClick = onDeleteCounterClick,
                        onAddRowCounterClick = onAddRowCounterClick
                    )
                }
            }
        }
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun ProjectDetailScreenContentPreview() {
    ProjectDetailScreenContent(
        uiState = ProjectDetailUiState(
            isLoadingProject = false,
            project = Project(
                id = 1,
                name = "Blue winter sweater",
                notes = "Using 4 mm needles. Repeat the cable pattern every eight rows.",
                rowCounters = listOf(RowCounter(1, "Row Counter", 42)),
                lastSavedAt = Instant.parse("2026-07-23T14:35:00Z"),
                createdAt = Instant.parse("2026-07-23T14:30:00Z")
            ),
        ),
        snackbarHostState = remember { SnackbarHostState() },
        onBackClick = {},
        onDeleteClick = {},
        onResetCounterClick = {},
        onEditCounterClick = {},
        onDeleteCounterClick = {},
        onIncrementClick = {},
        onDecrementClick = {},
        onProjectIconClick = {},
        projectNameState = rememberTextFieldState(
            initialText = "Blue winter sweater",
        ),
        projectNotesState = rememberTextFieldState(
            initialText = "Using 4 mm needles. Repeat the cable pattern every eight rows.",
        ),
        onAddRowCounterClick = {}
    )
}