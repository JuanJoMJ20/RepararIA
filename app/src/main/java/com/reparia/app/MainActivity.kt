package com.reparia.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.reparia.app.ui.RepariaApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF4F46E5),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFEEF2FF),
                    onPrimaryContainer = Color(0xFF312E81),
                    surface = Color.White,
                    onSurface = Color(0xFF0F172A),
                    background = Color(0xFFF8FAFC),
                    onBackground = Color(0xFF0F172A)
                )
            ) {
                Surface {
                    RepariaApp()
                }
            }
        }
    }
}
