"use client"

import { useState } from "react"
import { Settings } from "lucide-react"
import { Sidebar } from "@/components/dashboard/sidebar"
import { Header } from "@/components/dashboard/header"
import { OverviewView } from "@/components/dashboard/overview-view"
import { InventoryView } from "@/components/dashboard/inventory-view"
import { PurchaseOrdersView } from "@/components/dashboard/purchase-orders-view"
import { EmployeesView } from "@/components/dashboard/employees-view"

// Títulos exibidos no header conforme a aba ativa.
const TITLES = {
  overview: "Visão Geral",
  inventory: "Estoque de Ingredientes",
  purchases: "Pedidos de Compra",
  employees: "Funcionários",
  settings: "Configurações",
}

export default function Page() {
  // useState controla qual seção está ativa (navegação por abas).
  const [active, setActive] = useState("inventory")

  return (
    <div className="flex h-screen bg-slate-100">
      <Sidebar active={active} onChange={setActive} />

      <div className="flex flex-1 flex-col overflow-hidden">
        <Header title={TITLES[active]} />

        <main className="flex-1 overflow-y-auto p-6">
          {active === "overview" ? <OverviewView /> : null}
          {active === "inventory" ? <InventoryView /> : null}
          {active === "purchases" ? <PurchaseOrdersView /> : null}
          {active === "employees" ? <EmployeesView /> : null}
          {active === "settings" ? (
            <div className="flex flex-col items-center justify-center gap-3 rounded-xl border border-dashed border-slate-300 bg-white py-20 text-center">
              <Settings className="h-8 w-8 text-slate-400" />
              <p className="text-sm text-slate-500">Configurações em breve.</p>
            </div>
          ) : null}
        </main>
      </div>
    </div>
  )
}
