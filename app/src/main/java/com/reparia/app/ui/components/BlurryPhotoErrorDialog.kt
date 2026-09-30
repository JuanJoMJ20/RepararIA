package com.reparia.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reparia.app.data.ErrorDetail

/**
 * REGLA DE UI/UX — MANEJO DE ERRORES EN 3 PARTES:
 * 1. Qué pasó: Diagnóstico claro de falla.
 * 2. Por qué: Causa física/técnica sin disculpas genéricas ni vaguedad.
 * 3. Qué hacer ahora: Acción correctiva directa e inmediata.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlurryPhotoErrorDialog(
    show: Boolean,
    errorDetail: ErrorDetail,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    if (!show) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "No pudimos analizar la foto",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Parte 1: Qué pasó
            ErrorPartCard(
                label = "1. Qué pasó",
                detail = errorDetail.whatHappened,
                accentColor = Color(0xFFB91C1C),
                backgroundColor = Color(0xFFFEF2F2)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Parte 2: Por qué
            ErrorPartCard(
                label = "2. Por qué",
                detail = errorDetail.whyItHappened,
                accentColor = Color(0xFF334155),
                backgroundColor = Color(0xFFF1F5F9)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Parte 3: Qué hacer
            ErrorPartCard(
                label = "3. Qué hacer ahora",
                detail = errorDetail.actionRequired,
                accentColor = Color(0xFF047857),
                backgroundColor = Color(0xFFECFDF5)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botones de acción correctiva
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Descartar")
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = onRetry,
                    modifier = Modifier
                        .weight(1.2f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Tomar otra foto", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun ErrorPartCard(
    label: String,
    detail: String,
    accentColor: Color,
    backgroundColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = detail,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                ),
                color = accentColor
            )
        }
    }
}
