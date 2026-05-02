import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authApi } from '../api/endpoints.js';
import { useAuthStore } from '../store/authStore.js';

export default function Login() {
  const [form, setForm] = useState({ email: 'demo@fxwallet.local', password: 'Password123!' });
  const [error, setError] = useState('');
  const setSession = useAuthStore((state) => state.setSession);
  const navigate = useNavigate();

  async function submit(event) {
    event.preventDefault();
    setError('');
    try {
      setSession(await authApi.login(form));
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || err.message);
    }
  }

  return (
    <AuthFrame title="Sign in">
      <form onSubmit={submit} className="space-y-4">
        <input className="input" placeholder="Email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
        <input className="input" type="password" placeholder="Password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
        {error && <p className="text-sm text-coral">{error}</p>}
        <button className="btn-primary w-full">Sign in</button>
        <p className="text-sm text-slate-500">Need an account? <Link className="font-medium text-teal" to="/register">Create one</Link></p>
      </form>
    </AuthFrame>
  );
}

function AuthFrame({ title, children }) {
  return (
    <main className="grid min-h-screen place-items-center bg-mist px-4 py-10">
      <section className="grid w-full max-w-5xl overflow-hidden rounded-md border border-line bg-white shadow-xl lg:grid-cols-[1.05fr_0.95fr]">
        <div className="hidden bg-[#0f6e56] p-8 text-white lg:block">
          <p className="text-xs font-semibold uppercase tracking-[0.18em] text-emerald-100">FX Wallet</p>
          <h1 className="mt-4 max-w-sm text-3xl font-semibold leading-tight">Move money across currencies with control.</h1>
          <div className="mt-8 grid gap-3">
            <AuthMetric label="Wallet ledger" value="6 currencies" />
            <AuthMetric label="FX quote lock" value="30 seconds" />
            <AuthMetric label="Fraud review" value="Realtime alerts" />
          </div>
        </div>
        <div className="p-6 sm:p-8">
          <p className="text-xs font-semibold uppercase tracking-[0.16em] text-teal">Secure access</p>
          <h2 className="mb-6 mt-2 text-2xl font-semibold">{title}</h2>
          {children}
        </div>
      </section>
    </main>
  );
}

function AuthMetric({ label, value }) {
  return (
    <div className="rounded-md bg-white/10 p-3 ring-1 ring-white/15">
      <p className="text-xs text-emerald-100">{label}</p>
      <p className="text-lg font-semibold">{value}</p>
    </div>
  );
}
