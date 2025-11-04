package dev.sergiosabater.rickmortypedia.features.character.presentation.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sergiosabater.rickmortypedia.R
import dev.sergiosabater.rickmortypedia.features.character.domain.model.Character
import dev.sergiosabater.rickmortypedia.features.character.domain.model.CharacterStatus
import dev.sergiosabater.rickmortypedia.features.character.presentation.list.components.CharacterListItem
import dev.sergiosabater.rickmortypedia.features.character.presentation.list.components.CustomSearchBar
import dev.sergiosabater.rickmortypedia.features.character.presentation.list.components.SpeciesFilter
import dev.sergiosabater.rickmortypedia.features.character.presentation.list.components.SpeciesFilterBar

@Composable
fun CharactersListScreen(
    characters: List<Character>,
    uiState: CharactersListUiState,
    onIntent: (CharactersListIntent) -> Unit,
    onCharacterClick: (Character) -> Unit,
    isDarkTheme: Boolean?,
    onThemeToggle: () -> Unit
) {
    val searchQuery = uiState.searchQuery
    val selectedSpecies = uiState.selectedSpecies
    val uiStatus = uiState.uiStatus

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (uiStatus) {
            is CharactersListUiStatus.Loading -> {
                LoadingState()
            }

            is CharactersListUiStatus.Success -> {
                SuccessState(
                    characters = characters,
                    searchQuery = searchQuery,
                    selectedSpecies = selectedSpecies,
                    isDarkTheme = isDarkTheme,
                    onSearchQueryChange = { query ->
                        onIntent(CharactersListIntent.SearchQueryChanged(query))
                    },
                    onCharacterClick = onCharacterClick,
                    onThemeToggle = onThemeToggle,
                    onSpeciesSelected = { species ->
                        onIntent(CharactersListIntent.SpeciesFilterChanged(species))
                    }
                )
            }

            is CharactersListUiStatus.Error -> {
                ErrorState(
                    onRetry = {
                        onIntent(CharactersListIntent.RetryLoading)
                    }
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun SuccessState(
    characters: List<Character>,
    searchQuery: String,
    selectedSpecies: SpeciesFilter,
    isDarkTheme: Boolean?,
    onSearchQueryChange: (String) -> Unit,
    onCharacterClick: (Character) -> Unit,
    onThemeToggle: () -> Unit,
    onSpeciesSelected: (SpeciesFilter) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HeaderSection(
            isDarkTheme = isDarkTheme,
            onThemeToggle = onThemeToggle
        )

        CustomSearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        SpeciesFilterBar(
            selectedSpecies = selectedSpecies,
            onSpeciesSelected = onSpeciesSelected,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        CharactersList(
            characters = characters,
            searchQuery = searchQuery,
            onCharacterClick = onCharacterClick
        )
    }
}

@Composable
private fun HeaderSection(
    isDarkTheme: Boolean?,
    onThemeToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp, bottom = 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Rick and Morty Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(top = 64.dp)
                .align(Alignment.Center)
        )

        ThemeToggleButton(
            isDarkTheme = isDarkTheme ?: false,
            onToggle = onThemeToggle,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 16.dp)
        )
    }
}

@Composable
private fun CharactersList(
    characters: List<Character>,
    searchQuery: String,
    onCharacterClick: (Character) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = characters,
            key = { it.id }
        ) { character ->
            CharacterListItem(
                character = character,
                onClick = onCharacterClick
            )
        }

        if (characters.isEmpty()) {
            item {
                EmptyState(searchQuery = searchQuery)
            }
        }
    }
}

@Composable
private fun EmptyState(searchQuery: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (searchQuery.isNotEmpty()) {
                "No characters found"
            } else {
                "There are no characters available"
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorState(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Error loading characters",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun ThemeToggleButton(
    isDarkTheme: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isDarkTheme) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "theme_rotation"
    )

    IconButton(
        onClick = onToggle,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
            contentDescription = if (isDarkTheme) "Change to light mode" else "Change to dark mode",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.rotate(rotation)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CharactersListScreenPreview() {
    val sampleCharacters = listOf(
        Character(
            id = 1,
            name = "Rick Sanchez",
            status = CharacterStatus.ALIVE,
            species = "Human",
            type = "",
            gender = "Male",
            origin = "Earth (C-137)",
            location = "Citadel of Ricks",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            episodeCount = 51
        ),
        Character(
            id = 2,
            name = "Morty Smith",
            status = CharacterStatus.ALIVE,
            species = "Human",
            type = "",
            gender = "Male",
            origin = "Earth (C-137)",
            location = "Citadel of Ricks",
            image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
            episodeCount = 51
        ),
        Character(
            id = 3,
            name = "Summer Smith",
            status = CharacterStatus.ALIVE,
            species = "Human",
            type = "",
            gender = "Female",
            origin = "Earth (Replacement Dimension)",
            location = "Earth (Replacement Dimension)",
            image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
            episodeCount = 42
        ),
        Character(
            id = 4,
            name = "Beth Smith",
            status = CharacterStatus.ALIVE,
            species = "Human",
            type = "",
            gender = "Female",
            origin = "Earth (Replacement Dimension)",
            location = "Earth (Replacement Dimension)",
            image = "https://rickandmortyapi.com/api/character/avatar/4.jpeg",
            episodeCount = 42
        )
    )

    val uiState = CharactersListUiState(
        searchQuery = "",
        selectedSpecies = SpeciesFilter.ALL,
        uiStatus = CharactersListUiStatus.Success
    )

    MaterialTheme {
        CharactersListScreen(
            characters = sampleCharacters,
            uiState = uiState,
            onIntent = {},
            onCharacterClick = {},
            isDarkTheme = false,
            onThemeToggle = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CharactersListScreenErrorPreview() {
    val uiState = CharactersListUiState(
        searchQuery = "",
        selectedSpecies = SpeciesFilter.ALL,
        uiStatus = CharactersListUiStatus.Error
    )

    MaterialTheme {
        CharactersListScreen(
            characters = emptyList(),
            uiState = uiState,
            onIntent = {},
            onCharacterClick = {},
            isDarkTheme = false,
            onThemeToggle = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CharactersListScreenEmptyPreview() {
    val uiState = CharactersListUiState(
        searchQuery = "Nonexistent Character",
        selectedSpecies = SpeciesFilter.ALL,
        uiStatus = CharactersListUiStatus.Success
    )

    MaterialTheme {
        CharactersListScreen(
            characters = emptyList(),
            uiState = uiState,
            onIntent = {},
            onCharacterClick = {},
            isDarkTheme = false,
            onThemeToggle = {}
        )
    }
}

