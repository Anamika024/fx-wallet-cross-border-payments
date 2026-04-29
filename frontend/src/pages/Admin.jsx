import { Check, X } from 'lucide-react';
import { useCallback } from 'react';
import { adminApi } from '../api/endpoints.js';
import StatusMessage from '../components/StatusMessage.jsx';
import { useResource } from '../hooks/useResource.js';
import { useRealtimeTopic } from '../hooks/useRealtimeTopic.js';
import { money } from '../utils/format.js';

export default function Admin() {
  const alerts = useResource(adminApi.alerts, []);
  const users = useResource(adminApi.users, []);
  const refreshAlerts = useCallback(() => alerts.refresh(), [alerts.refresh]);
  useRealtimeTopic('/topic/admin/fraud-alerts', refreshAlerts);

  async function act(id, action) {
    await adminApi[action](id);
    alerts.refresh();
  }

  return (
    <div className="space-y-6">
      <section className="rounded-md border border-line bg-white p-5">
        <h2 className="mb-4 text-xl font-semibold">Fraud review queue</h2>
        <StatusMessage loading={alerts.loading} error={alerts.error}>
          <div className="space-y-3">
            {(alerts.data || []).map((alert) => (
              <article className="flex flex-col gap-3 rounded-md border border-line p-4 md:flex-row md:items-center md:justify-between" key={alert.id}>
                <div><p className="font-semibold">{alert.ruleTriggered} · {alert.userEmail}</p><p className="text-sm text-slate-500">{money(alert.transfer.sourceAmount, alert.transfer.sourceCurrency)} to {alert.transfer.targetCurrency}</p></div>
                <div className="flex gap-2"><button className="icon-btn" onClick={() => act(alert.id, 'approve')}><Check size={18} /></button><button className="icon-btn" onClick={() => act(alert.id, 'reject')}><X size={18} /></button></div>
              </article>
            ))}
          </div>
        </StatusMessage>
      </section>
      <section className="rounded-md border border-line bg-white p-5">
        <h2 className="mb-4 text-xl font-semibold">Users</h2>
        <StatusMessage loading={users.loading} error={users.error}>
          <div className="grid gap-3 md:grid-cols-2">
            {(users.data || []).map((user) => <article className="rounded-md border border-line p-4" key={user.id}><p className="font-semibold">{user.fullName}</p><p className="text-sm text-slate-500">{user.email} · {user.role}</p></article>)}
          </div>
        </StatusMessage>
      </section>
    </div>
  );
}
