const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:3000"

function apiUrl(path) {
    // ensure single slash between base and path
    return `${API_BASE_URL.replace(/\/$/, "")}${path.startsWith("/") ? path : `/${path}`}`
}

const defaultOptions = {
    headers: {
        "Content-Type": "application/json",
    },
}

export async function getEmployees() {
    const res = await fetch(apiUrl("/api/employees"))
    if(!res.ok) throw new Error("Falha ao buscar funcionários")
    return res.json()
}

export async function createEmployee(payload) {
    const res = await fetch(apiUrl("/api/employees"), {
        method: "POST",
        body: JSON.stringify(payload),
        ...defaultOptions,
    })
    if(!res.ok) throw new Error("Funcionário não retornado")
    return res.json()
}

export async function getEmployeeById(id) {
    const res = await fetch(apiUrl(`/api/employees/${id}`))
    if(!res.ok) throw new Error("Falha ao buscar funcionário")
    return res.json()
}

export async function updateEmployee(id, payload) {
    const res = await fetch(apiUrl(`/api/employees/${id}`), {
        method: "POST",
        body: JSON.stringify(payload),
        ...defaultOptions,
    })
    if(!res.ok) throw new Error("Falha ao atualizar funcionário")
    return res.json()
}

export async function deleteEmployee(id) {
    const res = await fetch(apiUrl(`/api/employees/${id}`), {
        method: "DELETE",
    })
    if(!res.ok) throw new Error("Falha ao excluir funcionário")
    return res.text()
}
