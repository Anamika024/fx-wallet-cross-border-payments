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
  return <main className="grid min-h-screen place-items-center bg-mist px-4"><section className="w-full max-w-md rounded-md border border-line bg-white p-6"><p className="text-xs font-semibold uppercase text-teal">FX Wallet</p><h1 className="mb-6 text-2xl font-semibold">{title}</h1>{children}</section></main>;
}
