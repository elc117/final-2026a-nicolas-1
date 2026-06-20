"use client"

import { useMemo, useState } from "react"
import {
  Plus,
  ShoppingCart,
  Clock,
  CheckCircle2,
  PackageCheck,
  MessageCircle,
  Mail,
  Pencil,
  ClipboardCheck,
  Search,
  Send,
  XCircle,
} from "lucide-react"
import { Badge } from "@/components/ui-lite/badge"
import { Button, Input, Select } from "@/components/ui-lite/form-controls"
import { Modal } from "@/components/ui-lite/modal"
import { Toast } from "@/components/ui-lite/toast"
import { PurchaseOrderModal } from "@/components/dashboard/purchase-order-modal"

// Mesma base de ingredientes usada no estoque (mock). Em produção viria do backend.
const INGREDIENTES = [
  { id: 1, nome: "Farinha de Trigo", unidadeMedida: "KG", quantidadeAtual: 45, estoqueMinimo: 20 },
  { id: 2, nome: "Azeite Extra Virgem", unidadeMedida: "L", quantidadeAtual: 3, estoqueMinimo: 10 },
  { id: 3, nome: "Tomate", unidadeMedida: "KG", quantidadeAtual: 8, estoqueMinimo: 15 },
  { id: 4, nome: "Queijo Mussarela", unidadeMedida: "KG", quantidadeAtual: 22, estoqueMinimo: 12 },
  { id: 5, nome: "Ovos", unidadeMedida: "UN", quantidadeAtual: 180, estoqueMinimo: 60 },
  { id: 6, nome: "Leite Integral", unidadeMedida: "L", quantidadeAtual: 5, estoqueMinimo: 18 },
]

/**
 * Estrutura de dados alinhada ao backend:
 * { id, ingredientName, amount, unit, messageChannel: 'WHATSAPP'|'EMAIL',
 *   supplierContact, orderDate, status: 'PENDING'|'SENT'|'CANCELED'|'COMPLETED' }
 */
const INITIAL_ORDERS = [
  {
    id: 1,
    ingredientName: "Tomate",
    amount: 30,
    unit: "KG",
    messageChannel: "WHATSAPP",
    supplierContact: "(11) 98765-4321",
    orderDate: "2026-06-18",
    status: "SENT",
  },
  {
    id: 2,
    ingredientName: "Azeite Extra Virgem",
    amount: 20,
    unit: "L",
    messageChannel: "EMAIL",
    supplierContact: "fornecedor@azeites.com",
    orderDate: "2026-06-19",
    status: "PENDING",
  },
  {
    id: 3,
    ingredientName: "Leite Integral",
    amount: 40,
    unit: "L",
    messageChannel: "WHATSAPP",
    supplierContact: "(11) 91234-5678",
    orderDate: "2026-06-20",
    status: "PENDING",
  },
]

// Configuração visual de cada status.
const STATUS_CONFIG = {
  PENDING: { variant: "warning", label: "Pendente" },
  SENT: { variant: "info", label: "Enviado" },
  CANCELED: { variant: "danger", label: "Cancelado" },
  COMPLETED: { variant: "success", label: "Concluído" },
}

const STATUS_FILTERS = [
  { value: "ALL", label: "Todos os status" },
  { value: "PENDING", label: "Pendente" },
  { value: "SENT", label: "Enviado" },
  { value: "CANCELED", label: "Cancelado" },
  { value: "COMPLETED", label: "Concluído" },
]

function formatDate(iso) {
  const [y, m, d] = iso.split("-")
  return `${d}/${m}/${y}`
}

function channelLabel(channel) {
  return channel === "WHATSAPP" ? "WhatsApp" : "E-mail"
}

// Card de métrica reutilizável.
function MetricCard({ icon: Icon, label, value, accent }) {
  return (
    <div className="flex items-center gap-4 rounded-xl border border-slate-200 bg-white p-4">
      <div className={`flex h-11 w-11 flex-shrink-0 items-center justify-center rounded-lg ${accent}`}>
        <Icon className="h-5 w-5" />
      </div>
      <div>
        <p className="text-xs font-medium uppercase tracking-wide text-slate-500">{label}</p>
        <p className="text-2xl font-semibold text-slate-800">{value}</p>
      </div>
    </div>
  )
}

export function PurchaseOrdersView() {
  const [orders, setOrders] = useState(INITIAL_ORDERS)

  // Modal de criação/edição.
  const [formOpen, setFormOpen] = useState(false)
  const [editingOrder, setEditingOrder] = useState(null)

  // Diálogo de verificação (máquina de estados).
  const [verifyOrder, setVerifyOrder] = useState(null)

  // Filtros.
  const [search, setSearch] = useState("")
  const [statusFilter, setStatusFilter] = useState("ALL")

  // Notificação toast.
  const [toast, setToast] = useState(null)

  const total = orders.length
  const pending = orders.filter((o) => o.status === "PENDING").length
  const sent = orders.filter((o) => o.status === "SENT").length
  const completed = orders.filter((o) => o.status === "COMPLETED").length

  // Lista filtrada por nome do ingrediente e status.
  const filteredOrders = useMemo(() => {
    const term = search.trim().toLowerCase()
    return orders.filter((o) => {
      const matchesText = !term || o.ingredientName.toLowerCase().includes(term)
      const matchesStatus = statusFilter === "ALL" || o.status === statusFilter
      return matchesText && matchesStatus
    })
  }, [orders, search, statusFilter])

  function openCreate() {
    setEditingOrder(null)
    setFormOpen(true)
  }

  function openEdit(order) {
    setEditingOrder(order)
    setFormOpen(true)
  }

  // Cria (PENDING) ou atualiza um pedido a partir do modal.
  function handleFormSubmit(payload) {
    if (editingOrder) {
      // Em produção: PUT /api/pedidos/{id}
      setOrders((prev) =>
        prev.map((o) => (o.id === editingOrder.id ? { ...o, ...payload } : o)),
      )
      setToast({ id: Date.now(), variant: "info", title: "Pedido atualizado", message: "As alterações foram salvas." })
    } else {
      // Em produção: POST /api/pedidos -> nasce sempre como PENDING.
      const nextId = orders.length ? Math.max(...orders.map((o) => o.id)) + 1 : 1
      const novo = {
        id: nextId,
        ...payload,
        orderDate: new Date().toISOString().slice(0, 10),
        status: "PENDING",
      }
      setOrders((prev) => [novo, ...prev])
      setToast({ id: Date.now(), variant: "info", title: "Pedido criado", message: "Cadastrado como pendente. Verifique para enviar." })
    }
    setFormOpen(false)
    setEditingOrder(null)
  }

  // Confirma o envio (PENDING -> SENT) e dispara o "envio automático".
  function confirmSend(order) {
    // Em produção: PATCH /api/pedidos/{id}/status { status: 'SENT' }
    setOrders((prev) => prev.map((o) => (o.id === order.id ? { ...o, status: "SENT" } : o)))
    setVerifyOrder(null)
    setToast({
      id: Date.now(),
      variant: "success",
      title: "Pedido enviado com sucesso",
      message: `Enviado automaticamente via ${channelLabel(order.messageChannel)} para ${order.supplierContact}.`,
    })
  }

  // Cancela o pedido (PENDING -> CANCELED).
  function cancelOrder(order) {
    // Em produção: PATCH /api/pedidos/{id}/status { status: 'CANCELED' }
    setOrders((prev) => prev.map((o) => (o.id === order.id ? { ...o, status: "CANCELED" } : o)))
    setVerifyOrder(null)
    setToast({ id: Date.now(), variant: "error", title: "Pedido cancelado", message: "O pedido não será enviado ao fornecedor." })
  }

  // Confirma o recebimento (SENT -> COMPLETED).
  function confirmReceipt(order) {
    // Em produção: PATCH /api/pedidos/{id}/status { status: 'COMPLETED' }
    setOrders((prev) => prev.map((o) => (o.id === order.id ? { ...o, status: "COMPLETED" } : o)))
    setToast({
      id: Date.now(),
      variant: "success",
      title: "Recebimento confirmado",
      message: `${order.ingredientName} foi adicionado ao estoque.`,
    })
  }

  return (
    <div className="flex flex-col gap-6">
      {/* Cabeçalho */}
      <div className="flex flex-wrap items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-semibold text-slate-800">Painel de Pedidos de Compra</h2>
          <p className="text-sm text-slate-500">
            Gerencie o ciclo de vida dos pedidos de ingredientes aos fornecedores.
          </p>
        </div>
        <Button onClick={openCreate}>
          <Plus className="h-4 w-4" />
          Novo Pedido
        </Button>
      </div>

      {/* Métricas rápidas */}
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <MetricCard icon={ShoppingCart} label="Total de Pedidos" value={total} accent="bg-slate-100 text-slate-600" />
        <MetricCard icon={Clock} label="Pendentes" value={pending} accent="bg-amber-100 text-amber-600" />
        <MetricCard icon={CheckCircle2} label="Enviados" value={sent} accent="bg-blue-100 text-blue-600" />
        <MetricCard icon={PackageCheck} label="Concluídos" value={completed} accent="bg-emerald-100 text-emerald-600" />
      </div>

      {/* Tabela de pedidos */}
      <div className="overflow-hidden rounded-xl border border-slate-200 bg-white">
        {/* Barra de filtros */}
        <div className="flex flex-col gap-3 border-b border-slate-200 p-4 sm:flex-row sm:items-center sm:justify-between">
          <div className="relative w-full sm:max-w-xs">
            <Search className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
            <Input
              type="search"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Buscar por ingrediente..."
              className="pl-9"
              aria-label="Buscar pedidos por nome do ingrediente"
            />
          </div>
          <div className="w-full sm:w-56">
            <Select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              aria-label="Filtrar por status"
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
                <th className="px-4 py-3 font-medium">Ingrediente</th>
                <th className="px-4 py-3 font-medium">Qtd.</th>
                <th className="px-4 py-3 font-medium">Canal</th>
                <th className="px-4 py-3 font-medium">Destinatário</th>
                <th className="px-4 py-3 font-medium">Data</th>
                <th className="px-4 py-3 font-medium">Status</th>
                <th className="px-4 py-3 text-right font-medium">Ações</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {filteredOrders.map((order) => {
                const badge = STATUS_CONFIG[order.status]
                const isClosed = order.status === "CANCELED" || order.status === "COMPLETED"
                return (
                  <tr
                    key={order.id}
                    className={"transition-colors hover:bg-slate-50 " + (isClosed ? "opacity-60" : "")}
                  >
                    <td className="px-4 py-3 font-mono text-xs text-slate-400">
                      #{String(order.id).padStart(3, "0")}
                    </td>
                    <td className="px-4 py-3 font-medium text-slate-800">{order.ingredientName}</td>
                    <td className="px-4 py-3 text-slate-600">
                      {order.amount} {order.unit}
                    </td>
                    <td className="px-4 py-3">
                      {order.messageChannel === "WHATSAPP" ? (
                        <Badge variant="success">
                          <MessageCircle className="h-3 w-3" />
                          WhatsApp
                        </Badge>
                      ) : (
                        <Badge variant="info">
                          <Mail className="h-3 w-3" />
                          E-mail
                        </Badge>
                      )}
                    </td>
                    <td className="px-4 py-3 text-slate-500">{order.supplierContact}</td>
                    <td className="px-4 py-3 text-slate-500">{formatDate(order.orderDate)}</td>
                    <td className="px-4 py-3">
                      <Badge variant={badge.variant}>{badge.label}</Badge>
                    </td>
                    <td className="px-4 py-3">
                      <div className="flex items-center justify-end gap-1">
                        {/* PENDING: Editar + Verificar Pedido */}
                        {order.status === "PENDING" ? (
                          <>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => openEdit(order)}
                              aria-label={`Editar pedido ${order.id}`}
                            >
                              <Pencil className="h-4 w-4" />
                              Editar
                            </Button>
                            <Button
                              variant="primary"
                              size="sm"
                              onClick={() => setVerifyOrder(order)}
                              aria-label={`Verificar pedido ${order.id}`}
                            >
                              <ClipboardCheck className="h-4 w-4" />
                              Verificar Pedido
                            </Button>
                          </>
                        ) : null}

                        {/* SENT: Confirmar Recebimento */}
                        {order.status === "SENT" ? (
                          <Button
                            variant="outline"
                            size="sm"
                            onClick={() => confirmReceipt(order)}
                            aria-label={`Confirmar recebimento do pedido ${order.id}`}
                          >
                            <PackageCheck className="h-4 w-4" />
                            Confirmar Recebimento
                          </Button>
                        ) : null}

                        {/* CANCELED / COMPLETED: sem ações */}
                        {isClosed ? <span className="text-xs italic text-slate-400">Sem ações</span> : null}
                      </div>
                    </td>
                  </tr>
                )
              })}

              {filteredOrders.length === 0 ? (
                <tr>
                  <td colSpan={8} className="px-4 py-12 text-center">
                    <div className="flex flex-col items-center gap-2 text-slate-400">
                      <ShoppingCart className="h-8 w-8" />
                      <p className="text-sm">
                        {orders.length === 0
                          ? "Nenhum pedido registrado."
                          : "Nenhum pedido encontrado para os filtros selecionados."}
                      </p>
                    </div>
                  </td>
                </tr>
              ) : null}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modal de criação / edição */}
      <PurchaseOrderModal
        open={formOpen}
        onClose={() => {
          setFormOpen(false)
          setEditingOrder(null)
        }}
        ingredientes={INGREDIENTES}
        onSubmit={handleFormSubmit}
        initialData={editingOrder}
      />

      {/* Diálogo de verificação do pedido (PENDING) */}
      <Modal
        open={Boolean(verifyOrder)}
        onClose={() => setVerifyOrder(null)}
        title="Verificar Pedido"
        description="Revise os dados antes de enviar ou cancelar o pedido."
        footer={
          <>
            <Button variant="danger" onClick={() => verifyOrder && cancelOrder(verifyOrder)}>
              <XCircle className="h-4 w-4" />
              Cancelar Pedido
            </Button>
            <Button variant="primary" onClick={() => verifyOrder && confirmSend(verifyOrder)}>
              <Send className="h-4 w-4" />
              Confirmar Envio
            </Button>
          </>
        }
      >
        {verifyOrder ? (
          <div className="flex flex-col gap-3">
            <div className="grid grid-cols-2 gap-3 rounded-lg border border-slate-200 bg-slate-50 p-4 text-sm">
              <div>
                <p className="text-xs uppercase tracking-wide text-slate-400">Ingrediente</p>
                <p className="font-medium text-slate-800">{verifyOrder.ingredientName}</p>
              </div>
              <div>
                <p className="text-xs uppercase tracking-wide text-slate-400">Quantidade</p>
                <p className="font-medium text-slate-800">
                  {verifyOrder.amount} {verifyOrder.unit}
                </p>
              </div>
              <div>
                <p className="text-xs uppercase tracking-wide text-slate-400">Canal</p>
                <p className="font-medium text-slate-800">{channelLabel(verifyOrder.messageChannel)}</p>
              </div>
              <div>
                <p className="text-xs uppercase tracking-wide text-slate-400">Destinatário</p>
                <p className="font-medium text-slate-800">{verifyOrder.supplierContact}</p>
              </div>
            </div>
            <p className="text-sm text-slate-500">
              Ao confirmar, o pedido será enviado automaticamente via{" "}
              <strong className="text-slate-700">{channelLabel(verifyOrder.messageChannel)}</strong> ao fornecedor.
            </p>
          </div>
        ) : null}
      </Modal>

      {/* Notificação */}
      <Toast toast={toast} onClose={() => setToast(null)} />
    </div>
  )
}
