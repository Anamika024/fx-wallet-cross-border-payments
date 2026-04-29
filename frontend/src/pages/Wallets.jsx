import { useState } from 'react';
import { walletApi } from '../api/endpoints.js';
import StatusMessage from '../components/StatusMessage.jsx';
import TransactionTable from '../components/TransactionTable.jsx';
import WalletCard from '../components/WalletCard.jsx';
import { useResource } from '../hooks/useResource.js';
import { currencies } from '../utils/format.js';

export default function Wallets() {
  const wallets = useResource(walletApi.list, []);
  const [selected, setSelected] = useState(null);
  const detail = useResource(() => selected ? walletApi.detail(selected) : Promise.resolve(null), [selected]);

  async function create(currency) {
    await walletApi.create(currency);
    wallets.refresh();
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <h2 className="text-2xl font-semibold">Wallets</h2>
        <select className="input max-w-xs" onChange={(e) => e.target.value && create(e.target.value)} defaultValue="">
          <option value="">Create currency wallet</option>
          {currencies.map((currency) => <option key={currency}>{currency}</option>)}
        </select>
      </div>
      <StatusMessage loading={wallets.loading} error={wallets.error}>
        <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
          {(wallets.data || []).map((wallet) => (
            <button className="text-left" key={wallet.id} onClick={() => setSelected(wallet.id)}>
              <WalletCard wallet={wallet} />
            </button>
          ))}
        </div>
      </StatusMessage>
      {selected && <StatusMessage loading={detail.loading} error={detail.error}><TransactionTable rows={detail.data?.transactions || []} /></StatusMessage>}
    </div>
  );
}
