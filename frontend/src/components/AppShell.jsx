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
  const initials = (user?.fullName || user?.email || 'FX')
    .split(/[ .@_-]/)
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0]?.toUpperCase())
    .join('');

  return (
    <div className="min-h-screen bg-mist text-ink">
      <header className="sticky top-0 z-20 border-b border-line bg-white/95 backdrop-blur">
        <div className="mx-auto flex max-w-7xl flex-col gap-3 px-4 py-3 lg:px-6">
          <div className="flex flex-wrap items-center justify-between gap-3">
            <div className="flex items-center gap-3">
              <div className="grid h-9 w-9 place-items-center rounded-md bg-emerald-50 text-sm font-semibold text-teal ring-1 ring-emerald-100">FX</div>
              <div>
                <p className="text-[11px] font-semibold uppercase tracking-[0.16em] text-teal">FX Wallet</p>
                <h1 className="text-base font-semibold leading-tight">Cross-border Payments</h1>
              </div>
            </div>
            <div className="flex items-center gap-3">
              <div className="hidden items-center gap-2 rounded-md border border-line bg-mist px-2.5 py-1.5 sm:flex">
                <div className="grid h-7 w-7 place-items-center rounded-full bg-emerald-100 text-[11px] font-semibold text-teal">{initials}</div>
                <div className="leading-tight">
                  <p className="text-xs font-medium">{user?.fullName}</p>
                  <p className="text-[11px] text-slate-500">{user?.role}</p>
                </div>
              </div>
              <button className="icon-btn" title="Sign out" onClick={() => { logout(); navigate('/login'); }}>
                <LogOut size={16} />
              </button>
            </div>
          </div>
          <div className="flex flex-col gap-3 xl:flex-row xl:items-center xl:justify-between">
            <nav className="flex gap-1 overflow-x-auto rounded-md border border-line bg-mist p-1">
              {links.map(([to, Icon, label]) => (
                <NavLink key={to} to={to} className={({ isActive }) => `nav-pill ${isActive ? 'nav-pill-active' : ''}`}>
                  <Icon size={16} /> {label}
                </NavLink>
              ))}
            </nav>
            <FXTicker />
          </div>
        </div>
      </header>
      <main>
        <div className="mx-auto max-w-7xl px-4 py-5 lg:px-6">
          <Outlet />
        </div>
      </main>
    </div>
  );
}
