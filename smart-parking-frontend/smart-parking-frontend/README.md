# Smart Parking Hub — Frontend

React + Vite frontend for the Smart Parking Space Allocation & Dynamic Pricing Hub backend.

## Prerequisites

- Node.js 18+ installed
- The Spring Boot backend already running on `http://localhost:8080`

## Setup (in VS Code)

1. Open this folder in VS Code (`File → Open Folder`).
2. Open the built-in terminal (`` Ctrl+` ``) and run:

   ```bash
   npm install
   npm run dev
   ```

3. Vite will start the dev server, usually at `http://localhost:5173`. Open that URL in your browser.

That's it — no build step needed for development. If your backend runs on a different host/port, edit
`BASE_URL` at the top of `src/api/client.js`.

## Project structure

```
src/
  api/client.js         -> single place with every backend API call
  components/
    Navbar.jsx
    StatusBanner.jsx
  pages/
    Dashboard.jsx
    Customers.jsx
    Vehicles.jsx
    ParkingLots.jsx
    ParkingEntryExit.jsx  -> the core entry/exit flow
    PricingRules.jsx
    Payments.jsx
    Vendors.jsx
    PurchaseOrders.jsx
    Budgets.jsx
    Reports.jsx
  App.jsx
  main.jsx
  styles.css
```

## Suggested test flow

1. **Customers** — create a customer.
2. **Vehicles** — add a vehicle for that customer.
3. **Parking Lots** — create a lot (slots auto-generate).
4. **Pricing Rules** (optional) — add a peak-hour rule.
5. **Entry / Exit** — check the vehicle in, note the Session ID, then check it out to get an invoice.
6. **Payments** — look up the invoice by ID and pay it.
7. **Vendors / Purchase Orders** — try the maintenance expense flow (PO → Vendor Bill → Pay).
8. **Budgets** — after at least one payment/bill exists, create a budget against real accounts.
9. **Reports** — view parking revenue, P&L, and balance sheet.

## Notes

- If you see "Could not reach the backend", make sure Spring Boot is running and CORS is allowed
  (see below).
- CORS: if browser requests are blocked, add a `CorsConfig` bean to your Spring Boot app allowing
  `http://localhost:5173`, or annotate controllers with `@CrossOrigin(origins = "http://localhost:5173")`.
