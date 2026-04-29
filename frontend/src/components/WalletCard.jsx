import { Wallet } from 'lucide-react';
import { money } from '../utils/format.js';

export default function WalletCard({ wallet }) {
  return (
    <article className="rounded-md border border-line bg-white p-4">
      <div className="flex items-start justify-between gap-3">
        <div>
          <p className="text-sm text-slate-500">{wallet.defaultWallet ? 'Default wallet' : 'Currency wallet'}</p>
          <h3 className="text-2xl font-semibold">{wallet.currencyCode}</h3>
        </div>
        <Wallet className="text-teal" size={24} />
      </div>
      <p className="mt-5 text-2xl font-semibold">{money(wallet.balance, wallet.currencyCode)}</p>
      <p className="mt-2 text-xs font-medium uppercase text-slate-500">{wallet.status}</p>
    </article>
  );
}
