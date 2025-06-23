import { Suspense, lazy, useEffect } from 'react';
import { Routes, Route } from 'react-router-dom';
import { Layout } from './components/layout/Layout';
import { LoadingSpinner } from './components/ui/LoadingSpinner';
import { ProtectedRoute } from './components/auth/ProtectedRoute';
import { useAuthStore } from './stores/authStore';
import { useNotificationStore } from './stores/notificationStore';
import HomePage from './pages/HomePage';

const LoginPage = lazy(() => import('./pages/auth/LoginPage'));
const RegisterPage = lazy(() => import('./pages/auth/RegisterPage'));
const CourseCatalogPage = lazy(() => import('./pages/courses/CourseCatalogPage'));
const CourseDetailsPage = lazy(() => import('./pages/courses/CourseDetailsPage'));
const LessonPage = lazy(() => import('./pages/courses/LessonPage'));
const DashboardPage = lazy(() => import('./pages/dashboard/DashboardPage'));
const UserProfilePage = lazy(() => import('./pages/user/UserProfilePage'));
const QuizPage = lazy(() => import('./pages/courses/QuizPage'));
const NotificationsPage = lazy(() => import('./pages/notifications/NotificationsPage'));
const NotFoundPage = lazy(() => import('./pages/NotFoundPage'));

function App() {
  const { isAuthenticated, user } = useAuthStore();
  const { fetchNotifications } = useNotificationStore();

  useEffect(() => {
    if (isAuthenticated && user) {
      fetchNotifications(user.id);
    }
  }, [isAuthenticated, user, fetchNotifications]);

  return (
    <Layout>
      <Suspense fallback={<div className="flex justify-center items-center h-[calc(100vh-64px)]"><LoadingSpinner size="lg" /></div>}>
        <Routes>
          {/* Public routes */}
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/courses" element={<CourseCatalogPage />} />
          <Route path="/courses/:courseId" element={<CourseDetailsPage />} />
          
          {/* Protected routes */}
          <Route path="/dashboard" element={
            <ProtectedRoute isAuthenticated={isAuthenticated}>
              <DashboardPage />
            </ProtectedRoute>
          } />
          <Route path="/profile" element={
            <ProtectedRoute isAuthenticated={isAuthenticated}>
              <UserProfilePage />
            </ProtectedRoute>
          } />
          <Route path="/notifications" element={
            <ProtectedRoute isAuthenticated={isAuthenticated}>
              <NotificationsPage />
            </ProtectedRoute>
          } />
          <Route path="/courses/:courseId/lessons/:lessonId" element={
            <ProtectedRoute isAuthenticated={isAuthenticated}>
              <LessonPage />
            </ProtectedRoute>
          } />
          <Route path="/courses/:courseId/quizzes/:quizId" element={
            <ProtectedRoute isAuthenticated={isAuthenticated}>
              <QuizPage />
            </ProtectedRoute>
          } />
          
          {/* 404 route */}
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </Suspense>
    </Layout>
  );
}

export default App;