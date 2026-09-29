import { useEffect, useState } from "react";
import { api } from "../api/client.js";
import StatusBanner from "../components/StatusBanner.jsx";

const TABS = [
  { key: "revenue", label: "Parking Revenue" },
  { key: "pnl", label: "Profit & Loss" },
  { key: "balance", label: "Balance Sheet" },
];

export default function Reports() {
  const [activeTab, setActiveTab] = useState("revenue");
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadReport(tab) {
    setLoading(true);
    setError("");
    setData(null);
    try {
      let result;
      if (tab === "revenue") result = await api.getParkingRevenueReport();
      else if (tab === "pnl") result = await api.getProfitLoss();
      else if (tab === "balance") result = await api.getBalanceSheet();
      setData(result);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadReport(activeTab);
  }, [activeTab]);

  return (
    <div>
      <h1>Reports</h1>

      <div className="tabs">
        {TABS.map((tab) => (
          <button
            key={tab.key}
            className={activeTab === tab.key ? "tab active" : "tab"}
            onClick={() => setActiveTab(tab.key)}
          >
            {tab.label}
          </button>
        ))}
      </div>

      <StatusBanner loading={loading} error={error} />

      {data && activeTab === "revenue" && <RevenueReport data={data} />}
      {data && activeTab === "pnl" && <AccountTypeReport data={data} incomeKey="income" totalIncomeKey="totalIncome" expenseKey="expenses" totalExpenseKey="totalExpenses" netKey="netProfit" netLabel="Net Profit" />}
      {data && activeTab === "balance" && <BalanceReport data={data} />}
    </div>
  );
}

function RevenueReport({ data }) {
  return (
    <div className="card-grid">
      <Stat label="Total Sessions" value={data.totalSessions} />
      <Stat label="Total Invoiced" value={`₹${data.totalInvoiced}`} />
      <Stat label="Total Collected" value={`₹${data.totalCollected}`} />
      <Stat label="Outstanding" value={`₹${data.outstanding}`} />
      <Stat label="Paid Invoices" value={data.paidInvoices} />
      <Stat label="Unpaid Invoices" value={data.unpaidInvoices} />
    </div>
  );
}

function AccountTypeReport({ data, incomeKey, totalIncomeKey, expenseKey, totalExpenseKey, netKey, netLabel }) {
  return (
    <div>
      <div className="two-col">
        <AccountTable title="Income" accounts={data[incomeKey]} total={data[totalIncomeKey]} />
        <AccountTable title="Expenses" accounts={data[expenseKey]} total={data[totalExpenseKey]} />
      </div>
      <div className="card-grid">
        <Stat label={netLabel} value={`₹${data[netKey]}`} />
      </div>
    </div>
  );
}

function BalanceReport({ data }) {
  return (
    <div>
      <div className="two-col">
        <AccountTable title="Assets" accounts={data.assets} total={data.totalAssets} />
        <AccountTable title="Liabilities" accounts={data.liabilities} total={data.totalLiabilities} />
      </div>
      <div className="card-grid">
        <Stat label="Equity" value={`₹${data.equity}`} />
      </div>
    </div>
  );
}

function AccountTable({ title, accounts, total }) {
  return (
    <div className="form-card">
      <h3>{title}</h3>
      <table className="mini-table">
        <thead>
          <tr>
            <th>Account</th>
            <th>Balance</th>
          </tr>
        </thead>
        <tbody>
          {accounts.length === 0 && (
            <tr>
              <td colSpan={2} className="muted">
                No entries yet
              </td>
            </tr>
          )}
          {accounts.map((a) => (
            <tr key={a.id}>
              <td>{a.accountName}</td>
              <td>₹{a.balance}</td>
            </tr>
          ))}
        </tbody>
        <tfoot>
          <tr>
            <td>
              <strong>Total</strong>
            </td>
            <td>
              <strong>₹{total}</strong>
            </td>
          </tr>
        </tfoot>
      </table>
    </div>
  );
}

function Stat({ label, value }) {
  return (
    <div className="stat-card">
      <div className="stat-value">{value}</div>
      <div className="stat-label">{label}</div>
    </div>
  );
}
