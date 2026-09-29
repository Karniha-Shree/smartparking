import { useEffect, useState } from "react";
import { api } from "../api/client.js";

export default function Dashboard() {
  const [counts, setCounts] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadCounts() {
      try {
        const [customers, vehicles, lots, availableSlots] = await Promise.all([
          api.getCustomers(),
          api.getVehicles(),
          api.getParkingLots(),
          api.getAvailableSlots(),
        ]);
        setCounts({
          customers: customers.length,
          vehicles: vehicles.length,
          lots: lots.length,
          availableSlots: availableSlots.length,
        });
      } catch (err) {
        setError("Could not reach the backend. Is it running on http://localhost:8080 ?");
      }
    }
    loadCounts();
  }, []);

  return (
    <div>
      <h1>Dashboard</h1>
      <p className="muted">Smart Parking Space Allocation &amp; Dynamic Pricing Hub</p>

      {error && <p className="banner banner-error">{error}</p>}

      {counts && (
        <div className="card-grid">
          <StatCard label="Customers" value={counts.customers} />
          <StatCard label="Vehicles" value={counts.vehicles} />
          <StatCard label="Parking Lots" value={counts.lots} />
          <StatCard label="Available Slots" value={counts.availableSlots} />
        </div>
      )}

      <h2>Quick Start</h2>
      <ol className="quick-start">
        <li>Create a Customer and a Vehicle for them.</li>
        <li>Create a Parking Lot (slots are auto-generated).</li>
        <li>Optionally configure a Pricing Rule (peak hour rate).</li>
        <li>Go to Entry / Exit to check a vehicle in, then check it out to get an invoice.</li>
        <li>Go to Payments to pay the invoice — this also records the accounting entry.</li>
        <li>Check Reports for revenue, P&amp;L, and balance sheet.</li>
      </ol>
    </div>
  );
}

function StatCard({ label, value }) {
  return (
    <div className="stat-card">
      <div className="stat-value">{value}</div>
      <div className="stat-label">{label}</div>
    </div>
  );
}
