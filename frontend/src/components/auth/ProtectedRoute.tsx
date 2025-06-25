import { Navigate } from 'react-router-dom';
import { ReactNode } from 'react';

interface ProtectedRouteProps {
  children: ReactNode;
  isAuthenticated: boolean;
  redirectPath?: string;
}

export const ProtectedRoute = ({
  children,
  isAuthenticated,
  redirectPath = '/login'
}: ProtectedRouteProps) => {
  if (!isAuthenticated) {
    return <Navigate to={redirectPath} replace />;
  }

  return <>{children}</>;
};