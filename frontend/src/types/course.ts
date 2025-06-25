export interface Course {
  id: string;
  title: string;
  description: string;
  thumbnail: string;
  instructorId: string;
  instructorName: string;
  category: string;
  level: 'beginner' | 'intermediate' | 'advanced';
  duration: number;
  rating: number;
  ratingCount: number;
  enrollmentCount: number;
  price: number;
  isPublished: boolean;
  createdAt: string;
  updatedAt: string;
  lessons: Lesson[];
  quizzes: Quiz[];
}

export interface Lesson {
  id: string;
  courseId: string;
  title: string;
  description: string;
  content: string;
  videoUrl?: string;
  duration: number;
  order: number;
  isPublished: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface Quiz {
  id: string;
  courseId: string;
  lessonId?: string;
  title: string;
  description: string;
  passingScore: number;
  timeLimit?: number;
  order: number;
  isPublished: boolean;
  createdAt: string;
  updatedAt: string;
  questions: QuizQuestion[];
}

export interface QuizQuestion {
  id: string;
  quizId: string;
  question: string;
  questionType: 'multiple-choice' | 'true-false' | 'short-answer';
  points: number;
  options?: QuizOption[];
  correctAnswer?: string;
}

export interface QuizOption {
  id: string;
  questionId: string;
  text: string;
  isCorrect: boolean;
}

export interface QuizSubmission {
  id: string;
  userId: string;
  quizId: string;
  score: number;
  isPassed: boolean;
  completedAt: string;
  answers: QuizAnswer[];
}

export interface QuizAnswer {
  questionId: string;
  selectedOptionId?: string;
  textAnswer?: string;
  isCorrect: boolean;
}

export interface CourseEnrollment {
  id: string;
  userId: string;
  courseId: string;
  progress: number;
  status: 'in-progress' | 'completed' | 'abandoned';
  certificateIssued: boolean;
  enrolledAt: string;
  completedAt?: string;
}

export interface CourseFilters {
  category?: string;
  level?: string;
  search?: string;
  priceRange?: [number, number];
  sortBy?: 'popular' | 'newest' | 'price-asc' | 'price-desc' | 'rating';
}