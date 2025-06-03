"use client"

import type React from "react"
import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { Link } from "react-router-dom"
import Layout from "../../components/layout/Layout"
import { createCourse } from "../../api/courses"
import { useAuth } from "../../context/AuthContext"
import Card, { CardContent, CardHeader } from "../../components/ui/Card"
import Button from "../../components/ui/Button"
import Input from "../../components/ui/Input"

interface Section {
    id?: string
    title: string
    order: number
    lessons: Lesson[]
}

interface Lesson {
    id?: string
    title: string
    description: string
    videoUrl: string
    duration: number
    order: number
    isPreview: boolean
}

const CreateCoursePage: React.FC = () => {
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [errorDetails, setErrorDetails] = useState<string | null>(null)
    const navigate = useNavigate()
    const { user } = useAuth()

    const [courseName, setCourseName] = useState("")
    const [courseDescription, setCourseDescription] = useState("")
    const [courseCategory, setCourseCategory] = useState("General")
    const [courseLevel, setCourseLevel] = useState("beginner")
    const [coursePrice, setCoursePrice] = useState(0)
    const [courseThumbnail, setCourseThumbnail] = useState("")

    const [sections, setSections] = useState<Section[]>([
        {
            title: "Introduction",
            order: 1,
            lessons: [
                {
                    title: "Welcome to the Course",
                    description: "Course introduction and overview",
                    videoUrl: "",
                    duration: 300,
                    order: 1,
                    isPreview: true,
                },
            ],
        },
    ])

    const addSection = () => {
        const newSection: Section = {
            title: `Section ${sections.length + 1}`,
            order: sections.length + 1,
            lessons: [],
        }
        setSections([...sections, newSection])
    }

    const updateSection = (index: number, field: keyof Section, value: any) => {
        const updatedSections = [...sections]
        updatedSections[index] = { ...updatedSections[index], [field]: value }
        setSections(updatedSections)
    }

    const removeSection = (index: number) => {
        const updatedSections = sections.filter((_, i) => i !== index)
        setSections(updatedSections)
    }

    const addLesson = (sectionIndex: number) => {
        const updatedSections = [...sections]
        const newLesson: Lesson = {
            title: `Lesson ${updatedSections[sectionIndex].lessons.length + 1}`,
            description: "",
            videoUrl: "",
            duration: 300,
            order: updatedSections[sectionIndex].lessons.length + 1,
            isPreview: false,
        }
        updatedSections[sectionIndex].lessons.push(newLesson)
        setSections(updatedSections)
    }

    const updateLesson = (sectionIndex: number, lessonIndex: number, field: keyof Lesson, value: any) => {
        const updatedSections = [...sections]
        updatedSections[sectionIndex].lessons[lessonIndex] = {
            ...updatedSections[sectionIndex].lessons[lessonIndex],
            [field]: value,
        }
        setSections(updatedSections)
    }

    const removeLesson = (sectionIndex: number, lessonIndex: number) => {
        const updatedSections = [...sections]
        updatedSections[sectionIndex].lessons = updatedSections[sectionIndex].lessons.filter((_, i) => i !== lessonIndex)
        setSections(updatedSections)
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setIsLoading(true)
        setError(null)
        setErrorDetails(null)

        try {
            const courseData = {
                name: courseName,
                title: courseName,
                description: courseDescription,
                category: courseCategory,
                level: courseLevel,
                price: coursePrice,
                thumbnail: courseThumbnail || undefined,
                instructorId: user?.id,
                sections: sections.map((section) => ({
                    title: section.title,
                    order: section.order,
                    lessons: section.lessons.map((lesson) => ({
                        title: lesson.title,
                        description: lesson.description,
                        videoUrl: lesson.videoUrl,
                        duration: lesson.duration,
                        order: lesson.order,
                        isPreview: lesson.isPreview,
                    })),
                })),
            }

            console.log("Creating course with full structure:", courseData)

            const newCourse = await createCourse(courseData)
            console.log("Created course:", newCourse)

            navigate(`/instructor/courses/${newCourse.id}/edit`)
        } catch (err: any) {
            console.error("Failed to create course:", err)

            setError(
                err.response?.data?.message ||
                "Failed to create course. Please make sure all required fields are filled correctly.",
            )

            let details = "Error details:\n"
            if (err.response) {
                details += `Status: ${err.response.status}\n`
                details += `Data: ${JSON.stringify(err.response.data, null, 2)}\n`
            } else {
                details += err.message || "Unknown error"
            }
            setErrorDetails(details)
        } finally {
            setIsLoading(false)
        }
    }

    return (
        <Layout>
            <div className="max-w-6xl mx-auto px-4 py-8">
                <h1 className="text-3xl font-bold text-gray-900 mb-8">Create New Course</h1>

                {error && (
                    <Card className="mb-6">
                        <CardContent className="p-4 bg-red-50 text-red-700">
                            <p className="font-medium">{error}</p>
                            {errorDetails && (
                                <details className="mt-2">
                                    <summary className="cursor-pointer text-sm">Show technical details</summary>
                                    <pre className="mt-2 p-2 bg-gray-800 text-white rounded text-xs overflow-auto">{errorDetails}</pre>
                                </details>
                            )}
                            <div className="mt-4">
                                <Link to="/test-api">
                                    <Button variant="outline" size="sm">
                                        Go to API Test Page
                                    </Button>
                                </Link>
                            </div>
                        </CardContent>
                    </Card>
                )}

                {user && (
                    <Card className="mb-6">
                        <CardContent className="p-4 bg-blue-50 text-blue-700">
                            <p className="font-medium">Creating course as: {user.name}</p>
                            <p className="text-sm mt-1">Your instructor ID will be automatically assigned to this course.</p>
                        </CardContent>
                    </Card>
                )}

                <form onSubmit={handleSubmit} className="space-y-6">
                    {/* Basic Course Information */}
                    <Card>
                        <CardHeader>
                            <h2 className="text-xl font-semibold">Course Details</h2>
                            <p className="text-sm text-gray-500 mt-1">Basic information about your course</p>
                        </CardHeader>
                        <CardContent className="space-y-4">
                            <Input
                                label="Course Name *"
                                type="text"
                                value={courseName}
                                onChange={(e) => setCourseName(e.target.value)}
                                required
                                fullWidth
                            />

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Description *</label>
                                <textarea
                                    className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-full"
                                    rows={4}
                                    value={courseDescription}
                                    onChange={(e) => setCourseDescription(e.target.value)}
                                    required
                                />
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Category</label>
                                    <select
                                        className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-full"
                                        value={courseCategory}
                                        onChange={(e) => setCourseCategory(e.target.value)}
                                    >
                                        <option value="General">General</option>
                                        <option value="Programming">Programming</option>
                                        <option value="Design">Design</option>
                                        <option value="Business">Business</option>
                                        <option value="Marketing">Marketing</option>
                                    </select>
                                </div>

                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-1">Level</label>
                                    <select
                                        className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-full"
                                        value={courseLevel}
                                        onChange={(e) => setCourseLevel(e.target.value)}
                                    >
                                        <option value="beginner">Beginner</option>
                                        <option value="intermediate">Intermediate</option>
                                        <option value="advanced">Advanced</option>
                                    </select>
                                </div>

                                <Input
                                    label="Price ($)"
                                    type="number"
                                    value={coursePrice}
                                    onChange={(e) => setCoursePrice(Number(e.target.value))}
                                    min="0"
                                    step="0.01"
                                    fullWidth
                                />
                            </div>

                            <Input
                                label="Thumbnail URL (optional)"
                                type="url"
                                value={courseThumbnail}
                                onChange={(e) => setCourseThumbnail(e.target.value)}
                                placeholder="https://example.com/image.jpg"
                                fullWidth
                            />
                        </CardContent>
                    </Card>

                    {/* Course Sections */}
                    <Card>
                        <CardHeader>
                            <div className="flex justify-between items-center">
                                <div>
                                    <h2 className="text-xl font-semibold">Course Content</h2>
                                    <p className="text-sm text-gray-500 mt-1">Organize your course into sections and lessons</p>
                                </div>
                                <Button type="button" onClick={addSection} variant="outline">
                                    Add Section
                                </Button>
                            </div>
                        </CardHeader>
                        <CardContent className="space-y-6">
                            {sections.map((section, sectionIndex) => (
                                <div key={sectionIndex} className="border border-gray-200 rounded-lg p-4">
                                    <div className="flex justify-between items-center mb-4">
                                        <Input
                                            label={`Section ${sectionIndex + 1} Title`}
                                            type="text"
                                            value={section.title}
                                            onChange={(e) => updateSection(sectionIndex, "title", e.target.value)}
                                            fullWidth
                                        />
                                        {sections.length > 1 && (
                                            <Button
                                                type="button"
                                                onClick={() => removeSection(sectionIndex)}
                                                variant="outline"
                                                className="ml-4 text-red-600 hover:text-red-800"
                                            >
                                                Remove Section
                                            </Button>
                                        )}
                                    </div>

                                    {/* Lessons in this section */}
                                    <div className="space-y-4">
                                        <div className="flex justify-between items-center">
                                            <h4 className="font-medium text-gray-700">Lessons</h4>
                                            <Button type="button" onClick={() => addLesson(sectionIndex)} variant="outline" size="sm">
                                                Add Lesson
                                            </Button>
                                        </div>

                                        {section.lessons.map((lesson, lessonIndex) => (
                                            <div key={lessonIndex} className="bg-gray-50 p-4 rounded border">
                                                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                                                    <Input
                                                        label="Lesson Title"
                                                        type="text"
                                                        value={lesson.title}
                                                        onChange={(e) => updateLesson(sectionIndex, lessonIndex, "title", e.target.value)}
                                                        fullWidth
                                                    />
                                                    <Input
                                                        label="Duration (seconds)"
                                                        type="number"
                                                        value={lesson.duration}
                                                        onChange={(e) =>
                                                            updateLesson(sectionIndex, lessonIndex, "duration", Number(e.target.value))
                                                        }
                                                        min="0"
                                                        fullWidth
                                                    />
                                                </div>

                                                <div className="mb-4">
                                                    <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
                                                    <textarea
                                                        className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-full"
                                                        rows={2}
                                                        value={lesson.description}
                                                        onChange={(e) => updateLesson(sectionIndex, lessonIndex, "description", e.target.value)}
                                                    />
                                                </div>

                                                <div className="mb-4">
                                                    <Input
                                                        label="Video URL"
                                                        type="url"
                                                        value={lesson.videoUrl}
                                                        onChange={(e) => updateLesson(sectionIndex, lessonIndex, "videoUrl", e.target.value)}
                                                        placeholder="https://example.com/video.mp4"
                                                        fullWidth
                                                    />
                                                </div>

                                                <div className="flex justify-between items-center">
                                                    <label className="flex items-center">
                                                        <input
                                                            type="checkbox"
                                                            checked={lesson.isPreview}
                                                            onChange={(e) => updateLesson(sectionIndex, lessonIndex, "isPreview", e.target.checked)}
                                                            className="mr-2"
                                                        />
                                                        <span className="text-sm text-gray-700">Preview lesson (free to watch)</span>
                                                    </label>

                                                    {section.lessons.length > 1 && (
                                                        <Button
                                                            type="button"
                                                            onClick={() => removeLesson(sectionIndex, lessonIndex)}
                                                            variant="outline"
                                                            size="sm"
                                                            className="text-red-600 hover:text-red-800"
                                                        >
                                                            Remove Lesson
                                                        </Button>
                                                    )}
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            ))}
                        </CardContent>
                    </Card>

                    {/* Submit Button */}
                    <div className="flex justify-end space-x-4">
                        <Link to="/instructor/dashboard">
                            <Button type="button" variant="outline">
                                Cancel
                            </Button>
                        </Link>
                        <Button type="submit" isLoading={isLoading}>
                            Create Course
                        </Button>
                    </div>
                </form>
            </div>
        </Layout>
    )
}

export default CreateCoursePage
