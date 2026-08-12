package nl.kmartin.knitster.ui.viewmodel

import kotlinx.coroutines.flow.StateFlow

interface MviViewModel<State, Intent> {
    val uiState: StateFlow<State>
    fun onIntent(intent: Intent)
}