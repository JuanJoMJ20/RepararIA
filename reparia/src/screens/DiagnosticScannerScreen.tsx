import type { Diagnosis, Scenario } from "../hooks/useRepairDiagnosis";

// Entradas simuladas: cada una dispara un resultado distinto para demostrar la interfaz
const OPTIONS: { id: Scenario; icon: string; label: string; hint: string }[] = [
  { id: "faucet", icon: "📷", label: "Foto: grifo", hint: "Resultado seguro" },
  { id: "electric", icon: "⚡", label: "Foto: enchufe", hint: "Riesgo alto" },
  { id: "blurry", icon: "🌫️", label: "Foto borrosa", hint: "Error en 3 partes" },
  { id: "audio", icon: "🎙️", label: "Audio: nevera", hint: "Resultado seguro" },
];

export default function DiagnosticScannerScreen({ d }: { d: Diagnosis }) {
  const { scenario, status, selectScenario, analyze } = d;
  const scanning = status === "scanning";
  const selected = OPTIONS.find((o) => o.id === scenario);

  return (
    <div className="space-y-4 p-4">
      <style>{`@keyframes laser{0%{top:0}50%{top:calc(100% - 4px)}100%{top:0}}
        @keyframes pulseRec{0%,100%{transform:scale(1)}50%{transform:scale(1.25)}}`}</style>

      <h1 className="text-xl font-bold text-slate-900">¿Qué falla tienes en casa?</h1>

      {/* Paso 1 de 3: selector de entrada */}
      <div className="grid grid-cols-2 gap-3">
        {OPTIONS.map((o) => (
          <button key={o.id} onClick={() => selectScenario(o.id)} disabled={scanning}
            className={`rounded-2xl border-2 p-3 text-left ${scenario === o.id ? "border-indigo-600 bg-indigo-50" : "border-slate-200 bg-white"}`}>
            <div className="text-2xl">{o.icon}</div>
            <div className="font-semibold text-slate-900">{o.label}</div>
            <div className="text-xs text-slate-500">{o.hint}</div>
          </button>
        ))}
      </div>

      {/* Vista previa con escaneo láser */}
      <div className="relative flex h-52 items-center justify-center overflow-hidden rounded-2xl bg-slate-800">
        {selected ? (
          <span className={`text-7xl ${scenario === "blurry" ? "blur-sm" : ""}`} style={scenario === "audio" ? { animation: "pulseRec 1s infinite" } : undefined}>{selected.icon}</span>
        ) : <p className="px-6 text-center text-slate-400">Elige una foto o un audio para empezar</p>}
        {scanning && <div className="absolute left-0 h-1 w-full bg-red-500 shadow-[0_0_14px_4px_rgba(239,68,68,0.8)]" style={{ animation: "laser 1.1s linear infinite" }} />}
        {scanning && <p className="absolute bottom-2 text-xs text-white">Analizando…</p>}
      </div>

      {/* Paso 2 de 3: ejecutar análisis */}
      <button onClick={analyze} disabled={!scenario || scanning}
        className="w-full rounded-2xl bg-indigo-600 py-4 text-lg font-bold text-white disabled:bg-slate-300">
        {scanning ? "Analizando…" : "Analizar falla con IA"}
      </button>
    </div>
  );
}
