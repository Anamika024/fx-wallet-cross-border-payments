import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import AppShell from './components/AppShell.jsx';
import ErrorBoundary from './components/ErrorBoundary.jsx';
import ProtectedRoute from './components/ProtectedRoute.jsx';
import Admin from './pages/Admin.jsx';
import Dashboard from './pages/Dashboard.jsx';
import Exchange from './pages/Exchange.jsx';
import Login from './pages/Login.jsx';
import Register from './pages/Register.jsx';
import Transfer from './pages/Transfer.jsx';
import Wallets from './pages/Wallets.jsx';
import './styles/index.css';

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <ErrorBoundary>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route element={<ProtectedRoute><AppShell /></ProtectedRoute>}>
            <Route path="/" element={<Dashboard />} />
            <Route path="/wallets" element={<Wallets />} />
            <Route path="/exchange" element={<Exchange />} />
            <Route path="/transfer" element={<Transfer />} />
            <Route path="/admin" element={<Admin />} />
          </Route>
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </ErrorBoundary>
  </React.StrictMode>
);
