package com.example.stateduuz

import androidx.navigation.NavHostController

object AllDestinations {
    const val OliyTalim = "OliyTalim"
    const val ProfessionalTalim = "ProfessionalTalim"
    const val Qabul = "Qabul"
    const val Doctorantura = "Doctorantura"

}

class AppNavigationActions(private val navController: NavHostController) {

    fun navigateToOliyTalim() {
        navController.navigate(AllDestinations.OliyTalim) {
            popUpTo(AllDestinations.OliyTalim)
        }
    }

    fun navigateToProfessionalTalim() {
        navController.navigate(AllDestinations.ProfessionalTalim) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToQabul() {
        navController.navigate(AllDestinations.Qabul) {
            launchSingleTop = true
            restoreState = true
        }
    }
    fun navigateToDoctorantura() {
        navController.navigate(AllDestinations.Doctorantura) {
            launchSingleTop = true
            restoreState = true
        }
    }
}