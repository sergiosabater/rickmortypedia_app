package dev.sergiosabater.rickmortypedia.core.navigation

sealed class NavigationRoute(val route: String) {

    data object Splash : NavigationRoute("splash")

    data object CharacterList : NavigationRoute("character_list")

    data object Error : NavigationRoute("error")

    data class CharacterDetail(val characterId: Int? = null) : NavigationRoute("character_detail") {
        companion object {
            const val ROUTE_WITH_ARGS = "character_detail/{characterId}"
            const val ARG_CHARACTER_ID = "characterId"
        }

        fun createRoute(id: Int): String = "character_detail/$id"
    }
}