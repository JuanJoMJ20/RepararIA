package com.reparia.app.data

/**
 * Nivel de riesgo asignado al diagnóstico por la IA.
 * Determina la variación de TONO en la regla de Voz y Tono:
 * - DANGER (Electricidad / Gas): Tono Imperativo, directo y preventivo.
 * - SAFE (Tareas simples): Tono Motivador, cercano y empoderador.
 */
enum class RiskLevel {
    DANGER,
    SAFE
}

/**
 * Escenarios de entrada del scanner de diagnóstico para pruebas y simulación rápida.
 */
enum class ScenarioType(val title: String, val icon: String, val hint: String) {
    FAUCET("Foto: Grifo goteando", "🚰", "Resultado seguro"),
    ELECTRIC("Foto: Enchufe chispas", "⚡", "Riesgo eléctrico alto"),
    BLURRY("Foto: Borrosa / Sombra", "🌫️", "Dispara error de 3 partes"),
    AUDIO("Audio: Motor nevera", "🎙️", "Diagnóstico por sonido")
}

/**
 * Paso individual dentro de la lista de verificación de reparación.
 */
data class RepairStep(
    val id: Int,
    val description: String,
    val isCompleted: Boolean = false
)

/**
 * Guía de reparación generada por el diagnóstico de IA.
 */
data class DiagnosisGuide(
    val title: String,
    val risk: RiskLevel,
    val estimatedMinutes: Int,
    val toolsNeeded: List<String>,
    val steps: List<RepairStep>
)

/**
 * Anatomía del Mensaje de Error (Regra de 3 partes):
 * 1. Qué pasó (Diagnóstico del fallo de entrada)
 * 2. Por qué (Causa técnica clara sin disculpas vanas)
 * 3. Qué hacer ahora (Instrucción correctiva directiva y accionable)
 */
data class ErrorDetail(
    val whatHappened: String,
    val whyItHappened: String,
    val actionRequired: String
)
