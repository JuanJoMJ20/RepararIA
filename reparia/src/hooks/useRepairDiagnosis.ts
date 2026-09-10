import { useCallback, useEffect, useRef, useState } from "react";

export type DiagnosisStatus = "idle" | "scanning" | "success" | "error_blurry" | "high_risk_detected";
export type Scenario = "faucet" | "blurry" | "electric" | "audio";
export interface RepairGuide {
  title: string; risk: "safe" | "danger"; minutes: number;
  tools: string[]; steps: { id: number; text: string }[];
}

// Datos simulados (sustituyen al backend de IA en este avance)
export const GUIDES: Record<Exclude<Scenario, "blurry">, RepairGuide> = {
  faucet: {
    title: "Grifo que gotea", risk: "safe", minutes: 20,
    tools: ["Llave inglesa", "Destornillador plano", "Cinta de teflón"],
    steps: [
      { id: 1, text: "Cierra la llave de paso bajo el lavamanos." },
      { id: 2, text: "Retira la manija con el destornillador." },
      { id: 3, text: "Cambia el empaque desgastado (O-ring)." },
      { id: 4, text: "Envuelve la rosca con cinta de teflón y rearma." },
    ],
  },
  audio: {
    title: "Zumbido en la nevera (compresor)", risk: "safe", minutes: 15,
    tools: ["Aspiradora", "Cepillo suave", "Nivel"],
    steps: [
      { id: 1, text: "Desenchufa la nevera y espera 5 minutos." },
      { id: 2, text: "Limpia el polvo de la rejilla trasera." },
      { id: 3, text: "Nivela las patas para evitar vibración." },
    ],
  },
  electric: {
    title: "Tomacorriente con chispas", risk: "danger", minutes: 30,
    tools: ["Probador de voltaje", "Guantes dieléctricos", "Destornillador aislado"],
    steps: [
      { id: 1, text: "Baja el breaker del circuito en el tablero." },
      { id: 2, text: "Confirma con el probador que no hay voltaje." },
      { id: 3, text: "Retira la placa y revisa cables sueltos o quemados." },
      { id: 4, text: "Si hay cobre ennegrecido, llama a un electricista." },
    ],
  },
};

export function useRepairDiagnosis() {
  const [status, setStatus] = useState<DiagnosisStatus>("idle");
  const [scenario, setScenario] = useState<Scenario | null>(null);
  const [doneSteps, setDoneSteps] = useState<number[]>([]);
  const [elapsedSec, setElapsedSec] = useState(0);
  const t0 = useRef(0);
  const timer = useRef<ReturnType<typeof setTimeout> | undefined>(undefined);
  useEffect(() => () => clearTimeout(timer.current), []);

  // PASO 1 de 3 (Métrica de Eficiencia): elegir foto/audio. Inicia el cronómetro Time on Task.
  const selectScenario = useCallback((s: Scenario) => {
    t0.current = Date.now(); setScenario(s); setStatus("idle");
  }, []);

  // PASO 2 de 3: analizar. PASO 3: resultado (diagnóstico + solución en la misma pantalla).
  const analyze = useCallback(() => {
    if (!scenario) return;
    setStatus("scanning");
    timer.current = setTimeout(() => {
      setElapsedSec(Math.round((Date.now() - t0.current) / 1000));
      setStatus(scenario === "blurry" ? "error_blurry" : scenario === "electric" ? "high_risk_detected" : "success");
    }, 2200);
  }, [scenario]);

  const toggleStep = (id: number) =>
    setDoneSteps((p) => (p.includes(id) ? p.filter((x) => x !== id) : [...p, id]));

  const reset = useCallback(() => {
    clearTimeout(timer.current); setStatus("idle"); setScenario(null); setDoneSteps([]);
  }, []);

  const guide = scenario && scenario !== "blurry" ? GUIDES[scenario] : null;
  return { status, scenario, guide, doneSteps, elapsedSec, selectScenario, analyze, toggleStep, reset };
}
export type Diagnosis = ReturnType<typeof useRepairDiagnosis>;
