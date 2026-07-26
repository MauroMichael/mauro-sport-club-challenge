import { Navigate, Routes, Route } from "react-router-dom";
import OrdersPage from "./pages/OrdersPage";
import OrderDetailPage from "./pages/OrderDetailPage";
import "./App.css"

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/orders" replace />} />
      <Route path="/orders" element={<OrdersPage />} />
      <Route path="/orders/:id" element={<OrderDetailPage />} />
    </Routes>
  );
}

export default App;
