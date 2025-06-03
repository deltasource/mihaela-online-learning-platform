"use client"

import type React from "react"
import { useState, useEffect } from "react"
import { Link } from "react-router-dom"
import { BookOpen, Users, DollarSign, BarChart2, Plus, Edit, Trash2, Eye, RefreshCw } from "lucide-react"
import Layout from "../../components/layout/Layout"
import Card, { CardContent, CardHeader } from "../../components/ui/Card"
import Button from "../../components/ui/Button"
import type { Course } from "../../types"
import { getCoursesByInstructorEmail, deleteCourse } from "../../api/courses"
import { useAuth } from "../../context/AuthContext"

const InstructorDashboard: React.FC = () => {
  const [courses, setCourses] = useState<Course[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [isDeleting, setIsDeleting] = useState<string | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [lastUpdated, setLastUpdated] = useState<Date>(new Date())
  const { user } = useAuth()

  const fetchCourses = async () => {
    if (!user?.email) {
      setError("User email not found")
      setIsLoading(false)
      return
    }

    setIsLoading(true)
    setError(null)

    try {
      console.log("Fetching courses for instructor:", user.email)
      const coursesData = await getCoursesByInstructorEmail(user.email)
      console.log("Fetched courses:", coursesData)

      if (coursesData && Array.isArray(coursesData)) {
        setCourses(coursesData)
        console.log(`Successfully loaded ${coursesData.length} courses`)
      } else {
        console.error("Invalid courses data format:", coursesData)
        setError("Received invalid data format from server")
      }

      setLastUpdated(new Date())
    } catch (error) {
      console.error("Failed to fetch instructor courses:", error)
      setError("Failed to load courses. Please try again.")
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => {
    fetchCourses()
  }, [user?.email])

  const handleDeleteCourse = async (courseId: string) => {
    if (window.confirm("Are you sure you want to delete this course? This action cannot be undone.")) {
      setIsDeleting(courseId)
      try {
        await deleteCourse(courseId)
        setCourses(courses.filter((course) => course.id !== courseId))
        console.log("Course deleted successfully:", courseId)

        alert("Course deleted successfully!")
      } catch (error) {
        console.error("Failed to delete course:", error)
        alert("Failed to delete course. Please try again.")
      } finally {
        setIsDeleting(null)
      }
    }
  }

  const handleRefreshCourses = async () => {
    await fetchCourses()
  }

  const totalCourses = courses.length
  const totalStudents = courses.reduce((total, course) => total + (course.totalStudents || 0), 0)
  const averageRating =
      totalCourses > 0 ? courses.reduce((total, course) => total + (course.rating || 0), 0) / totalCourses : 0
  const totalRevenue = courses.reduce((total, course) => total + (course.price || 0) * (course.totalStudents || 0), 0)

  const coursesByCategory = courses.reduce(
      (acc, course) => {
        const category = course.category || "General"
        acc[category] = (acc[category] || 0) + 1
        return acc
      },
      {} as Record<string, number>,
  )

  const coursesByLevel = courses.reduce(
      (acc, course) => {
        const level = course.level || "beginner"
        acc[level] = (acc[level] || 0) + 1
        return acc
      },
      {} as Record<string, number>,
  )

  return (
      <Layout>
        <div className="bg-gray-50 min-h-screen py-8">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            {/* Welcome Section */}
            <div className="bg-white rounded-lg shadow-md overflow-hidden mb-8">
              <div className="p-6 md:p-8 bg-gradient-to-r from-indigo-600 to-purple-700 text-white">
                <h1 className="text-3xl font-bold mb-2">Instructor Dashboard</h1>
                <p className="text-indigo-100">
                  Welcome back, {user?.name}! Manage your courses and track your performance.
                </p>
                <p className="text-indigo-200 text-sm mt-2">Last updated: {lastUpdated.toLocaleString()}</p>
              </div>
            </div>

            {/* Error Message */}
            {error && (
                <Card className="mb-6">
                  <CardContent className="p-4 bg-red-50 text-red-700">
                    <p className="font-medium">{error}</p>
                    <Button onClick={handleRefreshCourses} variant="outline" size="sm" className="mt-2">
                      Try Again
                    </Button>
                  </CardContent>
                </Card>
            )}

            {/* Main Stats Section */}
            <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
              <Card>
                <CardContent className="p-6 flex items-center">
                  <div className="bg-blue-100 p-3 rounded-full mr-4">
                    <BookOpen className="h-6 w-6 text-blue-600" />
                  </div>
                  <div>
                    <p className="text-gray-500 text-sm">Total Courses</p>
                    <p className="text-2xl font-bold text-gray-900">{totalCourses}</p>
                    <p className="text-xs text-gray-400 mt-1">{Object.keys(coursesByCategory).length} categories</p>
                  </div>
                </CardContent>
              </Card>

              <Card>
                <CardContent className="p-6 flex items-center">
                  <div className="bg-green-100 p-3 rounded-full mr-4">
                    <Users className="h-6 w-6 text-green-600" />
                  </div>
                  <div>
                    <p className="text-gray-500 text-sm">Total Students</p>
                    <p className="text-2xl font-bold text-gray-900">{totalStudents}</p>
                    <p className="text-xs text-gray-400 mt-1">
                      Avg: {totalCourses > 0 ? (totalStudents / totalCourses).toFixed(1) : 0} per course
                    </p>
                  </div>
                </CardContent>
              </Card>

              <Card>
                <CardContent className="p-6 flex items-center">
                  <div className="bg-yellow-100 p-3 rounded-full mr-4">
                    <BarChart2 className="h-6 w-6 text-yellow-600" />
                  </div>
                  <div>
                    <p className="text-gray-500 text-sm">Average Rating</p>
                    <p className="text-2xl font-bold text-gray-900">{averageRating.toFixed(1)}</p>
                    <p className="text-xs text-gray-400 mt-1">
                      {courses.filter((c) => (c.rating || 0) >= 4).length} courses rated 4+
                    </p>
                  </div>
                </CardContent>
              </Card>

              <Card>
                <CardContent className="p-6 flex items-center">
                  <div className="bg-purple-100 p-3 rounded-full mr-4">
                    <DollarSign className="h-6 w-6 text-purple-600" />
                  </div>
                  <div>
                    <p className="text-gray-500 text-sm">Total Revenue</p>
                    <p className="text-2xl font-bold text-gray-900">${totalRevenue.toFixed(2)}</p>
                    <p className="text-xs text-gray-400 mt-1">
                      {courses.filter((c) => (c.price || 0) > 0).length} paid courses
                    </p>
                  </div>
                </CardContent>
              </Card>
            </div>

            {/* Course Distribution */}
            {totalCourses > 0 && (
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                  <Card>
                    <CardHeader>
                      <h3 className="text-lg font-semibold">Courses by Category</h3>
                    </CardHeader>
                    <CardContent>
                      <div className="space-y-2">
                        {Object.entries(coursesByCategory).map(([category, count]) => (
                            <div key={category} className="flex justify-between items-center">
                              <span className="text-sm text-gray-600 capitalize">{category}</span>
                              <span className="text-sm font-medium">
                          {count} course{count !== 1 ? "s" : ""}
                        </span>
                            </div>
                        ))}
                      </div>
                    </CardContent>
                  </Card>

                  <Card>
                    <CardHeader>
                      <h3 className="text-lg font-semibold">Courses by Level</h3>
                    </CardHeader>
                    <CardContent>
                      <div className="space-y-2">
                        {Object.entries(coursesByLevel).map(([level, count]) => (
                            <div key={level} className="flex justify-between items-center">
                              <span className="text-sm text-gray-600 capitalize">{level}</span>
                              <span className="text-sm font-medium">
                          {count} course{count !== 1 ? "s" : ""}
                        </span>
                            </div>
                        ))}
                      </div>
                    </CardContent>
                  </Card>
                </div>
            )}

            {/* Courses Section */}
            <Card className="mb-8">
              <CardHeader className="flex justify-between items-center">
                <div>
                  <h2 className="text-xl font-bold text-gray-900">Your Courses</h2>
                  <p className="text-sm text-gray-500 mt-1">
                    Manage and track your course performance • {totalCourses} total courses
                  </p>
                </div>
                <div className="flex space-x-2">
                  <Button onClick={handleRefreshCourses} variant="outline" size="sm" disabled={isLoading}>
                    <RefreshCw className={`h-4 w-4 mr-2 ${isLoading ? "animate-spin" : ""}`} />
                    Refresh
                  </Button>
                  <Link to="/instructor/courses/new">
                    <Button className="flex items-center">
                      <Plus className="h-4 w-4 mr-2" />
                      Create Course
                    </Button>
                  </Link>
                </div>
              </CardHeader>
              <CardContent>
                {isLoading ? (
                    <div className="flex justify-center py-12">
                      <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-600"></div>
                    </div>
                ) : courses.length === 0 ? (
                    <div className="text-center py-8">
                      <BookOpen className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                      <h3 className="text-lg font-medium text-gray-900 mb-2">No courses yet</h3>
                      <p className="text-gray-500 mb-4">You haven't created any courses yet. Start teaching today!</p>
                      <Link to="/instructor/courses/new">
                        <Button>Create Your First Course</Button>
                      </Link>
                    </div>
                ) : (
                    <div className="overflow-x-auto">
                      <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                        <tr>
                          <th
                              scope="col"
                              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                          >
                            Course
                          </th>
                          <th
                              scope="col"
                              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                          >
                            Students
                          </th>
                          <th
                              scope="col"
                              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                          >
                            Rating
                          </th>
                          <th
                              scope="col"
                              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                          >
                            Price
                          </th>
                          <th
                              scope="col"
                              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                          >
                            Revenue
                          </th>
                          <th
                              scope="col"
                              className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                          >
                            Created
                          </th>
                          <th
                              scope="col"
                              className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider"
                          >
                            Actions
                          </th>
                        </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                        {courses.map((course) => (
                            <tr key={course.id} className="hover:bg-gray-50">
                              <td className="px-6 py-4 whitespace-nowrap">
                                <div className="flex items-center">
                                  <div className="flex-shrink-0 h-10 w-10">
                                    <img
                                        className="h-10 w-10 rounded-md object-cover"
                                        src={course.thumbnail || "/placeholder.svg"}
                                        alt={course.title}
                                        onError={(e) => {
                                          const target = e.target as HTMLImageElement
                                          target.src = "/placeholder.svg?height=40&width=40"
                                        }}
                                    />
                                  </div>
                                  <div className="ml-4">
                                    <div className="text-sm font-medium text-gray-900 max-w-xs truncate">
                                      {course.title}
                                    </div>
                                    <div className="text-sm text-gray-500 capitalize">
                                      {course.category} • {course.level}
                                    </div>
                                  </div>
                                </div>
                              </td>
                              <td className="px-6 py-4 whitespace-nowrap">
                                <div className="text-sm text-gray-900">{course.totalStudents || 0}</div>
                                <div className="text-xs text-gray-500">enrolled</div>
                              </td>
                              <td className="px-6 py-4 whitespace-nowrap">
                                <div className="flex items-center">
                                  <div className="text-sm text-gray-900 mr-1">{(course.rating || 0).toFixed(1)}</div>
                                  <svg className="h-4 w-4 text-yellow-500" fill="currentColor" viewBox="0 0 20 20">
                                    <path
                                        fillRule="evenodd"
                                        d="M10 15.585l-7.07 3.707 1.35-7.857L.587 7.11l7.87-1.142L10 0l2.543 5.968 7.87 1.142-5.693 4.325 1.35 7.857z"
                                        clipRule="evenodd"
                                    />
                                  </svg>
                                </div>
                              </td>
                              <td className="px-6 py-4 whitespace-nowrap">
                                <div className="text-sm text-gray-900">
                                  {(course.price || 0) === 0 ? "Free" : `$${(course.price || 0).toFixed(2)}`}
                                </div>
                              </td>
                              <td className="px-6 py-4 whitespace-nowrap">
                                <div className="text-sm text-gray-900">
                                  ${((course.price || 0) * (course.totalStudents || 0)).toFixed(2)}
                                </div>
                              </td>
                              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                {course.createdAt ? new Date(course.createdAt).toLocaleDateString() : "N/A"}
                              </td>
                              <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                <div className="flex justify-end space-x-2">
                                  <Link
                                      to={`/courses/${course.id}`}
                                      className="text-blue-600 hover:text-blue-900 p-1 rounded hover:bg-blue-50"
                                      title="View Course"
                                  >
                                    <Eye className="h-4 w-4" />
                                  </Link>
                                  <Link
                                      to={`/instructor/courses/${course.id}/edit`}
                                      className="text-indigo-600 hover:text-indigo-900 p-1 rounded hover:bg-indigo-50"
                                      title="Edit Course"
                                  >
                                    <Edit className="h-4 w-4" />
                                  </Link>
                                  <button
                                      onClick={() => handleDeleteCourse(course.id)}
                                      className="text-red-600 hover:text-red-900 p-1 rounded hover:bg-red-50"
                                      disabled={isDeleting === course.id}
                                      title="Delete Course"
                                  >
                                    {isDeleting === course.id ? (
                                        <div className="animate-spin h-4 w-4 border-2 border-red-600 rounded-full border-t-transparent"></div>
                                    ) : (
                                        <Trash2 className="h-4 w-4" />
                                    )}
                                  </button>
                                </div>
                              </td>
                            </tr>
                        ))}
                        </tbody>
                      </table>
                    </div>
                )}
              </CardContent>
            </Card>

            {/* Quick Actions */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <Card>
                <CardContent className="p-6 text-center">
                  <BookOpen className="h-8 w-8 text-blue-600 mx-auto mb-4" />
                  <h3 className="text-lg font-medium text-gray-900 mb-2">Create New Course</h3>
                  <p className="text-gray-500 mb-4">Start building your next course with sections and lessons</p>
                  <Link to="/instructor/courses/new">
                    <Button>Get Started</Button>
                  </Link>
                </CardContent>
              </Card>

              <Card>
                <CardContent className="p-6 text-center">
                  <Users className="h-8 w-8 text-green-600 mx-auto mb-4" />
                  <h3 className="text-lg font-medium text-gray-900 mb-2">Student Management</h3>
                  <p className="text-gray-500 mb-4">View and manage student enrollments</p>
                  <Button variant="outline" disabled>
                    Coming Soon
                  </Button>
                </CardContent>
              </Card>

              <Card>
                <CardContent className="p-6 text-center">
                  <BarChart2 className="h-8 w-8 text-purple-600 mx-auto mb-4" />
                  <h3 className="text-lg font-medium text-gray-900 mb-2">Analytics & Reports</h3>
                  <p className="text-gray-500 mb-4">Detailed insights into your teaching performance</p>
                  <Button variant="outline" disabled>
                    Coming Soon
                  </Button>
                </CardContent>
              </Card>
            </div>
          </div>
        </div>
      </Layout>
  )
}

export default InstructorDashboard
