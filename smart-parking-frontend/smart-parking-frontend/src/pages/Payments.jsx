import { useState } from "react";
import { api } from "../api/client.js";
import StatusBanner from "../components/StatusBanner.jsx";

const emptyForm = { invoiceId: "", amount: "", paymentMethod: "CASH" };

export default function Payments() {
  const [form, setForm] = useState(emptyForm);
  const [invoicePreview, setInvoicePreview] = useState(null);
  const [payment, setPayment] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleLookupInvoice() {
    setError("");
    setInvoicePreview(null);
    if (!form.invoiceId) return;
    try {
      const invoice = await api.getInvoice(Number(form.invoiceId));
      setInvoicePreview(invoice);
      setForm((f) => ({ ...f, amount: invoice.amount }));
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setPayment(null);
    setLoading(true);
    try {
      const result = await api.createPayment({
        invoiceId: Number(form.invoiceId),
        amount: Number(form.amount),
        paymentMethod: form.paymentMethod,
      });
      setPayment(result);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div>
      <h1>Payments</h1>
      <p className="muted">
        Enter the Invoice ID you received after vehicle exit, look it up to confirm the amount, then pay it.
      </p>

      <form className="form-card" onSubmit={handleSubmit}>
        <h3>Pay an Invoice</h3>
        <div className="form-row">
          <input
            name="invoiceId"
            type="number"
            placeholder="Invoice ID"
            value={form.invoiceId}
            onChange={handleChange}
            onBlur={handleLookupInvoice}
            required
          />
          <button type="button" className="secondary" onClick={handleLookupInvoice}>
            Look Up
          </button>
        </div>

        {invoicePreview && (
          <div className="result-box">
            <strong>{invoicePreview.invoiceNumber}</strong>
            <p>
              Amount: ₹{invoicePreview.amount} <br />
              Paid: {invoicePreview.paid ? "Yes" : "No"}
            </p>
          </div>
        )}

        <div className="form-row">
          <input
            name="amount"
            type="number"
            step="0.01"
            placeholder="Amount"
            value={form.amount}
            onChange={handleChange}
            required
          />
          <select name="paymentMethod" value={form.paymentMethod} onChange={handleChange}>
            <option value="CASH">Cash</option>
            <option value="CARD">Card</option>
            <option value="UPI">UPI</option>
            <option value="BANK_TRANSFER">Bank Transfer</option>
          </select>
        </div>

        <div className="form-actions">
          <button type="submit" disabled={loading}>
            {loading ? "Processing..." : "Pay Now"}
          </button>
        </div>
      </form>

      <StatusBanner error={error} />

      {payment && (
        <div className="result-box success">
          <strong>Payment #{payment.id} successful</strong>
          <table className="mini-table">
            <tbody>
              <tr>
                <td>Invoice</td>
                <td>{payment.invoiceNumber}</td>
              </tr>
              <tr>
                <td>Amount</td>
                <td>₹{payment.amount}</td>
              </tr>
              <tr>
                <td>Method</td>
                <td>{payment.paymentMethod}</td>
              </tr>
              <tr>
                <td>Status</td>
                <td>{payment.paymentStatus}</td>
              </tr>
              <tr>
                <td>Date</td>
                <td>{new Date(payment.paymentDate).toLocaleString()}</td>
              </tr>
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
