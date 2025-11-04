package dev.sergiosabater.rickmortypedia.core.navigation

import androidx.navigation.NavController

class AppNavigator() {

    private var navController: NavController? = null

    fun setNavController(controller: NavController) {
        navController = controller
    }

    fun navigateToCharacterList() {
        navController?.navigate(NavigationRoute.CharacterList.route) {
            popUpTo(NavigationRoute.Splash.route) { inclusive = true }
        }
    }

    fun navigateToCharacterDetail(characterId: Int) {
        navController?.navigate(
            NavigationRoute.CharacterDetail().createRoute(characterId)
        )
    }

    fun navigateToError() {
        navController?.navigate(NavigationRoute.Error.route) {
            popUpTo(NavigationRoute.Splash.route) { inclusive = true }
        }
    }

    fun navigateToSplash() {
        navController?.navigate(NavigationRoute.Splash.route) {
            popUpTo(NavigationRoute.Error.route) { inclusive = true }
        }
    }

    fun navigateBack() {
        navController?.popBackStack()
    }
}