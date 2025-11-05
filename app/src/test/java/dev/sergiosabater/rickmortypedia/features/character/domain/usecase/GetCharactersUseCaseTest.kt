package dev.sergiosabater.rickmortypedia.features.character.domain.usecase

import dev.sergiosabater.rickmortypedia.features.character.domain.model.Character
import dev.sergiosabater.rickmortypedia.features.character.domain.repository.CharacterRepository
import dev.sergiosabater.rickmortypedia.util.fixtures.CharacterTestFixtures
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetCharactersUseCaseTest {

    @Mock
    private lateinit var repository: CharacterRepository

    private lateinit var useCase: GetCharactersUseCase

    @Before
    fun setup() {
        // Initialize Mockito annotations and create use case instance with mocked repository
        MockitoAnnotations.openMocks(this)
        useCase = GetCharactersUseCase(repository)
    }

    @Test
    fun `invoke returns success when repository returns characters`() = runTest {
        // Given - Setup scenario: repository will return successful result with characters
        val page = 2 // Specific page to test pagination
        val expectedCharacters = listOf(CharacterTestFixtures.character1, CharacterTestFixtures.character2)

        // Configure mock repository to return successful result with expected characters
        whenever(repository.getCharacters(page)).thenReturn(Result.success(expectedCharacters))

        // When - Execute the use case with specific page parameter
        val result = useCase(page)

        // Then - Verify the expected behavior and outcomes
        assertTrue(result.isSuccess) // Use case should return successful result
        assertEquals(expectedCharacters, result.getOrNull()) // Returned characters should match expected ones
        verify(repository).getCharacters(page) // Verify repository was called with correct page parameter
    }

    @Test
    fun `invoke returns failure when repository throws exception`() = runTest {
        // Given - Setup scenario: repository will return failure result
        val errorMessage = "Network error"
        val exception = Exception(errorMessage) // Create exception with specific message

        // Configure mock repository to return failure result when called without page parameter
        whenever(repository.getCharacters(null)).thenReturn(Result.failure(exception))

        // When - Execute the use case without page parameter (default behavior)
        val result = useCase()

        // Then - Verify the expected failure behavior
        assertTrue(result.isFailure) // Use case should return failure result
        assertEquals(errorMessage, result.exceptionOrNull()?.message) // Error message should match expected
        verify(repository).getCharacters(null) // Verify repository was called without page parameter
    }

    @Test
    fun `invoke returns empty list when repository returns empty list`() = runTest {
        // Given - Setup scenario: repository will return successful but empty result
        val emptyList = emptyList<Character>() // Create empty list for testing

        // Configure mock repository to return successful result with empty list
        whenever(repository.getCharacters(null)).thenReturn(Result.success(emptyList))

        // When - Execute the use case without page parameter
        val result = useCase()

        // Then - Verify the behavior with empty list result
        assertTrue(result.isSuccess) // Use case should return successful result (even though list is empty)
        assertEquals(emptyList, result.getOrNull()) // Returned list should be empty
        assertTrue(result.getOrNull()!!.isEmpty()) // Additional verification that the list is indeed empty
        verify(repository).getCharacters(null) // Verify repository was called without page parameter
    }
}