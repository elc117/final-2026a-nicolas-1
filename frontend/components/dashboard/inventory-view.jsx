"use client"

import { useEffect, useMemo, useState } from "react"
import { createIngredient, getIngredients, getIngredientById, updateIngredient, deleteIngredient} from "@/lib/api/ingredients"
import { createOrder } from "@/lib/api/orders"
import { Plus, Pencil, Trash2, AlertTriangle, Package, Search, ShoppingCart } from "lucide-react"
import { Badge } from "@/components/ui-lite/badge"
import { Button, Input, Select } from "@/components/ui-lite/form-controls"
import { ConfirmDialog } from "@/components/ui-lite/confirm-dialog"
import { Toast } from "@/components/ui-lite/toast"
import { IngredientModal } from "@/components/dashboard/ingredient-modal"
import { PurchaseOrderModal } from "@/components/dashboard/purchase-order-modal"
import { title } from "process"

// Opções do filtro de status do estoque.
const STATUS_FILTERS = [
  { value: "ALL", label: "Todos os status" },
  { value: "IN_STOCK", label: "Em Estoque" },
  { value: "LOW", label: "Estoque Baixo" },
]

export function InventoryView() {
  const [ingredients, setIngredients] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    async function loadIngredients() {
      try{
        const data = await getIngredients()
        setIngredients(data)
      } catch (err) {
        setError(err.message)
      } finally {
        setLoading(false)
      }
    }

    loadIngredients()
  }, [])

  const [modalOpen, setModalOpen] = useState(false)
  const [editing, setEditing] = useState(null)

  // Filtros (busca por nome + status).
  const [search, setSearch] = useState("")
  const [statusFilter, setStatusFilter] = useState("ALL")

  // Item aguardando confirmação de exclusão.
  const [toDelete, setToDelete] = useState(null)

  // Reposição Inteligente: modal de pedido + ingrediente para pré-preencher.
  const [orderModalOpen, setOrderModalOpen] = useState(false)
  const [prefillIngredient, setPrefillIngredient] = useState(null)

  // Notificação toast.
  const [toast, setToast] = useState(null)

  const lowStockCount = ingredients.filter((i) => i.currentAmount < i.minimumStock).length

  // Lista filtrada por nome e status, em tempo real.
  const filteredIngredients = useMemo(() => {
    const term = search.trim().toLowerCase()
    return ingredients.filter((i) => {
      const matchesText = !term || i.name.toLowerCase().includes(term)
      const isLow = i.currentAmount < i.minimumStock
      const matchesStatus =
        statusFilter === "ALL" ||
        (statusFilter === "LOW" && isLow) ||
        (statusFilter === "IN_STOCK" && !isLow)
      return matchesText && matchesStatus
    })
  }, [ingredients, search, statusFilter])

  function openCreate() {
    setEditing(null)
    setModalOpen(true)
  }

  function openEdit(ingredient) {
    setEditing(ingredient)
    setModalOpen(true)
  }

  // Confirma a exclusão após o usuário aceitar no diálogo.
  async function confirmDelete() {
    if (!toDelete) return
    try{
      await deleteIngredient(toDelete.id)
      setIngredients((prev) => prev.filter((i) => i.id !== toDelete.id))
    } catch (err) {
      setToast({ id: Date.now(), variant: "danger", title: "Erro", message: err.message })
    } finally {
      setToDelete(null)
    }

  }

  // Abre o modal de pedido a partir de um ingrediente (Reposição Inteligente).
  function openCreateOrder(ingredient) {
    setPrefillIngredient(ingredient)
    setOrderModalOpen(true)
  }

  // Recebe o pedido criado a partir do estoque.
  async function handleOrderSubmit(payload) {
    try {
      const backendPayload = {
        amount: payload.amount,
        date: new Date().toISOString().slice(0, 10),
        status: "PENDING",
        contact: payload.supplierContact,
        contactChannel: payload.messageChannel,
        ingredient: {
          id: Number(payload.ingredienteId),
        }
      }
      await createOrder(backendPayload)
      setOrderModalOpen(false)
      setPrefillIngredient(null)
      setToast({
        id: Date.now(),
        variant: "success",
        title: "Pedido criado",
        message: `Pedido de ${payload.amount} ${payload.unit} de ${payload.ingredientName} criado como pendente.`,
      })
    } catch (err) {
      setToast({
        id: Date.now(),
        variant: "danger",
        title: "Erro ao criar pedido",
        message: err.message,
      })
    }
  }

  async function handleSubmit(payload) {
    if (payload.id) {
      const updated = await updateIngredient(payload.id, payload)
      setIngredients((prev) => prev.map((i) => (i.id === updated.id ? updated : i)))
    } else {
      const created = await createIngredient(payload)
      setIngredients((prev) => [...prev, created])
    }
    setModalOpen(false)
  }

  return (
    <div className="flex flex-col gap-6">
      {/* Cabeçalho da seção */}
      <div className="flex flex-wrap items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-semibold text-slate-800">Estoque de Ingredientes</h2>
          <p className="text-sm text-slate-500">
            {ingredients.length} itens cadastrados
            {lowStockCount > 0 ? (
              <span className="ml-1 text-red-500">· {lowStockCount} com estoque baixo</span>
            ) : null}
          </p>
        </div>
        <Button onClick={openCreate}>
          <Plus className="h-4 w-4" />
          Adicionar Ingrediente
        </Button>
      </div>

      {/* Tabela */}
      <div className="overflow-hidden rounded-xl border border-slate-200 bg-white">
        {/* Barra de filtros */}
        <div className="flex flex-col gap-3 border-b border-slate-200 p-4 sm:flex-row sm:items-center sm:justify-between">
          <div className="relative w-full sm:max-w-xs">
            <Search className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
            <Input
              type="search"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Buscar por nome do ingrediente..."
              className="pl-9"
              aria-label="Buscar ingredientes por nome"
            />
          </div>
          <div className="w-full sm:w-56">
            <Select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              aria-label="Filtrar por status do estoque"
            >
              {STATUS_FILTERS.map((s) => (
                <option key={s.value} value={s.value}>
                  {s.label}
                </option>
              ))}
            </Select>
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead className="border-b border-slate-200 bg-slate-50 text-xs uppercase tracking-wide text-slate-500">
              <tr>
                <th className="px-4 py-3 font-medium">ID</th>
                <th className="px-4 py-3 font-medium">Nome</th>
                <th className="px-4 py-3 font-medium">Qtd. Atual</th>
                <th className="px-4 py-3 font-medium">Status</th>
                <th className="px-4 py-3 text-right font-medium">Ações</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {filteredIngredients.map((item) => {
                const isLow = item.currentAmount < item.minimumStock
                return (
                  <tr key={item.id} className="transition-colors hover:bg-slate-50">
                    <td className="px-4 py-3 font-mono text-xs text-slate-400">
                      #{String(item.id).padStart(3, "0")}
                    </td>
                    <td className="px-4 py-3 font-medium text-slate-800">{item.name}</td>
                    <td className="px-4 py-3">
                      <span className={isLow ? "font-semibold text-red-600" : "text-slate-700"}>
                        {item.currentAmount} {item.measurementUnit}
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      {isLow ? (
                        <Badge variant="danger">
                          <AlertTriangle className="h-3 w-3" />
                          Estoque Baixo
                        </Badge>
                      ) : (
                        <Badge variant="success">Em Estoque</Badge>
                      )}
                    </td>
                    <td className="px-4 py-3">
                      <div className="flex items-center justify-end gap-1">
                        <Button
                          variant={isLow ? "primary" : "outline"}
                          size="sm"
                          onClick={() => openCreateOrder(item)}
                          aria-label={`Criar pedido de compra para ${item.name}`}
                        >
                          <ShoppingCart className="h-4 w-4" />
                          Criar Pedido
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => openEdit(item)}
                          aria-label={`Editar ${item.name}`}
                        >
                          <Pencil className="h-4 w-4" />
                          Editar
                        </Button>
                        <Button
                          variant="danger"
                          size="sm"
                          onClick={() => setToDelete(item)}
                          aria-label={`Excluir ${item.name}`}
                        >
                          <Trash2 className="h-4 w-4" />
                          Excluir
                        </Button>
                      </div>
                    </td>
                  </tr>
                )
              })}

              {filteredIngredients.length === 0 ? (
                <tr>
                  <td colSpan={5} className="px-4 py-12 text-center">
                    <div className="flex flex-col items-center gap-2 text-slate-400">
                      <Package className="h-8 w-8" />
                      <p className="text-sm">
                        {ingredients.length === 0
                          ? "Nenhum ingrediente cadastrado."
                          : "Nenhum ingrediente encontrado para os filtros selecionados."}
                      </p>
                    </div>
                  </td>
                </tr>
              ) : null}
            </tbody>
          </table>
        </div>
      </div>

      <IngredientModal
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        initialData={editing}
        onSubmit={handleSubmit}
      />

      {/* Modal de pedido com Reposição Inteligente (pré-preenchido pelo ingrediente). */}
      <PurchaseOrderModal
        open={orderModalOpen}
        onClose={() => {
          setOrderModalOpen(false)
          setPrefillIngredient(null)
        }}
        ingredientes={ingredients}
        onSubmit={handleOrderSubmit}
        prefillIngredient={prefillIngredient}
      />

      {/* Confirmação de exclusão */}
      <ConfirmDialog
        open={Boolean(toDelete)}
        onClose={() => setToDelete(null)}
        onConfirm={confirmDelete}
        title="Excluir Ingrediente"
      >
        {toDelete ? (
          <p>
            Deseja realmente excluir o ingrediente{" "}
            <strong className="text-slate-800">{toDelete.name}</strong> (Quantidade:{" "}
            <strong className="text-slate-800">
              {toDelete.currentAmount} {toDelete.measurementUnit}
            </strong>
            )? Esta ação não pode ser desfeita.
          </p>
        ) : null}
      </ConfirmDialog>

      {/* Notificação */}
      <Toast toast={toast} onClose={() => setToast(null)} />
    </div>
  )
}
