import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useCourseStore } from '../../stores/courseStore';
import { useAuthStore } from '../../stores/authStore';
import { CourseProgress } from '../../components/courses/CourseProgress';
import { EnrollButton } from '../../components/courses/EnrollButton';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { 
  Clock, Star, Users, BookOpen, Award, BarChart, 
  CheckCircle, Calendar, AlertCircle, Heart
} from 'lucide-react';
import { Alert } from '../../components/ui/Alert';

const CourseDetailsPage = () => {
  const { courseId } = useParams<{ courseId: string }>();
  const { user } = useAuthStore();
  const { 
    currentCourse, 
    isLoading, 
    error, 
    fetchCourseById, 
    enrolledCourses,
    fetchEnrolledCourses 
  } = useCourseStore();
  const [isEnrolled, setIsEnrolled] = useState(false);
  const [currentEnrollment, setCurrentEnrollment] = useState(null);

  useEffect(() => {
    if (courseId) {
      fetchCourseById(courseId);
    }
  }, [fetchCourseById, courseId]);

  useEffect(() => {
    if (user) {
      fetchEnrolledCourses(user.id);
    }
  }, [fetchEnrolledCourses, user]);

  useEffect(() => {
    if (user && courseId && enrolledCourses.length > 0) {
      const enrollment = enrolledCourses.find(e => e.courseId === courseId);
      if (enrollment) {
        setIsEnrolled(true);
        setCurrentEnrollment(enrollment);
      } else {
        setIsEnrolled(false);
        setCurrentEnrollment(null);
      }
    }
  }, [user, courseId, enrolledCourses]);

  const formatDuration = (minutes: number) => {
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return `${hours}h ${mins}m`;
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
        <Alert variant="error" title="Error loading course">
          {error}
        </Alert>
      </div>
    );
  }

  if (!currentCourse) {
    return (
      <div className="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        <div className="text-center py-12">
          <AlertCircle className="mx-auto h-12 w-12 text-error-500" />
          <h2 className="mt-2 text-lg font-medium">Course not found</h2>
          <p className="mt-1 text-gray-500">The course you're looking for doesn't exist or has been removed.</p>
          <div className="mt-6">
            <Link to="/courses" className="btn-primary">
              Browse Courses
            </Link>
          </div>
        </div>
      </div>
    );
  }

  const { 
    title, description, thumbnail, instructorName, category, level, 
    duration, rating, ratingCount, enrollmentCount, 
    lessons, quizzes, createdAt
  } = currentCourse;

  const sortedLessons = [...lessons].sort((a, b) => a.order - b.order);
  const firstLessonId = sortedLessons.length > 0 ? sortedLessons[0].id : undefined;

  return (
    <div className="bg-gray-50">
      <div
        className="relative bg-cover bg-center py-16" 
        style={{ 
          backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)), url(${thumbnail})`,
          backgroundSize: 'cover'
        }}
      >
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative text-white">
          <div className="md:w-2/3">
            <div className="mb-2 flex items-center space-x-2">
              <span className="badge bg-primary-100 text-primary-800 border border-primary-200">
                {category}
              </span>
              <span className="badge bg-gray-100 text-gray-800 border border-gray-200">
                {level.charAt(0).toUpperCase() + level.slice(1)}
              </span>
              <span className="badge bg-green-100 text-green-800 border border-green-200 flex items-center">
                <Heart className="h-3 w-3 mr-1 fill-current" />
                FREE
              </span>
            </div>
            <h1 className="text-3xl md:text-4xl font-bold text-white mb-4">{title}</h1>
            <p className="text-lg text-gray-200 mb-6">{description}</p>
            
            <div className="flex flex-wrap items-center text-sm space-x-4 mb-6">
              <div className="flex items-center">
                <Star className="h-5 w-5 text-yellow-400 mr-1" />
                <span>{rating.toFixed(1)}</span>
                <span className="ml-1 text-gray-300">({ratingCount} reviews)</span>
              </div>
              <div className="flex items-center">
                <Users className="h-5 w-5 text-gray-300 mr-1" />
                <span>{enrollmentCount} students</span>
              </div>
              <div className="flex items-center">
                <Clock className="h-5 w-5 text-gray-300 mr-1" />
                <span>{formatDuration(duration)}</span>
              </div>
              <div className="flex items-center">
                <Calendar className="h-5 w-5 text-gray-300 mr-1" />
                <span>Last updated: {new Date(createdAt).toLocaleDateString()}</span>
              </div>
            </div>
            
            <div className="flex items-center mb-2">
              <div className="w-10 h-10 rounded-full bg-primary-100 flex items-center justify-center">
                <User className="w-6 h-6 text-primary-600" />
              </div>
              <div className="ml-3">
                <p className="text-white font-medium">{instructorName}</p>
                <p className="text-sm text-gray-300">Instructor</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="md:flex md:space-x-8">
          {/* Main content */}
          <div className="md:w-2/3">
            <div className="bg-white shadow rounded-lg overflow-hidden mb-8">
              <div className="p-6">
                <h2 className="text-2xl font-bold mb-4">What You'll Learn</h2>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="flex items-start">
                    <CheckCircle className="h-5 w-5 text-primary-600 mt-0.5 mr-3" />
                    <p>Build responsive websites with HTML, CSS, and JavaScript</p>
                  </div>
                  <div className="flex items-start">
                    <CheckCircle className="h-5 w-5 text-primary-600 mt-0.5 mr-3" />
                    <p>Understand key web development concepts and best practices</p>
                  </div>
                  <div className="flex items-start">
                    <CheckCircle className="h-5 w-5 text-primary-600 mt-0.5 mr-3" />
                    <p>Create modern, clean, and maintainable code</p>
                  </div>
                  <div className="flex items-start">
                    <CheckCircle className="h-5 w-5 text-primary-600 mt-0.5 mr-3" />
                    <p>Apply professional development workflows and tools</p>
                  </div>
                </div>
              </div>
            </div>
            
            <div className="bg-white shadow rounded-lg overflow-hidden mb-8">
              <div className="p-6">
                <h2 className="text-2xl font-bold mb-4">Course Content</h2>
                <div className="mb-4">
                  <p className="text-gray-600">
                    {lessons.length} lessons • {formatDuration(duration)} total duration • 100% Free
                  </p>
                </div>
                
                <div className="space-y-4">
                  {sortedLessons.map((lesson, index) => (
                    <div key={lesson.id} className="border-b pb-4 last:border-b-0 last:pb-0">
                      <div className="flex items-start justify-between">
                        <div className="flex items-start">
                          <span className="flex-shrink-0 w-7 h-7 bg-primary-100 text-primary-800 rounded-full flex items-center justify-center mr-3 font-semibold text-sm">
                            {index + 1}
                          </span>
                          <div>
                            <h4 className="font-medium text-gray-900">{lesson.title}</h4>
                            <p className="text-sm text-gray-500">{lesson.description}</p>
                          </div>
                        </div>
                        <div className="flex items-center text-sm">
                          <Clock className="h-4 w-4 text-gray-400 mr-1" />
                          <span>{lesson.duration} min</span>
                        </div>
                      </div>
                    </div>
                  ))}
                  
                  {quizzes && quizzes.length > 0 && (
                    <div className="border-b pb-4 last:border-b-0 last:pb-0">
                      <div className="flex items-start justify-between">
                        <div className="flex items-start">
                          <span className="flex-shrink-0 w-7 h-7 bg-secondary-100 text-secondary-800 rounded-full flex items-center justify-center mr-3 font-semibold text-sm">
                            Q
                          </span>
                          <div>
                            <h4 className="font-medium text-gray-900">{quizzes[0].title}</h4>
                            <p className="text-sm text-gray-500">{quizzes[0].description}</p>
                          </div>
                        </div>
                        <div className="flex items-center text-sm">
                          <Clock className="h-4 w-4 text-gray-400 mr-1" />
                          <span>{quizzes[0].timeLimit || 30} min</span>
                        </div>
                      </div>
                    </div>
                  )}
                </div>
              </div>
            </div>
            
            <div className="bg-white shadow rounded-lg overflow-hidden mb-8">
              <div className="p-6">
                <h2 className="text-2xl font-bold mb-4">Requirements</h2>
                <ul className="list-disc list-inside space-y-2 text-gray-700">
                  <li>Basic understanding of HTML, CSS, and JavaScript (beginner level is fine)</li>
                  <li>A computer with internet access and a modern web browser</li>
                  <li>No special software is required - we'll set up everything during the course</li>
                  <li>Enthusiasm and willingness to learn!</li>
                </ul>
              </div>
            </div>
          </div>
          
          {/* Sidebar */}
          <div className="md:w-1/3 mt-8 md:mt-0">
            <div className="bg-white shadow rounded-lg overflow-hidden mb-8 sticky top-6">
              <div className="p-6">
                <div className="flex justify-between items-center mb-4">
                  <div className="flex items-center">
                    <Heart className="h-6 w-6 text-green-600 mr-2 fill-current" />
                    <span className="text-3xl font-bold text-green-600">FREE</span>
                  </div>
                </div>
                
                <EnrollButton 
                  courseId={courseId || ''} 
                  isEnrolled={isEnrolled} 
                  firstLessonId={firstLessonId}
                />
                
                <div className="mt-6">
                  <h4 className="font-semibold text-gray-900 mb-2">This free course includes:</h4>
                  <ul className="space-y-3">
                    <li className="flex items-center text-gray-700">
                      <BookOpen className="h-5 w-5 text-gray-400 mr-3" />
                      <span>{lessons.length} lessons</span>
                    </li>
                    <li className="flex items-center text-gray-700">
                      <Clock className="h-5 w-5 text-gray-400 mr-3" />
                      <span>{formatDuration(duration)} total length</span>
                    </li>
                    <li className="flex items-center text-gray-700">
                      <BarChart className="h-5 w-5 text-gray-400 mr-3" />
                      <span>{quizzes.length} quiz{quizzes.length !== 1 ? 'zes' : ''}</span>
                    </li>
                    <li className="flex items-center text-gray-700">
                      <Award className="h-5 w-5 text-gray-400 mr-3" />
                      <span>Certificate of completion</span>
                    </li>
                    <li className="flex items-center text-gray-700">
                      <Heart className="h-5 w-5 text-green-600 mr-3 fill-current" />
                      <span>Lifetime access</span>
                    </li>
                  </ul>
                </div>
                
                <div className="mt-6 pt-6 border-t border-gray-200 flex justify-center">
                  <button className="text-primary-600 hover:text-primary-800 font-medium">
                    Share this free course
                  </button>
                </div>
              </div>
            </div>
            
            {isEnrolled && (
              <CourseProgress 
                course={currentCourse} 
                enrollment={currentEnrollment}
              />
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default CourseDetailsPage;

const User = ({ className }: { className?: string }) => {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
      className={className}
    >
      <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
      <circle cx="12" cy="7" r="4"></circle>
    </svg>
  );
};
