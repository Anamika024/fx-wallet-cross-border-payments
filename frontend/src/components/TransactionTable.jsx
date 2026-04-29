import { compactDate, money } from '../utils/format.js';

export default function TransactionTable({ rows = [] }) {
  return (
    <div className="overflow-hidden rounded-md border border-line bg-white">
      <table className="w-full min-w-[620px] text-left text-sm">
        <thead className="bg-slate-100 text-xs uppercase text-slate-500">
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
              <td className="px-4 py-3"><span className="rounded bg-mist px-2 py-1 text-xs">{tx.status}</span></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
