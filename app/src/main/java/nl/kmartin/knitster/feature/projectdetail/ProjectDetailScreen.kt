package nl.kmartin.knitster.feature.projectdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.kmartin.knitster.data.model.Project
import nl.kmartin.knitster.data.model.ProjectIcon
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailIconPickerBottomSheet
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailLastSaved
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailNotes
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailRowCounter
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailTitleSection
import nl.kmartin.knitster.feature.projectdetail.component.ProjectDetailTopAppBar
import nl.kmartin.knitster.theme.Dimensions
import nl.kmartin.knitster.ui.component.ObserveSnackbarError
import java.time.Instant

private val minimumRegularContentHeight = 310.dp

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
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteProjectDialog by rememberSaveable { mutableStateOf(false) }
    var showIconPicker by rememberSaveable { mutableStateOf(false) }

    ObserveSnackbarError(
        errorMessage = uiState.saveProjectErrorMsg,
        snackbarHostState = snackbarHostState,
        onErrorShown = viewModel::clearSaveProjectErrorShown
    )

    ObserveSnackbarError(
        errorMessage = uiState.deleteProjectErrorMsg,
        snackbarHostState = snackbarHostState,
        onErrorShown = viewModel::clearDeleteProjectErrorMsg
    )

    ObserveResetRowCounterUndo(
        uiState.rowCountBeforeReset,
        snackbarHostState = snackbarHostState,
        onUndo = viewModel::undoResetRowCounter,
        onDismiss = viewModel::clearRowCountBeforeReset
    )

    ObserveProjectDeleted(
        projectDeleted = uiState.projectDeleted,
        onProjectDeleted = onNavigateBack
    )

    if (showDeleteProjectDialog) {
        DeleteProjectDialog(
            onConfirm = {
                showDeleteProjectDialog = false
                viewModel.deleteProject()
            },
            onDismiss = { showDeleteProjectDialog = false },
            projectName = uiState.project?.name.orEmpty()
        )
    }

    if (showIconPicker) {
        ProjectDetailIconPickerBottomSheet(
            onDismiss = { showIconPicker = false },
            onIconSelected = { selectedIcon ->
                viewModel.updateProjectIcon(selectedIcon)
            },
            currentIcon = uiState.project?.icon ?: ProjectIcon.DEFAULT
        )
    }

    ProjectDetailContent(
        modifier = modifier,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onNavigateBack,
        onDeleteClick = { showDeleteProjectDialog = true },
        onResetCounterClick = viewModel::resetRowCounter,
        onIncrementClick = viewModel::incrementRowCounter,
        onDecrementClick = viewModel::decrementRowCounter,
        onProjectIconClick = { showIconPicker = true },
        projectNameState = viewModel.projectNameState,
        projectNotesState = viewModel.projectNotesState,
    )
}

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
fun ProjectDetailContent(
    modifier: Modifier = Modifier,
    uiState: ProjectDetailUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onResetCounterClick: () -> Unit,
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit,
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
                onResetCounterClick = onResetCounterClick,
                isLoading = uiState.isLoadingProject
            )
        }
    ) { innerPadding ->
        if (!uiState.isLoadingProject && uiState.project != null) {
            val isKeyboardVisible = WindowInsets.isImeVisible
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
                        modifier = Modifier
                            .then(if (!isCompact) Modifier.weight(1f) else Modifier),
                        projectNotesState = projectNotesState,
                    )
                    AnimatedVisibility(
                        visible = !isKeyboardVisible,
                        enter = expandVertically(
                            expandFrom = Alignment.Bottom,
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearOutSlowInEasing
                            )
                        ),
                        exit = shrinkVertically(
                            shrinkTowards = Alignment.Bottom,
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutLinearInEasing
                            )
                        )
                    ) {
                        ProjectDetailRowCounter(
                            rowCount = uiState.project.rowCount,
                            onIncrementClick = onIncrementClick,
                            onDecrementClick = onDecrementClick,
                            compact = isCompact
                        )
                    }
                    ProjectDetailLastSaved(
                        lastSavedAt = uiState.project.lastSavedAt
                    )
                }
            }
        }
    }
}

/**
 * Observes row counter resets and displays an undo snackbar.
 *
 * @param rowCountBeforeReset The row count before it was reset, or `null` if no undo is pending.
 * @param snackbarHostState State used to display the snackbar.
 * @param onUndo Called when the user chooses to undo the reset.
 * @param onDismiss Called when the undo opportunity expires.
 */
@Composable
private fun ObserveResetRowCounterUndo(
    rowCountBeforeReset: Int?,
    snackbarHostState: SnackbarHostState,
    onUndo: () -> Unit,
    onDismiss: () -> Unit,
) {
    LaunchedEffect(rowCountBeforeReset) {
        if (rowCountBeforeReset != null) {
            when (
                snackbarHostState.showSnackbar(
                    message = "Row counter reset from: $rowCountBeforeReset",
                    actionLabel = "Undo",
                    duration = SnackbarDuration.Short,
                )
            ) {
                SnackbarResult.ActionPerformed -> onUndo()
                SnackbarResult.Dismissed -> onDismiss()
            }
        }
    }
}

@Composable
private fun ObserveProjectDeleted(
    projectDeleted: Boolean,
    onProjectDeleted: () -> Unit
) {
    LaunchedEffect(projectDeleted) {
        if (projectDeleted) onProjectDeleted()
    }
}

@Composable
private fun DeleteProjectDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    projectName: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = buildAnnotatedString {
                    append("Delete ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(projectName)
                    }
                    append("?")
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        text = { Text("This action cannot be undone.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
private fun ProjectDetailContentPreview() {
    ProjectDetailContent(
        uiState = ProjectDetailUiState(
            isLoadingProject = false,
            project = Project(
                id = 1,
                name = "Blue winter sweater",
                notes = "Using 4 mm needles. Repeat the cable pattern every eight rows.",
                rowCount = 24,
                lastSavedAt = Instant.parse("2026-07-23T14:35:00Z"),
                createdAt = Instant.parse("2026-07-23T14:30:00Z")
            ),
        ),
        snackbarHostState = remember { SnackbarHostState() },
        onBackClick = {},
        onDeleteClick = {},
        onResetCounterClick = {},
        onIncrementClick = {},
        onDecrementClick = {},
        onProjectIconClick = {},
        projectNameState = rememberTextFieldState(
            initialText = "Blue winter sweater",
        ),
        projectNotesState = rememberTextFieldState(
            initialText = "Using 4 mm needles. Repeat the cable pattern every eight rows.",
        ),
    )
}