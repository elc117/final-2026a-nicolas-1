"use client"

import { useEffect, useState } from "react"
import { Modal } from "@/components/ui-lite/modal"
import { Field, Input, Select, Button } from "@/components/ui-lite/form-controls"

// Estado inicial vazio do formulário de ingrediente.
const EMPTY = {
  name: "",
  currentAmount: "",
  measurementUnit: "KG",
  minimumStock: "",
}

/**
 * Modal de criação/edição de ingrediente.
 *
 * Props:
 * - open, onClose: controle do modal
 * - initialData: objeto do ingrediente para edição (ou null para criar)
 * - onSubmit: function(payload) -> recebe o objeto pronto para enviar ao backend
 */
export function IngredientModal({ open, onClose, initialData, onSubmit }) {
  const [form, setForm] = useState(EMPTY)
  const [errors, setErrors] = useState({})

  const isEditing = Boolean(initialData)

  // Sincroniza o formulário ao abrir (preenche em edição, limpa em criação).
  useEffect(() => {
    if (open) {
      setForm(
        initialData
          ? {
              name: initialData.name ?? "",
              currentAmount: String(initialData.currentAmount ?? ""),
              measurementUnit: initialData.measurementUnit ?? "KG",
              minimumStock: String(initialData.minimumStock ?? ""),
            }
          : EMPTY,
      )
      setErrors({})
    }
  }, [open, initialData])

  function update(field, value) {
    setForm((prev) => ({ ...prev, [field]: value }))
  }

  // Validações visuais simples.
  function validate() {
    const next = {}
    if (!form.name.trim()) next.name = "Informe o nome do ingrediente."
    if (form.currentAmount === "" || Number(form.currentAmount) < 0)
      next.currentAmount = "Quantidade inválida."
    if (form.minimumStock === "" || Number(form.minimumStock) < 0)
      next.minimumStock = "Estoque mínimo inválido."
    setErrors(next)
    return Object.keys(next).length === 0
  }

  function handleSubmit(e) {
    e.preventDefault()
    if (!validate()) return

    // Objeto JSON pronto para envio via fetch/axios ao backend Java.
    const payload = {
      ...(isEditing ? { id: initialData.id } : {}),
      name: form.name.trim(),
      currentAmount: Number(form.currentAmount),
      measurementUnit: form.measurementUnit,
      minimumStock: Number(form.minimumStock),
    }

    onSubmit(payload)
  }

  return (
    <Modal
      open={open}
      onClose={onClose}
      title={isEditing ? "Editar Ingrediente" : "Adicionar Ingrediente"}
      description="Preencha as informações do item de estoque."
      footer={
        <>
          <Button type="button" variant="secondary" onClick={onClose}>
            Cancelar
          </Button>
          <Button type="submit" form="ingredient-form">
            {isEditing ? "Salvar alterações" : "Adicionar"}
          </Button>
        </>
      }
    >
      <form id="ingredient-form" onSubmit={handleSubmit} className="flex flex-col gap-4">
        <Field label="Nome do ingrediente" htmlFor="name" required error={errors.name}>
          <Input
            id="name"
            value={form.name}
            error={errors.name}
            onChange={(e) => update("name", e.target.value)}
            placeholder="Ex: Farinha de Trigo"
          />
        </Field>

        <div className="grid grid-cols-2 gap-4">
          <Field label="Quantidade atual" htmlFor="currentAmount" required error={errors.currentAmount}>
            <Input
              id="currentAmount"
              type="number"
              step="0.01"
              min="0"
              value={form.currentAmount}
              error={errors.currentAmount}
              onChange={(e) => update("currentAmount", e.target.value)}
              placeholder="0"
            />
          </Field>

          <Field label="Unidade de medida" htmlFor="measurementUnit">
            <Select
              id="measurementUnit"
              value={form.measurementUnit}
              onChange={(e) => update("measurementUnit", e.target.value)}
            >
              <option value="KG">KG — Quilograma</option>
              <option value="G">G — Grama</option>
              <option value="L">L — Litro</option>
              <option value="ML">ML — Mililitro</option>
              <option value="UN">Un. — Unidade</option>
            </Select>
          </Field>
        </div>

        <Field
          label="Estoque mínimo"
          htmlFor="minimumStock"
          required
          error={errors.minimumStock}
          hint="Quando a quantidade ficar abaixo disso, um alerta será exibido."
        >
          <Input
            id="minimumStock"
            type="number"
            step="0.01"
            min="0"
            value={form.minimumStock}
            error={errors.minimumStock}
            onChange={(e) => update("minimumStock", e.target.value)}
            placeholder="0"
          />
        </Field>
      </form>
    </Modal>
  )
}
