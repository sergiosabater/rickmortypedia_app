package dev.sergiosabater.rickmortypedia.features.character.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.sergiosabater.rickmortypedia.features.character.domain.model.Character
import dev.sergiosabater.rickmortypedia.features.character.domain.usecase.GetCharactersUseCase
import dev.sergiosabater.rickmortypedia.features.character.domain.usecase.SearchCharactersUseCase
import dev.sergiosabater.rickmortypedia.features.character.presentation.list.components.SpeciesFilter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// MVI Intent
sealed interface CharactersListIntent {
    data object LoadCharacters : CharactersListIntent
    data class SearchQueryChanged(val query: String) : CharactersListIntent
    data class SpeciesFilterChanged(val species: SpeciesFilter) : CharactersListIntent
    data object RetryLoading : CharactersListIntent
}

class CharactersListViewModel(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val searchCharactersUseCase: SearchCharactersUseCase
) : ViewModel() {

    // State
    private val _uiState = MutableStateFlow(CharactersListUiState())
    val uiState: StateFlow<CharactersListUiState> = _uiState.asStateFlow()

    // Filtered characters (derived state)
    @OptIn(FlowPreview::class)
    val filteredCharacters: StateFlow<List<Character>> = combine(
        uiState.map { it.allCharacters },
        uiState.map { it.searchQuery }.debounce(300),
        uiState.map { it.selectedSpecies }
    ) { characters, query, species ->
        filterCharacters(characters, query, species)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        processIntent(CharactersListIntent.LoadCharacters)
    }

    fun processIntent(intent: CharactersListIntent) {
        when (intent) {
            is CharactersListIntent.LoadCharacters -> loadCharacters()
            is CharactersListIntent.SearchQueryChanged -> handleSearchQueryChange(intent.query)
            is CharactersListIntent.SpeciesFilterChanged -> handleSpeciesFilterChange(intent.species)
            is CharactersListIntent.RetryLoading -> loadCharacters()
        }
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            _uiState.update { it.copy(uiStatus = CharactersListUiStatus.Loading) }

            getCharactersUseCase()
                .onSuccess { characters ->
                    _uiState.update {
                        it.copy(
                            allCharacters = characters,
                            uiStatus = CharactersListUiStatus.Success
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(uiStatus = CharactersListUiStatus.Error) }
                }
        }
    }

    private fun handleSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    private fun handleSpeciesFilterChange(species: SpeciesFilter) {
        _uiState.update { it.copy(selectedSpecies = species) }
    }

    private suspend fun filterCharacters(
        characters: List<Character>,
        query: String,
        species: SpeciesFilter
    ): List<Character> {
        if (query.isEmpty() && species == SpeciesFilter.ALL) {
            return characters
        }

        val searchResults = if (query.isNotEmpty()) {
            searchCharactersUseCase(name = query)
                .getOrNull()
                ?: characters.filter { it.name.contains(query, ignoreCase = true) }
        } else {
            characters
        }

        return if (species != SpeciesFilter.ALL) {
            searchResults.filter { character ->
                character.species.equals(species.filterValue, ignoreCase = true)
            }
        } else {
            searchResults
        }
    }
}

// Ui State
data class CharactersListUiState(
    val allCharacters: List<Character> = emptyList(),
    val searchQuery: String = "",
    val selectedSpecies: SpeciesFilter = SpeciesFilter.ALL,
    val uiStatus: CharactersListUiStatus = CharactersListUiStatus.Loading
)

sealed class CharactersListUiStatus {
    data object Loading : CharactersListUiStatus()
    data object Success : CharactersListUiStatus()
    data object Error : CharactersListUiStatus()
}