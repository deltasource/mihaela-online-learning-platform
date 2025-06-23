import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuthStore } from '../../stores/authStore';
import { useCourseStore } from '../../stores/courseStore';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { BookOpen, Award, BarChart, Clock, ChevronRight, Search } from 'lucide-react';
import { CourseEnrollment, Course } from '../../types/course';

const DashboardPage = () => {
  const { user } = useAuthStore();
  const { courses, enrolledCourses, isLoading, fetchEnrolledCourses, fetchCourses } = useCourseStore();
  const [enrolledCoursesWithDetails, setEnrolledCoursesWithDetails] = useState<(CourseEnrollment & { course?: Course })[]>([]);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    if (user) {
      fetchEnrolledCourses(user.id);
      fetchCourses();
    }
  }, [user, fetchEnrolledCourses, fetchCourses]);

  useEffect(() => {
    if (enrolledCourses.length > 0 && courses.length > 0) {
      const enrichedEnrollments = enrolledCourses.map(enrollment => {
        const courseDetails = courses.find(c => c.id === enrollment.courseId);
        return {
          ...enrollment,
          course: courseDetails
        };
      });
      
      setEnrolledCoursesWithDetails(enrichedEnrollments);
    }
  }, [enrolledCourses, courses]);

  const filteredEnrollments = enrolledCoursesWithDetails.filter(enrollment =>
    enrollment.course?.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
    enrollment.course?.category.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const coursesInProgress = enrolledCoursesWithDetails.filter(e => e.status === 'in-progress').length;
  const coursesCompleted = enrolledCoursesWithDetails.filter(e => e.status === 'completed').length;
  const overallProgress = enrolledCoursesWithDetails.length > 0 
    ? Math.round(enrolledCoursesWithDetails.reduce((sum, e) => sum + e.progress, 0) / enrolledCoursesWithDetails.length)
    : 0;

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-[calc(100vh-64px)]">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  return (
    <div className="bg-gray-50 min-h-[calc(100vh-64px)]">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Welcome section */}
        <div className="bg-white shadow rounded-lg overflow-hidden mb-8">
          <div className="p-6">
            <h1 className="text-2xl font-bold text-gray-900 mb-2">
              Welcome back, {user?.firstName || 'Student'}!
            </h1>
            <p className="text-gray-600">
              Track your progress and continue your learning journey.
            </p>
          </div>
        </div>

        {/* Stats */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="bg-white shadow rounded-lg overflow-hidden">
            <div className="p-5">
              <div className="flex items-center">
                <div className="flex-shrink-0">
                  <BookOpen className="h-6 w-6 text-primary-600" />
                </div>
                <div className="ml-5 w-0 flex-1">
                  <dl>
                    <dt className="text-sm font-medium text-gray-500 truncate">
                      Courses Enrolled
                    </dt>
                    <dd>
                      <div className="text-lg font-medium text-gray-900">
                        {enrolledCoursesWithDetails.length}
                      </div>
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>

          <div className="bg-white shadow rounded-lg overflow-hidden">
            <div className="p-5">
              <div className="flex items-center">
                <div className="flex-shrink-0">
                  <BarChart className="h-6 w-6 text-primary-600" />
                </div>
                <div className="ml-5 w-0 flex-1">
                  <dl>
                    <dt className="text-sm font-medium text-gray-500 truncate">
                      In Progress
                    </dt>
                    <dd>
                      <div className="text-lg font-medium text-gray-900">
                        {coursesInProgress}
                      </div>
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>

          <div className="bg-white shadow rounded-lg overflow-hidden">
            <div className="p-5">
              <div className="flex items-center">
                <div className="flex-shrink-0">
                  <Award className="h-6 w-6 text-primary-600" />
                </div>
                <div className="ml-5 w-0 flex-1">
                  <dl>
                    <dt className="text-sm font-medium text-gray-500 truncate">
                      Completed
                    </dt>
                    <dd>
                      <div className="text-lg font-medium text-gray-900">
                        {coursesCompleted}
                      </div>
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Overall progress */}
        <div className="bg-white shadow rounded-lg overflow-hidden mb-8">
          <div className="p-6">
            <h2 className="text-lg font-medium text-gray-900 mb-4">Overall Progress</h2>
            <div>
              <div className="flex justify-between mb-1">
                <span className="text-sm font-medium text-gray-700">{overallProgress}% Complete</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2.5">
                <div 
                  className="bg-primary-600 h-2.5 rounded-full" 
                  style={{ width: `${overallProgress}%` }}
                ></div>
              </div>
            </div>
          </div>
        </div>

        {/* My courses */}
        <div className="bg-white shadow rounded-lg overflow-hidden">
          <div className="p-6 border-b border-gray-200">
            <div className="flex justify-between items-center">
              <h2 className="text-lg font-medium text-gray-900">My Courses</h2>
              <Link 
                to="/courses" 
                className="text-sm font-medium text-primary-600 hover:text-primary-800"
              >
                Browse more courses
              </Link>
            </div>
            
            <div className="mt-4 relative rounded-md shadow-sm">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <Search className="h-5 w-5 text-gray-400" />
              </div>
              <input
                type="text"
                placeholder="Search your courses..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="input pl-10"
              />
            </div>
          </div>
          
          {filteredEnrollments.length === 0 ? (
            <div className="p-6 text-center">
              <p className="text-gray-500">
                {searchTerm 
                  ? 'No courses matching your search.' 
                  : "You haven't enrolled in any courses yet."}
              </p>
              <Link 
                to="/courses" 
                className="mt-4 inline-block btn-primary"
              >
                Explore Courses
              </Link>
            </div>
          ) : (
            <ul className="divide-y divide-gray-200">
              {filteredEnrollments.map(enrollment => {
                const course = enrollment.course;
                if (!course) return null;
                
                return (
                  <li key={enrollment.id}>
                    <Link 
                      to={`/courses/${enrollment.courseId}`} 
                      className="block hover:bg-gray-50"
                    >
                      <div className="flex items-center p-4 sm:px-6">
                        <div className="min-w-0 flex-1 flex items-center">
                          <div className="flex-shrink-0">
                            <img
                              className="h-16 w-16 object-cover rounded"
                              src={course.thumbnail}
                              alt={course.title}
                            />
                          </div>
                          <div className="min-w-0 flex-1 px-4">
                            <div>
                              <p className="text-sm font-medium text-primary-600 truncate">
                                {course.title}
                              </p>
                              <p className="mt-1 text-sm text-gray-500 truncate">
                                {course.category}
                              </p>
                              <div className="mt-2 flex items-center text-sm text-gray-500">
                                <div className="flex items-center">
                                  <Clock className="flex-shrink-0 mr-1.5 h-4 w-4 text-gray-400" />
                                  <span>
                                    {Math.floor(course.duration / 60)}h {course.duration % 60}m
                                  </span>
                                </div>
                                <span className="mx-2">•</span>
                                <span>{enrollment.status === 'completed' ? 'Completed' : 'In progress'}</span>
                              </div>
                            </div>
                          </div>
                        </div>
                        
                        <div className="ml-5 flex-shrink-0">
                          <div className="w-16 flex flex-col items-center">
                            <div className="w-12 h-12 rounded-full bg-primary-50 flex items-center justify-center">
                              <span className="text-primary-700 font-semibold">{enrollment.progress}%</span>
                            </div>
                          </div>
                        </div>
                        
                        <div className="ml-5 flex-shrink-0">
                          <ChevronRight className="h-5 w-5 text-gray-400" />
                        </div>
                      </div>
                    </Link>
                  </li>
                );
              })}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;