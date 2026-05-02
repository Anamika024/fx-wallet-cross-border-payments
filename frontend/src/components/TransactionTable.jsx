import { compactDate, money } from '../utils/format.js';

export default function TransactionTable({ rows = [] }) {
  return (
    <div className="overflow-hidden rounded-md border border-line bg-white shadow-sm">
      <table className="w-full min-w-[620px] text-left text-sm">
        <thead className="bg-mist text-[11px] uppercase tracking-[0.08em] text-slate-500">
          <tr>
            <th className="px-4 py-3">Date</th>
            <th className="px-4 py-3">Type</th>
            <th className="px-4 py-3">Description</th>
            <th className="px-4 py-3 text-right">Amount</th>
            <th className="px-4 py-3">Status</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-line">
          {rows.map((tx) => (
            <tr key={tx.id}>
              <td className="px-4 py-3 text-slate-500">{compactDate(tx.createdAt)}</td>
              <td className="px-4 py-3 font-medium">{tx.type}</td>
              <td className="px-4 py-3">{tx.description}</td>
              <td className="px-4 py-3 text-right font-semibold">{money(tx.amount, tx.currencyCode)}</td>
              <td className="px-4 py-3"><span className={`status-pill ${tx.status === 'COMPLETED' ? 'status-done' : tx.status === 'REVIEW' ? 'status-review' : tx.status === 'FAILED' ? 'status-fail' : 'status-pending'}`}>{tx.status}</span></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
