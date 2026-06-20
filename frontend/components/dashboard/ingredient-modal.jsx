"use client"

import { useEffect, useState } from "react"
import { Modal } from "@/components/ui-lite/modal"
import { Field, Input, Select, Button } from "@/components/ui-lite/form-controls"

// Estado inicial vazio do formulário de ingrediente.
const EMPTY = {
  nome: "",
  quantidadeAtual: "",
  unidadeMedida: "KG",
  estoqueMinimo: "",
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
              nome: initialData.nome ?? "",
              quantidadeAtual: String(initialData.quantidadeAtual ?? ""),
              unidadeMedida: initialData.unidadeMedida ?? "KG",
              estoqueMinimo: String(initialData.estoqueMinimo ?? ""),
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
    if (!form.nome.trim()) next.nome = "Informe o nome do ingrediente."
    if (form.quantidadeAtual === "" || Number(form.quantidadeAtual) < 0)
      next.quantidadeAtual = "Quantidade inválida."
    if (form.estoqueMinimo === "" || Number(form.estoqueMinimo) < 0)
      next.estoqueMinimo = "Estoque mínimo inválido."
    setErrors(next)
    return Object.keys(next).length === 0
  }

  function handleSubmit(e) {
    e.preventDefault()
    if (!validate()) return

    // Objeto JSON pronto para envio via fetch/axios ao backend Java.
    const payload = {
      ...(isEditing ? { id: initialData.id } : {}),
      nome: form.nome.trim(),
      quantidadeAtual: Number(form.quantidadeAtual),
      unidadeMedida: form.unidadeMedida,
      estoqueMinimo: Number(form.estoqueMinimo),
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
        <Field label="Nome do ingrediente" htmlFor="nome" required error={errors.nome}>
          <Input
            id="nome"
            value={form.nome}
            error={errors.nome}
            onChange={(e) => update("nome", e.target.value)}
            placeholder="Ex: Farinha de Trigo"
          />
        </Field>

        <div className="grid grid-cols-2 gap-4">
          <Field label="Quantidade atual" htmlFor="quantidadeAtual" required error={errors.quantidadeAtual}>
            <Input
              id="quantidadeAtual"
              type="number"
              step="0.01"
              min="0"
              value={form.quantidadeAtual}
              error={errors.quantidadeAtual}
              onChange={(e) => update("quantidadeAtual", e.target.value)}
              placeholder="0"
            />
          </Field>

          <Field label="Unidade de medida" htmlFor="unidadeMedida">
            <Select
              id="unidadeMedida"
              value={form.unidadeMedida}
              onChange={(e) => update("unidadeMedida", e.target.value)}
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
          htmlFor="estoqueMinimo"
          required
          error={errors.estoqueMinimo}
          hint="Quando a quantidade ficar abaixo disso, um alerta será exibido."
        >
          <Input
            id="estoqueMinimo"
            type="number"
            step="0.01"
            min="0"
            value={form.estoqueMinimo}
            error={errors.estoqueMinimo}
            onChange={(e) => update("estoqueMinimo", e.target.value)}
            placeholder="0"
          />
        </Field>
      </form>
    </Modal>
  )
}
