import { useEffect, useState } from "react";
import { api } from "../api/client.js";
import StatusBanner from "../components/StatusBanner.jsx";

const emptyForm = { vendorId: "", description: "", amount: "" };

export default function PurchaseOrders() {
  const [vendors, setVendors] = useState([]);
  const [orders, setOrders] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [lastBill, setLastBill] = useState(null);

  async function loadAll() {
    setLoading(true);
    setError("");
    try {
      const [v, o] = await Promise.all([api.getVendors(), api.getPurchaseOrders()]);
      setVendors(v);
      setOrders(o);
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
    try {
      await api.createPurchaseOrder({ ...form, vendorId: Number(form.vendorId), amount: Number(form.amount) });
      setForm(emptyForm);
      loadAll();
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleConvertToBill(id) {
    setError("");
    try {
      const bill = await api.convertToVendorBill(id);
      setLastBill(bill);
      loadAll();
    } catch (err) {
      setError(err.message);
    }
  }

  async function handlePayBill(billId) {
    setError("");
    try {
      const bill = await api.payVendorBill(billId);
      setLastBill(bill);
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div>
      <h1>Purchase Orders</h1>
      <p className="muted">
        Flow: create a PO for a vendor service (e.g. barrier maintenance) → convert it into a Vendor Bill → pay
        the bill. Paying records the expense in accounting automatically.
      </p>

      <form className="form-card" onSubmit={handleSubmit}>
        <h3>New Purchase Order</h3>
        <div className="form-row">
          <select name="vendorId" value={form.vendorId} onChange={handleChange} required>
            <option value="">-- Vendor --</option>
            {vendors.map((v) => (
              <option key={v.id} value={v.id}>
                {v.name}
              </option>
            ))}
          </select>
          <input
            name="amount"
            type="number"
            step="0.01"
            placeholder="Amount"
            value={form.amount}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-row">
          <input
            name="description"
            placeholder="Description (e.g. Automated barrier repair)"
            value={form.description}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-actions">
          <button type="submit">Create PO</button>
        </div>
      </form>

      <StatusBanner loading={loading} error={error} />

      {lastBill && (
        <div className="result-box">
          <strong>Vendor Bill {lastBill.billNumber}</strong>
          <p>
            Vendor: {lastBill.vendorName} <br />
            Amount: ₹{lastBill.amount} <br />
            Status: {lastBill.status}
          </p>
          {lastBill.status === "PENDING" && (
            <button onClick={() => handlePayBill(lastBill.id)}>Pay This Bill</button>
          )}
        </div>
      )}

      <table className="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>PO Number</th>
            <th>Vendor</th>
            <th>Description</th>
            <th>Amount</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((po) => (
            <tr key={po.id}>
              <td>{po.id}</td>
              <td>{po.poNumber}</td>
              <td>{po.vendorName}</td>
              <td>{po.description}</td>
              <td>₹{po.amount}</td>
              <td>{po.status}</td>
              <td>
                {po.status === "CREATED" && (
                  <button onClick={() => handleConvertToBill(po.id)}>Convert to Bill</button>
                )}
                {po.status !== "CREATED" && <span className="muted">—</span>}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
