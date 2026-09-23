package com.reparia.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reparia.app.data.DiagnosisGuide
import com.reparia.app.data.ErrorDetail
import com.reparia.app.data.RepairStep
import com.reparia.app.data.RiskLevel
import com.reparia.app.data.ScenarioType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RepairUiState(
    val selectedScenario: ScenarioType? = null,
    val isScanning: Boolean = false,
    val diagnosisGuide: DiagnosisGuide? = null,
    val completedStepIds: Set<Int> = emptySet(),
    val elapsedSeconds: Float = 0f,
    val showErrorDialog: Boolean = false,
    val errorDetail: ErrorDetail? = null
)

class RepairViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RepairUiState())
    val uiState: StateFlow<RepairUiState> = _uiState.asStateFlow()

    fun selectScenario(scenario: ScenarioType) {
        _uiState.update { it.copy(selectedScenario = scenario) }
    }

    fun analyzeScenario() {
        val scenario = _uiState.value.selectedScenario ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isScanning = true) }

            val startTime = System.currentTimeMillis()
            delay(1500) // Simulación de análisis IA con visión computacional
            val durationSec = (System.currentTimeMillis() - startTime) / 1000f

            if (scenario == ScenarioType.BLURRY) {
                _uiState.update {
                    it.copy(
                        isScanning = false,
                        showErrorDialog = true,
                        errorDetail = ErrorDetail(
                            whatHappened = "No logramos identificar la pieza.",
                            whyItHappened = "La fotografía presenta demasiada sombra o desenfoque en la zona de enfoque principal.",
                            actionRequired = "Activa el flash de tu cámara y toma la foto a 15 cm de distancia."
                        )
                    )
                }
            } else {
                val guide = createGuideForScenario(scenario)
                _uiState.update {
                    it.copy(
                        isScanning = false,
                        diagnosisGuide = guide,
                        elapsedSeconds = durationSec,
                        completedStepIds = emptySet()
                    )
                }
            }
        }
    }

    fun toggleStep(stepId: Int) {
        _uiState.update { state ->
            val updated = state.completedStepIds.toMutableSet()
            if (updated.contains(stepId)) {
                updated.remove(stepId)
            } else {
                updated.add(stepId)
            }
            state.copy(completedStepIds = updated)
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(showErrorDialog = false) }
    }

    fun reset() {
        _uiState.value = RepairUiState()
    }

    private fun createGuideForScenario(scenario: ScenarioType): DiagnosisGuide {
        return when (scenario) {
            ScenarioType.ELECTRIC -> DiagnosisGuide(
                title = "Cortocircuito en conmutador eléctrico",
                risk = RiskLevel.DANGER,
                estimatedMinutes = 15,
                toolsNeeded = listOf("Destornillador aislado", "Probador de voltaje", "Cinta aislante"),
                steps = listOf(
                    RepairStep(1, "Desconectar la brequera o taco de energía eléctrica principal."),
                    RepairStep(2, "Verificar con el probador que la línea esté 100% desenergizada."),
                    RepairStep(3, "Retirar la tapa del interruptor y apretar los tornillos del borne."),
                    RepairStep(4, "Reemplazar la cubierta plástica y restablecer el fluido de energía.")
                )
            )

            ScenarioType.FAUCET -> DiagnosisGuide(
                title = "Fuga por desgaste en goteo de grifo",
                risk = RiskLevel.SAFE,
                estimatedMinutes = 10,
                toolsNeeded = listOf("Llave inglesa", "Empaque O-ring #4", "Cinta de teflón"),
                steps = listOf(
                    RepairStep(1, "Cerrar la llave de paso de agua bajo el lavamanos."),
                    RepairStep(2, "Desenroscar el aireador del grifo con la llave inglesa."),
                    RepairStep(3, "Sustituir el empaque O-ring desgastado por uno nuevo."),
                    RepairStep(4, "Reajustar el aireador y abrir la llave de agua para probar.")
                )
            )

            ScenarioType.AUDIO -> DiagnosisGuide(
                title = "Vibración en condensador de nevera",
                risk = RiskLevel.SAFE,
                estimatedMinutes = 8,
                toolsNeeded = listOf("Nivel de burbuja", "Trapo seco"),
                steps = listOf(
                    RepairStep(1, "Retirar objetos sueltos sobre la superficie superior de la refrigeradora."),
                    RepairStep(2, "Ajustar las patas niveladoras delanteras girándolas hacia la derecha."),
                    RepairStep(3, "Limpiar el polvo de la rejilla trasera con un trapo seco.")
                )
            )

            else -> DiagnosisGuide(
                title = "Ajuste general de mantenimiento",
                risk = RiskLevel.SAFE,
                estimatedMinutes = 5,
                toolsNeeded = listOf("Herramientas básicas"),
                steps = listOf(
                    RepairStep(1, "Inspección visual y limpieza."),
                    RepairStep(2, "Ajuste de fijaciones y prueba de funcionamiento.")
                )
            )
        }
    }
}
