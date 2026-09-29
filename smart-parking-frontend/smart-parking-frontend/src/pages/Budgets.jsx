import { useEffect, useState } from "react";
import { api } from "../api/client.js";
import StatusBanner from "../components/StatusBanner.jsx";

const emptyForm = { name: "", startDate: "", endDate: "" };
const emptyLine = { accountId: "", plannedAmount: "" };

export default function Budgets() {
  const [budgets, setBudgets] = useState([]);
  const [accounts, setAccounts] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [lines, setLines] = useState([{ ...emptyLine }]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadAll() {
    setLoading(true);
    setError("");
    try {
      const [budgetList, balanceSheet, profitLoss] = await Promise.all([
        api.getBudgets(),
        api.getBalanceSheet(),
        api.getProfitLoss(),
      ]);
      setBudgets(budgetList);

      // The backend has no dedicated "list accounts" endpoint, so we gather the
      // Chart of Accounts from the report endpoints instead (they already return account rows).
      const combined = [
        ...balanceSheet.assets,
        ...balanceSheet.liabilities,
        ...profitLoss.income,
        ...profitLoss.expenses,
      ];
      const uniqueById = Object.values(
        combined.reduce((acc, a) => {
          acc[a.id] = a;
          return acc;
        }, {})
      );
      setAccounts(uniqueById);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadAll();
  }, []);

  function handleFormChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  function handleLineChange(index, field, value) {
    const updated = [...lines];
    updated[index] = { ...updated[index], [field]: value };
    setLines(updated);
  }

  function addLine() {
    setLines([...lines, { ...emptyLine }]);
  }

  function removeLine(index) {
    setLines(lines.filter((_, i) => i !== index));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    try {
      const payload = {
        ...form,
        lines: lines
          .filter((l) => l.accountId && l.plannedAmount)
          .map((l) => ({ accountId: Number(l.accountId), plannedAmount: Number(l.plannedAmount) })),
      };
      await api.createBudget(payload);
      setForm(emptyForm);
      setLines([{ ...emptyLine }]);
      loadAll();
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div>
      <h1>Budgets</h1>
      <p className="muted">
        {accounts.length === 0
          ? "No accounts exist yet — process at least one payment or vendor bill first so the Chart of Accounts has entries to budget against."
          : "Pick accounts from your Chart of Accounts and set a planned amount for each."}
      </p>

      <form className="form-card" onSubmit={handleSubmit}>
        <h3>New Budget</h3>
        <div className="form-row">
          <input name="name" placeholder="Budget name" value={form.name} onChange={handleFormChange} required />
        </div>
        <div className="form-row">
          <label>
            Start date
            <input name="startDate" type="date" value={form.startDate} onChange={handleFormChange} required />
          </label>
          <label>
            End date
            <input name="endDate" type="date" value={form.endDate} onChange={handleFormChange} required />
          </label>
        </div>

        <h4>Budget Lines</h4>
        {lines.map((line, i) => (
          <div className="form-row" key={i}>
            <select value={line.accountId} onChange={(e) => handleLineChange(i, "accountId", e.target.value)}>
              <option value="">-- Account --</option>
              {accounts.map((a) => (
                <option key={a.id} value={a.id}>
                  {a.accountName}
                </option>
              ))}
            </select>
            <input
              type="number"
              step="0.01"
              placeholder="Planned amount"
              value={line.plannedAmount}
              onChange={(e) => handleLineChange(i, "plannedAmount", e.target.value)}
            />
            {lines.length > 1 && (
              <button type="button" className="secondary" onClick={() => removeLine(i)}>
                Remove
              </button>
            )}
          </div>
        ))}
        <button type="button" className="secondary" onClick={addLine}>
          + Add Line
        </button>

        <div className="form-actions">
          <button type="submit">Create Budget</button>
        </div>
      </form>

      <StatusBanner loading={loading} error={error} />

      {budgets.map((b) => (
        <div className="result-box" key={b.id}>
          <strong>
            {b.name} ({b.startDate} to {b.endDate})
          </strong>
          <table className="mini-table">
            <thead>
              <tr>
                <th>Account</th>
                <th>Planned</th>
                <th>Actual</th>
                <th>Variance</th>
              </tr>
            </thead>
            <tbody>
              {b.lines.map((line) => (
                <tr key={line.id}>
                  <td>{line.accountName}</td>
                  <td>₹{line.plannedAmount}</td>
                  <td>₹{line.actualAmount}</td>
                  <td>₹{line.variance}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ))}
    </div>
  );
}
