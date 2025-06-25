import { simulateApiDelay } from './client';
import { Notification } from '../types/notification';

const MOCK_NOTIFICATIONS: Notification[] = [
  {
    id: '1',
    userId: '1',
    title: 'Welcome to LearnIT!',
    message: 'Start your learning journey with our free courses. Check out the Web Development course to get started.',
    type: 'system',
    isRead: false,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
  },
  {
    id: '2',
    userId: '1',
    title: 'New Course Available',
    message: 'A new React course has been added to the platform. Perfect for advancing your frontend skills!',
    type: 'new_course',
    isRead: false,
    createdAt: new Date(Date.now() - 86400000).toISOString(),
    updatedAt: new Date(Date.now() - 86400000).toISOString(),
    metadata: {
      courseId: '2'
    }
  },
  {
    id: '3',
    userId: '1',
    title: 'Course Progress Update',
    message: 'Great job! You\'ve completed 50% of the Introduction to Web Development course.',
    type: 'course_update',
    isRead: true,
    createdAt: new Date(Date.now() - 172800000).toISOString(),
    updatedAt: new Date(Date.now() - 172800000).toISOString(),
    metadata: {
      courseId: '1'
    }
  }
];

export const notificationService = {
  async getNotifications(userId: string): Promise<Notification[]> {
    await simulateApiDelay();

    const notificationsJSON = localStorage.getItem(`notifications_${userId}`);
    if (notificationsJSON) {
      return JSON.parse(notificationsJSON);
    }

    localStorage.setItem(`notifications_${userId}`, JSON.stringify(MOCK_NOTIFICATIONS));
    return MOCK_NOTIFICATIONS.filter(n => n.userId === userId);
  },
  
  async markAsRead(notificationId: string): Promise<void> {
    await simulateApiDelay();
    const allNotifications = this.getAllNotificationsFromStorage();
    const updatedNotifications = allNotifications.map(n => 
      n.id === notificationId ? { ...n, isRead: true, updatedAt: new Date().toISOString() } : n
    );
    
    this.saveAllNotificationsToStorage(updatedNotifications);
  },
  
  async markAllAsRead(userId: string): Promise<void> {
    await simulateApiDelay();
    const allNotifications = this.getAllNotificationsFromStorage();
    const updatedNotifications = allNotifications.map(n => 
      n.userId === userId ? { ...n, isRead: true, updatedAt: new Date().toISOString() } : n
    );
    
    this.saveAllNotificationsToStorage(updatedNotifications);
  },
  
  async createNotification(notificationData: Omit<Notification, 'id' | 'createdAt' | 'updatedAt'>): Promise<Notification> {
    await simulateApiDelay();
    const notification: Notification = {
      ...notificationData,
      id: Math.random().toString(36).substring(2),
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };
    
    const allNotifications = this.getAllNotificationsFromStorage();
    allNotifications.unshift(notification);
    this.saveAllNotificationsToStorage(allNotifications);
    
    return notification;
  },
  
  async deleteNotification(notificationId: string): Promise<void> {
    await simulateApiDelay();

    const allNotifications = this.getAllNotificationsFromStorage();
    const updatedNotifications = allNotifications.filter(n => n.id !== notificationId);
    this.saveAllNotificationsToStorage(updatedNotifications);
  },
  
  getAllNotificationsFromStorage(): Notification[] {
    const allUsers = ['1', '2', '3'];
    let allNotifications: Notification[] = [];
    
    allUsers.forEach(userId => {
      const userNotificationsJSON = localStorage.getItem(`notifications_${userId}`);
      if (userNotificationsJSON) {
        const userNotifications = JSON.parse(userNotificationsJSON);
        allNotifications = [...allNotifications, ...userNotifications];
      }
    });
    
    return allNotifications;
  },
  
  saveAllNotificationsToStorage(notifications: Notification[]): void {
    const notificationsByUser: Record<string, Notification[]> = {};
    
    notifications.forEach(notification => {
      if (!notificationsByUser[notification.userId]) {
        notificationsByUser[notification.userId] = [];
      }
      notificationsByUser[notification.userId].push(notification);
    });
    
    Object.entries(notificationsByUser).forEach(([userId, userNotifications]) => {
      localStorage.setItem(`notifications_${userId}`, JSON.stringify(userNotifications));
    });
  }
};
