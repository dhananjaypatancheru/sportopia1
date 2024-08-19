package com.dhanunjay.sportopia.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dhanunjay.sportopia.data.home.HomeViewModel
import com.dhanunjay.sportopia.navigation.AppRouter
import com.dhanunjay.sportopia.navigation.Screen
import com.dhanunjay.sportopia.screens.HomeScreen
import com.dhanunjay.sportopia.screens.LoginScreen
import com.dhanunjay.sportopia.screens.SignUpScreen
import com.dhanunjay.sportopia.screens.TermsAndConditionsScreen


@Composable
fun SportopiaApps(homeViewModel: HomeViewModel = viewModel()) {

    homeViewModel.checkForActiveSession()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray
    ) {

        if (homeViewModel.isUserLoggedIn.value == true) {
            AppRouter.navigateTo(Screen.HomeScreen)
        }

        Crossfade(targetState = AppRouter.currentScreen) { currentState ->
            when (currentState.value) {
                is Screen.SignUpScreen -> {
                    SignUpScreen()
                }

                is Screen.TermsAndConditionsScreen -> {
                    TermsAndConditionsScreen()
                }

                is Screen.LoginScreen -> {
                    LoginScreen()
                }

                is Screen.HomeScreen -> {
                    HomeScreen()
                }
            }
        }

    }
}