package dev.sergiosabater.rickmortypedia.features.character.presentation.list

import app.cash.turbine.test
import dev.sergiosabater.rickmortypedia.features.character.domain.usecase.GetCharactersUseCase
import dev.sergiosabater.rickmortypedia.features.character.domain.usecase.SearchCharactersUseCase
import dev.sergiosabater.rickmortypedia.features.character.presentation.list.components.SpeciesFilter
import dev.sergiosabater.rickmortypedia.util.ViewModelTest
import dev.sergiosabater.rickmortypedia.util.fixtures.CharacterTestFixtures
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue


@ExperimentalCoroutinesApi
class CharactersListViewModelTest : ViewModelTest() {

    @Mock
    private lateinit var getCharactersUseCase: GetCharactersUseCase

    @Mock
    private lateinit var searchCharactersUseCase: SearchCharactersUseCase

    private lateinit var viewModel: CharactersListViewModel

    @Before
    fun setup() = runTest {
        // Set up default successful behavior for getCharactersUseCase
        whenever(getCharactersUseCase.invoke()).thenReturn(
            Result.success(CharacterTestFixtures.mockCharacters)
        )
    }


    // INITIAL CHARGE TESTS

    @Test
    fun `WHEN ViewModel is initialized THEN should start loading characters`() = runTest {
        // Given - Default setup with successful use case mock
        // When - Create ViewModel instance
        viewModel = CharactersListViewModel(getCharactersUseCase, searchCharactersUseCase)

        // Then - Verify the initial state is correct using Turbine to collect Flow
        viewModel.uiState.test {
            val state = awaitItem() // Collect the first emitted state
            assertIs<CharactersListUiStatus.Success>(state.uiStatus) // Should be in Success state
            assertEquals(CharacterTestFixtures.mockCharacters, state.allCharacters) // Should contain all mock characters
        }
    }

    @Test
    fun `WHEN loading characters fails THEN should update state to Error`() = runTest {
        // Given - Override default behavior to simulate failure scenario
        val exception = Exception("Network error")
        whenever(getCharactersUseCase.invoke()).thenReturn(Result.failure(exception))

        // When - Create ViewModel instance (triggers initial character loading that will fail)
        viewModel = CharactersListViewModel(getCharactersUseCase, searchCharactersUseCase)

        // Then - Verify the state updates to Error and characters list is empty
        viewModel.uiState.test {
            val state = awaitItem() // Collect the emitted state
            assertIs<CharactersListUiStatus.Error>(state.uiStatus) // Should be in Error state
            assertTrue(state.allCharacters.isEmpty()) // Character list should be empty on error
        }
    }


    // SEARCH TESTS

    @Test
    fun `WHEN search query changes THEN should update state with new query`() = runTest {
        // Given - Create ViewModel with successful initial load
        viewModel = CharactersListViewModel(getCharactersUseCase, searchCharactersUseCase)

        // When - Process search query change intent
        viewModel.processIntent(CharactersListIntent.SearchQueryChanged("Rick"))

        // Then - Verify the search query is updated in the state
        viewModel.uiState.test {
            val state = awaitItem() // Collect state after intent processing
            assertEquals("Rick", state.searchQuery) // Search query should match what was sent
        }
    }

    @Test
    fun `WHEN search query is applied THEN should filter characters by name`() = runTest {
        // Given - Create ViewModel with successful initial load
        viewModel = CharactersListViewModel(getCharactersUseCase, searchCharactersUseCase)

        // When - Process search query change intent
        viewModel.processIntent(CharactersListIntent.SearchQueryChanged("Rick"))

        // Then - Verify filtered characters after debounce
        viewModel.filteredCharacters.test {
            skipItems(1) // Skip initial empty value emitted by the flow
            advanceTimeBy(300) // Advance time to trigger the debounced search
            val filtered = awaitItem() // Collect the filtered results
            assertEquals(1, filtered.size) // Should filter to only one character
            assertEquals(CharacterTestFixtures.character1.name, filtered.first().name) // Should be Rick Sanchez
        }
    }

    @Test
    fun `WHEN search query is empty THEN should show all characters`() = runTest {
        // Given - Create ViewModel and set a search query first
        viewModel = CharactersListViewModel(getCharactersUseCase, searchCharactersUseCase)
        viewModel.processIntent(CharactersListIntent.SearchQueryChanged("Rick")) // Set initial filter

        // When - Clear the search query by setting it to empty string
        viewModel.processIntent(CharactersListIntent.SearchQueryChanged(""))

        // Then - Verify all characters are shown when search is cleared
        viewModel.filteredCharacters.test {
            skipItems(1) // Skip initial state
            advanceTimeBy(300) // Wait for debounce period
            val filtered = awaitItem() // Collect the results
            assertEquals(CharacterTestFixtures.mockCharacters.size, filtered.size) // Should show all characters
        }
    }


    // FILTER BY SPECIES

    @Test
    fun `WHEN Human species filter is applied THEN should show only humans`() = runTest {
        // Given - Create ViewModel with successful initial load
        viewModel = CharactersListViewModel(getCharactersUseCase, searchCharactersUseCase)

        // When - Apply human species filter
        viewModel.processIntent(CharactersListIntent.SpeciesFilterChanged(SpeciesFilter.HUMAN))

        // Then - Verify only human characters are shown after debounce
        viewModel.filteredCharacters.test {
            skipItems(1) // Skip initial state
            advanceTimeBy(300) // Wait for debounce period
            val filtered = awaitItem() // Collect filtered results
            assertEquals(3, filtered.size) // Should show 3 human characters from fixtures
            assertTrue(filtered.all { it.species == "Human" }) // All should be human species
        }
    }

    @Test
    fun `WHEN ALL species filter is applied THEN should show all characters`() = runTest {
        // Given - Create ViewModel and apply human filter first
        viewModel = CharactersListViewModel(getCharactersUseCase, searchCharactersUseCase)
        viewModel.processIntent(CharactersListIntent.SpeciesFilterChanged(SpeciesFilter.HUMAN))

        // When - Change filter to ALL species
        viewModel.processIntent(CharactersListIntent.SpeciesFilterChanged(SpeciesFilter.ALL))

        // Then - Verify all characters are shown when ALL filter is applied
        viewModel.filteredCharacters.test {
            skipItems(1) // Skip initial state
            advanceTimeBy(300) // Wait for debounce period
            val filtered = awaitItem() // Collect results
            assertEquals(CharacterTestFixtures.mockCharacters.size, filtered.size) // Should show all characters
        }
    }


    // FILTER COMBINATION

    @Test
    fun `WHEN search and species filter are combined THEN should apply both filters`() = runTest {
        // Given - Create ViewModel with successful initial load
        viewModel = CharactersListViewModel(getCharactersUseCase, searchCharactersUseCase)

        // When - Apply both search query and species filter
        viewModel.processIntent(CharactersListIntent.SearchQueryChanged("Smith"))
        viewModel.processIntent(CharactersListIntent.SpeciesFilterChanged(SpeciesFilter.HUMAN))

        // Then - Verify both filters are applied correctly after debounce
        viewModel.filteredCharacters.test {
            skipItems(1) // Skip initial state
            advanceTimeBy(300) // Wait for debounce period
            val filtered = awaitItem() // Collect results
            assertEquals(2, filtered.size) // Should show 2 characters (Morty Smith and Summer Smith)
            assertTrue(filtered.all { it.name.contains("Smith") }) // All should contain "Smith" in name
            assertTrue(filtered.all { it.species == "Human" }) // All should be human species
        }
    }
}