package dev.sergiosabater.rickmortypedia.features.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.sergiosabater.rickmortypedia.core.common.error.DomainError
import dev.sergiosabater.rickmortypedia.features.character.domain.usecase.GetCharactersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SplashIntent {
    object LoadInitialData : SplashIntent()
    object RetryLoad : SplashIntent()
}

sealed class SplashUiState {
    data object Loading : SplashUiState()
    data object Success : SplashUiState()
    data class Error(val message: String?) : SplashUiState()
}

class SplashViewModel(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        handleIntent(SplashIntent.LoadInitialData)
    }

    fun handleIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.LoadInitialData -> loadInitialData()
            is SplashIntent.RetryLoad -> loadInitialData()
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = SplashUiState.Loading

            val result = getCharactersUseCase()

            when {
                result.isSuccess -> {
                    val characters = result.getOrNull() ?: emptyList()
                    if (characters.isNotEmpty()) {
                        _uiState.value = SplashUiState.Success
                    } else {
                        _uiState.value = SplashUiState.Error("No characters were found")
                    }
                }

                else -> {
                    val exception = result.exceptionOrNull()
                    val errorMessage = if (exception is DomainError) {
                        exception.message
                    } else {
                        exception?.message ?: "Unknown error occurred"
                    }
                    _uiState.value = SplashUiState.Error(errorMessage)
                }
            }
        }
    }
}