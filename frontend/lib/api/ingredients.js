const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:3000"

function apiUrl(path) {
    return `${API_BASE_URL}${path}`
}

const defaultOptions = {
    headers: {
        "Content-Type": "application/json",
    },
}


export async function getIngredients() {
    const res = await fetch(apiUrl("/api/ingredients"))
    if(!res.ok) throw new Error("Falha ao buscar ingredientes")
    return res.json()
}

export async function createIngredient(payload) {
    const res = await fetch(apiUrl("/api/ingredients"), {
        method: "POST",
        body: JSON.stringify(payload),
        ...defaultOptions,
    })
    if(!res.ok) throw new Error("Ingrediente não retornado")
    return res.json() 
}

export async function getIngredientById(id) {
    const res = await fetch(apiUrl(`/api/ingredients/${id}`))
    if(!res.ok) throw new Error("Falha ao buscar ingredientes")
    return res.json()
}

export async function updateIngredient(id, payload) {
    const res = await fetch(apiUrl(`/api/ingredients/${id}`), {
        method: "POST",
        body: JSON.stringify(payload),
        ...defaultOptions,
    })
    if(!res.ok) throw new Error("Falha ao atualizar ingrediente")
    return res.json()
}

export async function deleteIngredient(id) {
    const res = await fetch(apiUrl(`/api/ingredients/${id}`), {
        method: "DELETE",
    })
    if(!res.ok) throw new Error("Falha ao excluir ingrediente")
    return res.text()
}
