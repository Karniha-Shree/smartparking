import { NavLink } from "react-router-dom";

const links = [
  { to: "/", label: "Dashboard", end: true },
  { to: "/customers", label: "Customers" },
  { to: "/vehicles", label: "Vehicles" },
  { to: "/parking-lots", label: "Parking Lots" },
  { to: "/parking", label: "Entry / Exit" },
  { to: "/pricing-rules", label: "Pricing Rules" },
  { to: "/payments", label: "Payments" },
  { to: "/vendors", label: "Vendors" },
  { to: "/purchase-orders", label: "Purchase Orders" },
  { to: "/budgets", label: "Budgets" },
  { to: "/reports", label: "Reports" },
];

export default function Navbar() {
  return (
    <nav className="navbar">
      <div className="navbar-title">🅿️ Smart Parking Hub</div>
      <ul className="navbar-links">
        {links.map((link) => (
          <li key={link.to}>
            <NavLink
              to={link.to}
              end={link.end}
              className={({ isActive }) => (isActive ? "active" : "")}
            >
              {link.label}
            </NavLink>
          </li>
        ))}
      </ul>
    </nav>
  );
}
