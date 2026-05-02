import { Wallet } from 'lucide-react';
import { money } from '../utils/format.js';

const flagStyles = {
  INR: 'bg-[#ff9933] text-white',
  USD: 'bg-[#3c3b6e] text-white',
  EUR: 'bg-[#003399] text-[#ffd700]',
  GBP: 'bg-[#012169] text-white',
  AED: 'bg-[#00732f] text-white',
  SGD: 'bg-[#ef3340] text-white'
};

export default function WalletCard({ wallet }) {
  return (
    <article className="group rounded-md border border-line bg-white p-4 shadow-sm transition hover:-translate-y-0.5 hover:border-emerald-200 hover:shadow-md">
      <div className="flex items-start justify-between gap-3">
        <div className="flex items-center gap-3">
          <div className={`grid h-9 w-11 place-items-center rounded-md text-xs font-semibold ${flagStyles[wallet.currencyCode] || 'bg-mist text-ink'}`}>{wallet.currencyCode.slice(0, 2)}</div>
          <div>
            <p className="text-xs text-slate-500">{wallet.defaultWallet ? 'Default wallet' : 'Currency wallet'}</p>
            <h3 className="text-xl font-semibold">{wallet.currencyCode}</h3>
          </div>
        </div>
        <Wallet className="text-teal opacity-80 transition group-hover:opacity-100" size={22} />
      </div>
      <p className="mt-5 text-2xl font-semibold tracking-tight">{money(wallet.balance, wallet.currencyCode)}</p>
      <div className="mt-3 flex items-center justify-between">
        <span className="status-pill status-done">{wallet.status}</span>
        <span className="text-[11px] text-slate-500">Available balance</span>
      </div>
    </article>
  );
}
