import { ArrowLeftRight, BarChart3, LogOut, ShieldCheck, WalletCards } from 'lucide-react';
import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore.js';
import FXTicker from './FXTicker.jsx';

const links = [
  ['/', BarChart3, 'Dashboard'],
  ['/wallets', WalletCards, 'Wallets'],
  ['/exchange', ArrowLeftRight, 'Exchange'],
  ['/transfer', ArrowLeftRight, 'Transfer'],
  ['/admin', ShieldCheck, 'Admin']
];

export default function AppShell() {
  const { user, logout } = useAuthStore();
  const navigate = useNavigate();
  return (
    <div className="min-h-screen bg-mist text-ink">
      <aside className="fixed inset-y-0 left-0 hidden w-64 border-r border-line bg-white px-4 py-5 lg:block">
        <div className="mb-8">
          <p className="text-xs font-semibold uppercase tracking-wide text-teal">FX Wallet</p>
          <h1 className="text-xl font-semibold">Cross-border Payments</h1>
        </div>
        <nav className="space-y-1">
          {links.map(([to, Icon, label]) => (
            <NavLink key={to} to={to} className={({ isActive }) => `flex items-center gap-3 rounded-md px-3 py-2 text-sm font-medium ${isActive ? 'bg-teal text-white' : 'text-ink hover:bg-mist'}`}>
              <Icon size={18} /> {label}
            </NavLink>
          ))}
        </nav>
        <button className="absolute bottom-5 left-4 right-4 flex items-center justify-center gap-2 rounded-md border border-line px-3 py-2 text-sm" onClick={() => { logout(); navigate('/login'); }}>
          <LogOut size={16} /> Sign out
        </button>
      </aside>
      <main className="lg:pl-64">
        <header className="sticky top-0 z-10 border-b border-line bg-white/95 px-4 py-3 backdrop-blur lg:px-8">
          <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
            <div>
              <p className="text-sm text-slate-500">Signed in as {user?.fullName}</p>
              <p className="font-semibold">{user?.email} · {user?.role}</p>
            </div>
            <FXTicker />
          </div>
        </header>
        <div className="px-4 py-6 lg:px-8">
          <Outlet />
        </div>
      </main>
    </div>
  );
}
