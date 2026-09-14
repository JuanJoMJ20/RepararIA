interface Props { open: boolean; onRetry: () => void; onClose: () => void }

/**
 * UI/UX — ANATOMÍA DEL MENSAJE DE ERROR (3 partes, en este orden):
 * 1. Qué pasó  2. Por qué  3. Qué hacer.
 * Sin disculpas ni vaguedad: el mensaje dirige la acción correctiva.
 */
export default function ErrorMessageModal({ open, onRetry, onClose }: Props) {
  if (!open) return null;
  const parts = [
    { label: "Qué pasó", text: "No logramos identificar la pieza.", tone: "text-red-700" },
    { label: "Por qué", text: "La fotografía presenta demasiada sombra o desenfoque.", tone: "text-slate-700" },
    { label: "Qué hacer", text: "Activa el flash y toma la foto a 15 cm de distancia.", tone: "text-emerald-800" },
  ];
  return (
    <div role="alertdialog" aria-modal="true" className="absolute inset-0 z-50 flex items-end bg-slate-900/60">
      <div className="w-full rounded-t-3xl bg-white p-5 pb-8 shadow-2xl">
        <div className="mx-auto mb-4 h-1 w-10 rounded-full bg-slate-300" />
        <h2 className="mb-3 text-lg font-bold text-slate-900">No pudimos analizar la foto</h2>
        <dl className="space-y-3">
          {parts.map((p) => (
            <div key={p.label} className="rounded-xl bg-slate-50 p-3">
              <dt className="text-sm font-semibold text-slate-500">{p.label}</dt>
              <dd className={`mt-0.5 font-medium ${p.tone}`}>{p.text}</dd>
            </div>
          ))}
        </dl>
        <div className="mt-5 flex gap-3">
          <button onClick={onClose} className="flex-1 rounded-xl border border-slate-300 py-3 font-semibold text-slate-700">Cancelar</button>
          <button onClick={onRetry} className="flex-1 rounded-xl bg-indigo-600 py-3 font-semibold text-white">Tomar otra foto</button>
        </div>
      </div>
    </div>
  );
}
