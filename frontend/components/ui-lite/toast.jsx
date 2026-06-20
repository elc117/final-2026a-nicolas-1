"use client"

import { useEffect } from "react"
import { CheckCircle2, XCircle, Info, X } from "lucide-react"

/**
 * Toast de notificação simples e auto-dispensável.
 * Renderizado em posição fixa (canto inferior direito).
 *
 * Props:
 * - toast: { id, variant: 'success'|'error'|'info', title, message } | null
 * - onClose: function
 * - duration: ms até fechar automaticamente (padrão 4000)
 */
const VARIANTS = {
  success: { icon: CheckCircle2, ring: "border-emerald-200", accent: "text-emerald-600", bar: "bg-emerald-500" },
  error: { icon: XCircle, ring: "border-red-200", accent: "text-red-600", bar: "bg-red-500" },
  info: { icon: Info, ring: "border-blue-200", accent: "text-blue-600", bar: "bg-blue-500" },
}

export function Toast({ toast, onClose, duration = 4000 }) {
  useEffect(() => {
    if (!toast) return
    const timer = setTimeout(() => onClose?.(), duration)
    return () => clearTimeout(timer)
  }, [toast, duration, onClose])

  if (!toast) return null

  const cfg = VARIANTS[toast.variant] ?? VARIANTS.info
  const Icon = cfg.icon

  return (
    <div className="pointer-events-none fixed bottom-4 right-4 z-[60] flex w-full max-w-sm justify-end">
      <div
        role="status"
        aria-live="polite"
        className={`pointer-events-auto relative w-full overflow-hidden rounded-xl border bg-white p-4 shadow-lg ${cfg.ring}`}
      >
        <div className="flex items-start gap-3">
          <Icon className={`mt-0.5 h-5 w-5 flex-shrink-0 ${cfg.accent}`} />
          <div className="min-w-0 flex-1">
            {toast.title ? <p className="text-sm font-semibold text-slate-800">{toast.title}</p> : null}
            {toast.message ? <p className="mt-0.5 text-sm text-slate-600">{toast.message}</p> : null}
          </div>
          <button
            type="button"
            onClick={onClose}
            className="rounded-md p-0.5 text-slate-400 transition-colors hover:bg-slate-100 hover:text-slate-600"
            aria-label="Fechar notificação"
          >
            <X className="h-4 w-4" />
          </button>
        </div>
        <span className={`absolute bottom-0 left-0 h-1 w-full ${cfg.bar} opacity-70`} />
      </div>
    </div>
  )
}
