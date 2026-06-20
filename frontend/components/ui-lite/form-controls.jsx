"use client"

/**
 * Conjunto de controles de formulário em JavaScript puro (.jsx).
 * Estilizados com Tailwind para um visual de ERP limpo e profissional.
 */

// Wrapper de campo com label e mensagem de erro opcional.
export function Field({ label, htmlFor, required, error, children, hint }) {
  return (
    <div className="flex flex-col gap-1.5">
      {label ? (
        <label htmlFor={htmlFor} className="text-sm font-medium text-slate-700">
          {label}
          {required ? <span className="ml-0.5 text-red-500">*</span> : null}
        </label>
      ) : null}
      {children}
      {hint && !error ? <p className="text-xs text-slate-400">{hint}</p> : null}
      {error ? <p className="text-xs font-medium text-red-500">{error}</p> : null}
    </div>
  )
}

// Input de texto/numero padrão.
export function Input({ error, className = "", ...props }) {
  return (
    <input
      className={
        "w-full rounded-lg border bg-white px-3 py-2 text-sm text-slate-800 outline-none transition-colors placeholder:text-slate-400 focus:ring-2 " +
        (error
          ? "border-red-400 focus:border-red-500 focus:ring-red-100"
          : "border-slate-300 focus:border-emerald-500 focus:ring-emerald-100") +
        " " +
        className
      }
      {...props}
    />
  )
}

// Select estilizado.
export function Select({ error, className = "", children, ...props }) {
  return (
    <select
      className={
        "w-full rounded-lg border bg-white px-3 py-2 text-sm text-slate-800 outline-none transition-colors focus:ring-2 " +
        (error
          ? "border-red-400 focus:border-red-500 focus:ring-red-100"
          : "border-slate-300 focus:border-emerald-500 focus:ring-emerald-100") +
        " " +
        className
      }
      {...props}
    >
      {children}
    </select>
  )
}

// Switch (toggle) acessível.
export function Switch({ checked, onChange, label, description, id }) {
  return (
    <div className="flex items-center justify-between gap-4 rounded-lg border border-slate-200 bg-slate-50 px-4 py-3">
      <div>
        <label htmlFor={id} className="text-sm font-medium text-slate-700">
          {label}
        </label>
        {description ? (
          <p className="text-xs text-slate-500">{description}</p>
        ) : null}
      </div>
      <button
        id={id}
        type="button"
        role="switch"
        aria-checked={checked}
        onClick={() => onChange(!checked)}
        className={
          "relative inline-flex h-6 w-11 flex-shrink-0 items-center rounded-full transition-colors focus:outline-none focus:ring-2 focus:ring-emerald-200 " +
          (checked ? "bg-emerald-500" : "bg-slate-300")
        }
      >
        <span
          className={
            "inline-block h-5 w-5 transform rounded-full bg-white shadow transition-transform " +
            (checked ? "translate-x-5" : "translate-x-0.5")
          }
        />
      </button>
    </div>
  )
}

// Botão com variantes.
export function Button({ variant = "primary", size = "md", className = "", children, ...props }) {
  const base =
    "inline-flex items-center justify-center gap-2 rounded-lg font-medium transition-colors focus:outline-none focus:ring-2 disabled:cursor-not-allowed disabled:opacity-60"

  const variants = {
    primary: "bg-emerald-600 text-white hover:bg-emerald-700 focus:ring-emerald-200",
    secondary: "bg-slate-100 text-slate-700 hover:bg-slate-200 focus:ring-slate-200",
    danger: "bg-red-50 text-red-600 hover:bg-red-100 focus:ring-red-200",
    ghost: "text-slate-600 hover:bg-slate-100 focus:ring-slate-200",
    outline: "border border-slate-300 bg-white text-slate-700 hover:bg-slate-50 focus:ring-slate-200",
  }

  const sizes = {
    sm: "px-2.5 py-1.5 text-xs",
    md: "px-4 py-2 text-sm",
    lg: "px-5 py-2.5 text-sm",
  }

  return (
    <button className={`${base} ${variants[variant]} ${sizes[size]} ${className}`} {...props}>
      {children}
    </button>
  )
}
