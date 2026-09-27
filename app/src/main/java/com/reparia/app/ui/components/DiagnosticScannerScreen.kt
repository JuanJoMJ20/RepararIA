package com.reparia.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteTransition
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reparia.app.data.ScenarioType

/**
 * PASO 1 Y 2 DEL FLUJO DE EFICIENCIA (<3 Pasos para Diagnóstico):
 * Paso 1: Selección/Captura de imagen o audio
 * Paso 2: Ejecución e inspección visual con IA (Animación de escáner)
 */
@Composable
fun DiagnosticScannerScreen(
    selectedScenario: ScenarioType?,
    isScanning: Boolean,
    onSelectScenario: (ScenarioType) -> Unit,
    onAnalyze: () -> Unit,
    modifier: Modifier = Modifier
) {
    val options = ScenarioType.entries.toList()

    // Animación de láser para el escáner visual
    val infiniteTransition = rememberInfiniteTransition(label = "LaserTransition")
    val laserYRatio by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteTransition.run {
            androidx.compose.animation.core.infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        },
        label = "LaserYRatio"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "¿Qué falla tienes en casa?",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Paso 1 de 3: Selecciona una foto o muestra de audio",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Rejilla de Opciones
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.height(180.dp)
        ) {
            items(options) { option ->
                val isSelected = selectedScenario == option
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(enabled = !isScanning) { onSelectScenario(option) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFEEF2FF) else MaterialTheme.colorScheme.surface
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFE2E8F0)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = option.icon, fontSize = 26.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = option.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        )
                        Text(
                            text = option.hint,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Visor de Escáner IA con animación
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF1E293B)),
            contentAlignment = Alignment.Center
        ) {
            if (selectedScenario != null) {
                Text(
                    text = selectedScenario.icon,
                    fontSize = 72.sp,
                    modifier = if (selectedScenario == ScenarioType.BLURRY) {
                        Modifier.blur(8.dp)
                    } else Modifier
                )
            } else {
                Text(
                    text = "Elige una foto o audio para empezar",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Animación de Láser de Escaneo
            if (isScanning) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .align(Alignment.TopCenter)
                            .padding(horizontal = 8.dp)
                            .background(Color(0xFFEF4444))
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Analizando con visión computacional...",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Paso 2: Ejecutar análisis
        Button(
            onClick = onAnalyze,
            enabled = selectedScenario != null && !isScanning,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = if (isScanning) "Procesando diagnóstico..." else "Analizar falla con IA",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiagnosticScannerScreenPreview() {
    MaterialTheme {
        DiagnosticScannerScreen(
            selectedScenario = ScenarioType.FAUCET,
            isScanning = false,
            onSelectScenario = {},
            onAnalyze = {}
        )
    }
}

