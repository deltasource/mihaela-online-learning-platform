import { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useAuthStore } from '../../stores/authStore';
import { Alert } from '../../components/ui/Alert';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { User, Settings, Mail, Lock, LogOut } from 'lucide-react';

const profileSchema = z.object({
  firstName: z.string().min(1, 'First name is required'),
  lastName: z.string().min(1, 'Last name is required'),
  username: z.string().min(3, 'Username must be at least 3 characters'),
  email: z.string().email('Please enter a valid email address'),
  bio: z.string().optional(),
});

type ProfileFormData = z.infer<typeof profileSchema>;

const UserProfilePage = () => {
  const { user, updateProfile, isLoading, logout } = useAuthStore();
  const [updateSuccess, setUpdateSuccess] = useState(false);
  const [updateError, setUpdateError] = useState<string | null>(null);
  const [activeTab, setActiveTab] = useState('profile');

  const { register, handleSubmit, formState: { errors }, reset } = useForm<ProfileFormData>({
    resolver: zodResolver(profileSchema),
    defaultValues: {
      firstName: user?.firstName || '',
      lastName: user?.lastName || '',
      username: user?.username || '',
      email: user?.email || '',
      bio: user?.bio || '',
    }
  });

  // Update form values when user data changes
  useEffect(() => {
    if (user) {
      reset({
        firstName: user.firstName,
        lastName: user.lastName,
        username: user.username,
        email: user.email,
        bio: user.bio || '',
      });
    }
  }, [user, reset]);

  const onSubmit = async (data: ProfileFormData) => {
    try {
      setUpdateSuccess(false);
      setUpdateError(null);
      await updateProfile(data);
      setUpdateSuccess(true);
    } catch (error) {
      if (error instanceof Error) {
        setUpdateError(error.message);
      } else {
        setUpdateError('Failed to update profile. Please try again later.');
      }
    }
  };

  return (
    <div className="bg-gray-50 min-h-[calc(100vh-64px)]">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="md:flex md:space-x-8">
          {/* Sidebar */}
          <div className="md:w-1/4 mb-8 md:mb-0">
            <div className="bg-white shadow rounded-lg overflow-hidden">
              <div className="p-6 text-center border-b">
                <div className="inline-block h-24 w-24 rounded-full overflow-hidden bg-gray-100">
                  {user?.avatar ? (
                    <img
                      src={user.avatar}
                      alt={user.username}
                      className="h-full w-full object-cover"
                    />
                  ) : (
                    <div className="h-full w-full flex items-center justify-center bg-primary-100">
                      <User className="h-12 w-12 text-primary-600" />
                    </div>
                  )}
                </div>
                <h2 className="mt-4 text-xl font-semibold text-gray-900">
                  {user?.firstName} {user?.lastName}
                </h2>
                <p className="text-sm text-gray-500">{user?.email}</p>
              </div>
              
              <nav className="p-4">
                <ul className="space-y-1">
                  <li>
                    <button
                      onClick={() => setActiveTab('profile')}
                      className={`flex items-center w-full px-4 py-2 text-sm font-medium rounded-md ${
                        activeTab === 'profile'
                          ? 'bg-primary-50 text-primary-700'
                          : 'text-gray-700 hover:bg-gray-50'
                      }`}
                    >
                      <User className="mr-3 h-5 w-5" />
                      Profile
                    </button>
                  </li>
                  <li>
                    <button
                      onClick={() => setActiveTab('account')}
                      className={`flex items-center w-full px-4 py-2 text-sm font-medium rounded-md ${
                        activeTab === 'account'
                          ? 'bg-primary-50 text-primary-700'
                          : 'text-gray-700 hover:bg-gray-50'
                      }`}
                    >
                      <Settings className="mr-3 h-5 w-5" />
                      Account Settings
                    </button>
                  </li>
                  <li>
                    <button
                      onClick={() => setActiveTab('email')}
                      className={`flex items-center w-full px-4 py-2 text-sm font-medium rounded-md ${
                        activeTab === 'email'
                          ? 'bg-primary-50 text-primary-700'
                          : 'text-gray-700 hover:bg-gray-50'
                      }`}
                    >
                      <Mail className="mr-3 h-5 w-5" />
                      Email Preferences
                    </button>
                  </li>
                  <li>
                    <button
                      onClick={() => setActiveTab('security')}
                      className={`flex items-center w-full px-4 py-2 text-sm font-medium rounded-md ${
                        activeTab === 'security'
                          ? 'bg-primary-50 text-primary-700'
                          : 'text-gray-700 hover:bg-gray-50'
                      }`}
                    >
                      <Lock className="mr-3 h-5 w-5" />
                      Security
                    </button>
                  </li>
                </ul>
                
                <div className="pt-4 mt-4 border-t border-gray-200">
                  <button
                    onClick={() => logout()}
                    className="flex items-center w-full px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50 rounded-md"
                  >
                    <LogOut className="mr-3 h-5 w-5 text-gray-400" />
                    Logout
                  </button>
                </div>
              </nav>
            </div>
          </div>
          
          {/* Main content */}
          <div className="md:w-3/4">
            {activeTab === 'profile' && (
              <div className="bg-white shadow rounded-lg overflow-hidden">
                <div className="p-6 border-b border-gray-200">
                  <h2 className="text-xl font-semibold text-gray-900">Profile Information</h2>
                  <p className="mt-1 text-sm text-gray-500">
                    Update your personal information and public profile.
                  </p>
                </div>
                
                <div className="p-6">
                  {updateSuccess && (
                    <Alert variant="success" className="mb-6">
                      Your profile has been successfully updated!
                    </Alert>
                  )}
                  
                  {updateError && (
                    <Alert variant="error" className="mb-6">
                      {updateError}
                    </Alert>
                  )}
                  
                  <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                      <div>
                        <label htmlFor="firstName" className="label">
                          First name
                        </label>
                        <input
                          id="firstName"
                          type="text"
                          {...register('firstName')}
                          className={`input ${errors.firstName ? 'border-error-500' : ''}`}
                        />
                        {errors.firstName && (
                          <p className="mt-1 text-sm text-error-600">{errors.firstName.message}</p>
                        )}
                      </div>
                      
                      <div>
                        <label htmlFor="lastName" className="label">
                          Last name
                        </label>
                        <input
                          id="lastName"
                          type="text"
                          {...register('lastName')}
                          className={`input ${errors.lastName ? 'border-error-500' : ''}`}
                        />
                        {errors.lastName && (
                          <p className="mt-1 text-sm text-error-600">{errors.lastName.message}</p>
                        )}
                      </div>
                    </div>
                    
                    <div>
                      <label htmlFor="username" className="label">
                        Username
                      </label>
                      <input
                        id="username"
                        type="text"
                        {...register('username')}
                        className={`input ${errors.username ? 'border-error-500' : ''}`}
                      />
                      {errors.username && (
                        <p className="mt-1 text-sm text-error-600">{errors.username.message}</p>
                      )}
                    </div>
                    
                    <div>
                      <label htmlFor="email" className="label">
                        Email
                      </label>
                      <input
                        id="email"
                        type="email"
                        {...register('email')}
                        className={`input ${errors.email ? 'border-error-500' : ''}`}
                      />
                      {errors.email && (
                        <p className="mt-1 text-sm text-error-600">{errors.email.message}</p>
                      )}
                    </div>
                    
                    <div>
                      <label htmlFor="bio" className="label">
                        Bio
                      </label>
                      <textarea
                        id="bio"
                        rows={4}
                        {...register('bio')}
                        className="input"
                        placeholder="Tell us about yourself..."
                      ></textarea>
                    </div>
                    
                    <div>
                      <label className="label">Profile Photo</label>
                      <div className="mt-1 flex items-center space-x-5">
                        <div className="flex-shrink-0">
                          <div className="relative">
                            <div className="h-16 w-16 rounded-full overflow-hidden bg-gray-100">
                              {user?.avatar ? (
                                <img
                                  src={user.avatar}
                                  alt={user.username}
                                  className="h-full w-full object-cover"
                                />
                              ) : (
                                <div className="h-full w-full flex items-center justify-center bg-primary-100">
                                  <User className="h-8 w-8 text-primary-600" />
                                </div>
                              )}
                            </div>
                          </div>
                        </div>
                        <button
                          type="button"
                          className="btn-outline"
                        >
                          Change
                        </button>
                      </div>
                    </div>
                    
                    <div className="flex justify-end">
                      <button
                        type="submit"
                        disabled={isLoading}
                        className="btn-primary"
                      >
                        {isLoading ? <LoadingSpinner size="sm" color="text-white" /> : 'Save Changes'}
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            )}
            
            {activeTab === 'account' && (
              <div className="bg-white shadow rounded-lg overflow-hidden">
                <div className="p-6 border-b border-gray-200">
                  <h2 className="text-xl font-semibold text-gray-900">Account Settings</h2>
                  <p className="mt-1 text-sm text-gray-500">
                    Manage your account settings and preferences.
                  </p>
                </div>
                
                <div className="p-6">
                  <div className="space-y-6">
                    <div>
                      <h3 className="text-lg font-medium text-gray-900">Account Type</h3>
                      <p className="mt-1 text-sm text-gray-500">Your current account type is <strong>{user?.role}</strong>.</p>
                    </div>
                    
                    <div className="pt-5 border-t border-gray-200">
                      <h3 className="text-lg font-medium text-gray-900">Language</h3>
                      <select className="mt-2 input">
                        <option value="en">English</option>
                        <option value="es">Spanish</option>
                        <option value="fr">French</option>
                        <option value="de">German</option>
                      </select>
                    </div>
                    
                    <div className="pt-5 border-t border-gray-200">
                      <h3 className="text-lg font-medium text-gray-900 mb-4">Timezone</h3>
                      <select className="input">
                        <option value="UTC-8">Pacific Time (UTC-8)</option>
                        <option value="UTC-5">Eastern Time (UTC-5)</option>
                        <option value="UTC">Coordinated Universal Time (UTC)</option>
                        <option value="UTC+1">Central European Time (UTC+1)</option>
                        <option value="UTC+8">China Standard Time (UTC+8)</option>
                      </select>
                    </div>
                    
                    <div className="pt-5 border-t border-gray-200">
                      <h3 className="text-lg font-medium text-error-600">Danger Zone</h3>
                      <p className="mt-1 text-sm text-gray-500">
                        Once you delete your account, there is no going back. Please be certain.
                      </p>
                      <button className="mt-4 btn-danger">
                        Delete Account
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            )}
            
            {activeTab === 'email' && (
              <div className="bg-white shadow rounded-lg overflow-hidden">
                <div className="p-6 border-b border-gray-200">
                  <h2 className="text-xl font-semibold text-gray-900">Email Preferences</h2>
                  <p className="mt-1 text-sm text-gray-500">
                    Manage your email notifications and communication preferences.
                  </p>
                </div>
                
                <div className="p-6">
                  <div className="space-y-6">
                    <div>
                      <h3 className="text-lg font-medium text-gray-900 mb-4">Notification Settings</h3>
                      
                      <div className="space-y-4">
                        <div className="flex items-start">
                          <div className="flex items-center h-5">
                            <input
                              id="course-updates"
                              name="course-updates"
                              type="checkbox"
                              defaultChecked
                              className="h-4 w-4 text-primary-600 border-gray-300 rounded"
                            />
                          </div>
                          <div className="ml-3 text-sm">
                            <label htmlFor="course-updates" className="font-medium text-gray-700">Course updates</label>
                            <p className="text-gray-500">Get notified when courses you're enrolled in are updated.</p>
                          </div>
                        </div>
                        
                        <div className="flex items-start">
                          <div className="flex items-center h-5">
                            <input
                              id="new-courses"
                              name="new-courses"
                              type="checkbox"
                              defaultChecked
                              className="h-4 w-4 text-primary-600 border-gray-300 rounded"
                            />
                          </div>
                          <div className="ml-3 text-sm">
                            <label htmlFor="new-courses" className="font-medium text-gray-700">New courses</label>
                            <p className="text-gray-500">Receive notifications when new courses are added in categories you're interested in.</p>
                          </div>
                        </div>
                        
                        <div className="flex items-start">
                          <div className="flex items-center h-5">
                            <input
                              id="promotions"
                              name="promotions"
                              type="checkbox"
                              className="h-4 w-4 text-primary-600 border-gray-300 rounded"
                            />
                          </div>
                          <div className="ml-3 text-sm">
                            <label htmlFor="promotions" className="font-medium text-gray-700">Promotions and deals</label>
                            <p className="text-gray-500">Get notified about special offers and discounts.</p>
                          </div>
                        </div>
                        
                        <div className="flex items-start">
                          <div className="flex items-center h-5">
                            <input
                              id="newsletter"
                              name="newsletter"
                              type="checkbox"
                              defaultChecked
                              className="h-4 w-4 text-primary-600 border-gray-300 rounded"
                            />
                          </div>
                          <div className="ml-3 text-sm">
                            <label htmlFor="newsletter" className="font-medium text-gray-700">Newsletter</label>
                            <p className="text-gray-500">Subscribe to our monthly newsletter with tips and resources.</p>
                          </div>
                        </div>
                      </div>
                    </div>
                    
                    <div className="flex justify-end">
                      <button type="button" className="btn-primary">
                        Save Preferences
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            )}
            
            {activeTab === 'security' && (
              <div className="bg-white shadow rounded-lg overflow-hidden">
                <div className="p-6 border-b border-gray-200">
                  <h2 className="text-xl font-semibold text-gray-900">Security</h2>
                  <p className="mt-1 text-sm text-gray-500">
                    Manage your password and security settings.
                  </p>
                </div>
                
                <div className="p-6">
                  <div className="space-y-6">
                    <div>
                      <h3 className="text-lg font-medium text-gray-900 mb-4">Change Password</h3>
                      
                      <form className="space-y-4">
                        <div>
                          <label htmlFor="current-password" className="label">Current Password</label>
                          <input
                            type="password"
                            id="current-password"
                            className="input"
                          />
                        </div>
                        
                        <div>
                          <label htmlFor="new-password" className="label">New Password</label>
                          <input
                            type="password"
                            id="new-password"
                            className="input"
                          />
                        </div>
                        
                        <div>
                          <label htmlFor="confirm-password" className="label">Confirm New Password</label>
                          <input
                            type="password"
                            id="confirm-password"
                            className="input"
                          />
                        </div>
                        
                        <div className="flex justify-end">
                          <button type="submit" className="btn-primary">
                            Update Password
                          </button>
                        </div>
                      </form>
                    </div>
                    
                    <div className="pt-6 border-t border-gray-200">
                      <h3 className="text-lg font-medium text-gray-900 mb-4">Two-Factor Authentication</h3>
                      <p className="text-sm text-gray-500 mb-4">
                        Add an extra layer of security to your account by enabling two-factor authentication.
                      </p>
                      <button type="button" className="btn-outline">
                        Enable 2FA
                      </button>
                    </div>
                    
                    <div className="pt-6 border-t border-gray-200">
                      <h3 className="text-lg font-medium text-gray-900 mb-4">Session History</h3>
                      <p className="text-sm text-gray-500">
                        This is a list of devices that have logged into your account.
                      </p>
                      
                      <div className="mt-4 space-y-4">
                        <div className="flex justify-between items-center py-3 border-b border-gray-100">
                          <div>
                            <p className="text-sm font-medium text-gray-900">Chrome on Windows</p>
                            <p className="text-xs text-gray-500">Last active: 2 minutes ago</p>
                          </div>
                          <span className="badge badge-success">Current</span>
                        </div>
                        
                        <div className="flex justify-between items-center py-3 border-b border-gray-100">
                          <div>
                            <p className="text-sm font-medium text-gray-900">Safari on iPhone</p>
                            <p className="text-xs text-gray-500">Last active: 2 days ago</p>
                          </div>
                          <button className="text-sm text-primary-600 hover:text-primary-800">
                            Log out
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserProfilePage;