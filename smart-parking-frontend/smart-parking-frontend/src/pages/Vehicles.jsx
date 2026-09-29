import { useEffect, useState } from "react";
import { api } from "../api/client.js";
import StatusBanner from "../components/StatusBanner.jsx";

const emptyForm = { vehicleNumber: "", vehicleType: "", customerId: "" };

export default function Vehicles() {
  const [vehicles, setVehicles] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadAll() {
    setLoading(true);
    setError("");
    try {
      const [v, c] = await Promise.all([api.getVehicles(), api.getCustomers()]);
      setVehicles(v);
      setCustomers(c);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadAll();
  }, []);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    const payload = { ...form, customerId: Number(form.customerId) };
    try {
      if (editingId) {
        await api.updateVehicle(editingId, payload);
      } else {
        await api.createVehicle(payload);
      }
      setForm(emptyForm);
      setEditingId(null);
      loadAll();
    } catch (err) {
      setError(err.message);
    }
  }

  function startEdit(vehicle) {
    setEditingId(vehicle.id);
    setForm({
      vehicleNumber: vehicle.vehicleNumber || "",
      vehicleType: vehicle.vehicleType || "",
      customerId: String(vehicle.customerId || ""),
    });
  }

  async function handleDelete(id) {
    if (!confirm("Delete this vehicle?")) return;
    try {
      await api.deleteVehicle(id);
      loadAll();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div>
      <h1>Vehicles</h1>

      <form className="form-card" onSubmit={handleSubmit}>
        <h3>{editingId ? "Edit Vehicle" : "New Vehicle"}</h3>
        <div className="form-row">
          <input
            name="vehicleNumber"
            placeholder="Vehicle Number (e.g. TN01AB1234)"
            value={form.vehicleNumber}
            onChange={handleChange}
            required
          />
          <input
            name="vehicleType"
            placeholder="Type (CAR / BIKE / EV)"
            value={form.vehicleType}
            onChange={handleChange}
          />
        </div>
        <div className="form-row">
          <select name="customerId" value={form.customerId} onChange={handleChange} required>
            <option value="">-- Select Customer --</option>
            {customers.map((c) => (
              <option key={c.id} value={c.id}>
                {c.name} (#{c.id})
              </option>
            ))}
          </select>
        </div>
        <div className="form-actions">
          <button type="submit">{editingId ? "Update" : "Create"}</button>
          {editingId && (
            <button
              type="button"
              className="secondary"
              onClick={() => {
                setEditingId(null);
                setForm(emptyForm);
              }}
            >
              Cancel
            </button>
          )}
        </div>
      </form>

      <StatusBanner loading={loading} error={error} />

      <table className="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Vehicle Number</th>
            <th>Type</th>
            <th>Owner</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {vehicles.map((v) => (
            <tr key={v.id}>
              <td>{v.id}</td>
              <td>{v.vehicleNumber}</td>
              <td>{v.vehicleType}</td>
              <td>{v.customerName}</td>
              <td>
                <button onClick={() => startEdit(v)}>Edit</button>
                <button className="danger" onClick={() => handleDelete(v.id)}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
