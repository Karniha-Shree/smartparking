import { Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar.jsx";
import Dashboard from "./pages/Dashboard.jsx";
import Customers from "./pages/Customers.jsx";
import Vehicles from "./pages/Vehicles.jsx";
import ParkingLots from "./pages/ParkingLots.jsx";
import ParkingEntryExit from "./pages/ParkingEntryExit.jsx";
import PricingRules from "./pages/PricingRules.jsx";
import Payments from "./pages/Payments.jsx";
import Vendors from "./pages/Vendors.jsx";
import PurchaseOrders from "./pages/PurchaseOrders.jsx";
import Budgets from "./pages/Budgets.jsx";
import Reports from "./pages/Reports.jsx";

export default function App() {
  return (
    <div className="app-shell">
      <Navbar />
      <main className="content">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/customers" element={<Customers />} />
          <Route path="/vehicles" element={<Vehicles />} />
          <Route path="/parking-lots" element={<ParkingLots />} />
          <Route path="/parking" element={<ParkingEntryExit />} />
          <Route path="/pricing-rules" element={<PricingRules />} />
          <Route path="/payments" element={<Payments />} />
          <Route path="/vendors" element={<Vendors />} />
          <Route path="/purchase-orders" element={<PurchaseOrders />} />
          <Route path="/budgets" element={<Budgets />} />
          <Route path="/reports" element={<Reports />} />
        </Routes>
      </main>
    </div>
  );
}
