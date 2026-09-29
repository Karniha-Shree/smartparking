import { useEffect, useState } from "react";
import { api } from "../api/client.js";
import StatusBanner from "../components/StatusBanner.jsx";

const emptyForm = { name: "", email: "", phone: "", address: "" };

export default function Vendors() {
  const [vendors, setVendors] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadVendors() {
    setLoading(true);
    setError("");
    try {
      setVendors(await api.getVendors());
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadVendors();
  }, []);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    try {
      await api.createVendor(form);
      setForm(emptyForm);
      loadVendors();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div>
      <h1>Vendors</h1>

      <form className="form-card" onSubmit={handleSubmit}>
        <h3>New Vendor</h3>
        <div className="form-row">
          <input name="name" placeholder="Name" value={form.name} onChange={handleChange} required />
          <input name="email" placeholder="Email" value={form.email} onChange={handleChange} />
        </div>
        <div className="form-row">
          <input name="phone" placeholder="Phone" value={form.phone} onChange={handleChange} />
          <input name="address" placeholder="Address" value={form.address} onChange={handleChange} />
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
            <th>Email</th>
            <th>Phone</th>
            <th>Address</th>
          </tr>
        </thead>
        <tbody>
          {vendors.map((v) => (
            <tr key={v.id}>
              <td>{v.id}</td>
              <td>{v.name}</td>
              <td>{v.email}</td>
              <td>{v.phone}</td>
              <td>{v.address}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
