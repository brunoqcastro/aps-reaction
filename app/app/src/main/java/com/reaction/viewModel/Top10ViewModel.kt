package com.reaction.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reaction.data.ReactionRecord
import com.reaction.repository.ReactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Top10State(
    val items: List<ReactionRecord> = emptyList(),
    val isRefreshing: Boolean = false,
    val error: String? = null
)

class Top10ViewModel(
    private val repo: ReactionRepository = ReactionRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(Top10State())
    val uiState: StateFlow<Top10State> = _uiState

    init {
        loadTop10()
    }

    fun loadTop10() {
        _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)
        viewModelScope.launch {
            try {
                val list = repo.getTop10()
                _uiState.value = Top10State(items = list, isRefreshing = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isRefreshing = false, error = e.localizedMessage ?: "Erro")
            }
        }
    }
}
