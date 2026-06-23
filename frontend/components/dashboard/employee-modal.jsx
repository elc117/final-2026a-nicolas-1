"use client"

import { useEffect, useState } from "react"
import { User, KeyRound } from "lucide-react"
import { Modal } from "@/components/ui-lite/modal"
import { Field, Input, Select, Switch, Button } from "@/components/ui-lite/form-controls"

const EMPTY = {
  name: "",
  surname: "",
  cpf: "",
  role: "",
  hasAccess: false,
  login: "",
  password: "",
  accessProfile: "GENERAL",
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
              name: initialData.name ?? "",
              surname: initialData.surname ?? "",
              cpf: initialData.cpf ?? "",
              role: initialData.role ?? "",
              hasAccess: initialData.hasAccess ?? false,
              login: initialData.user?.login ?? "",
              password: "",
              accessProfile: initialData.user?.accessProfile ?? "GENERAL",
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
    if (!form.name.trim()) next.name = "Informe o nome."
    if (!form.surname.trim()) next.surname = "Informe o sobrenome."
    if (form.cpf.replace(/\D/g, "").length !== 11) next.cpf = "CPF deve conter 11 dígitos."
    if (!form.role.trim()) next.role = "Informe o cargo."

    if (form.hasAccess) {
      if (!form.login.trim()) next.login = "Informe o login."
      if (!isEditing && !form.password.trim()) next.password = "Informe a senha."
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
      name: form.name.trim(),
      surname: form.surname.trim(),
      cpf: form.cpf,
      role: form.role.trim(),
      hasAccess: form.hasAccess,
      user: form.hasAccess
        ? {
            ...(isEditing && initialData.user ? { id: initialData.user.id } : {}),
            login: form.login.trim(),
            password: form.password,
            accessProfile: form.accessProfile,
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
            <Field label="Nome" htmlFor="nome" required error={errors.name}>
              <Input
                id="nome"
                value={form.name}
                error={errors.name}
                onChange={(e) => update("name", e.target.value)}
                placeholder="Ex: Maria"
              />
            </Field>

            <Field label="Sobrenome" htmlFor="sobrenome" required error={errors.surname}>
              <Input
                id="sobrenome"
                value={form.surname}
                error={errors.surname}
                onChange={(e) => update("surname", e.target.value)}
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

            <Field label="Cargo" htmlFor="cargo" required error={errors.role}>
              <Input
                id="cargo"
                value={form.role}
                error={errors.role}
                onChange={(e) => update("role", e.target.value)}
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
            id="hasAccess"
            checked={form.hasAccess}
            onChange={(v) => update("hasAccess", v)}
            label="Liberar acesso ao sistema?"
            description="Cria credenciais de login para este funcionário."
          />

          {form.hasAccess ? (
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
                  error={errors.password}
                  hint={isEditing ? "Deixe em branco para manter a atual." : undefined}
                >
                  <Input
                    id="senha"
                    type="password"
                    value={form.password}
                    error={errors.password}
                    onChange={(e) => update("password", e.target.value)}
                    placeholder="••••••••"
                    autoComplete="new-password"
                  />
                </Field>
              </div>

                <Field label="Perfil de acesso" htmlFor="perfilAcesso">
                <Select
                  id="accessProfile"
                  value={form.accessProfile}
                  onChange={(e) => update("accessProfile", e.target.value)}
                >
                  <option value="ADMIN">Admin</option>
                  <option value="CHEF">Chefe de Cozinha</option>
                  <option value="GENERAL">Geral</option>
                </Select>
              </Field>
            </div>
          ) : null}
        </section>
      </form>
    </Modal>
  )
}
