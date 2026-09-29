import { useEffect, useState } from "react";
import { api } from "../api/client.js";
import StatusBanner from "../components/StatusBanner.jsx";

const emptyForm = { name: "", rate: "", startTime: "", endTime: "", peakHour: false, active: true };

export default function PricingRules() {
  const [rules, setRules] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadRules() {
    setLoading(true);
    setError("");
    try {
      setRules(await api.getPricingRules());
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadRules();
  }, []);

  function handleChange(e) {
    const { name, value, type, checked } = e.target;
    setForm({ ...form, [name]: type === "checkbox" ? checked : value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    try {
      const payload = {
        ...form,
        rate: Number(form.rate),
        // Leave blank for an "all day" rule — matches backend's null-means-all-day logic.
        startTime: form.startTime || null,
        endTime: form.endTime || null,
      };
      await api.createPricingRule(payload);
      setForm(emptyForm);
      loadRules();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div>
      <h1>Pricing Rules</h1>
      <p className="muted">
        Leave start/end time blank for an "all day" base rate. Add a time-windowed rule (e.g. 17:00–21:00) for
        peak-hour pricing — it takes priority whenever "now" falls inside that window.
      </p>

      <form className="form-card" onSubmit={handleSubmit}>
        <h3>New Pricing Rule</h3>
        <div className="form-row">
          <input name="name" placeholder="Rule name (e.g. Peak Evening)" value={form.name} onChange={handleChange} required />
          <input
            name="rate"
            type="number"
            step="0.01"
            placeholder="Rate per hour (₹)"
            value={form.rate}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-row">
          <label>
            Start time
            <input name="startTime" type="time" value={form.startTime} onChange={handleChange} />
          </label>
          <label>
            End time
            <input name="endTime" type="time" value={form.endTime} onChange={handleChange} />
          </label>
        </div>
        <div className="form-row checkboxes">
          <label>
            <input name="peakHour" type="checkbox" checked={form.peakHour} onChange={handleChange} />
            Peak hour
          </label>
          <label>
            <input name="active" type="checkbox" checked={form.active} onChange={handleChange} />
            Active
          </label>
        </div>
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
            <th>Rate/hr</th>
            <th>Window</th>
            <th>Peak</th>
            <th>Active</th>
          </tr>
        </thead>
        <tbody>
          {rules.map((r) => (
            <tr key={r.id}>
              <td>{r.id}</td>
              <td>{r.name}</td>
              <td>₹{r.rate}</td>
              <td>{r.startTime && r.endTime ? `${r.startTime} - ${r.endTime}` : "All day"}</td>
              <td>{r.peakHour ? "Yes" : "No"}</td>
              <td>{r.active ? "Yes" : "No"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
