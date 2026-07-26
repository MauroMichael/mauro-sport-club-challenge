const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

export async function getOrders(filters = {}) {
    const params = new URLSearchParams();

    if (filters.status) {
        params.append("status", filters.status);
    }

    if (filters.dateFrom) {
        params.append("dateFrom", filters.dateFrom);
    }

    if (filters.dateTo) {
        params.append("dateTo", filters.dateTo);
    }

    const queryString = params.toString();
    const url = queryString ? `${API_BASE_URL}/orders?${queryString}` : `${API_BASE_URL}/orders`;

    const response = await fetch(url);

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || errorData.message || "Failed to fetch orders");
    }

    return response.json();
}

export async function getOrderById(id) {
    const response = await fetch(`${API_BASE_URL}/orders/${id}`);

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || errorData.message || "Failed to fetch order detail");
    }

    return response.json();
}

export async function updateOrderStatus(id, status) {
    const response = await fetch(`${API_BASE_URL}/orders/${id}/status`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ status }),
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || errorData.message || "Failed to update order status");
    }

    return response.json();
}