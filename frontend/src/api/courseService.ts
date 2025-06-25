import { simulateApiDelay } from './client';
import {Course, CourseEnrollment, CourseFilters, Lesson, Quiz, QuizAnswer, QuizSubmission} from '../types/course';

const MOCK_COURSES: Course[] = [
  {
    id: '1',
    title: 'Introduction to Web Development',
    description: 'Learn the fundamentals of web development including HTML, CSS, and JavaScript. Build responsive websites from scratch.',
    thumbnail: 'https://images.pexels.com/photos/1181271/pexels-photo-1181271.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1',
    instructorId: '101',
    instructorName: 'Sarah Johnson',
    category: 'Web Development',
    level: 'beginner',
    duration: 1200,
    rating: 4.7,
    ratingCount: 245,
    enrollmentCount: 1345,
    price: 49.99,
    isPublished: true,
    createdAt: '2023-01-15T10:30:00Z',
    updatedAt: '2023-06-20T14:45:00Z',
    lessons: [
      {
        id: '101',
        courseId: '1',
        title: 'Getting Started with HTML',
        description: 'Learn the basics of HTML and structure your first web page.',
        content: '<p>HTML (HyperText Markup Language) is the standard markup language for documents designed to be displayed in a web browser.</p><p>In this lesson, you will learn about basic HTML elements and how to create a simple web page.</p>',
        videoUrl: 'https://www.youtube.com/embed/UB1O30fR-EE',
        duration: 45,
        order: 1,
        isPublished: true,
        createdAt: '2023-01-15T10:30:00Z',
        updatedAt: '2023-01-15T10:30:00Z'
      },
      {
        id: '102',
        courseId: '1',
        title: 'CSS Fundamentals',
        description: 'Style your HTML with Cascading Style Sheets.',
        content: '<p>CSS (Cascading Style Sheets) is a stylesheet language used to describe the presentation of a document written in HTML.</p><p>Learn how to add styles to your web pages and make them visually appealing.</p>',
        videoUrl: 'https://www.youtube.com/embed/yfoY53QXEnI',
        duration: 60,
        order: 2,
        isPublished: true,
        createdAt: '2023-01-16T11:30:00Z',
        updatedAt: '2023-01-16T11:30:00Z'
      },
      {
        id: '103',
        courseId: '1',
        title: 'JavaScript Basics',
        description: 'Add interactivity to your websites with JavaScript.',
        content: '<p>JavaScript is a programming language that allows you to implement complex features on web pages.</p><p>This lesson covers variables, data types, functions, and basic DOM manipulation.</p>',
        videoUrl: 'https://www.youtube.com/embed/PkZNo7MFNFg',
        duration: 75,
        order: 3,
        isPublished: true,
        createdAt: '2023-01-17T10:30:00Z',
        updatedAt: '2023-01-17T10:30:00Z'
      }
    ],
    quizzes: [
      {
        id: '201',
        courseId: '1',
        title: 'HTML & CSS Quiz',
        description: 'Test your knowledge of HTML and CSS fundamentals.',
        passingScore: 70,
        timeLimit: 30,
        order: 1,
        isPublished: true,
        createdAt: '2023-01-20T10:30:00Z',
        updatedAt: '2023-01-20T10:30:00Z',
        questions: [
          {
            id: '301',
            quizId: '201',
            question: 'Which HTML element is used to define an unordered list?',
            questionType: 'multiple-choice',
            points: 10,
            options: [
              { id: '401', questionId: '301', text: '<ul>', isCorrect: true },
              { id: '402', questionId: '301', text: '<ol>', isCorrect: false },
              { id: '403', questionId: '301', text: '<li>', isCorrect: false },
              { id: '404', questionId: '301', text: '<list>', isCorrect: false }
            ]
          },
          {
            id: '302',
            quizId: '201',
            question: 'In CSS, which property is used to change the text color?',
            questionType: 'multiple-choice',
            points: 10,
            options: [
              { id: '405', questionId: '302', text: 'text-color', isCorrect: false },
              { id: '406', questionId: '302', text: 'color', isCorrect: true },
              { id: '407', questionId: '302', text: 'font-color', isCorrect: false },
              { id: '408', questionId: '302', text: 'text-style', isCorrect: false }
            ]
          }
        ]
      }
    ]
  },
  {
    id: '2',
    title: 'React for Beginners',
    description: 'Learn the fundamentals of React, including components, state, props, and hooks. Build modern, interactive UIs with the popular JavaScript library.',
    thumbnail: 'https://images.pexels.com/photos/11035380/pexels-photo-11035380.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1',
    instructorId: '102',
    instructorName: 'David Wilson',
    category: 'JavaScript Frameworks',
    level: 'intermediate',
    duration: 1500,
    rating: 4.9,
    ratingCount: 178,
    enrollmentCount: 890,
    price: 69.99,
    isPublished: true,
    createdAt: '2023-02-10T09:15:00Z',
    updatedAt: '2023-07-12T16:30:00Z',
    lessons: [
      {
        id: '201',
        courseId: '2',
        title: 'Introduction to React',
        description: 'Learn the basics of React and its core concepts.',
        content: '<p>React is a JavaScript library for building user interfaces, particularly single-page applications.</p><p>In this lesson, we will explore the core concepts of React and set up our development environment.</p>',
        videoUrl: 'https://www.youtube.com/embed/Tn6-PIqc4UM',
        duration: 60,
        order: 1,
        isPublished: true,
        createdAt: '2023-02-10T09:15:00Z',
        updatedAt: '2023-02-10T09:15:00Z'
      }
    ],
    quizzes: []
  },
  {
    id: '3',
    title: 'Data Science with Python',
    description: 'Master data analysis, visualization, and machine learning fundamentals using Python. Perfect for aspiring data scientists and analysts.',
    thumbnail: 'https://images.pexels.com/photos/669615/pexels-photo-669615.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1',
    instructorId: '103',
    instructorName: 'Emily Chen',
    category: 'Data Science',
    level: 'intermediate',
    duration: 2400,
    rating: 4.6,
    ratingCount: 312,
    enrollmentCount: 1678,
    price: 89.99,
    isPublished: true,
    createdAt: '2023-03-05T14:20:00Z',
    updatedAt: '2023-08-18T11:45:00Z',
    lessons: [],
    quizzes: []
  },
  {
    id: '4',
    title: 'UI/UX Design Principles',
    description: 'Learn the fundamentals of user interface and user experience design. Create intuitive, accessible, and beautiful digital products.',
    thumbnail: 'https://images.pexels.com/photos/196644/pexels-photo-196644.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1',
    instructorId: '104',
    instructorName: 'Alex Turner',
    category: 'Design',
    level: 'beginner',
    duration: 1800,
    rating: 4.8,
    ratingCount: 215,
    enrollmentCount: 945,
    price: 59.99,
    isPublished: true,
    createdAt: '2023-04-12T10:00:00Z',
    updatedAt: '2023-09-05T09:30:00Z',
    lessons: [],
    quizzes: []
  },
];

export const courseService = {
  async getCourses(filters?: CourseFilters): Promise<Course[]> {
    await simulateApiDelay();
    let filteredCourses = [...MOCK_COURSES];
    
    if (filters) {
      if (filters.category) {
        filteredCourses = filteredCourses.filter(course => 
          course.category.toLowerCase() === filters.category?.toLowerCase()
        );
      }
      
      if (filters.level) {
        filteredCourses = filteredCourses.filter(course => 
          course.level === filters.level
        );
      }
      
      if (filters.search) {
        const searchTerm = filters.search.toLowerCase();
        filteredCourses = filteredCourses.filter(course => 
          course.title.toLowerCase().includes(searchTerm) ||
          course.description.toLowerCase().includes(searchTerm) ||
          course.instructorName.toLowerCase().includes(searchTerm)
        );
      }
      
      if (filters.priceRange) {
        const [min, max] = filters.priceRange;
        filteredCourses = filteredCourses.filter(course => 
          course.price >= min && course.price <= max
        );
      }

      if (filters.sortBy) {
        switch (filters.sortBy) {
          case 'popular':
            filteredCourses.sort((a, b) => b.enrollmentCount - a.enrollmentCount);
            break;
          case 'newest':
            filteredCourses.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
            break;
          case 'price-asc':
            filteredCourses.sort((a, b) => a.price - b.price);
            break;
          case 'price-desc':
            filteredCourses.sort((a, b) => b.price - a.price);
            break;
          case 'rating':
            filteredCourses.sort((a, b) => b.rating - a.rating);
            break;
        }
      }
    }
    
    return filteredCourses;
  },
  
  async getCourseById(courseId: string): Promise<Course> {
    await simulateApiDelay();
    
    const course = MOCK_COURSES.find(c => c.id === courseId);
    if (!course) {
      throw new Error('Course not found');
    }
    
    return course;
  },
  
  async getLessonById(courseId: string, lessonId: string): Promise<Lesson> {
    await simulateApiDelay();

    const course = MOCK_COURSES.find(c => c.id === courseId);
    if (!course) {
      throw new Error('Course not found');
    }
    
    const lesson = course.lessons.find(l => l.id === lessonId);
    if (!lesson) {
      throw new Error('Lesson not found');
    }
    
    return lesson;
  },
  
  async getQuizById(courseId: string, quizId: string): Promise<Quiz> {
    await simulateApiDelay();

    const course = MOCK_COURSES.find(c => c.id === courseId);
    if (!course) {
      throw new Error('Course not found');
    }
    
    const quiz = course.quizzes.find(q => q.id === quizId);
    if (!quiz) {
      throw new Error('Quiz not found');
    }
    
    return quiz;
  },
  
  async enrollInCourse(courseId: string, userId: string): Promise<CourseEnrollment> {
    await simulateApiDelay();

    const enrollment: CourseEnrollment = {
      id: Math.random().toString(36).substring(2),
      userId,
      courseId,
      progress: 0,
      status: 'in-progress',
      certificateIssued: false,
      enrolledAt: new Date().toISOString()
    };
    
    const enrollmentsJSON = localStorage.getItem('enrollments');
    const enrollments: CourseEnrollment[] = enrollmentsJSON 
      ? JSON.parse(enrollmentsJSON) 
      : [];
      
    const existingEnrollment = enrollments.find(
      e => e.userId === userId && e.courseId === courseId
    );
    
    if (!existingEnrollment) {
      enrollments.push(enrollment);
      localStorage.setItem('enrollments', JSON.stringify(enrollments));
    } else {
      return existingEnrollment;
    }
    
    return enrollment;
  },
  
  async getUserEnrollments(userId: string): Promise<CourseEnrollment[]> {
    await simulateApiDelay();

    const enrollmentsJSON = localStorage.getItem('enrollments');
    const allEnrollments: CourseEnrollment[] = enrollmentsJSON 
      ? JSON.parse(enrollmentsJSON) 
      : [];
      
    return allEnrollments.filter(e => e.userId === userId);
  },
  
  async updateLessonProgress(
    userId: string, 
    courseId: string, 
    lessonId: string, 
    completed: boolean
  ): Promise<void> {
    await simulateApiDelay();

    const enrollmentsJSON = localStorage.getItem('enrollments');
    if (!enrollmentsJSON) return;
    
    const enrollments: CourseEnrollment[] = JSON.parse(enrollmentsJSON);
    const enrollment = enrollments.find(
      e => e.userId === userId && e.courseId === courseId
    );
    
    if (enrollment) {
      const course = MOCK_COURSES.find(c => c.id === courseId);
      if (course) {
        const totalLessons = course.lessons.length;
        const completedLessonsJSON = localStorage.getItem(`user_${userId}_course_${courseId}_lessons`);
        let completedLessons: string[] = completedLessonsJSON 
          ? JSON.parse(completedLessonsJSON) 
          : [];
          
        if (completed && !completedLessons.includes(lessonId)) {
          completedLessons.push(lessonId);
        } else if (!completed && completedLessons.includes(lessonId)) {
          completedLessons = completedLessons.filter(id => id !== lessonId);
        }
        
        localStorage.setItem(
          `user_${userId}_course_${courseId}_lessons`, 
          JSON.stringify(completedLessons)
        );
        
        if (totalLessons > 0) {
          enrollment.progress = Math.round((completedLessons.length / totalLessons) * 100);
          
          if (enrollment.progress === 100) {
            enrollment.status = 'completed';
            enrollment.completedAt = new Date().toISOString();
          }
          
          localStorage.setItem('enrollments', JSON.stringify(enrollments));
        }
      }
    }
  },
  
  async submitQuiz(userId: string, quizId: string, answers: QuizAnswer[]): Promise<QuizSubmission> {
    await simulateApiDelay();

    let totalPoints = 0;
    let earnedPoints = 0;

    let quiz: Quiz | undefined;
    for (const course of MOCK_COURSES) {
      const foundQuiz = course.quizzes.find(q => q.id === quizId);
      if (foundQuiz) {
        quiz = foundQuiz;
        break;
      }
    }
    
    if (!quiz) {
      throw new Error('Quiz not found');
    }
    
    quiz.questions.forEach(question => {
      totalPoints += question.points;
      
      const userAnswer = answers.find(a => a.questionId === question.id);
      if (userAnswer && userAnswer.isCorrect) {
        earnedPoints += question.points;
      }
    });
    
    const score = totalPoints > 0 ? Math.round((earnedPoints / totalPoints) * 100) : 0;
    const isPassed = score >= quiz.passingScore;
    
    const submission: QuizSubmission = {
      id: Math.random().toString(36).substring(2),
      userId,
      quizId,
      score,
      isPassed,
      completedAt: new Date().toISOString(),
      answers
    };
    
    const submissionsJSON = localStorage.getItem('quiz_submissions');
    const submissions: QuizSubmission[] = submissionsJSON 
      ? JSON.parse(submissionsJSON) 
      : [];
      
    submissions.push(submission);
    localStorage.setItem('quiz_submissions', JSON.stringify(submissions));
    
    return submission;
  },
  
  async getCategories(): Promise<string[]> {
    await simulateApiDelay();
    
    const categories = [...new Set(MOCK_COURSES.map(course => course.category))];
    return categories;
  }
};
