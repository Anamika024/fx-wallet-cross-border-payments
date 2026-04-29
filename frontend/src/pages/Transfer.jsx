import { useCallback, useState } from 'react';
import { transferApi, walletApi } from '../api/endpoints.js';
import StatusMessage from '../components/StatusMessage.jsx';
import { useResource } from '../hooks/useResource.js';
import { useRealtimeTopic } from '../hooks/useRealtimeTopic.js';
import { currencies, money } from '../utils/format.js';

export default function Transfer() {
  const wallets = useResource(walletApi.list, []);
  const transfers = useResource(transferApi.list, []);
  const [form, setForm] = useState({ senderWalletId: '', receiverEmail: '', externalDestination: 'SWIFT-DEMO-001', targetCurrency: 'USD', amount: '500' });
  const [message, setMessage] = useState('');
  const refreshRealtime = useCallback(() => {
    transfers.refresh();
    wallets.refresh();
  }, [transfers.refresh, wallets.refresh]);
  useRealtimeTopic('/topic/transfers', refreshRealtime);

  async function submit(event) {
    event.preventDefault();
    const source = form.senderWalletId || wallets.data?.[0]?.id;
    const result = await transferApi.send({ ...form, senderWalletId: source, amount: Number(form.amount) });
    setMessage(`Transfer ${result.state.toLowerCase()}${result.fraudFlagged ? ' and queued for review' : ''}.`);
    transfers.refresh();
    wallets.refresh();
  }

  return (
    <div className="grid gap-5 lg:grid-cols-[380px_1fr]">
      <section className="rounded-md border border-line bg-white p-5">
        <h2 className="mb-4 text-xl font-semibold">Send transfer</h2>
        <form onSubmit={submit} className="space-y-4">
          <select className="input" value={form.senderWalletId || wallets.data?.[0]?.id || ''} onChange={(e) => setForm({ ...form, senderWalletId: e.target.value })}>
            {(wallets.data || []).map((wallet) => <option key={wallet.id} value={wallet.id}>{wallet.currencyCode} · {money(wallet.balance, wallet.currencyCode)}</option>)}
          </select>
          <input className="input" placeholder="Recipient email" value={form.receiverEmail} onChange={(e) => setForm({ ...form, receiverEmail: e.target.value })} />
          <input className="input" placeholder="SWIFT/IBAN mock" value={form.externalDestination} onChange={(e) => setForm({ ...form, externalDestination: e.target.value })} />
          <select className="input" value={form.targetCurrency} onChange={(e) => setForm({ ...form, targetCurrency: e.target.value })}>{currencies.map((currency) => <option key={currency}>{currency}</option>)}</select>
          <input className="input" type="number" min="1" value={form.amount} onChange={(e) => setForm({ ...form, amount: e.target.value })} />
          <button className="btn-primary w-full">Send funds</button>
          {message && <p className="text-sm font-medium text-teal">{message}</p>}
        </form>
      </section>
      <section className="rounded-md border border-line bg-white p-5">
        <h2 className="mb-4 text-xl font-semibold">Transfer history</h2>
        <StatusMessage loading={transfers.loading} error={transfers.error}>
          <div className="space-y-3">
            {(transfers.data || []).map((transfer) => <article className="rounded-md border border-line p-4" key={transfer.id}><div className="flex items-center justify-between gap-3"><p className="font-semibold">{money(transfer.sourceAmount, transfer.sourceCurrency)} to {transfer.targetCurrency}</p><span className="rounded bg-mist px-2 py-1 text-xs">{transfer.state}</span></div><p className="mt-1 text-sm text-slate-500">Fee {money(transfer.fee, transfer.sourceCurrency)} · Rate {transfer.exchangeRate}</p></article>)}
          </div>
        </StatusMessage>
      </section>
    </div>
  );
}
