import { useEffect, useState } from "react";
import { api } from "../api/client.js";
import StatusBanner from "../components/StatusBanner.jsx";

const emptyForm = { name: "", email: "", phone: "", address: "" };

export default function Customers() {
  const [customers, setCustomers] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadCustomers() {
    setLoading(true);
    setError("");
    try {
      setCustomers(await api.getCustomers());
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadCustomers();
  }, []);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    try {
      if (editingId) {
        await api.updateCustomer(editingId, form);
      } else {
        await api.createCustomer(form);
      }
      setForm(emptyForm);
      setEditingId(null);
      loadCustomers();
    } catch (err) {
      setError(err.message);
    }
  }

  function startEdit(customer) {
    setEditingId(customer.id);
    setForm({
      name: customer.name || "",
      email: customer.email || "",
      phone: customer.phone || "",
      address: customer.address || "",
    });
  }

  async function handleDelete(id) {
    if (!confirm("Delete this customer?")) return;
    try {
      await api.deleteCustomer(id);
      loadCustomers();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div>
      <h1>Customers</h1>

      <form className="form-card" onSubmit={handleSubmit}>
        <h3>{editingId ? "Edit Customer" : "New Customer"}</h3>
        <div className="form-row">
          <input name="name" placeholder="Name" value={form.name} onChange={handleChange} required />
          <input name="email" placeholder="Email" value={form.email} onChange={handleChange} />
        </div>
        <div className="form-row">
          <input name="phone" placeholder="Phone" value={form.phone} onChange={handleChange} />
          <input name="address" placeholder="Address" value={form.address} onChange={handleChange} />
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
            <th>Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Address</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {customers.map((c) => (
            <tr key={c.id}>
              <td>{c.id}</td>
              <td>{c.name}</td>
              <td>{c.email}</td>
              <td>{c.phone}</td>
              <td>{c.address}</td>
              <td>
                <button onClick={() => startEdit(c)}>Edit</button>
                <button className="danger" onClick={() => handleDelete(c.id)}>
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
