import { simulateApiDelay } from './client';
import { AuthResponse, LoginRequest, RegisterRequest, User } from '../types/auth';

export const authService = {
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    try {
      await simulateApiDelay();

      const mockUser: User = {
        id: '1',
        email: credentials.email,
        username: credentials.email.split('@')[0],
        firstName: 'Mihaela',
        lastName: 'Kolarova',
        role: 'student',
        avatar: 'public/mihaela.jpg',
        bio: 'Enthusiastic learner passionate about web development and design.',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      };
      
      const mockToken = 'mock-jwt-token-' + Math.random().toString(36).substring(2);
      
      return { user: mockUser, token: mockToken };
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  },
  
  async register(userData: RegisterRequest): Promise<AuthResponse> {
    try {
      await simulateApiDelay();
      const mockUser: User = {
        id: Math.random().toString(36).substring(2),
        email: userData.email,
        username: userData.username,
        firstName: userData.firstName,
        lastName: userData.lastName,
        role: 'student',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      };
      
      const mockToken = 'mock-jwt-token-' + Math.random().toString(36).substring(2);
      
      return { user: mockUser, token: mockToken };
    } catch (error) {
      console.error('Register error:', error);
      throw error;
    }
  },
  
  async updateProfile(_userId: string, userData: Partial<User>): Promise<User> {
    try {
      await simulateApiDelay();
      const existingUserJSON = localStorage.getItem('user');
      if (!existingUserJSON) throw new Error('User not found');
      
      const existingUser = JSON.parse(existingUserJSON) as User;

      const updatedUser: User = {
        ...existingUser,
        ...userData,
        updatedAt: new Date().toISOString()
      };

      localStorage.setItem('user', JSON.stringify(updatedUser));
      
      return updatedUser;
    } catch (error) {
      console.error('Update profile error:', error);
      throw error;
    }
  },
  
  async getCurrentUser(): Promise<User | null> {
    try {
      const userJSON = localStorage.getItem('user');
      if (!userJSON) return null;
      
      return JSON.parse(userJSON) as User;
    } catch (error) {
      console.error('Get current user error:', error);
      return null;
    }
  }
};
