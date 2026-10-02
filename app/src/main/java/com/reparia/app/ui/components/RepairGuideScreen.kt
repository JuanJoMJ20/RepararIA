package com.reparia.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reparia.app.data.DiagnosisGuide

/**
 * PASO 3 DEL FLUJO DE EFICIENCIA: Guía Interactiva de Reparación
 * Integra la Métrica Clave Time-On-Task y el Banner de Voz y Tono Dinámico.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RepairGuideScreen(
    guide: DiagnosisGuide,
    elapsedSeconds: Float,
    completedStepIds: Set<Int>,
    onToggleStep: (Int) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalSteps = guide.steps.size
    val completedCount = completedStepIds.size
    val progress = if (totalSteps > 0) completedCount.toFloat() / totalSteps.toFloat() else 0f
    val isAllDone = completedCount == totalSteps && totalSteps > 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Banner Dinámico con Regla de Voz y Tono según riesgo
        DynamicWarningBanner(riskLevel = guide.risk)

        Spacer(modifier = Modifier.height(16.dp))

        // Encabezado con Métrica de Eficiencia (Time on Task)
        Text(
            text = guide.title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "⚡ Diagnóstico completado en ${"%.1f".format(elapsedSeconds)}s · 3 pasos · Estimado: ${guide.estimatedMinutes} min",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Herramientas necesarias
        Text(
            text = "Herramientas necesarias",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            guide.toolsNeeded.forEach { tool ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFEEF2FF)
                ) {
                    Text(
                        text = "🔧 $tool",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color(0xFF3730A3),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Barra de progreso y checklist
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pasos de reparación",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color(0xFFE2E8F0)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lista interactiva de pasos
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            guide.steps.forEach { step ->
                val isDone = completedStepIds.contains(step.id)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDone) Color(0xFFECFDF5) else MaterialTheme.colorScheme.surface)
                        .border(
                            width = 1.dp,
                            color = if (isDone) Color(0xFFA7F3D0) else Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onToggleStep(step.id) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isDone,
                        onCheckedChange = { onToggleStep(step.id) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF059669)
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = step.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        color = if (isDone) Color.Gray else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Mensaje de éxito final
        AnimatedVisibility(
            visible = isAllDone,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFD1FAE5)
            ) {
                Text(
                    text = "🎉 ¡Excelente trabajo! ¡Reparación completada con éxito!",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF065F46)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de reinicio
        OutlinedButton(
            onClick = onReset,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Iniciar nuevo diagnóstico")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
