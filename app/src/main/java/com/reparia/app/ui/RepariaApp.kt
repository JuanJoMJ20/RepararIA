package com.reparia.app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reparia.app.ui.components.BlurryPhotoErrorDialog
import com.reparia.app.ui.components.DiagnosticScannerScreen
import com.reparia.app.ui.components.RepairGuideScreen
import com.reparia.app.ui.viewmodel.RepairViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepariaApp(
    viewModel: RepairViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "REPARIA • IA Mantenimiento",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->

        AnimatedContent(
            targetState = state.diagnosisGuide != null,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "ScreenTransition",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { hasGuide ->
            if (hasGuide && state.diagnosisGuide != null) {
                RepairGuideScreen(
                    guide = state.diagnosisGuide!!,
                    elapsedSeconds = state.elapsedSeconds,
                    completedStepIds = state.completedStepIds,
                    onToggleStep = { viewModel.toggleStep(it) },
                    onReset = { viewModel.reset() }
                )
            } else {
                DiagnosticScannerScreen(
                    selectedScenario = state.selectedScenario,
                    isScanning = state.isScanning,
                    onSelectScenario = { viewModel.selectScenario(it) },
                    onAnalyze = { viewModel.analyzeScenario() }
                )
            }
        }

        // Componente de Error en 3 Partes para fotos borrosas
        if (state.errorDetail != null) {
            BlurryPhotoErrorDialog(
                show = state.showErrorDialog,
                errorDetail = state.errorDetail!!,
                onRetry = {
                    viewModel.dismissError()
                    viewModel.analyzeScenario()
                },
                onDismiss = { viewModel.dismissError() }
            )
        }
    }
}
