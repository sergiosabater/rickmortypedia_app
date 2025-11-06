package dev.sergiosabater.rickmortypedia.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.sergiosabater.rickmortypedia.features.character.presentation.detail.CharacterDetailIntent
import dev.sergiosabater.rickmortypedia.features.character.presentation.detail.CharacterDetailScreen
import dev.sergiosabater.rickmortypedia.features.character.presentation.detail.CharacterDetailViewModel
import dev.sergiosabater.rickmortypedia.features.character.presentation.list.CharactersListScreen
import dev.sergiosabater.rickmortypedia.features.character.presentation.list.CharactersListViewModel
import dev.sergiosabater.rickmortypedia.features.error.presentation.ErrorScreen
import dev.sergiosabater.rickmortypedia.features.splash.presentation.SplashScreen
import dev.sergiosabater.rickmortypedia.features.splash.presentation.SplashViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavHost(
    navigator: AppNavigator,
    isDarkTheme: Boolean?,
    onThemeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    LaunchedEffect(navController) {
        navigator.setNavController(navController)
    }

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Splash.route,
        modifier = modifier
    ) {
        // Splash Screen
        composable(route = NavigationRoute.Splash.route) {
            val viewModel: SplashViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsState()

            SplashScreen(
                uiState = uiState,
                onLoadingComplete = {
                    navigator.navigateToCharacterList()
                },
                onError = {
                    navigator.navigateToError()
                }
            )
        }

        // Character List Screen
        composable(route = NavigationRoute.CharacterList.route) {
            val viewModel: CharactersListViewModel = koinViewModel()
            val filteredCharacters by viewModel.filteredCharacters.collectAsStateWithLifecycle()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            CharactersListScreen(
                characters = filteredCharacters,
                uiState = uiState,
                onIntent = viewModel::processIntent,
                onCharacterClick = { character ->
                    navigator.navigateToCharacterDetail(character.id)
                },
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle
            )
        }

        // Character Detail Screen
        composable(
            route = NavigationRoute.CharacterDetail.ROUTE_WITH_ARGS,
            arguments = listOf(
                navArgument(NavigationRoute.CharacterDetail.ARG_CHARACTER_ID) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt(
                NavigationRoute.CharacterDetail.ARG_CHARACTER_ID
            ) ?: 0

            val viewModel: CharacterDetailViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsState()

            // Load character when navigating to the screen
            LaunchedEffect(characterId) {
                viewModel.handleIntent(CharacterDetailIntent.LoadCharacter(characterId))
            }

            CharacterDetailScreen(
                uiState = uiState,
                onBackClick = {
                    navigator.navigateBack()
                },
                onRetry = {
                    viewModel.handleIntent(CharacterDetailIntent.RetryLoad)
                }
            )
        }

        // Error Screen
        composable(route = NavigationRoute.Error.route) {
            ErrorScreen(
                onRetry = {
                    navigator.navigateToSplash()
                },
                onExit = {
                    navigator.navigateBack()
                }
            )
        }
    }
}