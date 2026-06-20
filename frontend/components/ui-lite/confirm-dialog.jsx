"use client"

import { AlertTriangle } from "lucide-react"
import { Modal } from "@/components/ui-lite/modal"
import { Button } from "@/components/ui-lite/form-controls"

/**
 * Diálogo de confirmação reutilizável (equivalente ao AlertDialog).
 * Usado para ações destrutivas como exclusão de registros.
 *
 * Props:
 * - open: boolean
 * - onClose: () => void            (fecha sem fazer nada / "Cancelar")
 * - onConfirm: () => void          (executa a ação destrutiva)
 * - title: string
 * - children: conteúdo do resumo do item
 * - confirmLabel: rótulo do botão de confirmação (padrão "Confirmar Exclusão")
 * - cancelLabel: rótulo do botão de cancelamento (padrão "Cancelar")
 */
export function ConfirmDialog({
  open,
  onClose,
  onConfirm,
  title = "Confirmar ação",
  children,
  confirmLabel = "Confirmar Exclusão",
  cancelLabel = "Cancelar",
}) {
  return (
    <Modal
      open={open}
      onClose={onClose}
      title={title}
      footer={
        <>
          <Button variant="ghost" onClick={onClose}>
            {cancelLabel}
          </Button>
          <Button variant="danger" onClick={onConfirm}>
            {confirmLabel}
          </Button>
        </>
      }
    >
      <div className="flex gap-3">
        <div className="flex h-10 w-10 flex-shrink-0 items-center justify-center rounded-full bg-red-100 text-red-600">
          <AlertTriangle className="h-5 w-5" />
        </div>
        <div className="text-sm text-slate-600">{children}</div>
      </div>
    </Modal>
  )
}
