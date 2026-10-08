package com.reparia.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reparia.app.data.RiskLevel

/**
 * REGLA DE UI/UX — VOZ Y TONO DINÁMICO
 *
 * VOZ (Constante): Experto técnico, paciente, claro y empático en 2ª persona ("Te acompaño").
 * TONO (Variable por Riesgo):
 * - DANGER (Gas/Electricidad): IMPERATIVO Y DIRECTO. Directivas de seguridad sin ambigüedades.
 * - SAFE (Reparación simple): MOTIVADOR Y CERCANO. Refuerzo de confianza ("¡Esto lo resuelves tú!").
 */
@Composable
fun DynamicWarningBanner(
    riskLevel: RiskLevel,
    modifier: Modifier = Modifier
) {
    val isDanger = riskLevel == RiskLevel.DANGER

    val containerColor by animateColorAsState(
        targetValue = if (isDanger) Color(0xFFDC2626) else Color(0xFFD1FAE5),
        animationSpec = tween(durationMillis = 350),
        label = "BannerContainerColor"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isDanger) Color.White else Color(0xFF065F46),
        animationSpec = tween(durationMillis = 350),
        label = "BannerContentColor"
    )

    val title = if (isDanger) {
        "¡CORTA EL SUMINISTRO ELÉCTRICO ANTES DE CONTINUAR!"
    } else {
        "¡Esto lo resuelves tú, paso a paso!"
    }

    val subtitle = if (isDanger) {
        "No toques cables ni la placa hasta bajar el interruptor principal y verificar con probador de voltaje."
    } else {
        "Es una reparación sencilla sin riesgo de sobrecarga. Te guiamos en cada movimiento."
    }

    val iconText = if (isDanger) "⚠️" else "💪"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                // Accesibilidad: Anuncia de inmediato a TalkBack cuando es riesgo alto
                liveRegion = if (isDanger) LiveRegionMode.Assertive else LiveRegionMode.Polite
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = iconText,
                fontSize = 28.sp,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (isDanger) FontWeight.ExtraBold else FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = if (isDanger) 0.5.sp else 0.sp
                    ),
                    color = contentColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.5.sp,
                        lineHeight = 18.sp
                    ),
                    color = contentColor.copy(alpha = 0.95f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DynamicWarningBannerDangerPreview() {
    MaterialTheme {
        DynamicWarningBanner(
            riskLevel = RiskLevel.DANGER,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DynamicWarningBannerSafePreview() {
    MaterialTheme {
        DynamicWarningBanner(
            riskLevel = RiskLevel.SAFE,
            modifier = Modifier.padding(16.dp)
        )
    }
}

