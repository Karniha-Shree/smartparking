import { useEffect, useState } from "react";
import { api } from "../api/client.js";
import StatusBanner from "../components/StatusBanner.jsx";

export default function ParkingEntryExit() {
  const [customers, setCustomers] = useState([]);
  const [vehicles, setVehicles] = useState([]);
  const [lots, setLots] = useState([]);
  const [availableSlots, setAvailableSlots] = useState([]);

  const [entryForm, setEntryForm] = useState({ customerId: "", vehicleId: "", parkingLotId: "" });
  const [exitSessionId, setExitSessionId] = useState("");

  const [lastSession, setLastSession] = useState(null);
  const [lastExit, setLastExit] = useState(null);

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  async function loadAll() {
    setLoading(true);
    setError("");
    try {
      const [c, v, l, s] = await Promise.all([
        api.getCustomers(),
        api.getVehicles(),
        api.getParkingLots(),
        api.getAvailableSlots(),
      ]);
      setCustomers(c);
      setVehicles(v);
      setLots(l);
      setAvailableSlots(s);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadAll();
  }, []);

  function handleEntryChange(e) {
    setEntryForm({ ...entryForm, [e.target.name]: e.target.value });
  }

  async function handleEntry(e) {
    e.preventDefault();
    setError("");
    setLastSession(null);
    try {
      const payload = {
        customerId: Number(entryForm.customerId),
        vehicleId: Number(entryForm.vehicleId),
        parkingLotId: Number(entryForm.parkingLotId),
      };
      const session = await api.vehicleEntry(payload);
      setLastSession(session);
      setExitSessionId(String(session.id));
      loadAll();
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleExit(e) {
    e.preventDefault();
    setError("");
    setLastExit(null);
    try {
      const result = await api.vehicleExit(Number(exitSessionId));
      setLastExit(result);
      loadAll();
    } catch (err) {
      setError(err.message);
    }
  }

  // Vehicles filtered to the selected customer, for a friendlier dropdown.
  const vehiclesForCustomer = entryForm.customerId
    ? vehicles.filter((v) => String(v.customerId) === entryForm.customerId)
    : vehicles;

  return (
    <div>
      <h1>Vehicle Entry / Exit</h1>
      <StatusBanner loading={loading} error={error} />

      <div className="two-col">
        <form className="form-card" onSubmit={handleEntry}>
          <h3>Vehicle Entry</h3>
          <select name="customerId" value={entryForm.customerId} onChange={handleEntryChange} required>
            <option value="">-- Customer --</option>
            {customers.map((c) => (
              <option key={c.id} value={c.id}>
                {c.name}
              </option>
            ))}
          </select>
          <select name="vehicleId" value={entryForm.vehicleId} onChange={handleEntryChange} required>
            <option value="">-- Vehicle --</option>
            {vehiclesForCustomer.map((v) => (
              <option key={v.id} value={v.id}>
                {v.vehicleNumber}
              </option>
            ))}
          </select>
          <select name="parkingLotId" value={entryForm.parkingLotId} onChange={handleEntryChange} required>
            <option value="">-- Parking Lot --</option>
            {lots.map((l) => (
              <option key={l.id} value={l.id}>
                {l.name}
              </option>
            ))}
          </select>
          <p className="hint">{availableSlots.length} slot(s) available across all lots right now.</p>
          <button type="submit">Check In</button>

          {lastSession && (
            <div className="result-box">
              <strong>Session #{lastSession.id} created</strong>
              <p>
                Slot: {lastSession.parkingSlot?.slotNumber} <br />
                Entry time: {formatDate(lastSession.entryTime)}
              </p>
            </div>
          )}
        </form>

        <form className="form-card" onSubmit={handleExit}>
          <h3>Vehicle Exit</h3>
          <input
            type="number"
            placeholder="Session ID"
            value={exitSessionId}
            onChange={(e) => setExitSessionId(e.target.value)}
            required
          />
          <button type="submit">Check Out</button>

          {lastExit && (
            <div className="result-box">
              <strong>Invoice {lastExit.invoiceNumber}</strong>
              <table className="mini-table">
                <tbody>
                  <tr>
                    <td>Vehicle</td>
                    <td>{lastExit.vehicleNumber}</td>
                  </tr>
                  <tr>
                    <td>Slot</td>
                    <td>{lastExit.slotNumber}</td>
                  </tr>
                  <tr>
                    <td>Entry</td>
                    <td>{formatDate(lastExit.entryTime)}</td>
                  </tr>
                  <tr>
                    <td>Exit</td>
                    <td>{formatDate(lastExit.exitTime)}</td>
                  </tr>
                  <tr>
                    <td>Duration</td>
                    <td>{lastExit.durationMinutes} min</td>
                  </tr>
                  <tr>
                    <td>Fee</td>
                    <td>₹{lastExit.parkingFee}</td>
                  </tr>
                  <tr>
                    <td>Payment status</td>
                    <td>{lastExit.paymentStatus}</td>
                  </tr>
                </tbody>
              </table>
              <p className="hint">Go to the Payments tab to pay this invoice.</p>
            </div>
          )}
        </form>
      </div>
    </div>
  );
}

function formatDate(value) {
  if (!value) return "-";
  return new Date(value).toLocaleString();
}
