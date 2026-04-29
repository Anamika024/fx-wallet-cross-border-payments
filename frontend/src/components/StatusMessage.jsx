export default function StatusMessage({ loading, error, children }) {
  if (loading) return <p className="rounded-md border border-line bg-white p-4 text-sm text-slate-500">Loading...</p>;
  if (error) return <p className="rounded-md border border-coral bg-white p-4 text-sm text-coral">{error}</p>;
  return children;
}
