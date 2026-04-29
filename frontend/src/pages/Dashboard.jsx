import { Bar, BarChart, Cell, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import { analyticsApi, walletApi } from '../api/endpoints.js';
import StatusMessage from '../components/StatusMessage.jsx';
import WalletCard from '../components/WalletCard.jsx';
import { useResource } from '../hooks/useResource.js';
import { money } from '../utils/format.js';

const colors = ['#087f7b', '#d95f43', '#c99700', '#415a77', '#6d597a', '#2d6a4f'];

export default function Dashboard() {
  const wallets = useResource(walletApi.list, []);
  const analytics = useResource(analyticsApi.dashboard, []);
  const spend = Object.entries(analytics.data?.spendByCurrency || {}).map(([name, value]) => ({ name, value: Number(value) }));
  const portfolio = (wallets.data || []).reduce((sum, wallet) => sum + Number(wallet.balance), 0);

  return (
    <div className="space-y-6">
      <section className="grid gap-4 lg:grid-cols-[1.1fr_0.9fr]">
        <div className="rounded-md border border-line bg-white p-5">
          <p className="text-sm text-slate-500">Portfolio value</p>
          <h2 className="mt-1 text-3xl font-semibold">{money(portfolio, 'INR')}</h2>
          <p className="mt-2 text-sm text-slate-500">Base currency display for all wallet balances.</p>
        </div>
        <StatusMessage loading={wallets.loading} error={wallets.error}>
          <div className="grid gap-3 sm:grid-cols-2">
            {(wallets.data || []).slice(0, 2).map((wallet) => <WalletCard key={wallet.id} wallet={wallet} />)}
          </div>
        </StatusMessage>
      </section>
      <section className="grid gap-4 lg:grid-cols-2">
        <div className="rounded-md border border-line bg-white p-5">
          <h2 className="mb-4 text-lg font-semibold">Spending by currency</h2>
          <ResponsiveContainer width="100%" height={260}>
            <PieChart>
              <Pie data={spend} dataKey="value" nameKey="name" innerRadius={62} outerRadius={96}>
                {spend.map((_, index) => <Cell key={index} fill={colors[index % colors.length]} />)}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>
        <div className="rounded-md border border-line bg-white p-5">
          <h2 className="mb-4 text-lg font-semibold">Monthly transfer volume</h2>
          <ResponsiveContainer width="100%" height={260}>
            <BarChart data={analytics.data?.monthlyVolume || []}>
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="volume" fill="#087f7b" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </section>
    </div>
  );
}
