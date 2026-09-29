import { useEffect, useState } from "react";
import { api } from "../api/client.js";
import StatusBanner from "../components/StatusBanner.jsx";

const emptyForm = { name: "", location: "", totalSlots: "" };

export default function ParkingLots() {
  const [lots, setLots] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [expandedLotId, setExpandedLotId] = useState(null);
  const [slots, setSlots] = useState([]);

  async function loadLots() {
    setLoading(true);
    setError("");
    try {
      setLots(await api.getParkingLots());
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadLots();
  }, []);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    try {
      await api.createParkingLot({ ...form, totalSlots: Number(form.totalSlots) });
      setForm(emptyForm);
      loadLots();
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleDelete(id) {
    if (!confirm("Delete this parking lot and all its slots?")) return;
    try {
      await api.deleteParkingLot(id);
      loadLots();
    } catch (err) {
      setError(err.message);
    }
  }

  async function toggleSlots(lotId) {
    if (expandedLotId === lotId) {
      setExpandedLotId(null);
      return;
    }
    try {
      const data = await api.getSlotsForLot(lotId);
      setSlots(data);
      setExpandedLotId(lotId);
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div>
      <h1>Parking Lots</h1>

      <form className="form-card" onSubmit={handleSubmit}>
        <h3>New Parking Lot</h3>
        <div className="form-row">
          <input name="name" placeholder="Lot name" value={form.name} onChange={handleChange} required />
          <input name="location" placeholder="Location" value={form.location} onChange={handleChange} />
          <input
            name="totalSlots"
            type="number"
            min="1"
            placeholder="Total slots"
            value={form.totalSlots}
            onChange={handleChange}
            required
          />
        </div>
        <p className="hint">Slots (A01, A02, ...) are generated automatically for you.</p>
        <div className="form-actions">
          <button type="submit">Create</button>
        </div>
      </form>

      <StatusBanner loading={loading} error={error} />

      <table className="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Location</th>
            <th>Total Slots</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {lots.map((lot) => (
            <tr key={lot.id}>
              <td colSpan={5} style={{ padding: 0 }}>
                <div className="lot-row">
                  <span>{lot.id}</span>
                  <span>{lot.name}</span>
                  <span>{lot.location}</span>
                  <span>{lot.totalSlots}</span>
                  <span>
                    <button onClick={() => toggleSlots(lot.id)}>
                      {expandedLotId === lot.id ? "Hide Slots" : "View Slots"}
                    </button>
                    <button className="danger" onClick={() => handleDelete(lot.id)}>
                      Delete
                    </button>
                  </span>
                </div>
                {expandedLotId === lot.id && (
                  <div className="slot-grid">
                    {slots.map((s) => (
                      <span key={s.id} className={`slot-badge ${s.status.toLowerCase()}`}>
                        {s.slotNumber} — {s.status}
                      </span>
                    ))}
                  </div>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
