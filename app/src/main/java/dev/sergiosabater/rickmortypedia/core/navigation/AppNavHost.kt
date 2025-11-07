package dev.sergiosabater.rickmortypedia.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.sergiosabater.rickmortypedia.features.character.presentation.detail.CharacterDetailScreen
import dev.sergiosabater.rickmortypedia.features.character.presentation.list.CharactersListScreen
import dev.sergiosabater.rickmortypedia.features.error.presentation.ErrorScreen
import dev.sergiosabater.rickmortypedia.features.splash.presentation.SplashScreen

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
            SplashScreen(
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
            CharactersListScreen(
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

            CharacterDetailScreen(
                characterId = characterId,
                onBackClick = {
                    navigator.navigateBack()
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