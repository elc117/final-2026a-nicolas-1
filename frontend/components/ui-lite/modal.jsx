"use client"

import { useEffect } from "react"
import { X } from "lucide-react"

/**
 * Modal (Dialog) genérico e reutilizável.
 *
 * Props:
 * - open: boolean -> controla a visibilidade
 * - onClose: function -> chamada ao fechar (overlay, botão X ou tecla ESC)
 * - title: string -> título exibido no cabeçalho
 * - description: string (opcional) -> subtítulo abaixo do título
 * - children: conteúdo do corpo do modal
 * - footer: conteúdo do rodapé (ações)
 */
export function Modal({ open, onClose, title, description, children, footer }) {
  // Fecha o modal ao pressionar ESC e bloqueia o scroll do body quando aberto.
  useEffect(() => {
    if (!open) return

    function handleKey(e) {
      if (e.key === "Escape") onClose?.()
    }

    document.addEventListener("keydown", handleKey)
    document.body.style.overflow = "hidden"

    return () => {
      document.removeEventListener("keydown", handleKey)
      document.body.style.overflow = ""
    }
  }, [open, onClose])

  if (!open) return null

  return (
    <div
      className="fixed inset-0 z-50 flex items-start justify-center overflow-y-auto bg-slate-950/60 p-4 backdrop-blur-sm sm:items-center"
      role="dialog"
      aria-modal="true"
      aria-labelledby="modal-title"
      onMouseDown={(e) => {
        // Fecha apenas se o clique foi no overlay (não nos filhos).
        if (e.target === e.currentTarget) onClose?.()
      }}
    >
      <div className="my-8 w-full max-w-lg rounded-xl border border-slate-200 bg-white shadow-xl">
        {/* Cabeçalho */}
        <div className="flex items-start justify-between gap-4 border-b border-slate-100 px-6 py-4">
          <div>
            <h2 id="modal-title" className="text-lg font-semibold text-slate-800">
              {title}
            </h2>
            {description ? (
              <p className="mt-0.5 text-sm text-slate-500">{description}</p>
            ) : null}
          </div>
          <button
            type="button"
            onClick={onClose}
            className="rounded-md p-1 text-slate-400 transition-colors hover:bg-slate-100 hover:text-slate-600"
            aria-label="Fechar"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        {/* Corpo */}
        <div className="px-6 py-5">{children}</div>

        {/* Rodapé */}
        {footer ? (
          <div className="flex items-center justify-end gap-3 border-t border-slate-100 px-6 py-4">
            {footer}
          </div>
        ) : null}
      </div>
    </div>
  )
}
