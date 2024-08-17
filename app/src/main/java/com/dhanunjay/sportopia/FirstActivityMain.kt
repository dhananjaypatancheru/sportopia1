package com.dhanunjay.sportopia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dhanunjay.sportopia.app.SportopiaApps

class FirstActivityMain : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SportopiaApps()
        }
    }
}



