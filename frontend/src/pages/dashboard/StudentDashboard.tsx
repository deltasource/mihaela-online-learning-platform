import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { BookOpen, Clock, Award, ChevronRight } from 'lucide-react';
import Layout from '../../components/layout/Layout';
import Card, { CardContent, CardHeader } from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import { Course, Enrollment } from '../../types';
import { getUserEnrollments } from '../../api/courses';
import { useAuth } from '../../context/AuthContext';

const StudentDashboard: React.FC = () => {
  const { user } = useAuth();
  const [enrollments, setEnrollments] = useState<Enrollment[]>([]);
  const [courses, setCourses] = useState<Course[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchEnrollments = async () => {
      setIsLoading(true);
      try {
        const enrollmentsData = await getUserEnrollments();
        setEnrollments(enrollmentsData);

        const mockCourses: Course[] = enrollmentsData.map(enrollment => ({
          id: enrollment.courseId,
          title: `Course ${enrollment.courseId.substring(0, 5)}`,
          description: 'Course description',
          thumbnail: `https://images.pexels.com/photos/1181677/pexels-photo-1181677.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2`,
          instructorId: 'instructor-id',
          instructorName: 'Instructor Name',
          category: 'Web Development',
          level: 'beginner',
          price: 0,
          rating: 4.5,
          totalStudents: 100,
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString(),
        }));
        
        setCourses(mockCourses);
      } catch (error) {
        console.error('Failed to fetch enrollments:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchEnrollments();
  }, []);

  return (
    <Layout>
      <div className="bg-gray-50 min-h-screen py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          {/* Welcome Section */}
          <div className="bg-white rounded-lg shadow-md overflow-hidden mb-8">
            <div className="p-6 md:p-8 bg-gradient-to-r from-blue-600 to-indigo-700 text-white">
              <h1 className="text-3xl font-bold mb-2">Welcome back, {user?.name}!</h1>
              <p className="text-blue-100">
                Continue your learning journey and track your progress.
              </p>
            </div>
          </div>

          {/* Stats Section */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <Card>
              <CardContent className="p-6 flex items-center">
                <div className="bg-blue-100 p-3 rounded-full mr-4">
                  <BookOpen className="h-6 w-6 text-blue-600" />
                </div>
                <div>
                  <p className="text-gray-500 text-sm">Enrolled Courses</p>
                  <p className="text-2xl font-bold text-gray-900">{enrollments.length}</p>
                </div>
              </CardContent>
            </Card>
            
            <Card>
              <CardContent className="p-6 flex items-center">
                <div className="bg-green-100 p-3 rounded-full mr-4">
                  <Clock className="h-6 w-6 text-green-600" />
                </div>
                <div>
                  <p className="text-gray-500 text-sm">Hours Learned</p>
                  <p className="text-2xl font-bold text-gray-900">12.5</p>
                </div>
              </CardContent>
            </Card>
            
            <Card>
              <CardContent className="p-6 flex items-center">
                <div className="bg-yellow-100 p-3 rounded-full mr-4">
                  <Award className="h-6 w-6 text-yellow-600" />
                </div>
                <div>
                  <p className="text-gray-500 text-sm">Certificates Earned</p>
                  <p className="text-2xl font-bold text-gray-900">2</p>
                </div>
              </CardContent>
            </Card>
          </div>

          {/* In Progress Courses */}
          <Card className="mb-8">
            <CardHeader>
              <h2 className="text-xl font-bold text-gray-900">Continue Learning</h2>
            </CardHeader>
            <CardContent>
              {isLoading ? (
                <div className="flex justify-center py-12">
                  <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-600"></div>
                </div>
              ) : enrollments.length === 0 ? (
                <div className="text-center py-8">
                  <BookOpen className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                  <h3 className="text-lg font-medium text-gray-900 mb-2">No courses yet</h3>
                  <p className="text-gray-500 mb-4">
                    You haven't enrolled in any courses yet. Start learning today!
                  </p>
                  <Link to="/courses">
                    <Button>Browse Courses</Button>
                  </Link>
                </div>
              ) : (
                <div className="space-y-4">
                  {courses.map((course, index) => {
                    const enrollment = enrollments[index];
                    return (
                      <div
                        key={enrollment.id}
                        className="flex flex-col md:flex-row items-center p-4 border rounded-lg hover:bg-gray-50"
                      >
                        <img
                          src={course.thumbnail}
                          alt={course.title}
                          className="w-full md:w-48 h-32 object-cover rounded-md mb-4 md:mb-0 md:mr-6"
                        />
                        <div className="flex-grow">
                          <h3 className="font-semibold text-lg text-gray-900 mb-1">
                            {course.title}
                          </h3>
                          <p className="text-gray-500 text-sm mb-2">
                            {course.instructorName}
                          </p>
                          <div className="w-full bg-gray-200 rounded-full h-2.5 mb-2">
                            <div
                              className="bg-blue-600 h-2.5 rounded-full"
                              style={{ width: `${enrollment.progress}%` }}
                            ></div>
                          </div>
                          <p className="text-gray-600 text-sm">
                            {enrollment.progress}% complete
                          </p>
                        </div>
                        <div className="mt-4 md:mt-0 md:ml-4">
                          <Link to={`/courses/${course.id}`}>
                            <Button variant="outline" className="flex items-center">
                              Continue
                              <ChevronRight className="ml-1 h-4 w-4" />
                            </Button>
                          </Link>
                        </div>
                      </div>
                    );
                  })}
                </div>
              )}
            </CardContent>
          </Card>

          {/* Recommended Courses */}
          <Card>
            <CardHeader>
              <h2 className="text-xl font-bold text-gray-900">Recommended For You</h2>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {[1, 2, 3].map((i) => (
                  <div key={i} className="border rounded-lg overflow-hidden hover:shadow-md transition-shadow">
                    <img
                      src={`https://images.pexels.com/photos/1181677/pexels-photo-1181677.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2`}
                      alt={`Recommended Course ${i}`}
                      className="w-full h-40 object-cover"
                    />
                    <div className="p-4">
                      <h3 className="font-semibold text-gray-900 mb-1">
                        Recommended Course {i}
                      </h3>
                      <p className="text-gray-500 text-sm mb-2">Instructor Name</p>
                      <div className="flex items-center mb-2">
                        <div className="flex">
                          {[...Array(5)].map((_, j) => (
                            <svg
                              key={j}
                              className={`h-4 w-4 ${j < 4 ? 'text-yellow-500' : 'text-gray-300'}`}
                              fill="currentColor"
                              viewBox="0 0 20 20"
                            >
                              <path
                                fillRule="evenodd"
                                d="M10 15.585l-7.07 3.707 1.35-7.857L.587 7.11l7.87-1.142L10 0l2.543 5.968 7.87 1.142-5.693 4.325 1.35 7.857z"
                                clipRule="evenodd"
                              />
                            </svg>
                          ))}
                        </div>
                        <span className="text-sm text-gray-600 ml-1">4.0</span>
                      </div>
                      <Link to={`/courses/recommended-${i}`}>
                        <Button variant="outline" fullWidth>
                          View Course
                        </Button>
                      </Link>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </Layout>
  );
};

export default StudentDashboard;