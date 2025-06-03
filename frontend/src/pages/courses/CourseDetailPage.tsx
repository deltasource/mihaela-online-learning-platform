"use client"

import type React from "react"
import { useState, useEffect } from "react"
import { useParams, useNavigate } from "react-router-dom"
import {
  Star,
  Clock,
  Users,
  Play,
  ChevronRight,
  CheckCircle,
  Lock,
  Award,
  Globe,
  Smartphone,
  Monitor,
  BookOpen,
  Target,
  TrendingUp,
} from "lucide-react"
import Layout from "../../components/layout/Layout"
import Button from "../../components/ui/Button"
import VideoPlayer from "../../components/courses/VideoPlayer"
import type { CourseDetails, Lesson } from "../../types"
import { getCourseById, enrollInCourse } from "../../api/courses"
import { useAuth } from "../../context/AuthContext"

const CourseDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const { user, isAuthenticated } = useAuth()
  const [course, setCourse] = useState<CourseDetails | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [selectedLesson, setSelectedLesson] = useState<Lesson | null>(null)
  const [isEnrolling, setIsEnrolling] = useState(false)
  const [activeTab, setActiveTab] = useState<"overview" | "curriculum" | "instructor" | "reviews">("overview")

  useEffect(() => {
    const fetchCourse = async () => {
      if (!id) return

      setIsLoading(true)
      try {
        const courseData = await getCourseById(id)
        setCourse(courseData)

        // Set the first preview lesson as selected by default
        const firstPreviewLesson = courseData.sections
            .flatMap((section) => section.lessons)
            .find((lesson) => lesson.isPreview)

        if (firstPreviewLesson) {
          setSelectedLesson(firstPreviewLesson)
        }
      } catch (err) {
        console.error("Failed to fetch course:", err)
        setError("Failed to load course details. Please try again later.")
      } finally {
        setIsLoading(false)
      }
    }

    fetchCourse()
  }, [id])

  const handleLessonSelect = (lesson: Lesson) => {
    // If not authenticated or not enrolled, only allow preview lessons
    if (!isAuthenticated || (user?.role !== "instructor" && course?.instructorId !== user?.id)) {
      if (!lesson.isPreview) {
        return
      }
    }

    setSelectedLesson(lesson)
  }

  const handleEnroll = async () => {
    if (!isAuthenticated) {
      navigate("/login?redirect=" + encodeURIComponent(`/courses/${id}`))
      return
    }

    if (!id) return

    setIsEnrolling(true)
    try {
      await enrollInCourse(id)
      // Refresh the page to update enrollment status
      window.location.reload()
    } catch (err) {
      console.error("Failed to enroll in course:", err)
      setError("Failed to enroll in this course. Please try again later.")
    } finally {
      setIsEnrolling(false)
    }
  }

  const formatDuration = (seconds: number) => {
    const hours = Math.floor(seconds / 3600)
    const minutes = Math.floor((seconds % 3600) / 60)

    if (hours > 0) {
      return `${hours}h ${minutes}m`
    }
    return `${minutes}m`
  }

  const getTotalLessons = () => {
    return course?.sections.reduce((total, section) => total + section.lessons.length, 0) || 0
  }

  const getTotalDuration = () => {
    const totalSeconds =
        course?.sections.reduce((total, section) => {
          return (
              total +
              section.lessons.reduce((sectionTotal, lesson) => {
                return sectionTotal + lesson.duration
              }, 0)
          )
        }, 0) || 0

    return formatDuration(totalSeconds)
  }

  const getPreviewLessonsCount = () => {
    return (
        course?.sections.reduce((total, section) => {
          return total + section.lessons.filter((lesson) => lesson.isPreview).length
        }, 0) || 0
    )
  }

  if (isLoading) {
    return (
        <Layout>
          <div className="flex justify-center items-center min-h-screen">
            <div className="text-center">
              <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-600 mx-auto mb-4"></div>
              <p className="text-gray-600">Loading course...</p>
            </div>
          </div>
        </Layout>
    )
  }

  if (error || !course) {
    return (
        <Layout>
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12 text-center">
            <h2 className="text-2xl font-bold text-red-600 mb-4">Course Not Found</h2>
            <p className="text-gray-700 mb-8">{error || "The course you're looking for doesn't exist."}</p>
            <Button onClick={() => navigate("/courses")}>Browse All Courses</Button>
          </div>
        </Layout>
    )
  }

  return (
      <Layout>
        <div className="bg-gray-50 min-h-screen">
          {/* Hero Section */}
          <div className="bg-gradient-to-r from-blue-900 to-blue-700 text-white">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
              <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Course Info */}
                <div className="lg:col-span-2">
                  <div className="mb-4">
                  <span className="inline-block bg-blue-600 text-blue-100 px-3 py-1 rounded-full text-sm font-medium">
                    {course.category}
                  </span>
                  </div>

                  <h1 className="text-4xl font-bold mb-4">{course.title}</h1>
                  <p className="text-xl text-blue-100 mb-6 leading-relaxed">{course.description}</p>

                  {/* Course Stats */}
                  <div className="flex flex-wrap items-center gap-6 mb-6">
                    <div className="flex items-center">
                      <Star className="h-5 w-5 text-yellow-400 mr-2" />
                      <span className="font-semibold">{course.rating.toFixed(1)}</span>
                      <span className="text-blue-200 ml-1">({course.totalStudents} students)</span>
                    </div>
                    <div className="flex items-center">
                      <Clock className="h-5 w-5 text-blue-200 mr-2" />
                      <span>{getTotalDuration()}</span>
                    </div>
                    <div className="flex items-center">
                      <BookOpen className="h-5 w-5 text-blue-200 mr-2" />
                      <span>{getTotalLessons()} lessons</span>
                    </div>
                    <div className="flex items-center">
                      <TrendingUp className="h-5 w-5 text-blue-200 mr-2" />
                      <span className="capitalize">{course.level}</span>
                    </div>
                  </div>

                  {/* Instructor Info */}
                  <div className="flex items-center">
                    <div className="w-12 h-12 bg-blue-600 rounded-full flex items-center justify-center mr-4">
                    <span className="text-white font-semibold text-lg">
                      {course.instructorName.charAt(0).toUpperCase()}
                    </span>
                    </div>
                    <div>
                      <p className="text-blue-100">Created by</p>
                      <p className="font-semibold">{course.instructorName}</p>
                    </div>
                  </div>
                </div>

                {/* Enrollment Card */}
                <div className="lg:col-span-1">
                  <div className="bg-white rounded-xl shadow-xl p-6 sticky top-8">
                    {/* Video Preview */}
                    <div className="aspect-video bg-gray-900 rounded-lg mb-6 relative overflow-hidden">
                      {selectedLesson ? (
                          <VideoPlayer videoUrl={selectedLesson.videoUrl} title={selectedLesson.title} />
                      ) : (
                          <div className="flex items-center justify-center h-full">
                            <div className="text-center">
                              <Play className="h-16 w-16 text-white mx-auto mb-4 opacity-80" />
                              <p className="text-white">Course Preview</p>
                            </div>
                          </div>
                      )}
                    </div>

                    {/* Price */}
                    <div className="text-center mb-6">
                      <div className="text-3xl font-bold text-gray-900 mb-2">
                        {course.price === 0 ? "Free" : `$${course.price.toFixed(2)}`}
                      </div>
                      {course.price > 0 && <p className="text-gray-600">One-time payment • Full access</p>}
                    </div>

                    {/* Enroll Button */}
                    <Button
                        fullWidth
                        size="lg"
                        onClick={handleEnroll}
                        isLoading={isEnrolling}
                        className="mb-6 text-lg py-4"
                    >
                      {course.price === 0 ? "Enroll for Free" : "Enroll Now"}
                    </Button>

                    {/* What's Included */}
                    <div className="space-y-3">
                      <h3 className="font-semibold text-gray-900">This course includes:</h3>
                      <div className="space-y-2 text-sm">
                        <div className="flex items-center text-gray-700">
                          <Monitor className="h-4 w-4 mr-3 text-green-600" />
                          {getTotalDuration()} on-demand video
                        </div>
                        <div className="flex items-center text-gray-700">
                          <BookOpen className="h-4 w-4 mr-3 text-green-600" />
                          {getTotalLessons()} lessons
                        </div>
                        <div className="flex items-center text-gray-700">
                          <Smartphone className="h-4 w-4 mr-3 text-green-600" />
                          Access on mobile and TV
                        </div>
                        <div className="flex items-center text-gray-700">
                          <Globe className="h-4 w-4 mr-3 text-green-600" />
                          Full lifetime access
                        </div>
                        <div className="flex items-center text-gray-700">
                          <Award className="h-4 w-4 mr-3 text-green-600" />
                          Certificate of completion
                        </div>
                      </div>
                    </div>

                    {/* Preview Info */}
                    {getPreviewLessonsCount() > 0 && (
                        <div className="mt-6 p-4 bg-blue-50 rounded-lg">
                          <p className="text-sm text-blue-800 font-medium">
                            🎬 {getPreviewLessonsCount()} free preview lesson{getPreviewLessonsCount() > 1 ? "s" : ""}{" "}
                            available
                          </p>
                        </div>
                    )}
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Main Content */}
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
            {/* Navigation Tabs */}
            <div className="bg-white rounded-lg shadow-sm border border-gray-200 mb-8">
              <div className="border-b border-gray-200">
                <nav className="flex space-x-8 px-6">
                  <button
                      onClick={() => setActiveTab("overview")}
                      className={`py-4 px-1 border-b-2 font-medium text-sm transition-colors ${
                          activeTab === "overview"
                              ? "border-blue-500 text-blue-600"
                              : "border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300"
                      }`}
                  >
                    Overview
                  </button>
                  <button
                      onClick={() => setActiveTab("curriculum")}
                      className={`py-4 px-1 border-b-2 font-medium text-sm transition-colors ${
                          activeTab === "curriculum"
                              ? "border-blue-500 text-blue-600"
                              : "border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300"
                      }`}
                  >
                    Curriculum
                  </button>
                  <button
                      onClick={() => setActiveTab("instructor")}
                      className={`py-4 px-1 border-b-2 font-medium text-sm transition-colors ${
                          activeTab === "instructor"
                              ? "border-blue-500 text-blue-600"
                              : "border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300"
                      }`}
                  >
                    Instructor
                  </button>
                  <button
                      onClick={() => setActiveTab("reviews")}
                      className={`py-4 px-1 border-b-2 font-medium text-sm transition-colors ${
                          activeTab === "reviews"
                              ? "border-blue-500 text-blue-600"
                              : "border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300"
                      }`}
                  >
                    Reviews
                  </button>
                </nav>
              </div>

              {/* Tab Content */}
              <div className="p-6">
                {activeTab === "overview" && (
                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                      <div>
                        <h2 className="text-2xl font-bold text-gray-900 mb-4">What you'll learn</h2>
                        <div className="grid grid-cols-1 gap-3">
                          {[
                            "Master the fundamentals and advanced concepts",
                            "Build real-world projects from scratch",
                            "Understand best practices and industry standards",
                            "Get hands-on experience with practical exercises",
                            "Learn from an experienced instructor",
                            "Join a community of learners",
                          ].map((item, index) => (
                              <div key={index} className="flex items-start">
                                <CheckCircle className="h-5 w-5 text-green-600 mr-3 mt-0.5 flex-shrink-0" />
                                <span className="text-gray-700">{item}</span>
                              </div>
                          ))}
                        </div>
                      </div>

                      <div>
                        <h2 className="text-2xl font-bold text-gray-900 mb-4">Course Requirements</h2>
                        <div className="space-y-3">
                          <div className="flex items-start">
                            <Target className="h-5 w-5 text-blue-600 mr-3 mt-0.5 flex-shrink-0" />
                            <span className="text-gray-700">Basic computer skills and internet access</span>
                          </div>
                          <div className="flex items-start">
                            <Target className="h-5 w-5 text-blue-600 mr-3 mt-0.5 flex-shrink-0" />
                            <span className="text-gray-700">
                          No prior experience required - we'll start from the basics
                        </span>
                          </div>
                          <div className="flex items-start">
                            <Target className="h-5 w-5 text-blue-600 mr-3 mt-0.5 flex-shrink-0" />
                            <span className="text-gray-700">Willingness to learn and practice</span>
                          </div>
                        </div>

                        <h2 className="text-2xl font-bold text-gray-900 mb-4 mt-8">Who this course is for</h2>
                        <div className="space-y-3">
                          <div className="flex items-start">
                            <Users className="h-5 w-5 text-purple-600 mr-3 mt-0.5 flex-shrink-0" />
                            <span className="text-gray-700">Beginners who want to learn from scratch</span>
                          </div>
                          <div className="flex items-start">
                            <Users className="h-5 w-5 text-purple-600 mr-3 mt-0.5 flex-shrink-0" />
                            <span className="text-gray-700">Intermediate learners looking to advance their skills</span>
                          </div>
                          <div className="flex items-start">
                            <Users className="h-5 w-5 text-purple-600 mr-3 mt-0.5 flex-shrink-0" />
                            <span className="text-gray-700">Anyone interested in building practical projects</span>
                          </div>
                        </div>
                      </div>
                    </div>
                )}

                {activeTab === "curriculum" && (
                    <div>
                      <h2 className="text-2xl font-bold text-gray-900 mb-6">Course Curriculum</h2>
                      <div className="space-y-4">
                        {course.sections.map((section, sectionIndex) => (
                            <div key={section.id} className="border border-gray-200 rounded-lg overflow-hidden">
                              <div className="bg-gray-50 px-6 py-4 border-b border-gray-200">
                                <div className="flex justify-between items-center">
                                  <h3 className="text-lg font-semibold text-gray-900">
                                    Section {sectionIndex + 1}: {section.title}
                                  </h3>
                                  <span className="text-sm text-gray-600">
                              {section.lessons.length} lesson{section.lessons.length !== 1 ? "s" : ""}
                            </span>
                                </div>
                              </div>

                              <div className="divide-y divide-gray-200">
                                {section.lessons.map((lesson, lessonIndex) => (
                                    <div
                                        key={lesson.id}
                                        className={`px-6 py-4 hover:bg-gray-50 transition-colors ${
                                            lesson.isPreview ? "cursor-pointer" : ""
                                        }`}
                                        onClick={() => lesson.isPreview && handleLessonSelect(lesson)}
                                    >
                                      <div className="flex items-center justify-between">
                                        <div className="flex items-center">
                                          <div className="flex-shrink-0 mr-4">
                                            {lesson.isPreview ? (
                                                <Play className="h-5 w-5 text-blue-600" />
                                            ) : (
                                                <Lock className="h-5 w-5 text-gray-400" />
                                            )}
                                          </div>
                                          <div>
                                            <h4 className="font-medium text-gray-900">
                                              {lessonIndex + 1}. {lesson.title}
                                            </h4>
                                            {lesson.description && (
                                                <p className="text-sm text-gray-600 mt-1">{lesson.description}</p>
                                            )}
                                          </div>
                                        </div>

                                        <div className="flex items-center space-x-4">
                                          {lesson.isPreview && (
                                              <span className="px-2 py-1 bg-blue-100 text-blue-700 rounded-full text-xs font-medium">
                                      Preview
                                    </span>
                                          )}
                                          <span className="text-sm text-gray-500">{formatDuration(lesson.duration)}</span>
                                          {lesson.isPreview && <ChevronRight className="h-4 w-4 text-gray-400" />}
                                        </div>
                                      </div>
                                    </div>
                                ))}
                              </div>
                            </div>
                        ))}
                      </div>
                    </div>
                )}

                {activeTab === "instructor" && (
                    <div className="max-w-3xl">
                      <div className="flex items-start space-x-6">
                        <div className="w-24 h-24 bg-blue-600 rounded-full flex items-center justify-center flex-shrink-0">
                      <span className="text-white font-bold text-2xl">
                        {course.instructorName.charAt(0).toUpperCase()}
                      </span>
                        </div>
                        <div className="flex-1">
                          <h2 className="text-2xl font-bold text-gray-900 mb-2">{course.instructorName}</h2>
                          <p className="text-gray-600 mb-4">Course Instructor</p>

                          <div className="grid grid-cols-2 gap-4 mb-6">
                            <div className="text-center p-4 bg-gray-50 rounded-lg">
                              <div className="text-2xl font-bold text-gray-900">4.8</div>
                              <div className="text-sm text-gray-600">Instructor Rating</div>
                            </div>
                            <div className="text-center p-4 bg-gray-50 rounded-lg">
                              <div className="text-2xl font-bold text-gray-900">12,543</div>
                              <div className="text-sm text-gray-600">Students</div>
                            </div>
                          </div>

                          <div className="prose prose-gray max-w-none">
                            <p>
                              An experienced instructor with years of industry experience. Passionate about teaching and
                              helping students achieve their learning goals through practical, hands-on instruction.
                            </p>
                            <p>
                              Has worked with leading companies and brings real-world expertise to every lesson. Committed
                              to creating engaging, comprehensive courses that provide lasting value.
                            </p>
                          </div>
                        </div>
                      </div>
                    </div>
                )}

                {activeTab === "reviews" && (
                    <div>
                      <div className="flex items-center justify-between mb-6">
                        <h2 className="text-2xl font-bold text-gray-900">Student Reviews</h2>
                        <div className="flex items-center">
                          <Star className="h-5 w-5 text-yellow-400 mr-1" />
                          <span className="font-semibold">{course.rating.toFixed(1)}</span>
                          <span className="text-gray-600 ml-1">({course.totalStudents} reviews)</span>
                        </div>
                      </div>

                      <div className="space-y-6">
                        {/* Sample reviews */}
                        {[
                          {
                            name: "Sarah Johnson",
                            rating: 5,
                            date: "2 weeks ago",
                            comment:
                                "Excellent course! The instructor explains everything clearly and the projects are very practical.",
                          },
                          {
                            name: "Mike Chen",
                            rating: 5,
                            date: "1 month ago",
                            comment:
                                "Great content and well-structured lessons. I learned a lot and feel confident applying these skills.",
                          },
                          {
                            name: "Emily Davis",
                            rating: 4,
                            date: "2 months ago",
                            comment: "Very informative course with good examples. Would recommend to anyone starting out.",
                          },
                        ].map((review, index) => (
                            <div key={index} className="border-b border-gray-200 pb-6">
                              <div className="flex items-start space-x-4">
                                <div className="w-10 h-10 bg-gray-300 rounded-full flex items-center justify-center">
                                  <span className="text-gray-600 font-medium text-sm">{review.name.charAt(0)}</span>
                                </div>
                                <div className="flex-1">
                                  <div className="flex items-center justify-between mb-2">
                                    <h4 className="font-medium text-gray-900">{review.name}</h4>
                                    <span className="text-sm text-gray-500">{review.date}</span>
                                  </div>
                                  <div className="flex items-center mb-2">
                                    {[...Array(5)].map((_, i) => (
                                        <Star
                                            key={i}
                                            className={`h-4 w-4 ${
                                                i < review.rating ? "text-yellow-400 fill-current" : "text-gray-300"
                                            }`}
                                        />
                                    ))}
                                  </div>
                                  <p className="text-gray-700">{review.comment}</p>
                                </div>
                              </div>
                            </div>
                        ))}
                      </div>
                    </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </Layout>
  )
}

export default CourseDetailPage
