import { Link, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { getOrderById } from "../services/OrdersApi";
import { updateOrderStatus } from "../services/OrdersApi";

export default function OrderDetailPage() {
  const { id } = useParams();

  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [updating, setUpdating] = useState(false);
  const [updateError, setUpdateError] = useState(null);
  const [updateSuccess, setUpdateSuccess] = useState("");

  useEffect(() => {
    async function loadOrder() {
      try {
        const data = await getOrderById(id);
        setOrder(data);
      } catch {
        setError("Could not load order details.");
      } finally {
        setLoading(false);
      }
    }

    loadOrder();
  }, [id]);

  if (loading) {
    return (
      <main className="page">
        <header className="page-header">
          <h1 className="page-title">Order Detail</h1>
          <p className="page-subtitle">Loading order details...</p>
        </header>
      </main>
    );
  }

  if (error) {
    return (
      <main className="page">
        <header className="page-header">
          <h1 className="page-title">Order Detail</h1>
          <p className="page-subtitle">{error}</p>
        </header>
        <Link className="link-back" to="/orders">
          Back to orders
        </Link>
      </main>
    );
  }

  if (!order) {
    return (
      <main className="page">
        <header className="page-header">
          <h1 className="page-title">Order Detail</h1>
          <p className="page-subtitle">Order not found.</p>
        </header>
        <Link className="link-back" to="/orders">
          Back to orders
        </Link>
      </main>
    );
  }

  async function handleStatusChange(newStatus) {
    try {
      setUpdating(true);
      setUpdateError("");
      setUpdateSuccess("");

      const updateOrder = await updateOrderStatus(id, newStatus);

      setOrder(updateOrder);
      setUpdateSuccess("Order status updated successfully.");
    } catch {
      setUpdateError("Failed to update order status.");
    } finally {
      setUpdating(false);
    }
  }

  return (
    <main className="page">
      <header className="page-header">
        <h1 className="page-title">Order #{order.id}</h1>
      </header>

      <section className="panel">
        {order.status === "PENDING" && (
          <section className="detail-section">
            <h2>Update Status</h2>

            <div className="actions">
              <button
                className="button"
                type="button"
                onClick={() => handleStatusChange("PAID")}
                disabled={updating}
              >
                {updating ? "Updating..." : "Mark as PAID"}
              </button>

              <button
                className="button button-secondary"
                type="button"
                onClick={() => handleStatusChange("CANCELLED")}
                disabled={updating}
              >
                {updating ? "Updating..." : "Mark as CANCELLED"}
              </button>
            </div>

            {updateError && <p className="message-error">{updateError}</p>}
          </section>
        )}
        {updateSuccess && <p className="message-success">{updateSuccess}</p>}

        <section className="detail-grid">
          <p>
            <strong>Date:</strong> {order.date}
          </p>
          <p>
            <strong>Status:</strong>{" "}
            <span
              className={`status-badge status-${order.status.toLowerCase()}`}
            >
              {order.status}
            </span>
          </p>
          <p>
            <strong>Total:</strong> ${order.total.toFixed(2)}
          </p>
          <p>
            <strong>Customer:</strong> {order.customerName}
          </p>
        </section>

        <section className="detail-section">
          <h2>Items</h2>
          {order.items.length === 0 ? (
            <p>No items found for this order.</p>
          ) : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Item</th>
                  <th>Quantity</th>
                  <th>Price</th>
                </tr>
              </thead>
              <tbody>
                {order.items.map((item) => (
                  <tr key={`${item.productName}-${item.quantity}`}>
                    <td>{item.productName}</td>
                    <td>{item.quantity}</td>
                    <td>${item.price.toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </section>

        <Link className="link-back" to="/orders">
          Back to orders
        </Link>
      </section>
    </main>
  );
}
