import apiClient from './client';
import { AuthResponse, LoginRequest, RegisterRequest, User } from '../types/auth';

const BASE_URL = 'http://localhost:8090';

export const authService = {
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    try {
      const response = await apiClient.post<AuthResponse>(
        `${BASE_URL}/api/auth/login`,
        credentials
      );

      localStorage.setItem('authToken', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data.user));

      return response.data;
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  },

  async register(userData: RegisterRequest): Promise<AuthResponse> {
    try {
      const response = await apiClient.post<AuthResponse>(
        `${BASE_URL}/api/auth/register`,
        userData
      );

      localStorage.setItem('authToken', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data.user));

      return response.data;
    } catch (error) {
      console.error('Register error:', error);
      throw error;
    }
  },

  async updateProfile(userId: string, userData: Partial<User>): Promise<User> {
    try {
      // Fixed: Added base URL
      const response = await apiClient.put<User>(
        `${BASE_URL}/api/users/${userId}`,
        userData
      );

      localStorage.setItem('user', JSON.stringify(response.data));

      return response.data;
    } catch (error) {
      console.error('Update profile error:', error);
      throw error;
    }
  },

  async getCurrentUser(): Promise<User | null> {
    try {
      const token = localStorage.getItem('authToken');

      if (!token) {
        return null;
      }

      const response = await apiClient.get<User>(`${BASE_URL}/api/auth/me`);

      localStorage.setItem('user', JSON.stringify(response.data));

      return response.data;
    } catch (error) {
      console.error('Get current user error:', error);
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');
      return null;
    }
  },

  logout(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
  }
};
