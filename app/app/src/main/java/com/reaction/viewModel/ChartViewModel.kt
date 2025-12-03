package com.reaction.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reaction.data.ReactionRecord
import com.reaction.repository.ReactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChartViewModel(
    private val repo: ReactionRepository = ReactionRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChartState())
    val uiState: StateFlow<ChartState> = _uiState

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            try {
                val list = repo.getLastReaction(10)
                _uiState.value = ChartState(items = list)
            } catch (e: Exception) {
                _uiState.value = ChartState(error = e.localizedMessage)
            }
        }
    }
}

data class ChartState(
    val items: List<ReactionRecord> = emptyList(),
    val error: String? = null
)

