package com.dhanunjay.sportopia.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dhanunjay.sportopia.SportopiaData.home.HomeViewModel
import com.dhanunjay.sportopia.navigationControl.AppRouter
import com.dhanunjay.sportopia.navigationControl.Screen
import com.dhanunjay.sportopia.SportopiaScreens.HomeScreen
import com.dhanunjay.sportopia.SportopiaScreens.LoginScreen
import com.dhanunjay.sportopia.SportopiaScreens.SignUpScreen
import com.dhanunjay.sportopia.SportopiaScreens.TermsAndConditionsScreen


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