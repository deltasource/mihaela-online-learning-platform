export interface Notification {
  id: string;
  userId: string;
  title: string;
  message: string;
  type: 'course_update' | 'new_course' | 'achievement' | 'system' | 'reminder';
  isRead: boolean;
  createdAt: string;
  updatedAt: string;
  metadata?: {
    courseId?: string;
    lessonId?: string;
    quizId?: string;
    [key: string]: any;
  };
}

export interface NotificationState {
  notifications: Notification[];
  unreadCount: number;
  isLoading: boolean;
  error: string | null;
  
  fetchNotifications: (userId: string) => Promise<void>;
  markAsRead: (notificationId: string) => Promise<void>;
  markAllAsRead: (userId: string) => Promise<void>;
  createNotification: (notification: Omit<Notification, 'id' | 'createdAt' | 'updatedAt'>) => Promise<void>;
  deleteNotification: (notificationId: string) => Promise<void>;
}
