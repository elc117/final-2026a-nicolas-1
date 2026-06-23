"use client"

import { useEffect, useState } from "react"
import { Save, MessageCircle, Mail, AlertTriangle } from "lucide-react"
import { Modal } from "@/components/ui-lite/modal"
import { Field, Input, Select, Button } from "@/components/ui-lite/form-controls"

const EMPTY = {
  ingredienteId: "",
  fornecedor: "",
  amount: "",
  messageChannel: "WHATSAPP",
  telefone: "",
  email: "",
}

// Constrói os campos do formulário a partir de um ingrediente, aplicando o
// "Fornecedor Padrão"/"Contato de Emergência" e a sugestão de quantidade de reposição.
function ingredientToForm(ing) {
  const isLow = ing.currentAmount < ing.minimumStock
  const minReorder = isLow ? ing.minimumStock - ing.currentAmount : 0
  const channel = ing.defaultChannel ?? "WHATSAPP"
  return {
    ingredienteId: String(ing.id),
    fornecedor: ing.defaultSupplier ?? "",
    messageChannel: channel,
    telefone: channel === "WHATSAPP" ? ing.supplierContact ?? "" : "",
    email: channel === "EMAIL" ? ing.supplierContact ?? "" : "",
    // Reposição inteligente: já sugere a quantidade necessária para sair do alerta.
    amount: isLow ? String(minReorder) : "",
  }
}

// Monta o texto descritivo da compra que será enviado ao fornecedor (pré-visualização).
function buildMessage(ingredientes, form) {
  const ing = ingredientes.find((i) => String(i.id) === String(form.ingredienteId))
  if (!ing || !form.amount) return ""
  return `Olá, gostaríamos de pedir ${form.amount}${ing.measurementUnit.toLowerCase()} de ${ing.name}. Aguardamos a confirmação do pedido. Obrigado!`
}

/**
 * Modal de criação/edição de pedido de compra.
 *
 * Props:
 * - open, onClose
 * - ingredientes: lista de ingredientes disponíveis (inclui estoque + fornecedor padrão)
 * - onSubmit(payload) -> { ingredientName, amount, unit, messageChannel, supplierContact, supplierName }
 * - initialData: pedido existente (modo edição) ou null (modo criação)
 * - prefillIngredient: ingrediente para pré-preencher o formulário (Reposição Inteligente)
 */
export function PurchaseOrderModal({ open, onClose, ingredientes, onSubmit, initialData, prefillIngredient }) {
  const [form, setForm] = useState(EMPTY)
  const [errors, setErrors] = useState({})

  const isEditing = Boolean(initialData)

  // Popula o formulário ao abrir: edição, reposição (prefill) ou em branco.
  useEffect(() => {
    if (!open) return
    setErrors({})

    if (initialData) {
      const ing = ingredientes.find((i) => i.name === initialData.ingredientName)
      const isWhats = initialData.messageChannel === "WHATSAPP"
      setForm({
        ingredienteId: ing ? String(ing.id) : "",
        fornecedor: initialData.supplierName ?? "",
        amount: String(initialData.amount ?? ""),
        messageChannel: initialData.messageChannel ?? "WHATSAPP",
        telefone: isWhats ? initialData.supplierContact ?? "" : "",
        email: isWhats ? "" : initialData.supplierContact ?? "",
      })
    } else if (prefillIngredient) {
      setForm({ ...EMPTY, ...ingredientToForm(prefillIngredient) })
    } else {
      setForm(EMPTY)
    }
  }, [open, initialData, prefillIngredient, ingredientes])

  function update(key, value) {
    // Ao trocar de ingrediente, recarrega fornecedor/canal/destinatário/sugestão de quantidade.
    if (key === "ingredienteId") {
      const ing = ingredientes.find((i) => String(i.id) === String(value))
      if (ing) {
        setForm({ ...EMPTY, ...ingredientToForm(ing) })
        setErrors({})
        return
      }
      setForm((prev) => ({ ...prev, ingredienteId: "" }))
      setErrors((prev) => ({ ...prev, ingredienteId: undefined }))
      return
    }
    setForm((prev) => ({ ...prev, [key]: value }))
    setErrors((prev) => ({ ...prev, [key]: undefined }))
  }

  // Inteligência de estoque derivada do ingrediente selecionado.
  const selected = ingredientes.find((i) => String(i.id) === String(form.ingredienteId))
  const isLow = selected ? selected.currentAmount < selected.minimumStock : false
  const minReorder = selected && isLow ? selected.minimumStock - selected.currentAmount : 0

  function validate() {
    const next = {}
    if (!form.ingredienteId) next.ingredienteId = "Selecione um ingrediente."
    if (!form.fornecedor.trim()) next.fornecedor = "Informe o fornecedor."
    if (!form.amount || Number(form.amount) <= 0) {
      next.amount = "Informe uma quantidade válida."
    } else if (isLow && Number(form.amount) < minReorder) {
      // Validação bloqueante: não permite pedir menos do que o necessário para sair do alerta.
      next.amount = `Pedido mínimo para reposição: ${minReorder} ${selected.measurementUnit}.`
    }
    if (form.messageChannel === "WHATSAPP" && form.telefone.replace(/\D/g, "").length < 10)
      next.telefone = "Informe um telefone válido."
    if (form.messageChannel === "EMAIL" && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email))
      next.email = "Informe um e-mail válido."
    setErrors(next)
    return Object.keys(next).length === 0
  }

  function handleSubmit() {
    if (!validate()) return

    const ing = ingredientes.find((i) => String(i.id) === String(form.ingredienteId))
    const supplierContact = form.messageChannel === "WHATSAPP" ? form.telefone : form.email

    // Em produção: POST /api/pedidos (criar) ou PUT /api/pedidos/{id} (editar)
    onSubmit({
      ingredientName: ing?.name ?? "",
      unit: ing?.measurementUnit ?? "",
      amount: Number(form.amount),
      messageChannel: form.messageChannel,
      supplierContact,
      supplierName: form.fornecedor.trim(),
    })
  }

  const previewMessage = buildMessage(ingredientes, form)
  // Destaque âmbar quando o item está em alerta e ainda não há erro no campo.
  const amountAmber = isLow && !errors.amount

  return (
    <Modal
      open={open}
      onClose={onClose}
      title={isEditing ? "Editar Pedido de Compra" : "Novo Pedido de Compra"}
      description={
        isEditing
          ? "Altere os dados do pedido. As mudanças só são possíveis enquanto o pedido está pendente."
          : "Cadastre o pedido. Ele será criado como pendente até você verificá-lo."
      }
      footer={
        <>
          <Button variant="secondary" onClick={onClose}>
            Cancelar
          </Button>
          <Button onClick={handleSubmit}>
            <Save className="h-4 w-4" />
            {isEditing ? "Salvar Alterações" : "Cadastrar Pedido"}
          </Button>
        </>
      }
    >
      <div className="flex flex-col gap-4">
        <Field label="Ingrediente" htmlFor="ingrediente" required error={errors.ingredienteId}>
          <Select
            id="ingrediente"
            value={form.ingredienteId}
            error={errors.ingredienteId}
            onChange={(e) => update("ingredienteId", e.target.value)}
          >
            <option value="">Selecione um ingrediente</option>
            {ingredientes.map((i) => (
              <option key={i.id} value={i.id}>
                {i.name} ({i.measurementUnit})
              </option>
            ))}
          </Select>
        </Field>

        {/* Aviso de reposição inteligente para itens em alerta. */}
        {isLow ? (
          <div className="flex items-start gap-2 rounded-lg border border-amber-200 bg-amber-50 p-3 text-amber-800">
            <AlertTriangle className="mt-0.5 h-4 w-4 flex-shrink-0" />
            <div className="text-sm">
              <p className="font-medium">Reposição necessária</p>
              <p className="text-amber-700">
                {selected.name} está abaixo do mínimo ({selected.currentAmount}/{selected.minimumStock}{" "}
                {selected.measurementUnit}). Pedido mínimo para reposição: {" "}
                <strong>
                  {minReorder} {selected.measurementUnit}
                </strong>
                .
              </p>
            </div>
          </div>
        ) : null}

        <Field
          label="Fornecedor"
          htmlFor="fornecedor"
          required
          error={errors.fornecedor}
          hint="Preenchido com o fornecedor padrão do ingrediente."
        >
          <Input
            id="fornecedor"
            value={form.fornecedor}
            error={errors.fornecedor}
            onChange={(e) => update("fornecedor", e.target.value)}
            placeholder="Ex: Distribuidora Central"
          />
        </Field>

        <Field
          label="Quantidade solicitada"
          htmlFor="amount"
          required
          error={errors.amount}
          hint={isLow ? `Mínimo para repor o estoque: ${minReorder} ${selected.measurementUnit}.` : undefined}
        >
          <Input
            id="amount"
            type="number"
            min={isLow ? minReorder : 1}
            value={form.amount}
            error={errors.amount}
            onChange={(e) => update("amount", e.target.value)}
            placeholder="Ex: 15"
            className={
              amountAmber
                ? "border-amber-400 bg-amber-50 focus:border-amber-500 focus:ring-amber-100"
                : ""
            }
          />
        </Field>

        <Field label="Canal de Envio" htmlFor="messageChannel" required>
          <Select
            id="messageChannel"
            value={form.messageChannel}
            onChange={(e) => update("messageChannel", e.target.value)}
          >
            <option value="WHATSAPP">WhatsApp</option>
            <option value="EMAIL">E-mail</option>
          </Select>
        </Field>

        {form.messageChannel === "WHATSAPP" ? (
          <Field label="Número do WhatsApp (Destinatário)" htmlFor="telefone" required error={errors.telefone}>
            <Input
              id="telefone"
              value={form.telefone}
              error={errors.telefone}
              onChange={(e) => update("telefone", e.target.value)}
              placeholder="Ex: (11) 98765-4321"
            />
          </Field>
        ) : (
          <Field label="E-mail do fornecedor (Destinatário)" htmlFor="email" required error={errors.email}>
            <Input
              id="email"
              type="email"
              value={form.email}
              error={errors.email}
              onChange={(e) => update("email", e.target.value)}
              placeholder="Ex: fornecedor@empresa.com"
            />
          </Field>
        )}

        {/* Pré-visualização da mensagem que será enviada ao verificar o pedido. */}
        {previewMessage ? (
          <div className="rounded-lg border border-slate-200 bg-slate-50 p-3">
            <div className="mb-1.5 flex items-center gap-1.5 text-xs font-medium text-slate-500">
              {form.messageChannel === "WHATSAPP" ? (
                <MessageCircle className="h-3.5 w-3.5" />
              ) : (
                <Mail className="h-3.5 w-3.5" />
              )}
              Pré-visualização da mensagem
            </div>
            <p className="text-sm leading-relaxed text-slate-700">{previewMessage}</p>
          </div>
        ) : null}
      </div>
    </Modal>
  )
}
