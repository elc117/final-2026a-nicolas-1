"use client"

import { LayoutDashboard, Package, Users, Settings, UtensilsCrossed, ShoppingCart } from "lucide-react"

// Itens de navegação. O "key" é usado para controlar a aba ativa via useState.
const NAV_ITEMS = [
  { key: "overview", label: "Visão Geral", icon: LayoutDashboard },
  { key: "inventory", label: "Estoque de Ingredientes", icon: Package },
  { key: "purchases", label: "Pedidos de Compra", icon: ShoppingCart },
  { key: "employees", label: "Funcionários", icon: Users },
  { key: "settings", label: "Configurações", icon: Settings },
]

export function Sidebar({ active, onChange }) {
  return (
    <aside className="flex w-64 flex-shrink-0 flex-col border-r border-slate-800 bg-slate-900">
      {/* Marca / logo */}
      <div className="flex items-center gap-3 border-b border-slate-800 px-6 py-5">
        <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-emerald-500">
          <UtensilsCrossed className="h-5 w-5 text-white" />
        </div>
        <div className="leading-tight">
          <p className="text-sm font-semibold text-white">Sabor &amp; Cia</p>
          <p className="text-xs text-slate-400">Painel Administrativo</p>
        </div>
      </div>

      {/* Navegação */}
      <nav className="flex flex-1 flex-col gap-1 px-3 py-4">
        {NAV_ITEMS.map((item) => {
          const Icon = item.icon
          const isActive = active === item.key
          return (
            <button
              key={item.key}
              type="button"
              onClick={() => onChange(item.key)}
              className={
                "flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors " +
                (isActive
                  ? "bg-emerald-500/10 text-emerald-400"
                  : "text-slate-400 hover:bg-slate-800 hover:text-slate-200")
              }
            >
              <Icon className="h-5 w-5" />
              {item.label}
            </button>
          )
        })}
      </nav>

      {/* Rodapé da sidebar */}
      <div className="border-t border-slate-800 px-6 py-4">
        <p className="text-xs text-slate-500">v1.0.0 — ERP Restaurante</p>
      </div>
    </aside>
  )
}
