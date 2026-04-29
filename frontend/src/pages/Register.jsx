import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authApi } from '../api/endpoints.js';
import { useAuthStore } from '../store/authStore.js';

export default function Register() {
  const [form, setForm] = useState({ fullName: '', email: '', password: '' });
  const [error, setError] = useState('');
  const setSession = useAuthStore((state) => state.setSession);
  const navigate = useNavigate();

  async function submit(event) {
    event.preventDefault();
    try {
      setSession(await authApi.register(form));
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || err.message);
    }
  }

  return (
    <main className="grid min-h-screen place-items-center bg-mist px-4">
      <section className="w-full max-w-md rounded-md border border-line bg-white p-6">
        <p className="text-xs font-semibold uppercase text-teal">FX Wallet</p>
        <h1 className="mb-6 text-2xl font-semibold">Create account</h1>
        <form onSubmit={submit} className="space-y-4">
          <input className="input" placeholder="Full name" value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} />
          <input className="input" placeholder="Email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
          <input className="input" type="password" placeholder="Password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
          {error && <p className="text-sm text-coral">{error}</p>}
          <button className="btn-primary w-full">Create account</button>
          <p className="text-sm text-slate-500">Already registered? <Link className="font-medium text-teal" to="/login">Sign in</Link></p>
        </form>
      </section>
    </main>
  );
}
