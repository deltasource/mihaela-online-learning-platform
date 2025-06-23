import { create } from 'zustand';
import { authService } from '../api/authService';
import { AuthState, LoginRequest, RegisterRequest } from '../types/auth';

export const useAuthStore = create<AuthState>((set) => {
  const storedToken = localStorage.getItem('token');
  const storedUserJSON = localStorage.getItem('user');
  const storedUser = storedUserJSON ? JSON.parse(storedUserJSON) : null;
  
  return {
    user: storedUser,
    token: storedToken,
    isAuthenticated: !!storedToken,
    isLoading: false,
    error: null,
    
    login: async (credentials: LoginRequest) => {
      set({ isLoading: true, error: null });
      
      try {
        const response = await authService.login(credentials);
        const { user, token } = response;
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));
        
        set({
          user,
          token,
          isAuthenticated: true,
          isLoading: false
        });
      } catch (error) {
        const errorMessage = 
          error instanceof Error 
            ? error.message 
            : 'Failed to login. Please check your credentials.';
            
        set({ error: errorMessage, isLoading: false });
        throw error;
      }
    },
    
    register: async (userData: RegisterRequest) => {
      set({ isLoading: true, error: null });
      
      try {
        const response = await authService.register(userData);
        const { user, token } = response;
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));
        
        set({
          user,
          token,
          isAuthenticated: true,
          isLoading: false
        });
      } catch (error) {
        const errorMessage = 
          error instanceof Error 
            ? error.message 
            : 'Failed to register. Please try again.';
            
        set({ error: errorMessage, isLoading: false });
        throw error;
      }
    },
    
    logout: () => {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      
      set({
        user: null,
        token: null,
        isAuthenticated: false,
        isLoading: false,
        error: null
      });
    },
    
    updateProfile: async (userData) => {
      set({ isLoading: true, error: null });
      
      try {
        if (!useAuthStore.getState().user) {
          throw new Error('User not authenticated');
        }
        
        const userId = useAuthStore.getState().user!.id;
        const updatedUser = await authService.updateProfile(userId, userData);
        localStorage.setItem('user', JSON.stringify(updatedUser));
        
        set({
          user: updatedUser,
          isLoading: false
        });
      } catch (error) {
        const errorMessage = 
          error instanceof Error 
            ? error.message 
            : 'Failed to update profile. Please try again.';
            
        set({ error: errorMessage, isLoading: false });
        throw error;
      }
    }
  };
});