import { useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { getOrders } from "../services/OrdersApi";

export default function OrdersPage() {
  const DEFAULT_PAGE_SIZE = 10;

  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchParams, setSearchParams] = useSearchParams();

  const [statusFilter, setStatusFilter] = useState(
    searchParams.get("status") || "",
  );
  const [dateFrom, setDateFrom] = useState(searchParams.get("dateFrom") || "");
  const [dateTo, setDateTo] = useState(searchParams.get("dateTo") || "");

  const [currentPage, setCurrentPage] = useState(
    Number(searchParams.get("page") || 0),
  );
  const [totalPages, setTotalPages] = useState(0);
  const [last, setLast] = useState(false);

  const dateRangeInvalid = dateFrom && dateTo && dateFrom > dateTo;

  useEffect(() => {
    async function loadOrders() {
      if (dateRangeInvalid) {
        setOrders([]);
        setTotalPages(0);
        setLast(false);
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
          page: currentPage,
          size: DEFAULT_PAGE_SIZE,
        });

        setOrders(data.content);
        setCurrentPage(data.page);
        setTotalPages(data.totalPages);
        setLast(data.last);
      } catch (err) {
        setError(err.message || "Could not load orders.");
      } finally {
        setLoading(false);
      }
    }

    loadOrders();
  }, [statusFilter, dateFrom, dateTo, dateRangeInvalid, currentPage]);

  useEffect(() => {
    const params = {};

    if (statusFilter) params.status = statusFilter;
    if (dateFrom) params.dateFrom = dateFrom;
    if (dateTo) params.dateTo = dateTo;
    if (currentPage > 0) params.page = String(currentPage);

    setSearchParams(params);
  }, [statusFilter, dateFrom, dateTo, currentPage, setSearchParams]);

  function goToPreviousPage() {
    if (currentPage > 0) {
      setCurrentPage((prev) => prev - 1);
    }
  }

  function goToNextPage() {
    if (!last) {
      setCurrentPage((prev) => prev + 1);
    }
  }

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
              onChange={(e) => {
                setStatusFilter(e.target.value);
                setCurrentPage(0);
              }}
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
              onChange={(e) => {
                setDateFrom(e.target.value);
                setCurrentPage(0);
              }}
            />
          </label>

          <label>
            Date to
            <input
              type="date"
              value={dateTo}
              onChange={(e) => {
                setDateTo(e.target.value);
                setCurrentPage(0);
              }}
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
        <div className="pagination">
          <button
            className="button"
            onClick={goToPreviousPage}
            disabled={currentPage === 0}
          >
            Previous
          </button>

          <span>
            Page {currentPage + 1} of {totalPages || 1}
          </span>

          <button
            className="button"
            onClick={goToNextPage}
            disabled={last || totalPages === 0}
          >
            Next
          </button>
        </div>
      </section>
    </main>
  );
}
