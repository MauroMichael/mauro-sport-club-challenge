import { useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { getOrders } from "../services/OrdersApi";

export default function OrdersPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchParams, setSearchParams] = useSearchParams();

  const [statusFilter, setStatusFilter] = useState(
    searchParams.get("status") || "",
  );
  const [dateFrom, setDateFrom] = useState(searchParams.get("dateFrom") || "");
  const [dateTo, setDateTo] = useState(searchParams.get("dateTo") || "");

  const dateRangeInvalid = dateFrom && dateTo && dateFrom > dateTo;

  useEffect(() => {
    async function loadOrders() {
      if (dateRangeInvalid) {
        setOrders([]);
        setError("Invalid date range: 'From' date cannot be after 'To' date.");
        setLoading(false);
        return;
      }
      try {
        setLoading(true);
        setError(null);

        const data = await getOrders({
          status: statusFilter || undefined,
          dateFrom: dateFrom || undefined,
          dateTo: dateTo || undefined,
        });

        setOrders(data);
      } catch {
        setError("Could not load orders.");
      } finally {
        setLoading(false);
      }
    }

    loadOrders();
  }, [statusFilter, dateFrom, dateTo, dateRangeInvalid]);

  useEffect(() => {
  const params = {};

  if (statusFilter) params.status = statusFilter;
  if (dateFrom) params.dateFrom = dateFrom;
  if (dateTo) params.dateTo = dateTo;

  setSearchParams(params);
}, [statusFilter, dateFrom, dateTo, setSearchParams]);

  if (loading) {
    return (
      <main className="page">
        <header className="page-header">
          <h1 className="page-title">Orders</h1>
          <p className="page-subtitle">Loading orders...</p>
        </header>
      </main>
    );
  }

  return (
    <main className="page">
      <header className="page-header">
        <h1 className="page-title">Orders</h1>
        <p className="page-subtitle">
          Browse customer orders and inspect their status.
        </p>
      </header>

      <section className="panel">
        <div className="filters">
          <label>
            Status
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
            >
              <option value="">All</option>
              <option value="PENDING">Pending</option>
              <option value="PAID">Paid</option>
              <option value="CANCELLED">Cancelled</option>
            </select>
          </label>

          <label>
            Date from
            <input
              type="date"
              value={dateFrom}
              onChange={(e) => setDateFrom(e.target.value)}
            />
          </label>

          <label>
            Date to
            <input
              type="date"
              value={dateTo}
              onChange={(e) => setDateTo(e.target.value)}
            />
          </label>
        </div>
        {error ? (
          <p className="message-error">{error}</p>
        ) : orders.length === 0 ? (
          <p className="empty-state">No orders match the selected filters.</p>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Date</th>
                <th>Status</th>
                <th>Total</th>
                <th>Customer</th>
                <th>Detail</th>
              </tr>
            </thead>
            <tbody>
              {orders.map((order) => (
                <tr key={order.id}>
                  <td>{order.id}</td>
                  <td>{order.date}</td>
                  <td>
                    <span
                      className={`status-badge status-${order.status.toLowerCase()}`}
                    >
                      {order.status}
                    </span>
                  </td>
                  <td>${order.total.toFixed(2)}</td>
                  <td>{order.customer}</td>
                  <td>
                    <Link to={`/orders/${order.id}`}>View Details</Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
    </main>
  );
}
