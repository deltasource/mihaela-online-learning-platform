import { create } from 'zustand';
import { courseService } from '../api/courseService';
import { Course, CourseEnrollment, CourseFilters, Lesson, Quiz, QuizSubmission } from '../types/course';

interface CourseState {
  courses: Course[];
  enrolledCourses: CourseEnrollment[];
  currentCourse: Course | null;
  currentLesson: Lesson | null;
  currentQuiz: Quiz | null;
  categories: string[];
  isLoading: boolean;
  error: string | null;
  
  fetchCourses: (filters?: CourseFilters) => Promise<void>;
  fetchCourseById: (courseId: string) => Promise<Course>;
  fetchLessonById: (courseId: string, lessonId: string) => Promise<Lesson>;
  fetchQuizById: (courseId: string, quizId: string) => Promise<Quiz>;
  fetchEnrolledCourses: (userId: string) => Promise<void>;
  enrollInCourse: (courseId: string, userId: string) => Promise<void>;
  markLessonCompleted: (userId: string, courseId: string, lessonId: string) => Promise<void>;
  markLessonIncomplete: (userId: string, courseId: string, lessonId: string) => Promise<void>;
  submitQuiz: (userId: string, quizId: string, answers: any[]) => Promise<QuizSubmission>;
  fetchCategories: () => Promise<void>;
}

export const useCourseStore = create<CourseState>((set, get) => ({
  courses: [],
  enrolledCourses: [],
  currentCourse: null,
  currentLesson: null,
  currentQuiz: null,
  categories: [],
  isLoading: false,
  error: null,
  
  fetchCourses: async (filters) => {
    set({ isLoading: true, error: null });
    
    try {
      const courses = await courseService.getCourses(filters);
      set({ courses, isLoading: false });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to fetch courses';
      set({ error: errorMessage, isLoading: false });
    }
  },
  
  fetchCourseById: async (courseId) => {
    set({ isLoading: true, error: null });
    
    try {
      const course = await courseService.getCourseById(courseId);
      set({ currentCourse: course, isLoading: false });
      return course;
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to fetch course';
      set({ error: errorMessage, isLoading: false });
      throw error;
    }
  },
  
  fetchLessonById: async (courseId, lessonId) => {
    set({ isLoading: true, error: null });
    
    try {
      const lesson = await courseService.getLessonById(courseId, lessonId);
      set({ currentLesson: lesson, isLoading: false });
      return lesson;
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to fetch lesson';
      set({ error: errorMessage, isLoading: false });
      throw error;
    }
  },
  
  fetchQuizById: async (courseId, quizId) => {
    set({ isLoading: true, error: null });
    
    try {
      const quiz = await courseService.getQuizById(courseId, quizId);
      set({ currentQuiz: quiz, isLoading: false });
      return quiz;
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to fetch quiz';
      set({ error: errorMessage, isLoading: false });
      throw error;
    }
  },
  
  fetchEnrolledCourses: async (userId) => {
    set({ isLoading: true, error: null });
    
    try {
      const enrollments = await courseService.getUserEnrollments(userId);
      set({ enrolledCourses: enrollments, isLoading: false });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to fetch enrolled courses';
      set({ error: errorMessage, isLoading: false });
    }
  },
  
  enrollInCourse: async (courseId, userId) => {
    set({ isLoading: true, error: null });
    
    try {
      const enrollment = await courseService.enrollInCourse(courseId, userId);
      const { enrolledCourses } = get();
      const updatedEnrollments = [...enrolledCourses];

      if (!updatedEnrollments.some(e => e.courseId === courseId && e.userId === userId)) {
        updatedEnrollments.push(enrollment);
      }
      
      set({ enrolledCourses: updatedEnrollments, isLoading: false });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to enroll in course';
      set({ error: errorMessage, isLoading: false });
    }
  },
  
  markLessonCompleted: async (userId, courseId, lessonId) => {
    try {
      await courseService.updateLessonProgress(userId, courseId, lessonId, true);
      
      await get().fetchEnrolledCourses(userId);
    } catch (error) {
      console.error('Failed to mark lesson as completed:', error);
    }
  },
  
  markLessonIncomplete: async (userId, courseId, lessonId) => {
    try {
      await courseService.updateLessonProgress(userId, courseId, lessonId, false);
      
      await get().fetchEnrolledCourses(userId);
    } catch (error) {
      console.error('Failed to mark lesson as incomplete:', error);
    }
  },
  
  submitQuiz: async (userId, quizId, answers) => {
    set({ isLoading: true, error: null });
    
    try {
      const submission = await courseService.submitQuiz(userId, quizId, answers);
      set({ isLoading: false });
      return submission;
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Failed to submit quiz';
      set({ error: errorMessage, isLoading: false });
      throw error;
    }
  },
  
  fetchCategories: async () => {
    try {
      const categories = await courseService.getCategories();
      set({ categories });
    } catch (error) {
      console.error('Failed to fetch categories:', error);
    }
  }
}));