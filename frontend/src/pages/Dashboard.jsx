import { AlertTriangle, ArrowUpRight, Landmark, WalletCards } from 'lucide-react';
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
  const activeWallets = wallets.data?.length || 0;
  const monthlyTotal = (analytics.data?.monthlyVolume || []).reduce((sum, row) => sum + Number(row.volume || 0), 0);

  return (
    <div className="space-y-5">
      <section className="grid gap-3 sm:grid-cols-2 xl:grid-cols-4">
        <Metric title="Portfolio value" value={money(portfolio, 'INR')} note="+ realtime wallet ledger" tone="up" icon={Landmark} />
        <Metric title="Monthly volume" value={money(monthlyTotal, 'INR')} note="Transfer throughput" icon={ArrowUpRight} />
        <Metric title="Active wallets" value={activeWallets} note="Multi-currency accounts" icon={WalletCards} />
        <Metric title="Fraud alerts" value="Live" note="Admin review queue" tone="warn" icon={AlertTriangle} />
      </section>

      <section className="grid gap-4 xl:grid-cols-[1.15fr_0.85fr]">
        <StatusMessage loading={wallets.loading} error={wallets.error}>
          <div className="surface-card">
            <div className="mb-3 flex items-center justify-between gap-3">
              <div>
                <p className="section-label">My wallets</p>
                <h2 className="text-lg font-semibold">Currency balances</h2>
              </div>
              <span className="status-pill status-done">Active</span>
            </div>
            <div className="grid gap-3 md:grid-cols-2">
              {(wallets.data || []).slice(0, 4).map((wallet) => <WalletCard key={wallet.id} wallet={wallet} />)}
            </div>
          </div>
        </StatusMessage>
        <div className="surface-card">
          <div className="mb-3">
            <p className="section-label">Risk operations</p>
            <h2 className="text-lg font-semibold">Payment control center</h2>
          </div>
          <div className="space-y-3">
            <ControlRow title="FX quote lock" detail="30 second confirmation window" status="Live" />
            <ControlRow title="High value rule" detail="Flags large transfers for admin review" status="Review" />
            <ControlRow title="Transfer state machine" detail="Processing, review, completed, failed" status="Tracked" />
            <ControlRow title="Realtime events" detail="FX, transfers, and fraud topics" status="STOMP" />
          </div>
        </div>
      </section>

      <section className="grid gap-4 lg:grid-cols-2">
        <div className="surface-card">
          <div className="mb-4 flex items-center justify-between">
            <div>
              <p className="section-label">Analytics</p>
              <h2 className="text-lg font-semibold">Spending by currency</h2>
            </div>
          </div>
          <ResponsiveContainer width="100%" height={260}>
            <PieChart>
              <Pie data={spend} dataKey="value" nameKey="name" innerRadius={62} outerRadius={96}>
                {spend.map((_, index) => <Cell key={index} fill={colors[index % colors.length]} />)}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>
        <div className="surface-card">
          <div className="mb-4">
            <p className="section-label">Throughput</p>
            <h2 className="text-lg font-semibold">Monthly transfer volume</h2>
          </div>
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

function Metric({ title, value, note, tone, icon: Icon }) {
  const toneClass = tone === 'warn' ? 'text-amber-700 bg-amber-50 ring-amber-100' : tone === 'up' ? 'text-teal bg-emerald-50 ring-emerald-100' : 'text-slate-700 bg-mist ring-line';
  return (
    <article className="metric-card">
      <div className="flex items-start justify-between gap-3">
        <div>
          <p className="text-xs text-slate-500">{title}</p>
          <p className="metric-value">{value}</p>
        </div>
        <div className={`grid h-9 w-9 place-items-center rounded-md ring-1 ${toneClass}`}>
          <Icon size={17} />
        </div>
      </div>
      <p className={`mt-2 text-xs ${tone === 'warn' ? 'text-amber-700' : tone === 'up' ? 'text-teal' : 'text-slate-500'}`}>{note}</p>
    </article>
  );
}

function ControlRow({ title, detail, status }) {
  return (
    <div className="flex items-center justify-between gap-3 border-b border-line pb-3 last:border-b-0 last:pb-0">
      <div>
        <p className="text-sm font-medium">{title}</p>
        <p className="text-xs text-slate-500">{detail}</p>
      </div>
      <span className="status-pill status-pending">{status}</span>
    </div>
  );
}
