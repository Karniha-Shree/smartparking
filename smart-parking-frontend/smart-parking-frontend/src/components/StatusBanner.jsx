// Shows a loading / error / success message consistently across pages.
export default function StatusBanner({ loading, error, success }) {
  if (loading) return <p className="banner banner-info">Loading...</p>;
  if (error) return <p className="banner banner-error">{error}</p>;
  if (success) return <p className="banner banner-success">{success}</p>;
  return null;
}
