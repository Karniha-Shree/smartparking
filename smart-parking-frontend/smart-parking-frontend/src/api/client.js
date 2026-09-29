// Base URL of your Spring Boot backend. Change this if your backend runs on a different port.
const BASE_URL = "http://localhost:8080/api";

// Generic fetch wrapper: handles JSON headers, error parsing, and empty (204) responses.
async function request(path, options = {}) {
  const response = await fetch(`${BASE_URL}${path}`, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });

  // 204 No Content (e.g. DELETE) has no JSON body to parse.
  if (response.status === 204) return null;

  const text = await response.text();
  const data = text ? JSON.parse(text) : null;

  if (!response.ok) {
    // Backend's GlobalExceptionHandler returns { message } or { errors: {...} }.
    const message =
      data?.message ||
      (data?.errors && Object.values(data.errors).join(", ")) ||
      `Request failed with status ${response.status}`;
    throw new Error(message);
  }

  return data;
}

const get = (path) => request(path, { method: "GET" });
const post = (path, body) => request(path, { method: "POST", body: JSON.stringify(body) });
const put = (path, body) => request(path, { method: "PUT", body: body ? JSON.stringify(body) : undefined });
const del = (path) => request(path, { method: "DELETE" });

export const api = {
  // Customers
  getCustomers: () => get("/customers"),
  getCustomer: (id) => get(`/customers/${id}`),
  createCustomer: (data) => post("/customers", data),
  updateCustomer: (id, data) => put(`/customers/${id}`, data),
  deleteCustomer: (id) => del(`/customers/${id}`),

  // Vehicles
  getVehicles: () => get("/vehicles"),
  getVehicle: (id) => get(`/vehicles/${id}`),
  createVehicle: (data) => post("/vehicles", data),
  updateVehicle: (id, data) => put(`/vehicles/${id}`, data),
  deleteVehicle: (id) => del(`/vehicles/${id}`),

  // Parking Lots
  getParkingLots: () => get("/parking/lots"),
  getParkingLot: (id) => get(`/parking/lots/${id}`),
  createParkingLot: (data) => post("/parking/lots", data),
  updateParkingLot: (id, data) => put(`/parking/lots/${id}`, data),
  deleteParkingLot: (id) => del(`/parking/lots/${id}`),
  getSlotsForLot: (id) => get(`/parking/lots/${id}/slots`),

  // Parking entry/exit
  vehicleEntry: (data) => post("/parking/entry", data),
  vehicleExit: (sessionId) => put(`/parking/exit/${sessionId}`),
  getAvailableSlots: () => get("/parking/available-slots"),
  getSession: (id) => get(`/parking/session/${id}`),

  // Pricing rules
  getPricingRules: () => get("/pricing-rules"),
  createPricingRule: (data) => post("/pricing-rules", data),

  // Invoices
  getInvoice: (id) => get(`/invoices/${id}`),

  // Payments
  createPayment: (data) => post("/payments", data),
  getPayment: (id) => get(`/payments/${id}`),

  // Vendors
  getVendors: () => get("/vendors"),
  getVendor: (id) => get(`/vendors/${id}`),
  createVendor: (data) => post("/vendors", data),

  // Purchase orders
  getPurchaseOrders: () => get("/purchase-orders"),
  getPurchaseOrder: (id) => get(`/purchase-orders/${id}`),
  createPurchaseOrder: (data) => post("/purchase-orders", data),
  convertToVendorBill: (id) => post(`/purchase-orders/${id}/vendor-bill`),
  payVendorBill: (billId) => put(`/purchase-orders/vendor-bill/${billId}/pay`),

  // Budgets
  getBudgets: () => get("/budgets"),
  getBudget: (id) => get(`/budgets/${id}`),
  createBudget: (data) => post("/budgets", data),

  // Reports
  getBalanceSheet: () => get("/reports/balance-sheet"),
  getProfitLoss: () => get("/reports/profit-loss"),
  getBudgetReport: () => get("/reports/budget"),
  getParkingRevenueReport: () => get("/reports/parking-revenue"),
};
