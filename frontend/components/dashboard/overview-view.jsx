"use client"

import { useEffect, useState } from "react"
import { Package, Users, AlertTriangle, ClipboardList } from "lucide-react"
import { getIngredients } from "@/lib/api/ingredients"
import { getEmployees } from "@/lib/api/employees"
import { getOrders } from "@/lib/api/orders"

export function OverviewView() {
  const [stats, setStats] = useState({
    ingredientsCount: 0,
    employeesCount: 0,
    lowStockCount: 0,
    ordersTodayCount: 0
  })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    async function loadData() {
      try {
        const [ingredients, employees, orders] = await Promise.all([
          getIngredients().catch(() => []),
          getEmployees().catch(() => []),
          getOrders().catch(() => [])
        ])

        const lowStock = ingredients.filter((i) => i.currentAmount < i.minimumStock).length

        // Formata a data de hoje no fuso local: AAAA-MM-DD
        const now = new Date()
        const year = now.getFullYear()
        const month = String(now.getMonth() + 1).padStart(2, "0")
        const day = String(now.getDate()).padStart(2, "0")
        const todayStr = `${year}-${month}-${day}`

        const ordersToday = orders.filter((o) => {
          if (!o.date) return false
          // Suporta formatos de data AAAA-MM-DD ou ISO string completa
          return o.date.startsWith(todayStr)
        }).length

        setStats({
          ingredientsCount: ingredients.length,
          employeesCount: employees.length,
          lowStockCount: lowStock,
          ordersTodayCount: ordersToday
        })
      } catch (err) {
        console.error("Erro ao carregar dados do painel:", err)
      } finally {
        setLoading(false)
      }
    }

    loadData()
  }, [])

  const statsItems = [
    {
      label: "Ingredientes em estoque",
      value: loading ? "..." : String(stats.ingredientsCount),
      icon: Package,
      accent: "text-emerald-600 bg-emerald-50"
    },
    {
      label: "Funcionários ativos",
      value: loading ? "..." : String(stats.employeesCount),
      icon: Users,
      accent: "text-blue-600 bg-blue-50"
    },
    {
      label: "Alertas de estoque baixo",
      value: loading ? "..." : String(stats.lowStockCount),
      icon: AlertTriangle,
      accent: "text-red-600 bg-red-50"
    },
    {
      label: "Pedidos hoje",
      value: loading ? "..." : String(stats.ordersTodayCount),
      icon: ClipboardList,
      accent: "text-slate-700 bg-slate-100"
    },
  ]

  return (
    <div className="flex flex-col gap-6">
      <div>
        <h2 className="text-xl font-semibold text-slate-800">Visão Geral</h2>
        <p className="text-sm text-slate-500">Resumo da operação do restaurante.</p>
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
        {statsItems.map((stat) => {
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

