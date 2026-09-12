import { useState } from "react";
import { useRepairDiagnosis } from "./hooks/useRepairDiagnosis";
import DiagnosticScannerScreen from "./screens/DiagnosticScannerScreen";
import RepairGuideView from "./components/RepairGuideView";
import ErrorMessageModal from "./components/ErrorMessageModal";

const TABS = [
  { id: "diag", icon: "🔍", label: "Diagnóstico" },
  { id: "guias", icon: "📘", label: "Mis Guías" },
  { id: "hist", icon: "🕘", label: "Historial" },
] as const;

export default function AppShell() {
  const [tab, setTab] = useState<(typeof TABS)[number]["id"]>("diag");
  const d = useRepairDiagnosis();
  const showResult = d.status === "success" || d.status === "high_risk_detected";

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-200">
      {/* Marco de teléfono 390x844 */}
      <div className="relative flex h-[844px] w-[390px] flex-col overflow-hidden rounded-[40px] border-8 border-slate-900 bg-slate-50">
        <div className="flex h-8 items-center justify-between bg-white px-6 text-xs font-semibold text-slate-900">
          <span>9:41</span><span>ReparIA</span><span>📶 🔋</span>
        </div>

        <main className="flex-1 overflow-y-auto">
          {tab === "diag" && (showResult ? <RepairGuideView d={d} /> : <DiagnosticScannerScreen d={d} />)}
          {tab === "guias" && <p className="p-6 text-slate-500">Aquí aparecerán tus guías guardadas (próximo avance).</p>}
          {tab === "hist" && <p className="p-6 text-slate-500">Aquí verás tus diagnósticos anteriores (próximo avance).</p>}
        </main>

        <nav className="flex border-t border-slate-200 bg-white pb-4">
          {TABS.map((t) => (
            <button key={t.id} onClick={() => setTab(t.id)} aria-current={tab === t.id}
              className={`flex flex-1 flex-col items-center py-2 text-xs ${tab === t.id ? "font-bold text-indigo-600" : "text-slate-500"}`}>
              <span className="text-xl">{t.icon}</span>{t.label}
            </button>
          ))}
        </nav>

        <ErrorMessageModal open={d.status === "error_blurry"} onRetry={d.reset} onClose={d.reset} />
      </div>
    </div>
  );
}
