import { Navigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore.js';

export default function ProtectedRoute({ children }) {
  const token = useAuthStore((state) => state.accessToken);
  const user = useAuthStore((state) => state.user);
  return token && user ? children : <Navigate to="/login" replace />;
}
