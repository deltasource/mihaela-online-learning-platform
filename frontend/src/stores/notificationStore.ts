import { create } from 'zustand';
import { notificationService } from '../api/notificationService';
import { NotificationState } from '../types/notification';

export const useNotificationStore = create<NotificationState>((set, get) => ({
  notifications: [],
  unreadCount: 0,
  isLoading: false,
  error: null,
  
  fetchNotifications: async (userId: string) => {
    set({ isLoading: true, error: null });
    
    try {
      const notifications = await notificationService.getNotifications(userId);
      const unreadCount = notifications.filter(n => !n.isRead).length;
      
      set({ 
        notifications, 
        unreadCount,
        isLoading: false 
      });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to fetch notifications';
      set({ error: errorMessage, isLoading: false });
    }
  },
  
  markAsRead: async (notificationId: string) => {
    try {
      await notificationService.markAsRead(notificationId);
      
      const { notifications } = get();
      const updatedNotifications = notifications.map(n => 
        n.id === notificationId ? { ...n, isRead: true } : n
      );
      const unreadCount = updatedNotifications.filter(n => !n.isRead).length;
      
      set({ 
        notifications: updatedNotifications,
        unreadCount
      });
    } catch (error) {
      console.error('Failed to mark notification as read:', error);
    }
  },
  
  markAllAsRead: async (userId: string) => {
    try {
      await notificationService.markAllAsRead(userId);
      
      const { notifications } = get();
      const updatedNotifications = notifications.map(n => ({ ...n, isRead: true }));
      
      set({ 
        notifications: updatedNotifications,
        unreadCount: 0
      });
    } catch (error) {
      console.error('Failed to mark all notifications as read:', error);
    }
  },
  
  createNotification: async (notificationData) => {
    try {
      const notification = await notificationService.createNotification(notificationData);
      
      const { notifications } = get();
      const updatedNotifications = [notification, ...notifications];
      const unreadCount = updatedNotifications.filter(n => !n.isRead).length;
      
      set({ 
        notifications: updatedNotifications,
        unreadCount
      });
    } catch (error) {
      console.error('Failed to create notification:', error);
    }
  },
  
  deleteNotification: async (notificationId: string) => {
    try {
      await notificationService.deleteNotification(notificationId);
      
      const { notifications } = get();
      const updatedNotifications = notifications.filter(n => n.id !== notificationId);
      const unreadCount = updatedNotifications.filter(n => !n.isRead).length;
      
      set({ 
        notifications: updatedNotifications,
        unreadCount
      });
    } catch (error) {
      console.error('Failed to delete notification:', error);
    }
  }
}));