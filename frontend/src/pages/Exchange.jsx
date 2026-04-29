import { useState } from 'react';
import { Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import { fxApi, walletApi } from '../api/endpoints.js';
import StatusMessage from '../components/StatusMessage.jsx';
import { useResource } from '../hooks/useResource.js';
import { currencies, money } from '../utils/format.js';

export default function Exchange() {
  const wallets = useResource(walletApi.list, []);
  const [form, setForm] = useState({ sourceWalletId: '', targetCurrency: 'USD', amount: '1000' });
  const [quote, setQuote] = useState(null);
  const [message, setMessage] = useState('');
  const source = (wallets.data || []).find((wallet) => wallet.id === form.sourceWalletId) || wallets.data?.[0];
  const history = useResource(() => source ? fxApi.history(source.currencyCode, form.targetCurrency) : Promise.resolve({ points: [] }), [source?.currencyCode, form.targetCurrency]);

  async function preview() {
    setQuote(await fxApi.quote({ ...form, sourceWalletId: form.sourceWalletId || source.id, amount: Number(form.amount) }));
  }

  async function convert() {
    await fxApi.convert(quote.quoteId);
    setMessage('Conversion completed.');
    setQuote(null);
    wallets.refresh();
  }

  return (
    <div className="grid gap-5 lg:grid-cols-[380px_1fr]">
      <section className="rounded-md border border-line bg-white p-5">
        <h2 className="mb-4 text-xl font-semibold">FX exchange</h2>
        <div className="space-y-4">
          <select className="input" value={form.sourceWalletId || source?.id || ''} onChange={(e) => setForm({ ...form, sourceWalletId: e.target.value })}>
            {(wallets.data || []).map((wallet) => <option key={wallet.id} value={wallet.id}>{wallet.currencyCode} · {money(wallet.balance, wallet.currencyCode)}</option>)}
          </select>
          <select className="input" value={form.targetCurrency} onChange={(e) => setForm({ ...form, targetCurrency: e.target.value })}>
            {currencies.map((currency) => <option key={currency}>{currency}</option>)}
          </select>
          <input className="input" type="number" min="1" value={form.amount} onChange={(e) => setForm({ ...form, amount: e.target.value })} />
          <button className="btn-primary w-full" onClick={preview}>Preview locked rate</button>
          {quote && <div className="rounded-md bg-mist p-4 text-sm"><p className="font-semibold">{money(quote.sourceAmount, quote.from)} → {money(quote.targetAmount, quote.to)}</p><p>Rate {quote.rate} · Fee {money(quote.fee, quote.from)}</p><button className="btn-secondary mt-3 w-full" onClick={convert}>Confirm conversion</button></div>}
          {message && <p className="text-sm font-medium text-teal">{message}</p>}
        </div>
      </section>
      <section className="rounded-md border border-line bg-white p-5">
        <h2 className="mb-4 text-xl font-semibold">7-day rate history</h2>
        <StatusMessage loading={history.loading} error={history.error}>
          <ResponsiveContainer width="100%" height={320}>
            <LineChart data={history.data?.points || []}>
              <XAxis dataKey="date" />
              <YAxis />
              <Tooltip />
              <Line type="monotone" dataKey="rate" stroke="#087f7b" strokeWidth={3} dot={false} />
            </LineChart>
          </ResponsiveContainer>
        </StatusMessage>
      </section>
    </div>
  );
}
