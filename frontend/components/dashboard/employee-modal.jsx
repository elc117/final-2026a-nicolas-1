"use client"

import { useEffect, useState } from "react"
import { User, KeyRound } from "lucide-react"
import { Modal } from "@/components/ui-lite/modal"
import { Field, Input, Select, Switch, Button } from "@/components/ui-lite/form-controls"

const EMPTY = {
  nome: "",
  sobrenome: "",
  cpf: "",
  cargo: "",
  temAcesso: false,
  login: "",
  senha: "",
  perfilAcesso: "GARCOM",
}

// Máscara simples de CPF: 000.000.000-00
function maskCpf(value) {
  const digits = value.replace(/\D/g, "").slice(0, 11)
  return digits
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d{1,2})$/, "$1-$2")
}

/**
 * Modal de cadastro/edição de funcionário, dividido em:
 *  - Parte 1: Dados Pessoais
 *  - Parte 2: Acesso ao Sistema (opcional, controlado por switch)
 */
export function EmployeeModal({ open, onClose, initialData, onSubmit }) {
  const [form, setForm] = useState(EMPTY)
  const [errors, setErrors] = useState({})

  const isEditing = Boolean(initialData)

  useEffect(() => {
    if (open) {
      setForm(
        initialData
          ? {
              nome: initialData.nome ?? "",
              sobrenome: initialData.sobrenome ?? "",
              cpf: initialData.cpf ?? "",
              cargo: initialData.cargo ?? "",
              temAcesso: Boolean(initialData.login),
              login: initialData.login ?? "",
              senha: "",
              perfilAcesso: initialData.perfilAcesso ?? "GARCOM",
            }
          : EMPTY,
      )
      setErrors({})
    }
  }, [open, initialData])

  function update(field, value) {
    setForm((prev) => ({ ...prev, [field]: value }))
  }

  function validate() {
    const next = {}
    if (!form.nome.trim()) next.nome = "Informe o nome."
    if (!form.sobrenome.trim()) next.sobrenome = "Informe o sobrenome."
    if (form.cpf.replace(/\D/g, "").length !== 11) next.cpf = "CPF deve conter 11 dígitos."
    if (!form.cargo.trim()) next.cargo = "Informe o cargo."

    if (form.temAcesso) {
      if (!form.login.trim()) next.login = "Informe o login."
      if (!isEditing && !form.senha.trim()) next.senha = "Informe a senha."
    }

    setErrors(next)
    return Object.keys(next).length === 0
  }

  function handleSubmit(e) {
    e.preventDefault()
    if (!validate()) return

    // Monta o objeto JSON para envio ao backend Java.
    const payload = {
      ...(isEditing ? { id: initialData.id } : {}),
      nome: form.nome.trim(),
      sobrenome: form.sobrenome.trim(),
      cpf: form.cpf,
      cargo: form.cargo.trim(),
      acesso: form.temAcesso
        ? {
            login: form.login.trim(),
            senha: form.senha,
            perfilAcesso: form.perfilAcesso,
          }
        : null,
    }

    onSubmit(payload)
  }

  return (
    <Modal
      open={open}
      onClose={onClose}
      title={isEditing ? "Editar Funcionário" : "Cadastrar Funcionário"}
      description="Dados pessoais e, opcionalmente, acesso ao sistema."
      footer={
        <>
          <Button type="button" variant="secondary" onClick={onClose}>
            Cancelar
          </Button>
          <Button type="submit" form="employee-form">
            {isEditing ? "Salvar alterações" : "Cadastrar"}
          </Button>
        </>
      }
    >
      <form id="employee-form" onSubmit={handleSubmit} className="flex flex-col gap-6">
        {/* Parte 1 — Dados Pessoais */}
        <section className="flex flex-col gap-4">
          <div className="flex items-center gap-2 text-sm font-semibold text-slate-700">
            <User className="h-4 w-4 text-emerald-600" />
            Dados Pessoais
          </div>

          <div className="grid grid-cols-2 gap-4">
            <Field label="Nome" htmlFor="nome" required error={errors.nome}>
              <Input
                id="nome"
                value={form.nome}
                error={errors.nome}
                onChange={(e) => update("nome", e.target.value)}
                placeholder="Ex: Maria"
              />
            </Field>

            <Field label="Sobrenome" htmlFor="sobrenome" required error={errors.sobrenome}>
              <Input
                id="sobrenome"
                value={form.sobrenome}
                error={errors.sobrenome}
                onChange={(e) => update("sobrenome", e.target.value)}
                placeholder="Ex: da Silva"
              />
            </Field>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <Field label="CPF" htmlFor="cpf" required error={errors.cpf}>
              <Input
                id="cpf"
                value={form.cpf}
                error={errors.cpf}
                onChange={(e) => update("cpf", maskCpf(e.target.value))}
                placeholder="000.000.000-00"
                inputMode="numeric"
              />
            </Field>

            <Field label="Cargo" htmlFor="cargo" required error={errors.cargo}>
              <Input
                id="cargo"
                value={form.cargo}
                error={errors.cargo}
                onChange={(e) => update("cargo", e.target.value)}
                placeholder="Ex: Cozinheiro(a)"
              />
            </Field>
          </div>
        </section>

        {/* Parte 2 — Acesso ao Sistema (opcional) */}
        <section className="flex flex-col gap-4 border-t border-slate-100 pt-5">
          <div className="flex items-center gap-2 text-sm font-semibold text-slate-700">
            <KeyRound className="h-4 w-4 text-emerald-600" />
            Acesso ao Sistema
          </div>

          <Switch
            id="temAcesso"
            checked={form.temAcesso}
            onChange={(v) => update("temAcesso", v)}
            label="Liberar acesso ao sistema?"
            description="Cria credenciais de login para este funcionário."
          />

          {form.temAcesso ? (
            <div className="flex flex-col gap-4 rounded-lg bg-slate-50 p-4">
              <div className="grid grid-cols-2 gap-4">
                <Field label="Login" htmlFor="login" required error={errors.login}>
                  <Input
                    id="login"
                    value={form.login}
                    error={errors.login}
                    onChange={(e) => update("login", e.target.value)}
                    placeholder="Ex: maria.silva"
                    autoComplete="off"
                  />
                </Field>

                <Field
                  label="Senha"
                  htmlFor="senha"
                  required={!isEditing}
                  error={errors.senha}
                  hint={isEditing ? "Deixe em branco para manter a atual." : undefined}
                >
                  <Input
                    id="senha"
                    type="password"
                    value={form.senha}
                    error={errors.senha}
                    onChange={(e) => update("senha", e.target.value)}
                    placeholder="••••••••"
                    autoComplete="new-password"
                  />
                </Field>
              </div>

              <Field label="Perfil de acesso" htmlFor="perfilAcesso">
                <Select
                  id="perfilAcesso"
                  value={form.perfilAcesso}
                  onChange={(e) => update("perfilAcesso", e.target.value)}
                >
                  <option value="ADMIN">Admin</option>
                  <option value="COZINHA">Cozinha</option>
                  <option value="GARCOM">Garçom</option>
                </Select>
              </Field>
            </div>
          ) : null}
        </section>
      </form>
    </Modal>
  )
}
