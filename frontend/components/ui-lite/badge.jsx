"use client"

/**
 * Badge de status simples.
 * Variantes: success, danger, warning, neutral, info
 */
export function Badge({ variant = "neutral", className = "", children }) {
  const variants = {
    success: "bg-emerald-50 text-emerald-700 ring-emerald-600/20",
    danger: "bg-red-50 text-red-700 ring-red-600/20",
    warning: "bg-amber-50 text-amber-700 ring-amber-600/20",
    neutral: "bg-slate-100 text-slate-600 ring-slate-500/20",
    info: "bg-blue-50 text-blue-700 ring-blue-600/20",
  }

  return (
    <span
      className={`inline-flex items-center gap-1 rounded-full px-2.5 py-0.5 text-xs font-medium ring-1 ring-inset ${variants[variant]} ${className}`}
    >
      {children}
    </span>
  )
}
