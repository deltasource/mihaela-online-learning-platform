import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { BookOpen, User, LogOut, Menu, X, Bell, GraduationCap, ChevronDown } from 'lucide-react';
import { useAuthStore } from '../../stores/authStore';
import { useNotificationStore } from '../../stores/notificationStore';

export const Header = () => {
  const { user, isAuthenticated, logout } = useAuthStore();
  const { notifications, unreadCount } = useNotificationStore();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isNotificationOpen, setIsNotificationOpen] = useState(false);
  const [isUserMenuOpen, setIsUserMenuOpen] = useState(false);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
    setIsMenuOpen(false);
    setIsUserMenuOpen(false);
  };

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const toggleNotifications = () => {
    setIsNotificationOpen(!isNotificationOpen);
    setIsUserMenuOpen(false);
  };

  const toggleUserMenu = () => {
    setIsUserMenuOpen(!isUserMenuOpen);
    setIsNotificationOpen(false);
  };

  const isInstructor = user?.role === 'instructor';

  const handleOutsideClick = () => {
    setIsNotificationOpen(false);
    setIsUserMenuOpen(false);
  };

  return (
      <>
        {(isNotificationOpen || isUserMenuOpen) && (
            <div
                className="fixed inset-0 z-40"
                onClick={handleOutsideClick}
            ></div>
        )}

        <header className="bg-white shadow-sm relative z-50">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex justify-between h-16">
              <div className="flex items-center">
                <Link to="/" className="flex-shrink-0 flex items-center">
                  <BookOpen className="h-8 w-8 text-primary-600" />
                  <span className="ml-2 text-xl font-bold text-gray-900">LearnIT</span>
                </Link>
                <nav className="hidden md:ml-8 md:flex md:space-x-8">
                  <Link to="/courses" className="text-gray-900 hover:text-primary-600 px-3 py-2 rounded-md text-sm font-medium">
                    Courses
                  </Link>
                  {isAuthenticated && (
                      <Link
                          to="/dashboard"
                          className="text-gray-900 hover:text-primary-600 px-3 py-2 rounded-md text-sm font-medium flex items-center"
                      >
                        {isInstructor ? (
                            <>
                              <GraduationCap className="h-4 w-4 mr-1" />
                              Instructor
                            </>
                        ) : (
                            'Dashboard'
                        )}
                      </Link>
                  )}
                </nav>
              </div>
              <div className="hidden md:flex md:items-center md:space-x-4">
                {isAuthenticated ? (
                    <div className="flex items-center space-x-4">
                      {/* Notifications */}
                      <div className="relative">
                        <button
                            onClick={toggleNotifications}
                            className="relative p-2 text-gray-400 hover:text-gray-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 rounded-md"
                        >
                          <Bell className="h-6 w-6" />
                          {unreadCount > 0 && (
                              <span className="absolute top-0 right-0 block h-2 w-2 rounded-full bg-red-400 ring-2 ring-white"></span>
                          )}
                        </button>

                        {isNotificationOpen && (
                            <div className="absolute right-0 mt-2 w-80 bg-white rounded-md shadow-lg py-1 z-50 border border-gray-200">
                              <div className="px-4 py-2 border-b border-gray-200">
                                <h3 className="text-sm font-medium text-gray-900">Notifications</h3>
                              </div>
                              <div className="max-h-96 overflow-y-auto">
                                {notifications.length === 0 ? (
                                    <div className="px-4 py-3 text-sm text-gray-500">
                                      No notifications yet
                                    </div>
                                ) : (
                                    notifications.slice(0, 5).map((notification) => (
                                        <div key={notification.id} className="px-4 py-3 hover:bg-gray-50 border-b border-gray-100 last:border-b-0">
                                          <p className="text-sm font-medium text-gray-900">{notification.title}</p>
                                          <p className="text-xs text-gray-500 mt-1">{notification.message}</p>
                                          <p className="text-xs text-gray-400 mt-1">
                                            {new Date(notification.createdAt).toLocaleDateString()}
                                          </p>
                                        </div>
                                    ))
                                )}
                              </div>
                              {notifications.length > 5 && (
                                  <div className="px-4 py-2 border-t border-gray-200">
                                    <Link
                                        to="/notifications"
                                        className="text-sm text-primary-600 hover:text-primary-800"
                                        onClick={() => setIsNotificationOpen(false)}
                                    >
                                      View all notifications
                                    </Link>
                                  </div>
                              )}
                            </div>
                        )}
                      </div>

                      {/* User Menu */}
                      <div className="relative">
                        <button
                            onClick={toggleUserMenu}
                            className="flex items-center space-x-2 p-2 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
                        >
                          <div className="w-8 h-8 rounded-full bg-primary-100 flex items-center justify-center">
                            {user?.avatar ? (
                                <img
                                    src={user.avatar}
                                    alt={user.username}
                                    className="w-8 h-8 rounded-full"
                                />
                            ) : (
                                <User className="w-5 h-5 text-primary-600" />
                            )}
                          </div>
                          <span className="text-sm font-medium text-gray-700">
                        {user?.firstName}
                      </span>
                          {isInstructor && (
                              <span className="text-xs bg-primary-100 text-primary-800 px-2 py-1 rounded-full">
                          Instructor
                        </span>
                          )}
                          <ChevronDown className={`h-4 w-4 text-gray-400 transition-transform ${isUserMenuOpen ? 'rotate-180' : ''}`} />
                        </button>

                        {isUserMenuOpen && (
                            <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-50 border border-gray-200">
                              <Link
                                  to="/profile"
                                  className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                                  onClick={() => setIsUserMenuOpen(false)}
                              >
                                My Profile
                              </Link>
                              <Link
                                  to="/notifications"
                                  className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                                  onClick={() => setIsUserMenuOpen(false)}
                              >
                                Notifications
                              </Link>
                              {isInstructor && (
                                  <Link
                                      to="/instructor/courses/new"
                                      className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                                      onClick={() => setIsUserMenuOpen(false)}
                                  >
                                    Create Course
                                  </Link>
                              )}
                              <button
                                  onClick={handleLogout}
                                  className="w-full text-left block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 flex items-center"
                              >
                                <LogOut className="h-4 w-4 mr-2" />
                                Log Out
                              </button>
                            </div>
                        )}
                      </div>
                    </div>
                ) : (
                    <>
                      <Link to="/login" className="btn-outline">
                        Log In
                      </Link>
                      <Link to="/register" className="btn-primary">
                        Sign Up Free
                      </Link>
                    </>
                )}
              </div>
              <div className="flex items-center md:hidden">
                <button
                    onClick={toggleMenu}
                    className="p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100 focus:outline-none"
                >
                  {isMenuOpen ? (
                      <X className="h-6 w-6" aria-hidden="true" />
                  ) : (
                      <Menu className="h-6 w-6" aria-hidden="true" />
                  )}
                </button>
              </div>
            </div>
          </div>

          {/* Mobile menu */}
          {isMenuOpen && (
              <div className="md:hidden">
                <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
                  <Link
                      to="/courses"
                      className="block px-3 py-2 rounded-md text-base font-medium text-gray-900 hover:bg-gray-50"
                      onClick={() => setIsMenuOpen(false)}
                  >
                    Courses
                  </Link>
                  {isAuthenticated && (
                      <Link
                          to="/dashboard"
                          className="block px-3 py-2 rounded-md text-base font-medium text-gray-900 hover:bg-gray-50"
                          onClick={() => setIsMenuOpen(false)}
                      >
                        {isInstructor ? 'Instructor Dashboard' : 'Dashboard'}
                      </Link>
                  )}
                </div>
                <div className="pt-4 pb-3 border-t border-gray-200">
                  {isAuthenticated ? (
                      <div className="px-2 space-y-1">
                        <div className="flex items-center px-5">
                          <div className="flex-shrink-0">
                            {user?.avatar ? (
                                <img
                                    src={user.avatar}
                                    alt={user.username}
                                    className="w-8 h-8 rounded-full"
                                />
                            ) : (
                                <div className="w-8 h-8 rounded-full bg-primary-100 flex items-center justify-center">
                                  <User className="w-5 h-5 text-primary-600" />
                                </div>
                            )}
                          </div>
                          <div className="ml-3">
                            <div className="text-base font-medium text-gray-800 flex items-center">
                              {user?.firstName} {user?.lastName}
                              {isInstructor && (
                                  <span className="ml-2 text-xs bg-primary-100 text-primary-800 px-2 py-1 rounded-full">
                            Instructor
                          </span>
                              )}
                            </div>
                            <div className="text-sm font-medium text-gray-500">{user?.email}</div>
                          </div>
                        </div>
                        <Link
                            to="/profile"
                            className="block px-3 py-2 rounded-md text-base font-medium text-gray-900 hover:bg-gray-50"
                            onClick={() => setIsMenuOpen(false)}
                        >
                          My Profile
                        </Link>
                        <Link
                            to="/notifications"
                            className="block px-3 py-2 rounded-md text-base font-medium text-gray-900 hover:bg-gray-50"
                            onClick={() => setIsMenuOpen(false)}
                        >
                          Notifications
                          {unreadCount > 0 && (
                              <span className="ml-2 inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">
                        {unreadCount}
                      </span>
                          )}
                        </Link>
                        {isInstructor && (
                            <Link
                                to="/instructor/courses/new"
                                className="block px-3 py-2 rounded-md text-base font-medium text-gray-900 hover:bg-gray-50"
                                onClick={() => setIsMenuOpen(false)}
                            >
                              Create Course
                            </Link>
                        )}
                        <button
                            onClick={handleLogout}
                            className="w-full text-left block px-3 py-2 rounded-md text-base font-medium text-gray-900 hover:bg-gray-50 flex items-center"
                        >
                          <LogOut className="h-4 w-4 mr-2" />
                          Log Out
                        </button>
                      </div>
                  ) : (
                      <div className="px-2 space-y-1">
                        <Link
                            to="/login"
                            className="block px-3 py-2 rounded-md text-base font-medium text-gray-900 hover:bg-gray-50"
                            onClick={() => setIsMenuOpen(false)}
                        >
                          Log In
                        </Link>
                        <Link
                            to="/register"
                            className="block px-3 py-2 rounded-md text-base font-medium text-primary-600 hover:bg-gray-50"
                            onClick={() => setIsMenuOpen(false)}
                        >
                          Sign Up Free
                        </Link>
                      </div>
                  )}
                </div>
              </div>
          )}
        </header>
      </>
  );
};