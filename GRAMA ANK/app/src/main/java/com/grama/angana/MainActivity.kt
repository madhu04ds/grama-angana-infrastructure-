package com.grama.angana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.grama.angana.presentation.navigation.GramaAnganaNavHost
import com.grama.angana.ui.theme.GramaAnganaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GramaAnganaTheme {
                Surface {
                    GramaAnganaNavHost()
                }
            }
        }
    }
}
