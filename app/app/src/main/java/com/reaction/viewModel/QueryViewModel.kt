package com.reaction.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reaction.data.ReactionRecord
import com.reaction.repository.ReactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class QueryState(
    val idStr: String = "",
    val result: ReactionRecord? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val lastReactions: List<ReactionRecord> = emptyList(),
)

class QueryViewModel(
    private val repo: ReactionRepository = ReactionRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(QueryState())
    val uiState: StateFlow<QueryState> = _uiState

    init {
        loadLast10()
    }

    fun loadLast10() {
        viewModelScope.launch {
            val list = repo.getLastReaction(10)
            _uiState.value = _uiState.value.copy(lastReactions = list)
        }
    }
    fun setIdStr(value: String) {
        _uiState.value = _uiState.value.copy(idStr = value)
    }

    fun fetchById(onError: (String) -> Unit) {
        val id = _uiState.value.idStr.toIntOrNull()
        if (id == null) {
            onError("ID inválido")
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val item = repo.getReactionById(id)
                _uiState.value = _uiState.value.copy(isLoading = false, result = item)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage ?: "Erro")
                onError(e.localizedMessage ?: "Erro ao buscar")
            }
        }
    }
}
