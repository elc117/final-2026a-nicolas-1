"use client"

import { useMemo, useState } from "react"
import { Plus, Pencil, Trash2, Users, Search, UserCircle } from "lucide-react"
import { Badge } from "@/components/ui-lite/badge"
import { Button, Input, Select } from "@/components/ui-lite/form-controls"
import { ConfirmDialog } from "@/components/ui-lite/confirm-dialog"
import { EmployeeModal } from "@/components/dashboard/employee-modal"

// Dados de exemplo (mock). Em produção viriam do backend Java.
const INITIAL_EMPLOYEES = [
  { id: 1, nome: "Carlos", sobrenome: "Mendes", cpf: "123.456.789-00", cargo: "Gerente", login: "carlos.mendes", perfilAcesso: "ADMIN" },
  { id: 2, nome: "Ana", sobrenome: "Souza", cpf: "987.654.321-00", cargo: "Cozinheira", login: "ana.souza", perfilAcesso: "COZINHA" },
  { id: 3, nome: "Pedro", sobrenome: "Lima", cpf: "456.789.123-00", cargo: "Garçom", login: null, perfilAcesso: null },
  { id: 4, nome: "Juliana", sobrenome: "Alves", cpf: "321.654.987-00", cargo: "Auxiliar de Cozinha", login: null, perfilAcesso: null },
]

const PERFIL_LABEL = { ADMIN: "Admin", COZINHA: "Cozinha", GARCOM: "Garçom" }

export function EmployeesView() {
  const [employees, setEmployees] = useState(INITIAL_EMPLOYEES)
  const [modalOpen, setModalOpen] = useState(false)
  const [editing, setEditing] = useState(null)

  // Filtros: busca por nome, busca por usuário/login e filtro por cargo.
  const [searchName, setSearchName] = useState("")
  const [searchLogin, setSearchLogin] = useState("")
  const [cargoFilter, setCargoFilter] = useState("ALL")

  // Funcionário aguardando confirmação de exclusão.
  const [toDelete, setToDelete] = useState(null)

  // Lista de cargos disponíveis para o filtro (derivada dos dados).
  const cargoOptions = useMemo(() => {
    const unique = Array.from(new Set(employees.map((e) => e.cargo))).sort()
    return ["ALL", ...unique]
  }, [employees])

  // Lista filtrada de forma combinada e em tempo real.
  const filteredEmployees = useMemo(() => {
    const nameTerm = searchName.trim().toLowerCase()
    const loginTerm = searchLogin.trim().toLowerCase()
    return employees.filter((e) => {
      const fullName = `${e.nome} ${e.sobrenome}`.toLowerCase()
      const matchesName = !nameTerm || fullName.includes(nameTerm)
      const matchesLogin = !loginTerm || (e.login || "").toLowerCase().includes(loginTerm)
      const matchesCargo = cargoFilter === "ALL" || e.cargo === cargoFilter
      return matchesName && matchesLogin && matchesCargo
    })
  }, [employees, searchName, searchLogin, cargoFilter])

  function openCreate() {
    setEditing(null)
    setModalOpen(true)
  }

  function openEdit(employee) {
    setEditing(employee)
    setModalOpen(true)
  }

  // Confirma a exclusão após o usuário aceitar no diálogo.
  function confirmDelete() {
    if (!toDelete) return
    // Em produção: DELETE /api/funcionarios/{id}
    setEmployees((prev) => prev.filter((e) => e.id !== toDelete.id))
    setToDelete(null)
  }

  function handleSubmit(payload) {
    // Normaliza o objeto do acesso para a lista local.
    const normalized = {
      nome: payload.nome,
      sobrenome: payload.sobrenome,
      cpf: payload.cpf,
      cargo: payload.cargo,
      login: payload.acesso ? payload.acesso.login : null,
      perfilAcesso: payload.acesso ? payload.acesso.perfilAcesso : null,
    }

    if (payload.id) {
      setEmployees((prev) => prev.map((e) => (e.id === payload.id ? { ...e, ...normalized } : e)))
    } else {
      const nextId = employees.length ? Math.max(...employees.map((e) => e.id)) + 1 : 1
      setEmployees((prev) => [...prev, { id: nextId, ...normalized }])
    }
    setModalOpen(false)
  }

  return (
    <div className="flex flex-col gap-6">
      <div className="flex flex-wrap items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-semibold text-slate-800">Gerenciamento de Funcionários</h2>
          <p className="text-sm text-slate-500">{employees.length} funcionários cadastrados</p>
        </div>
        <Button onClick={openCreate}>
          <Plus className="h-4 w-4" />
          Cadastrar Funcionário
        </Button>
      </div>

      <div className="overflow-hidden rounded-xl border border-slate-200 bg-white">
        {/* Barra de filtros */}
        <div className="grid grid-cols-1 gap-3 border-b border-slate-200 p-4 md:grid-cols-3">
          <div className="relative">
            <Search className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
            <Input
              type="search"
              value={searchName}
              onChange={(e) => setSearchName(e.target.value)}
              placeholder="Buscar por nome..."
              className="pl-9"
              aria-label="Buscar funcionários por nome"
            />
          </div>
          <div className="relative">
            <UserCircle className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
            <Input
              type="search"
              value={searchLogin}
              onChange={(e) => setSearchLogin(e.target.value)}
              placeholder="Buscar por usuário/login..."
              className="pl-9"
              aria-label="Buscar funcionários por usuário ou login"
            />
          </div>
          <Select
            value={cargoFilter}
            onChange={(e) => setCargoFilter(e.target.value)}
            aria-label="Filtrar por cargo"
          >
            {cargoOptions.map((cargo) => (
              <option key={cargo} value={cargo}>
                {cargo === "ALL" ? "Todos os cargos" : cargo}
              </option>
            ))}
          </Select>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead className="border-b border-slate-200 bg-slate-50 text-xs uppercase tracking-wide text-slate-500">
              <tr>
                <th className="px-4 py-3 font-medium">ID</th>
                <th className="px-4 py-3 font-medium">Nome</th>
                <th className="px-4 py-3 font-medium">CPF</th>
                <th className="px-4 py-3 font-medium">Cargo</th>
                <th className="px-4 py-3 font-medium">Usuário Vinculado</th>
                <th className="px-4 py-3 text-right font-medium">Ações</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {filteredEmployees.map((emp) => (
                <tr key={emp.id} className="transition-colors hover:bg-slate-50">
                  <td className="px-4 py-3 font-mono text-xs text-slate-400">
                    #{String(emp.id).padStart(3, "0")}
                  </td>
                  <td className="px-4 py-3 font-medium text-slate-800">{`${emp.nome} ${emp.sobrenome}`}</td>
                  <td className="px-4 py-3 font-mono text-xs text-slate-500">{emp.cpf}</td>
                  <td className="px-4 py-3 text-slate-600">{emp.cargo}</td>
                  <td className="px-4 py-3">
                    {emp.login ? (
                      <div className="flex items-center gap-2">
                        <span className="font-mono text-xs text-slate-700">{emp.login}</span>
                        <Badge variant="info">{PERFIL_LABEL[emp.perfilAcesso]}</Badge>
                      </div>
                    ) : (
                      <Badge variant="neutral">Sem Acesso ao Sistema</Badge>
                    )}
                  </td>
                  <td className="px-4 py-3">
                    <div className="flex items-center justify-end gap-1">
                      <Button variant="ghost" size="sm" onClick={() => openEdit(emp)} aria-label={`Editar ${emp.nome} ${emp.sobrenome}`}>
                        <Pencil className="h-4 w-4" />
                        Editar
                      </Button>
                      <Button variant="danger" size="sm" onClick={() => setToDelete(emp)} aria-label={`Excluir ${emp.nome} ${emp.sobrenome}`}>
                        <Trash2 className="h-4 w-4" />
                        Excluir
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}

              {filteredEmployees.length === 0 ? (
                <tr>
                  <td colSpan={6} className="px-4 py-12 text-center">
                    <div className="flex flex-col items-center gap-2 text-slate-400">
                      <Users className="h-8 w-8" />
                      <p className="text-sm">
                        {employees.length === 0
                          ? "Nenhum funcionário cadastrado."
                          : "Nenhum funcionário encontrado para os filtros selecionados."}
                      </p>
                    </div>
                  </td>
                </tr>
              ) : null}
            </tbody>
          </table>
        </div>
      </div>

      <EmployeeModal
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        initialData={editing}
        onSubmit={handleSubmit}
      />

      {/* Confirmação de exclusão */}
      <ConfirmDialog
        open={Boolean(toDelete)}
        onClose={() => setToDelete(null)}
        onConfirm={confirmDelete}
        title="Excluir Funcionário"
      >
        {toDelete ? (
          <p>
            Deseja realmente excluir o funcionário{" "}
            <strong className="text-slate-800">{`${toDelete.nome} ${toDelete.sobrenome}`}</strong> (CPF:{" "}
            <strong className="text-slate-800">{toDelete.cpf}</strong> | Cargo:{" "}
            <strong className="text-slate-800">{toDelete.cargo}</strong>)? Esta ação não pode ser
            desfeita.
          </p>
        ) : null}
      </ConfirmDialog>
    </div>
  )
}
