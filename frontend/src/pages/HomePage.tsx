"use client"

import type React from "react"
import { useState, useEffect } from "react"
import { Link } from "react-router-dom"
import { BookOpen, Award, Users, Search } from "lucide-react"
import Layout from "../components/layout/Layout"
import CourseList from "../components/courses/CourseList"
import Button from "../components/ui/Button"
import type { Course } from "../types"
import { getAllCourses } from "../api/courses"

const HomePage: React.FC = () => {
  const [featuredCourses, setFeaturedCourses] = useState<Course[]>([])
  const [popularCourses, setPopularCourses] = useState<Course[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [searchQuery, setSearchQuery] = useState("")

  useEffect(() => {
    const fetchCourses = async () => {
      try {
        const courses = await getAllCourses()
        console.log("Fetched courses:", courses) // Debug log
        setFeaturedCourses(courses.slice(0, 4))
        setPopularCourses(courses.filter((course) => course.totalStudents > 50).slice(0, 4))
      } catch (error) {
        console.error("Failed to fetch courses:", error)
      } finally {
        setIsLoading(false)
      }
    }

    fetchCourses()
  }, [])

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault()
    if (searchQuery.trim()) {
      window.location.href = `/search?q=${encodeURIComponent(searchQuery)}`
    }
  }

  // Mock categories for the homepage
  const categories = [
    { id: 1, name: "Web Development", icon: "💻", count: 120 },
    { id: 2, name: "Data Science", icon: "📊", count: 85 },
    { id: 3, name: "Mobile Development", icon: "📱", count: 65 },
    { id: 4, name: "Design", icon: "🎨", count: 95 },
    { id: 5, name: "Business", icon: "💼", count: 110 },
    { id: 6, name: "Marketing", icon: "📈", count: 75 },
  ]

  return (
      <Layout>
        {/* Hero Section */}
        <section className="bg-gradient-to-r from-blue-600 to-indigo-700 text-white py-20">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex flex-col md:flex-row items-center">
              <div className="md:w-1/2 mb-10 md:mb-0">
                <h1 className="text-4xl md:text-5xl font-bold mb-4">Learn Without Limits</h1>
                <p className="text-xl mb-8">
                  Access thousands of free courses taught by expert instructors. Start learning today!
                </p>
                <form onSubmit={handleSearch} className="flex w-full max-w-md">
                  <div className="relative flex-grow">
                    <input
                        type="text"
                        placeholder="What do you want to learn?"
                        className="w-full px-4 py-3 rounded-l-md text-gray-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />
                    <button
                        type="submit"
                        className="absolute right-0 top-0 h-full px-4 bg-blue-500 rounded-r-md flex items-center justify-center"
                    >
                      <Search className="h-5 w-5" />
                    </button>
                  </div>
                </form>
              </div>
              <div className="md:w-1/2 flex justify-center">
                <img
                    src="https://images.pexels.com/photos/5905709/pexels-photo-5905709.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
                    alt="Students learning online"
                    className="rounded-lg shadow-xl max-w-full h-auto"
                    style={{ maxHeight: "400px" }}
                />
              </div>
            </div>
          </div>
        </section>

        {/* Stats Section */}
        <section className="py-12 bg-gray-50">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
              <div className="bg-white p-6 rounded-lg shadow-md text-center">
                <div className="flex justify-center mb-4">
                  <BookOpen className="h-12 w-12 text-blue-600" />
                </div>
                <h3 className="text-3xl font-bold text-gray-800">1000+</h3>
                <p className="text-gray-600">Free Courses</p>
              </div>
              <div className="bg-white p-6 rounded-lg shadow-md text-center">
                <div className="flex justify-center mb-4">
                  <Users className="h-12 w-12 text-blue-600" />
                </div>
                <h3 className="text-3xl font-bold text-gray-800">50,000+</h3>
                <p className="text-gray-600">Active Students</p>
              </div>
              <div className="bg-white p-6 rounded-lg shadow-md text-center">
                <div className="flex justify-center mb-4">
                  <Award className="h-12 w-12 text-blue-600" />
                </div>
                <h3 className="text-3xl font-bold text-gray-800">200+</h3>
                <p className="text-gray-600">Expert Instructors</p>
              </div>
            </div>
          </div>
        </section>

        {/* Featured Courses Section */}
        <section className="py-16">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex justify-between items-center mb-8">
              <h2 className="text-3xl font-bold text-gray-800">Featured Courses</h2>
              <Link to="/courses">
                <Button variant="outline">View All Courses</Button>
              </Link>
            </div>

            {isLoading ? (
                <div className="flex justify-center py-12">
                  <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-600"></div>
                </div>
            ) : (
                <CourseList courses={featuredCourses} />
            )}
          </div>
        </section>

        {/* Categories Section */}
        <section className="py-16 bg-gray-50">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <h2 className="text-3xl font-bold text-gray-800 mb-8 text-center">Browse Top Categories</h2>
            <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-6">
              {categories.map((category) => (
                  <Link
                      key={category.id}
                      to={`/categories/${category.name.toLowerCase().replace(" ", "-")}`}
                      className="bg-white rounded-lg shadow-md p-6 text-center transition-transform hover:scale-105"
                  >
                    <div className="text-4xl mb-3">{category.icon}</div>
                    <h3 className="font-semibold text-gray-800 mb-1">{category.name}</h3>
                    <p className="text-sm text-gray-600">{category.count} courses</p>
                  </Link>
              ))}
            </div>
          </div>
        </section>

        {/* Popular Courses Section */}
        <section className="py-16">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex justify-between items-center mb-8">
              <h2 className="text-3xl font-bold text-gray-800">Most Popular</h2>
              <Link to="/courses?sort=popular">
                <Button variant="outline">View All</Button>
              </Link>
            </div>

            {isLoading ? (
                <div className="flex justify-center py-12">
                  <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-600"></div>
                </div>
            ) : (
                <CourseList courses={popularCourses} />
            )}
          </div>
        </section>

        {/* Testimonials Section */}
        <section className="py-16">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <h2 className="text-3xl font-bold text-gray-800 mb-12 text-center">What Our Students Say</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
              {[1, 2, 3].map((i) => (
                  <div key={`testimonial-${i}`} className="bg-white p-6 rounded-lg shadow-md">
                    <div className="flex items-center mb-4">
                      <div className="h-12 w-12 rounded-full bg-gray-200 flex items-center justify-center text-gray-600 font-bold text-xl">
                        {String.fromCharCode(64 + i)}
                      </div>
                      <div className="ml-4">
                        <h4 className="font-semibold">Student {i}</h4>
                        <p className="text-sm text-gray-600">Web Development</p>
                      </div>
                    </div>
                    <p className="text-gray-700">
                      "The courses on LearnFree are amazing! I've learned so much in such a short time. The instructors are
                      knowledgeable and the content is well-structured."
                    </p>
                    <div className="mt-4 flex">
                      {[...Array(5)].map((_, j) => (
                          <svg
                              key={`star-${i}-${j}`}
                              className={`h-5 w-5 ${j < 5 ? "text-yellow-500" : "text-gray-300"}`}
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
                  </div>
              ))}
            </div>
          </div>
        </section>

        {/* Become Instructor Section */}
        <section className="py-16 bg-blue-600 text-white">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex flex-col md:flex-row items-center">
              <div className="md:w-1/2 mb-8 md:mb-0">
                <h2 className="text-3xl font-bold mb-4">Become an Instructor</h2>
                <p className="text-xl mb-6">
                  Share your knowledge with thousands of students around the world. Create engaging courses and help
                  others learn.
                </p>
                <Link to="/become-instructor">
                  <Button variant="outline" className="border-white text-white hover:bg-white hover:text-blue-600">
                    Start Teaching Today
                  </Button>
                </Link>
              </div>
              <div className="md:w-1/2 flex justify-center">
                <img
                    src="https://images.pexels.com/photos/3184360/pexels-photo-3184360.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
                    alt="Instructor teaching"
                    className="rounded-lg shadow-xl max-w-full h-auto"
                    style={{ maxHeight: "400px" }}
                />
              </div>
            </div>
          </div>
        </section>

        {/* CTA Section */}
        <section className="py-16 bg-gray-50">
          <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
            <h2 className="text-3xl font-bold text-gray-800 mb-4">Ready to Start Learning?</h2>
            <p className="text-xl text-gray-600 mb-8">
              Join thousands of students who are already learning on our platform.
            </p>
            <Link to="/register">
              <Button size="lg">Sign Up for Free</Button>
            </Link>
          </div>
        </section>
      </Layout>
  )
}

export default HomePage
