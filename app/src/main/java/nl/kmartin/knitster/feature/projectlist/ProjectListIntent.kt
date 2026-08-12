package nl.kmartin.knitster.feature.projectlist

sealed interface ProjectListIntent {
    data object CreateProject : ProjectListIntent
    data object CreateProjectErrorDismissed : ProjectListIntent
    data object CreatedProjectHandled : ProjectListIntent
}
