package com.reaction.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reaction.data.ConfigResponse
import com.reaction.repository.ReactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UiConfigState(
    val minMsStr: String = "",
    val maxMsStr: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val lastRemote: ConfigResponse? = null
)

class ConfigViewModel(
    private val repo: ReactionRepository = ReactionRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiConfigState())
    val uiState: StateFlow<UiConfigState> = _uiState

    fun loadConfig() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val cfg = repo.getConfig()
                _uiState.value = UiConfigState(
                    minMsStr = cfg.min_ms.toString(),
                    maxMsStr = cfg.max_ms.toString(),
                    isLoading = false,
                    lastRemote = cfg
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage ?: "Erro")
            }
        }
    }

    fun setMinStr(value: String) {
        _uiState.value = _uiState.value.copy(minMsStr = value)
    }

    fun setMaxStr(value: String) {
        _uiState.value = _uiState.value.copy(maxMsStr = value)
    }

    fun canUpdate(): Boolean {
        val state = _uiState.value
        val last = state.lastRemote
        val min = state.minMsStr.toIntOrNull()
        val max = state.maxMsStr.toIntOrNull()
        if (min == null || max == null) return false
        if (min > max) return false
        if (last == null) return true
        return (min != last.min_ms) || (max != last.max_ms)
    }

    fun updateConfig(onSuccess: (ConfigResponse) -> Unit, onError: (String) -> Unit) {
        val min = _uiState.value.minMsStr.toIntOrNull()
        val max = _uiState.value.maxMsStr.toIntOrNull()
        if (min == null || max == null) {
            onError("Valores inválidos")
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val cfg = repo.updateConfig(min, max)
                _uiState.value = UiConfigState(
                    minMsStr = cfg.min_ms.toString(),
                    maxMsStr = cfg.max_ms.toString(),
                    isLoading = false,
                    lastRemote = cfg
                )
                onSuccess(cfg)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage ?: "Erro")
                onError(e.localizedMessage ?: "Erro ao atualizar")
            }
        }
    }
}
