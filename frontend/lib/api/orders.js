const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080"

function apiUrl(path) {
    return `${API_BASE_URL}${path}`
}

const defaultOptions = {
    headers: {
        "Content-Type": "application/json",
    },
}

export async function getOrders() {
    const res = await fetch(apiUrl("/api/orders"))
    if (!res.ok) throw new Error("Falha ao buscar pedidos de compra")
    return res.json()
}

export async function createOrder(payload) {
    const res = await fetch(apiUrl("/api/orders"), {
        method: "POST",
        body: JSON.stringify(payload),
        ...defaultOptions,
    })
    if (!res.ok) throw new Error("Pedido não retornado")
    return res.json()
}

export async function getOrderById(id) {
    const res = await fetch(apiUrl(`/api/orders/${id}`))
    if (!res.ok) throw new Error("Falha ao buscar pedido de compra")
    return res.json()
}

export async function updateOrder(id, payload) {
    const res = await fetch(apiUrl(`/api/orders/${id}`), {
        method: "POST",
        body: JSON.stringify(payload),
        ...defaultOptions,
    })
    if (!res.ok) throw new Error("Falha ao atualizar pedido de compra")
    return res.json()
}

export async function deleteOrder(id) {
    const res = await fetch(apiUrl(`/api/orders/${id}`), {
        method: "DELETE",
    })
    if (!res.ok) throw new Error("Falha ao excluir pedido de compra")
    return res.text()
}
