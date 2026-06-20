"use client"

import { Package, Users, AlertTriangle, ClipboardList } from "lucide-react"

const STATS = [
  { label: "Ingredientes em estoque", value: "6", icon: Package, accent: "text-emerald-600 bg-emerald-50" },
  { label: "Funcionários ativos", value: "4", icon: Users, accent: "text-blue-600 bg-blue-50" },
  { label: "Alertas de estoque baixo", value: "3", icon: AlertTriangle, accent: "text-red-600 bg-red-50" },
  { label: "Pedidos hoje", value: "27", icon: ClipboardList, accent: "text-slate-700 bg-slate-100" },
]

export function OverviewView() {
  return (
    <div className="flex flex-col gap-6">
      <div>
        <h2 className="text-xl font-semibold text-slate-800">Visão Geral</h2>
        <p className="text-sm text-slate-500">Resumo da operação do restaurante.</p>
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
        {STATS.map((stat) => {
          const Icon = stat.icon
          return (
            <div key={stat.label} className="rounded-xl border border-slate-200 bg-white p-5">
              <div className={`mb-3 inline-flex h-10 w-10 items-center justify-center rounded-lg ${stat.accent}`}>
                <Icon className="h-5 w-5" />
              </div>
              <p className="text-2xl font-semibold text-slate-800">{stat.value}</p>
              <p className="text-sm text-slate-500">{stat.label}</p>
            </div>
          )
        })}
      </div>

      <div className="rounded-xl border border-dashed border-slate-300 bg-white p-8 text-center">
        <p className="text-sm text-slate-500">
          Selecione <span className="font-medium text-slate-700">Estoque de Ingredientes</span> ou{" "}
          <span className="font-medium text-slate-700">Funcionários</span> no menu lateral para gerenciar os dados.
        </p>
      </div>
    </div>
  )
}
