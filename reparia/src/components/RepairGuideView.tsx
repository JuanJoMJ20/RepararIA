import type { Diagnosis } from "../hooks/useRepairDiagnosis";

/**
 * UI/UX — VOZ Y TONO
 * VOZ (constante): experto paciente, claro y cercano, en segunda persona.
 * TONO (variable según contexto): riesgo alto => IMPERATIVO Y DIRECTO; riesgo bajo => MOTIVADOR.
 */
const TONE = {
  danger: { box: "bg-red-600 text-white", icon: "⚠️", title: "¡Corta el suministro eléctrico antes de continuar!", sub: "No toques cables ni la placa hasta confirmar que no hay voltaje." },
  safe: { box: "bg-emerald-100 text-emerald-900", icon: "💪", title: "¡Esto lo resuelves tú, paso a paso!", sub: "Es una reparación sencilla. Te acompaño en cada paso." },
};

export default function RepairGuideView({ d }: { d: Diagnosis }) {
  const { guide, doneSteps, toggleStep, elapsedSec, reset } = d;
  if (!guide) return null;
  const t = TONE[guide.risk];
  const pct = Math.round((doneSteps.length / guide.steps.length) * 100);

  return (
    <div className="space-y-4 p-4">
      {/* role="alert" anuncia el tono imperativo a lectores de pantalla */}
      <div role={guide.risk === "danger" ? "alert" : "status"} className={`rounded-2xl p-4 ${t.box}`}>
        <p className={guide.risk === "danger" ? "text-lg font-extrabold uppercase" : "text-lg font-bold"}>{t.icon} {t.title}</p>
        <p className="mt-1 text-sm">{t.sub}</p>
      </div>

      <div>
        <h2 className="text-xl font-bold text-slate-900">{guide.title}</h2>
        {/* Métrica de Eficiencia: Time on Task y nº de pasos (foto → analizar → solución) */}
        <p className="text-xs text-slate-500">Diagnóstico en {elapsedSec}s · 3 pasos · Reparación ≈ {guide.minutes} min</p>
      </div>

      <section>
        <h3 className="mb-2 font-semibold text-slate-800">Herramientas necesarias</h3>
        <div className="flex flex-wrap gap-2">
          {guide.tools.map((tool) => <span key={tool} className="rounded-full bg-indigo-50 px-3 py-1 text-sm text-indigo-800">🔧 {tool}</span>)}
        </div>
      </section>

      <section>
        <div className="mb-2 flex justify-between text-sm font-semibold text-slate-800">
          <h3>Pasos</h3><span>{pct}%</span>
        </div>
        <div className="mb-3 h-2 rounded-full bg-slate-200"><div className="h-2 rounded-full bg-indigo-600 transition-all" style={{ width: `${pct}%` }} /></div>
        <ul className="space-y-2">
          {guide.steps.map((s) => {
            const done = doneSteps.includes(s.id);
            return (
              <li key={s.id}>
                <label className={`flex cursor-pointer items-start gap-3 rounded-xl border p-3 ${done ? "border-emerald-300 bg-emerald-50" : "border-slate-200"}`}>
                  <input type="checkbox" checked={done} onChange={() => toggleStep(s.id)} className="mt-1 h-5 w-5 accent-indigo-600" />
                  <span className={done ? "text-slate-400 line-through" : "text-slate-800"}>{s.text}</span>
                </label>
              </li>
            );
          })}
        </ul>
        {pct === 100 && <p className="mt-3 rounded-xl bg-emerald-100 p-3 font-semibold text-emerald-900">✅ ¡Reparación completada!</p>}
      </section>

      <button onClick={reset} className="w-full rounded-xl border border-slate-300 py-3 font-semibold text-slate-700">Nuevo diagnóstico</button>
    </div>
  );
}
