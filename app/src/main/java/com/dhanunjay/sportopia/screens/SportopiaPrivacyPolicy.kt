package com.dhanunjay.sportopia.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dhanunjay.sportopia.R
import com.dhanunjay.sportopia.components.HeadingTextComponent
import com.dhanunjay.sportopia.components.NormalTextComponent
import com.dhanunjay.sportopia.navigation.AppRouter
import com.dhanunjay.sportopia.navigation.Screen
import com.dhanunjay.sportopia.navigation.SystemBackButtonHandler

@Composable
fun TermsAndConditionsScreen() {

    Surface(modifier = Modifier



        .fillMaxSize()
        .background(color = Color.White)
        .padding(16.dp))
    {
        Image(
            modifier = Modifier.size(350.dp),
            painter = painterResource(id = R.drawable.logos),
            contentDescription = null)

        HeadingTextComponent(value = stringResource(id = R.string.terms_and_conditions_header))
        }
    NormalTextComponent(value = stringResource(id = R.string.terms_and_conditions_headerdes))

    SystemBackButtonHandler {
        AppRouter.navigateTo(Screen.SignUpScreen)
    }
}

@Preview
@Composable
fun TermsAndConditionsScreenPreview(){
    TermsAndConditionsScreen()
}