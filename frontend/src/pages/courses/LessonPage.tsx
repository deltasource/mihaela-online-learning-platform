import { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { useCourseStore } from '../../stores/courseStore';
import { useAuthStore } from '../../stores/authStore';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { Alert } from '../../components/ui/Alert';
import { ChevronLeft, ChevronRight, Check, Clock } from 'lucide-react';

const LessonPage = () => {
  const { courseId, lessonId } = useParams<{ courseId: string; lessonId: string }>();
  const { 
    currentCourse, 
    currentLesson, 
    fetchCourseById, 
    fetchLessonById,
    markLessonCompleted,
    markLessonIncomplete
  } = useCourseStore();
  const { user } = useAuthStore();
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isLessonCompleted, setIsLessonCompleted] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const loadData = async () => {
      if (!courseId || !lessonId) {
        setError('Invalid course or lesson ID');
        setIsLoading(false);
        return;
      }

      try {
        setIsLoading(true);
        setError(null);
        if (!currentCourse || currentCourse.id !== courseId) {
          await fetchCourseById(courseId);
        }
        await fetchLessonById(courseId, lessonId);
        
        setIsLoading(false);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load lesson');
        setIsLoading(false);
      }
    };

    loadData();
  }, [courseId, lessonId, fetchCourseById, fetchLessonById, currentCourse]);
  useEffect(() => {
    if (user && courseId && lessonId) {
      const completedLessonsJSON = localStorage.getItem(
        `user_${user.id}_course_${courseId}_lessons`
      );
      
      if (completedLessonsJSON) {
        const completedLessons = JSON.parse(completedLessonsJSON);
        setIsLessonCompleted(completedLessons.includes(lessonId));
      }
    }
  }, [user, courseId, lessonId]);

  const handleMarkComplete = async () => {
    if (user && courseId && lessonId) {
      if (isLessonCompleted) {
        await markLessonIncomplete(user.id, courseId, lessonId);
        setIsLessonCompleted(false);
      } else {
        await markLessonCompleted(user.id, courseId, lessonId);
        setIsLessonCompleted(true);
      }
    }
  };

  const handlePreviousLesson = () => {
    if (!currentCourse) return;
    
    const sortedLessons = [...currentCourse.lessons].sort((a, b) => a.order - b.order);
    const currentIndex = sortedLessons.findIndex(lesson => lesson.id === lessonId);
    
    if (currentIndex > 0) {
      const prevLesson = sortedLessons[currentIndex - 1];
      navigate(`/courses/${courseId}/lessons/${prevLesson.id}`);
    }
  };

  const handleNextLesson = () => {
    if (!currentCourse) return;
    
    const sortedLessons = [...currentCourse.lessons].sort((a, b) => a.order - b.order);
    const currentIndex = sortedLessons.findIndex(lesson => lesson.id === lessonId);
    
    if (currentIndex < sortedLessons.length - 1) {
      const nextLesson = sortedLessons[currentIndex + 1];
      navigate(`/courses/${courseId}/lessons/${nextLesson.id}`);
    } else if (currentCourse.quizzes && currentCourse.quizzes.length > 0) {
      // Navigate to the first quiz if there are no more lessons
      navigate(`/courses/${courseId}/quizzes/${currentCourse.quizzes[0].id}`);
    }
  };

  if (isLoading) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  if (error) {
    return (
      <div className="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        <Alert variant="error" title="Error loading lesson">
          {error}
        </Alert>
        <div className="mt-6 text-center">
          <Link to={`/courses/${courseId}`} className="btn-primary">
            Back to Course
          </Link>
        </div>
      </div>
    );
  }

  if (!currentLesson || !currentCourse) {
    return (
      <div className="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        <div className="text-center py-12">
          <h2 className="text-2xl font-semibold text-gray-900">Lesson not found</h2>
          <p className="mt-2 text-gray-500">The lesson you're looking for doesn't exist or has been removed.</p>
          <div className="mt-6">
            <Link to={`/courses/${courseId}`} className="btn-primary">
              Back to Course
            </Link>
          </div>
        </div>
      </div>
    );
  }

  const { title, content, videoUrl, duration } = currentLesson;
  const sortedLessons = [...currentCourse.lessons].sort((a, b) => a.order - b.order);
  const currentIndex = sortedLessons.findIndex(lesson => lesson.id === lessonId);
  const isFirstLesson = currentIndex === 0;
  const isLastLesson = currentIndex === sortedLessons.length - 1;

  return (
    <div className="bg-gray-50 min-h-[calc(100vh-64px)]">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Breadcrumb */}
        <nav className="mb-6">
          <ol className="flex space-x-2 text-sm text-gray-500">
            <li>
              <Link to="/courses" className="hover:text-primary-600">Courses</Link>
            </li>
            <li className="flex items-center space-x-2">
              <span>/</span>
              <Link to={`/courses/${courseId}`} className="hover:text-primary-600">{currentCourse.title}</Link>
            </li>
            <li className="flex items-center space-x-2">
              <span>/</span>
              <span className="text-gray-700">{title}</span>
            </li>
          </ol>
        </nav>
        
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
          {/* Lesson sidebar */}
          <div className="lg:col-span-1">
            <div className="bg-white shadow rounded-lg overflow-hidden sticky top-6">
              <div className="p-4 bg-primary-50 border-b border-primary-100">
                <h2 className="text-lg font-semibold text-primary-800">Course Content</h2>
              </div>
              <div className="p-4">
                <div className="space-y-2">
                  {sortedLessons.map((lesson, index) => {
                    const isActive = lesson.id === lessonId;
                    return (
                      <Link
                        key={lesson.id}
                        to={`/courses/${courseId}/lessons/${lesson.id}`}
                        className={`block px-3 py-2 rounded-md text-sm ${
                          isActive
                            ? 'bg-primary-50 text-primary-700 font-medium'
                            : 'text-gray-700 hover:bg-gray-50'
                        }`}
                      >
                        <div className="flex justify-between items-center">
                          <div className="flex items-center">
                            <span className="w-6 h-6 flex items-center justify-center rounded-full bg-gray-100 text-gray-700 mr-2">
                              {index + 1}
                            </span>
                            <span className="truncate">{lesson.title}</span>
                          </div>
                          <span className="text-xs text-gray-500">{lesson.duration} min</span>
                        </div>
                      </Link>
                    );
                  })}
                  
                  {/* Quizzes */}
                  {currentCourse.quizzes && currentCourse.quizzes.length > 0 && (
                    <>
                      <div className="border-t border-gray-200 my-2 pt-2">
                        <h3 className="text-sm font-medium text-gray-700 mb-2">Quizzes</h3>
                      </div>
                      {currentCourse.quizzes.map((quiz, index) => (
                        <Link
                          key={quiz.id}
                          to={`/courses/${courseId}/quizzes/${quiz.id}`}
                          className="block px-3 py-2 rounded-md text-sm text-gray-700 hover:bg-gray-50"
                        >
                          <div className="flex justify-between items-center">
                            <div className="flex items-center">
                              <span className="w-6 h-6 flex items-center justify-center rounded-full bg-secondary-100 text-secondary-700 mr-2">
                                Q{index + 1}
                              </span>
                              <span className="truncate">{quiz.title}</span>
                            </div>
                            <span className="text-xs text-gray-500">{quiz.timeLimit || 30} min</span>
                          </div>
                        </Link>
                      ))}
                    </>
                  )}
                </div>
              </div>
            </div>
          </div>
          
          {/* Main content */}
          <div className="lg:col-span-3">
            <div className="bg-white shadow rounded-lg overflow-hidden">
              <div className="p-6">
                <h1 className="text-2xl font-bold text-gray-900 mb-4">{title}</h1>
                
                <div className="flex items-center text-sm text-gray-500 mb-6">
                  <Clock className="h-5 w-5 text-gray-400 mr-2" />
                  <span>{duration} min</span>
                </div>
                
                {videoUrl && (
                  <div className="aspect-w-16 aspect-h-9 mb-6 bg-gray-100 rounded-lg overflow-hidden">
                    <iframe
                      src={videoUrl}
                      title={title}
                      className="w-full h-96 rounded-lg"
                      frameBorder="0"
                      allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                      allowFullScreen
                    ></iframe>
                  </div>
                )}
                
                <div className="prose max-w-none" dangerouslySetInnerHTML={{ __html: content }} />
                
                <div className="mt-8 pt-6 border-t border-gray-200">
                  <div className="flex justify-between">
                    <button
                      onClick={handlePreviousLesson}
                      disabled={isFirstLesson}
                      className={`flex items-center ${
                        isFirstLesson 
                          ? 'text-gray-400 cursor-not-allowed' 
                          : 'text-primary-600 hover:text-primary-800'
                      }`}
                    >
                      <ChevronLeft className="h-5 w-5 mr-1" />
                      Previous Lesson
                    </button>
                    
                    <button
                      onClick={handleMarkComplete}
                      className={`flex items-center ${
                        isLessonCompleted
                          ? 'bg-success-50 text-success-600 border border-success-200 hover:bg-success-100 px-4 py-2 rounded-md'
                          : 'bg-primary-50 text-primary-600 border border-primary-200 hover:bg-primary-100 px-4 py-2 rounded-md'
                      }`}
                    >
                      <Check className="h-5 w-5 mr-1" />
                      {isLessonCompleted ? 'Completed' : 'Mark as Complete'}
                    </button>
                    
                    <button
                      onClick={handleNextLesson}
                      className="flex items-center text-primary-600 hover:text-primary-800"
                    >
                      Next Lesson
                      <ChevronRight className="h-5 w-5 ml-1" />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LessonPage;