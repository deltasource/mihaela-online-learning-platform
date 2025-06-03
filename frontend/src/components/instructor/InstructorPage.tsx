"use client"

import type React from "react"
import { useState, useEffect } from "react"
import { Link } from "react-router-dom"
import { User, BookOpen, Video, Star, Users, Search, Filter } from "lucide-react"
import Layout from "../../components/layout/Layout"
import Card, { CardContent } from "../../components/ui/Card"
import Button from "../../components/ui/Button"
import Input from "../../components/ui/Input"

interface InstructorWithStats {
    id: string
    firstName: string
    lastName: string
    email: string
    department: string
    bio?: string
    profileImage?: string
    totalCourses: number
    totalVideos: number
    totalStudents: number
    averageRating: number
    courses: Array<{
        id: string
        name: string
        description: string
        thumbnail: string
        category: string
        level: string
        price: number
        rating: number
        totalStudents: number
        videoCount: number
    }>
}

const InstructorsPage: React.FC = () => {
    const [instructors, setInstructors] = useState<InstructorWithStats[]>([])
    const [filteredInstructors, setFilteredInstructors] = useState<InstructorWithStats[]>([])
    const [isLoading, setIsLoading] = useState(true)
    const [searchTerm, setSearchTerm] = useState("")
    const [selectedDepartment, setSelectedDepartment] = useState("")
    const [sortBy, setSortBy] = useState<"name" | "courses" | "students" | "rating">("name")
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        fetchInstructors()
    }, [])

    useEffect(() => {
        filterAndSortInstructors()
    }, [instructors, searchTerm, selectedDepartment, sortBy])

    const fetchInstructors = async () => {
        setIsLoading(true)
        try {
            const response = await fetch("/api/instructors/with-stats")
            if (!response.ok) {
                throw new Error("Failed to fetch instructors")
            }
            const data = await response.json()
            setInstructors(data)
        } catch (err) {
            console.error("Error fetching instructors:", err)
            setError("Failed to load instructors. Please try again.")
        } finally {
            setIsLoading(false)
        }
    }

    const filterAndSortInstructors = () => {
        const filtered = instructors.filter((instructor) => {
            const matchesSearch =
                instructor.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                instructor.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                instructor.department.toLowerCase().includes(searchTerm.toLowerCase())

            const matchesDepartment = !selectedDepartment || instructor.department === selectedDepartment

            return matchesSearch && matchesDepartment
        })

        filtered.sort((a, b) => {
            switch (sortBy) {
                case "courses":
                    return b.totalCourses - a.totalCourses
                case "students":
                    return b.totalStudents - a.totalStudents
                case "rating":
                    return b.averageRating - a.averageRating
                case "name":
                default:
                    return `${a.firstName} ${a.lastName}`.localeCompare(`${b.firstName} ${b.lastName}`)
            }
        })

        setFilteredInstructors(filtered)
    }

    const getDepartments = () => {
        const departments = [...new Set(instructors.map((i) => i.department))]
        return departments.sort()
    }

    const getLevelBadgeColor = (level: string) => {
        switch (level.toLowerCase()) {
            case "beginner":
                return "bg-green-100 text-green-800"
            case "intermediate":
                return "bg-yellow-100 text-yellow-800"
            case "advanced":
                return "bg-red-100 text-red-800"
            default:
                return "bg-gray-100 text-gray-800"
        }
    }

    if (isLoading) {
        return (
            <Layout>
                <div className="flex justify-center items-center min-h-screen">
                    <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-600"></div>
                </div>
            </Layout>
        )
    }

    if (error) {
        return (
            <Layout>
                <div className="max-w-4xl mx-auto px-4 py-8 text-center">
                    <h2 className="text-2xl font-bold text-red-600 mb-4">Error</h2>
                    <p className="text-gray-700 mb-8">{error}</p>
                    <Button onClick={fetchInstructors}>Try Again</Button>
                </div>
            </Layout>
        )
    }

    return (
        <Layout>
            <div className="bg-gray-50 min-h-screen py-8">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    {/* Header */}
                    <div className="mb-8">
                        <h1 className="text-3xl font-bold text-gray-900 mb-2">Our Instructors</h1>
                        <p className="text-gray-600">Discover our talented instructors and explore their courses and videos</p>
                    </div>

                    {/* Filters and Search */}
                    <Card className="mb-8">
                        <CardContent className="p-6">
                            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                                <div className="relative">
                                    <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-4 w-4" />
                                    <Input
                                        type="text"
                                        placeholder="Search instructors..."
                                        value={searchTerm}
                                        onChange={(e) => setSearchTerm(e.target.value)}
                                        className="pl-10"
                                    />
                                </div>

                                <select
                                    value={selectedDepartment}
                                    onChange={(e) => setSelectedDepartment(e.target.value)}
                                    className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                >
                                    <option value="">All Departments</option>
                                    {getDepartments().map((dept) => (
                                        <option key={dept} value={dept}>
                                            {dept}
                                        </option>
                                    ))}
                                </select>

                                <select
                                    value={sortBy}
                                    onChange={(e) => setSortBy(e.target.value as any)}
                                    className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                >
                                    <option value="name">Sort by Name</option>
                                    <option value="courses">Sort by Courses</option>
                                    <option value="students">Sort by Students</option>
                                    <option value="rating">Sort by Rating</option>
                                </select>

                                <div className="flex items-center text-sm text-gray-600">
                                    <Filter className="h-4 w-4 mr-1" />
                                    {filteredInstructors.length} instructor{filteredInstructors.length !== 1 ? "s" : ""}
                                </div>
                            </div>
                        </CardContent>
                    </Card>

                    {/* Instructors Grid */}
                    {filteredInstructors.length === 0 ? (
                        <Card>
                            <CardContent className="p-12 text-center">
                                <User className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                                <h3 className="text-lg font-medium text-gray-900 mb-2">No instructors found</h3>
                                <p className="text-gray-500">Try adjusting your search criteria or filters.</p>
                            </CardContent>
                        </Card>
                    ) : (
                        <div className="space-y-8">
                            {filteredInstructors.map((instructor) => (
                                <Card key={instructor.id} className="overflow-hidden">
                                    <CardContent className="p-0">
                                        <div className="md:flex">
                                            {/* Instructor Info */}
                                            <div className="md:w-1/3 p-6 bg-gradient-to-br from-blue-50 to-indigo-50">
                                                <div className="flex items-center mb-4">
                                                    <div className="w-16 h-16 bg-blue-600 rounded-full flex items-center justify-center text-white text-xl font-bold mr-4">
                                                        {instructor.firstName[0]}
                                                        {instructor.lastName[0]}
                                                    </div>
                                                    <div>
                                                        <h3 className="text-xl font-bold text-gray-900">
                                                            {instructor.firstName} {instructor.lastName}
                                                        </h3>
                                                        <p className="text-blue-600 font-medium">{instructor.department}</p>
                                                    </div>
                                                </div>

                                                {instructor.bio && <p className="text-gray-600 text-sm mb-4 line-clamp-3">{instructor.bio}</p>}

                                                {/* Stats */}
                                                <div className="grid grid-cols-2 gap-4 mb-4">
                                                    <div className="text-center">
                                                        <div className="flex items-center justify-center mb-1">
                                                            <BookOpen className="h-4 w-4 text-blue-600 mr-1" />
                                                            <span className="text-lg font-bold text-gray-900">{instructor.totalCourses}</span>
                                                        </div>
                                                        <p className="text-xs text-gray-500">Courses</p>
                                                    </div>
                                                    <div className="text-center">
                                                        <div className="flex items-center justify-center mb-1">
                                                            <Video className="h-4 w-4 text-green-600 mr-1" />
                                                            <span className="text-lg font-bold text-gray-900">{instructor.totalVideos}</span>
                                                        </div>
                                                        <p className="text-xs text-gray-500">Videos</p>
                                                    </div>
                                                    <div className="text-center">
                                                        <div className="flex items-center justify-center mb-1">
                                                            <Users className="h-4 w-4 text-purple-600 mr-1" />
                                                            <span className="text-lg font-bold text-gray-900">{instructor.totalStudents}</span>
                                                        </div>
                                                        <p className="text-xs text-gray-500">Students</p>
                                                    </div>
                                                    <div className="text-center">
                                                        <div className="flex items-center justify-center mb-1">
                                                            <Star className="h-4 w-4 text-yellow-500 mr-1" />
                                                            <span className="text-lg font-bold text-gray-900">
                                {instructor.averageRating.toFixed(1)}
                              </span>
                                                        </div>
                                                        <p className="text-xs text-gray-500">Rating</p>
                                                    </div>
                                                </div>

                                                <Link to={`/instructors/${instructor.id}`}>
                                                    <Button fullWidth variant="outline">
                                                        View Profile
                                                    </Button>
                                                </Link>
                                            </div>

                                            {/* Courses */}
                                            <div className="md:w-2/3 p-6">
                                                <h4 className="text-lg font-semibold text-gray-900 mb-4">
                                                    Courses ({instructor.courses.length})
                                                </h4>

                                                {instructor.courses.length === 0 ? (
                                                    <div className="text-center py-8">
                                                        <BookOpen className="h-8 w-8 text-gray-400 mx-auto mb-2" />
                                                        <p className="text-gray-500">No courses available</p>
                                                    </div>
                                                ) : (
                                                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
                                                        {instructor.courses.slice(0, 4).map((course) => (
                                                            <div
                                                                key={course.id}
                                                                className="border rounded-lg overflow-hidden hover:shadow-md transition-shadow"
                                                            >
                                                                <img
                                                                    src={course.thumbnail || "/placeholder.svg?height=120&width=200&query=course"}
                                                                    alt={course.name}
                                                                    className="w-full h-24 object-cover"
                                                                />
                                                                <div className="p-3">
                                                                    <h5 className="font-medium text-gray-900 text-sm mb-1 line-clamp-1">{course.name}</h5>
                                                                    <div className="flex items-center justify-between mb-2">
                                    <span
                                        className={`px-2 py-1 rounded-full text-xs font-medium ${getLevelBadgeColor(course.level)}`}
                                    >
                                      {course.level}
                                    </span>
                                                                        <div className="flex items-center">
                                                                            <Video className="h-3 w-3 text-gray-400 mr-1" />
                                                                            <span className="text-xs text-gray-500">{course.videoCount}</span>
                                                                        </div>
                                                                    </div>
                                                                    <div className="flex items-center justify-between">
                                                                        <div className="flex items-center">
                                                                            <Star className="h-3 w-3 text-yellow-500 mr-1" />
                                                                            <span className="text-xs text-gray-600">{course.rating.toFixed(1)}</span>
                                                                        </div>
                                                                        <span className="text-xs text-gray-500">{course.totalStudents} students</span>
                                                                    </div>
                                                                    <Link to={`/courses/${course.id}`}>
                                                                        <Button size="sm" fullWidth className="mt-2">
                                                                            View Course
                                                                        </Button>
                                                                    </Link>
                                                                </div>
                                                            </div>
                                                        ))}
                                                    </div>
                                                )}

                                                {instructor.courses.length > 4 && (
                                                    <div className="mt-4 text-center">
                                                        <Link to={`/instructors/${instructor.id}/courses`}>
                                                            <Button variant="outline">View All {instructor.courses.length} Courses</Button>
                                                        </Link>
                                                    </div>
                                                )}
                                            </div>
                                        </div>
                                    </CardContent>
                                </Card>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </Layout>
    )
}

export default InstructorsPage
