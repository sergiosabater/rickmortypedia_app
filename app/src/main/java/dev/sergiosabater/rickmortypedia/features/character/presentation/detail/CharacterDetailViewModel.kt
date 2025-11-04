package dev.sergiosabater.rickmortypedia.features.character.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.sergiosabater.rickmortypedia.features.character.domain.model.Character
import dev.sergiosabater.rickmortypedia.features.character.domain.usecase.GetCharacterByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CharacterDetailIntent {
    data class LoadCharacter(val characterId: Int) : CharacterDetailIntent()
    object RetryLoad : CharacterDetailIntent()
}

sealed class CharacterDetailUiState {
    data object Loading : CharacterDetailUiState()
    data class Success(val character: Character) : CharacterDetailUiState()
    data class Error(val message: String) : CharacterDetailUiState()
}

class CharacterDetailViewModel(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CharacterDetailUiState>(CharacterDetailUiState.Loading)
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    private var currentCharacterId: Int? = null

    fun handleIntent(intent: CharacterDetailIntent) {
        when (intent) {
            is CharacterDetailIntent.LoadCharacter -> {
                currentCharacterId = intent.characterId
                loadCharacter(characterId = intent.characterId)
            }

            is CharacterDetailIntent.RetryLoad -> {
                currentCharacterId?.let { id ->
                    loadCharacter(characterId = id)
                }
            }
        }
    }

    private fun loadCharacter(characterId: Int) {
        viewModelScope.launch {
            _uiState.value = CharacterDetailUiState.Loading

            getCharacterByIdUseCase(characterId)
                .onSuccess { character ->
                    _uiState.value = CharacterDetailUiState.Success(character)
                }
                .onFailure { exception ->
                    _uiState.value = CharacterDetailUiState.Error(
                        exception.message ?: "Unknown Error"
                    )
                }
        }
    }
}