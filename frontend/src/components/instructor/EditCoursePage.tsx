"use client"

import type React from "react"
import { useState, useEffect } from "react"
import { useParams, useNavigate } from "react-router-dom"
import {
    Trash2,
    Upload,
    Star,
    Users,
    DollarSign,
    Calendar,
    Tag,
    BarChart3,
    Camera,
    X,
    Play,
    Clock,
    AlertCircle,
} from "lucide-react"
import Layout from "../../components/layout/Layout"
import CourseForm from "../../components/courses/CourseForm"
import VideoUploader from "../../components/courses/VideoUploader"
import Button from "../../components/ui/Button"
import Input from "../../components/ui/Input"
import Card, { CardContent, CardHeader } from "../../components/ui/Card"
import { getCourseById, updateCourse, deleteCourse } from "../../api/courses"
import { getVideosByCourse, deleteVideo } from "../../api/videos"
import type { VideoDTO } from "../../api/videos"

interface ExtendedVideoDTO extends VideoDTO {
    title?: string
    description?: string
    duration?: number
    fileSize?: number
    uploadedAt?: string
    thumbnailUrl?: string
    status?: "processing" | "ready" | "error"
}

const EditCoursePage: React.FC = () => {
    const { id } = useParams<{ id: string }>()
    const navigate = useNavigate()
    const [course, setCourse] = useState<any>(null)
    const [videos, setVideos] = useState<ExtendedVideoDTO[]>([])
    const [isLoading, setIsLoading] = useState(true)
    const [isSaving, setIsSaving] = useState(false)
    const [isDeleting, setIsDeleting] = useState(false)
    const [isUploadingThumbnail, setIsUploadingThumbnail] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [activeTab, setActiveTab] = useState<"details" | "videos" | "analytics">("details")
    const [thumbnailUrl, setThumbnailUrl] = useState("")
    const [showThumbnailEditor, setShowThumbnailEditor] = useState(false)
    const [uploadProgress, setUploadProgress] = useState<{ [key: string]: number }>({})
    const [thumbnailPreview, setThumbnailPreview] = useState<string | null>(null)

    useEffect(() => {
        const fetchCourseData = async () => {
            if (!id) return

            setIsLoading(true)
            try {
                const courseData = await getCourseById(id)
                setCourse(courseData)
                setThumbnailUrl(courseData.thumbnail || "")
                const videosData = await getVideosByCourse(id)
                console.log("Fetched videos:", videosData) // Debug log
                setVideos(videosData || [])
            } catch (err) {
                console.error("Failed to fetch course data:", err)
                setError("Failed to load course data. Please try again.")
            } finally {
                setIsLoading(false)
            }
        }

        fetchCourseData()
    }, [id])

    const handleUpdateCourse = async (data: any) => {
        if (!id) return

        setIsSaving(true)
        try {
            const updatedCourse = await updateCourse(id, data)
            setCourse(updatedCourse)
            setError(null)
            return updatedCourse
        } catch (err: any) {
            console.error("Failed to update course:", err)
            setError(err.message || "Failed to update course. Please try again.")
            throw err
        } finally {
            setIsSaving(false)
        }
    }

    const handleThumbnailUpload = async (event: React.ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0]
        if (!file || !id) return

        if (!file.type.startsWith("image/")) {
            setError("Please select a valid image file (JPG, PNG, GIF).")
            return
        }

        if (file.size > 5 * 1024 * 1024) {
            setError("Image file size must be less than 5MB.")
            return
        }

        const reader = new FileReader()
        reader.onload = (e) => {
            setThumbnailPreview(e.target?.result as string)
        }
        reader.readAsDataURL(file)

        setIsUploadingThumbnail(true)
        setError(null)

        try {
            const formData = new FormData()
            formData.append("file", file)
            formData.append("type", "thumbnail")
            formData.append("courseId", id)

            console.log("Uploading thumbnail for course:", id)

            const response = await fetch("/api/upload/thumbnail", {
                method: "POST",
                body: formData,
            })

            console.log("Upload response status:", response.status)

            if (!response.ok) {
                let errorMessage = "Failed to upload thumbnail"
                try {
                    const errorData = await response.json()
                    errorMessage = errorData.error || errorMessage
                } catch (parseError) {
                    console.error("Failed to parse error response:", parseError)
                }
                throw new Error(errorMessage)
            }

            const result = await response.json()
            console.log("Upload result:", result)

            if (!result.success || !result.url) {
                throw new Error("Upload succeeded but no URL returned")
            }

            const newThumbnailUrl = result.url

            const updatedCourse = await updateCourse(id, {
                ...course,
                thumbnail: newThumbnailUrl,
            })

            setCourse(updatedCourse)
            setThumbnailUrl(newThumbnailUrl)
            setShowThumbnailEditor(false)
            setThumbnailPreview(null)
            setError(null)

            event.target.value = ""
        } catch (err: any) {
            console.error("Failed to upload thumbnail:", err)
            setError(err.message || "Failed to upload thumbnail. Please try again.")
            setThumbnailPreview(null)
        } finally {
            setIsUploadingThumbnail(false)
        }
    }

    const handleThumbnailUrlUpdate = async () => {
        if (!id || !thumbnailUrl) return

        try {
            new URL(thumbnailUrl)
        } catch {
            setError("Please enter a valid URL")
            return
        }

        setIsUploadingThumbnail(true)
        setError(null)

        try {
            const updatedCourse = await updateCourse(id, {
                ...course,
                thumbnail: thumbnailUrl,
            })

            setCourse(updatedCourse)
            setShowThumbnailEditor(false)
            setError(null)
        } catch (err: any) {
            console.error("Failed to update thumbnail URL:", err)
            setError(err.message || "Failed to update thumbnail. Please try again.")
        } finally {
            setIsUploadingThumbnail(false)
        }
    }

    const handleDeleteCourse = async () => {
        if (!id) return

        if (window.confirm("Are you sure you want to delete this course? This action cannot be undone.")) {
            setIsDeleting(true)
            try {
                await deleteCourse(id)
                navigate("/instructor/dashboard")
            } catch (err: any) {
                console.error("Failed to delete course:", err)
                setError(err.message || "Failed to delete course. Please try again.")
            } finally {
                setIsDeleting(false)
            }
        }
    }

    const handleDeleteVideo = async (videoId: string) => {
        if (window.confirm("Are you sure you want to delete this video?")) {
            try {
                await deleteVideo(videoId)
                setVideos((prevVideos) => prevVideos.filter((video) => video.id !== videoId && video.videoId !== videoId))
                setError(null)
            } catch (err: any) {
                console.error("Failed to delete video:", err)
                setError(err.message || "Failed to delete video. Please try again.")
                handleVideoUploadComplete()
            }
        }
    }

    const handleVideoUploadComplete = async (newVideo?: ExtendedVideoDTO) => {
        if (!id) return

        try {
            if (newVideo) {
                setVideos((prevVideos) => [...prevVideos, newVideo])
            }
            const videosData = await getVideosByCourse(id)
            setVideos(videosData || [])
            setError(null)
        } catch (err) {
            console.error("Failed to refresh videos:", err)
            setError("Failed to refresh video list. Please try again.")
        }
    }

    const formatDate = (dateString: string) => {
        return new Date(dateString).toLocaleDateString("en-US", {
            year: "numeric",
            month: "long",
            day: "numeric",
        })
    }

    const formatDuration = (seconds: number) => {
        const minutes = Math.floor(seconds / 60)
        const remainingSeconds = seconds % 60
        return `${minutes}:${remainingSeconds.toString().padStart(2, "0")}`
    }

    const formatFileSize = (bytes: number) => {
        if (bytes === 0) return "0 Bytes"
        const k = 1024
        const sizes = ["Bytes", "KB", "MB", "GB"]
        const i = Math.floor(Math.log(bytes) / Math.log(k))
        return Number.parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + " " + sizes[i]
    }

    const getLevelColor = (level: string) => {
        switch (level?.toLowerCase()) {
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

    const getVideoStatusColor = (status: string) => {
        switch (status?.toLowerCase()) {
            case "ready":
                return "bg-green-100 text-green-800"
            case "processing":
                return "bg-yellow-100 text-yellow-800"
            case "error":
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

    if (error && !course) {
        return (
            <Layout>
                <div className="max-w-4xl mx-auto px-4 py-8 text-center">
                    <h2 className="text-2xl font-bold text-red-600 mb-4">Error</h2>
                    <p className="text-gray-700 mb-8">{error || "Course not found"}</p>
                    <Button onClick={() => navigate("/instructor/dashboard")}>Back to Dashboard</Button>
                </div>
            </Layout>
        )
    }

    return (
        <Layout>
            <div className="max-w-7xl mx-auto px-4 py-8">
                {/* Header */}
                <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-8 gap-4">
                    <div>
                        <h1 className="text-3xl font-bold text-gray-900">Edit Course</h1>
                        <p className="text-gray-600 mt-1">{course?.title || course?.name}</p>
                    </div>
                    <div className="flex gap-3">
                        <Button variant="secondary" onClick={() => navigate("/instructor/dashboard")}>
                            Back to Dashboard
                        </Button>
                        <Button variant="danger" onClick={handleDeleteCourse} isLoading={isDeleting}>
                            Delete Course
                        </Button>
                    </div>
                </div>

                {/* Error Display */}
                {error && (
                    <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-md flex items-start gap-3">
                        <AlertCircle className="h-5 w-5 text-red-600 flex-shrink-0 mt-0.5" />
                        <div className="flex-1">
                            <p className="text-red-700">{error}</p>
                            <button onClick={() => setError(null)} className="mt-2 text-red-600 hover:text-red-800 text-sm underline">
                                Dismiss
                            </button>
                        </div>
                    </div>
                )}

                {/* Course Overview Cards */}
                <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
                    <Card>
                        <CardContent className="p-4">
                            <div className="flex items-center">
                                <Users className="h-8 w-8 text-blue-600" />
                                <div className="ml-3">
                                    <p className="text-sm font-medium text-gray-500">Students</p>
                                    <p className="text-2xl font-bold text-gray-900">{course?.totalStudents || 0}</p>
                                </div>
                            </div>
                        </CardContent>
                    </Card>

                    <Card>
                        <CardContent className="p-4">
                            <div className="flex items-center">
                                <Star className="h-8 w-8 text-yellow-600" />
                                <div className="ml-3">
                                    <p className="text-sm font-medium text-gray-500">Rating</p>
                                    <p className="text-2xl font-bold text-gray-900">{course?.rating || 0}/5</p>
                                </div>
                            </div>
                        </CardContent>
                    </Card>

                    <Card>
                        <CardContent className="p-4">
                            <div className="flex items-center">
                                <DollarSign className="h-8 w-8 text-green-600" />
                                <div className="ml-3">
                                    <p className="text-sm font-medium text-gray-500">Price</p>
                                    <p className="text-2xl font-bold text-gray-900">${course?.price || 0}</p>
                                </div>
                            </div>
                        </CardContent>
                    </Card>

                    <Card>
                        <CardContent className="p-4">
                            <div className="flex items-center">
                                <Upload className="h-8 w-8 text-purple-600" />
                                <div className="ml-3">
                                    <p className="text-sm font-medium text-gray-500">Videos</p>
                                    <p className="text-2xl font-bold text-gray-900">{videos.length}</p>
                                </div>
                            </div>
                        </CardContent>
                    </Card>
                </div>

                {/* Course Details */}
                <div className="mb-6">
                    <Card>
                        <CardContent className="p-6">
                            <div className="flex flex-wrap gap-4 items-center">
                                <div className="flex items-center gap-2">
                                    <Tag className="h-4 w-4 text-gray-500" />
                                    <span className="text-sm text-gray-600">Category:</span>
                                    <span className="px-2 py-1 bg-blue-100 text-blue-800 rounded-full text-xs font-medium">
                    {course?.category || "General"}
                  </span>
                                </div>
                                <div className="flex items-center gap-2">
                                    <BarChart3 className="h-4 w-4 text-gray-500" />
                                    <span className="text-sm text-gray-600">Level:</span>
                                    <span className={`px-2 py-1 rounded-full text-xs font-medium ${getLevelColor(course?.level)}`}>
                    {course?.level || "Beginner"}
                  </span>
                                </div>
                                <div className="flex items-center gap-2">
                                    <Calendar className="h-4 w-4 text-gray-500" />
                                    <span className="text-sm text-gray-600">Created:</span>
                                    <span className="text-sm text-gray-900">
                    {course?.createdAt ? formatDate(course.createdAt) : "Unknown"}
                  </span>
                                </div>
                            </div>
                        </CardContent>
                    </Card>
                </div>

                {/* Tabs */}
                <div className="border-b border-gray-200 mb-6">
                    <nav className="-mb-px flex space-x-8">
                        <button
                            onClick={() => setActiveTab("details")}
                            className={`py-2 px-1 border-b-2 font-medium text-sm ${
                                activeTab === "details"
                                    ? "border-blue-500 text-blue-600"
                                    : "border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300"
                            }`}
                        >
                            Course Details
                        </button>
                        <button
                            onClick={() => setActiveTab("videos")}
                            className={`py-2 px-1 border-b-2 font-medium text-sm ${
                                activeTab === "videos"
                                    ? "border-blue-500 text-blue-600"
                                    : "border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300"
                            }`}
                        >
                            Videos ({videos.length})
                        </button>
                        <button
                            onClick={() => setActiveTab("analytics")}
                            className={`py-2 px-1 border-b-2 font-medium text-sm ${
                                activeTab === "analytics"
                                    ? "border-blue-500 text-blue-600"
                                    : "border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300"
                            }`}
                        >
                            Analytics
                        </button>
                    </nav>
                </div>

                {/* Tab Content */}
                {activeTab === "details" && (
                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                        <div>
                            <Card>
                                <CardHeader>
                                    <h3 className="text-lg font-medium text-gray-900">Edit Course Information</h3>
                                </CardHeader>
                                <CardContent>
                                    <CourseForm
                                        initialData={course}
                                        onSubmit={handleUpdateCourse}
                                        isLoading={isSaving}
                                        submitLabel="Save Changes"
                                        hideInstructorField={true}
                                    />
                                </CardContent>
                            </Card>
                        </div>

                        <div>
                            <Card>
                                <CardHeader>
                                    <div className="flex justify-between items-center">
                                        <h3 className="text-lg font-medium text-gray-900">Course Thumbnail</h3>
                                        <Button variant="secondary" size="sm" onClick={() => setShowThumbnailEditor(!showThumbnailEditor)}>
                                            <Camera className="h-4 w-4 mr-2" />
                                            Edit
                                        </Button>
                                    </div>
                                </CardHeader>
                                <CardContent>
                                    <div className="space-y-4">
                                        <img
                                            src={
                                                thumbnailPreview ||
                                                course?.thumbnail ||
                                                "/placeholder.svg?height=200&width=300&query=course+thumbnail"
                                            }
                                            alt={course?.title || "Course thumbnail"}
                                            className="w-full h-48 object-cover rounded-lg border"
                                            onError={(e) => {
                                                const target = e.target as HTMLImageElement
                                                target.src = "/placeholder.svg?height=200&width=300"
                                            }}
                                        />

                                        {showThumbnailEditor && (
                                            <div className="space-y-4 p-4 bg-gray-50 rounded-lg">
                                                <div className="flex justify-between items-center">
                                                    <h4 className="font-medium text-gray-900">Update Thumbnail</h4>
                                                    <button
                                                        onClick={() => {
                                                            setShowThumbnailEditor(false)
                                                            setThumbnailPreview(null)
                                                        }}
                                                        className="text-gray-400 hover:text-gray-600"
                                                    >
                                                        <X className="h-4 w-4" />
                                                    </button>
                                                </div>

                                                <div>
                                                    <label className="block text-sm font-medium text-gray-700 mb-2">Upload New Image</label>
                                                    <input
                                                        type="file"
                                                        accept="image/*"
                                                        onChange={handleThumbnailUpload}
                                                        className="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
                                                        disabled={isUploadingThumbnail}
                                                    />
                                                    <p className="text-xs text-gray-500 mt-1">
                                                        Max file size: 5MB. Supported formats: JPG, PNG, GIF
                                                    </p>
                                                    {isUploadingThumbnail && (
                                                        <div className="mt-2">
                                                            <div className="flex items-center gap-2">
                                                                <div className="animate-spin rounded-full h-4 w-4 border-t-2 border-b-2 border-blue-600"></div>
                                                                <span className="text-sm text-gray-600">Uploading...</span>
                                                            </div>
                                                        </div>
                                                    )}
                                                </div>

                                                <div className="text-center text-gray-500">or</div>

                                                <div>
                                                    <label className="block text-sm font-medium text-gray-700 mb-2">Image URL</label>
                                                    <div className="flex gap-2">
                                                        <Input
                                                            type="url"
                                                            value={thumbnailUrl}
                                                            onChange={(e) => setThumbnailUrl(e.target.value)}
                                                            placeholder="https://example.com/image.jpg"
                                                            className="flex-1"
                                                        />
                                                        <Button
                                                            onClick={handleThumbnailUrlUpdate}
                                                            isLoading={isUploadingThumbnail}
                                                            disabled={!thumbnailUrl || thumbnailUrl === course?.thumbnail}
                                                        >
                                                            Update
                                                        </Button>
                                                    </div>
                                                </div>
                                            </div>
                                        )}

                                        <p className="text-sm text-gray-600">
                                            Current thumbnail URL: {course?.thumbnail || "Default placeholder"}
                                        </p>
                                    </div>
                                </CardContent>
                            </Card>
                        </div>
                    </div>
                )}

                {activeTab === "videos" && (
                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                        <div>
                            <VideoUploader
                                courseId={id || ""}
                                onUploadComplete={handleVideoUploadComplete}
                                onError={(error) => setError(error)}
                                onProgress={(progress) => setUploadProgress(progress)}
                            />
                        </div>

                        <div>
                            <Card>
                                <CardHeader>
                                    <div className="flex justify-between items-center">
                                        <h3 className="text-lg font-medium text-gray-900">Course Videos</h3>
                                        <span className="text-sm text-gray-500">
                      {videos.length} video{videos.length !== 1 ? "s" : ""}
                    </span>
                                    </div>
                                </CardHeader>
                                <CardContent>
                                    {videos.length === 0 ? (
                                        <div className="text-center py-8">
                                            <Upload className="mx-auto h-12 w-12 text-gray-400" />
                                            <p className="text-gray-500 mt-2">No videos uploaded yet</p>
                                            <p className="text-sm text-gray-400">Upload your first video to get started</p>
                                        </div>
                                    ) : (
                                        <div className="space-y-4">
                                            {videos.map((video, index) => (
                                                <div
                                                    key={video.id || video.videoId || index}
                                                    className="border rounded-lg p-4 hover:bg-gray-50 transition-colors"
                                                >
                                                    <div className="flex justify-between items-start">
                                                        <div className="flex-1 min-w-0">
                                                            <div className="flex items-center gap-2 mb-2">
                                                                <Play className="h-4 w-4 text-blue-600 flex-shrink-0" />
                                                                <h4 className="text-sm font-medium text-gray-900 truncate">
                                                                    {video.title || video.fileName || `Video ${index + 1}`}
                                                                </h4>
                                                                {video.status && (
                                                                    <span
                                                                        className={`px-2 py-1 rounded-full text-xs font-medium ${getVideoStatusColor(video.status)}`}
                                                                    >
                                    {video.status}
                                  </span>
                                                                )}
                                                            </div>

                                                            {video.description && (
                                                                <p className="text-sm text-gray-600 mb-2 line-clamp-2">{video.description}</p>
                                                            )}

                                                            <div className="flex flex-wrap gap-4 text-xs text-gray-500">
                                                                {video.duration && (
                                                                    <div className="flex items-center gap-1">
                                                                        <Clock className="h-3 w-3" />
                                                                        <span>{formatDuration(video.duration)}</span>
                                                                    </div>
                                                                )}
                                                                {video.fileSize && <span>{formatFileSize(video.fileSize)}</span>}
                                                                {video.uploadedAt && <span>Uploaded {formatDate(video.uploadedAt)}</span>}
                                                            </div>

                                                            {video.filePath && (
                                                                <p className="text-xs text-gray-400 mt-1 font-mono truncate">{video.filePath}</p>
                                                            )}
                                                        </div>

                                                        <div className="flex items-center gap-2 ml-4">
                                                            {video.thumbnailUrl && (
                                                                <img
                                                                    src={video.thumbnailUrl || "/placeholder.svg"}
                                                                    alt="Video thumbnail"
                                                                    className="w-16 h-12 object-cover rounded border"
                                                                    onError={(e) => {
                                                                        const target = e.target as HTMLImageElement
                                                                        target.style.display = "none"
                                                                    }}
                                                                />
                                                            )}
                                                            <button
                                                                onClick={() => handleDeleteVideo(video.id || video.videoId || "")}
                                                                className="text-red-600 hover:text-red-800 p-1 rounded hover:bg-red-50 transition-colors"
                                                                title="Delete video"
                                                            >
                                                                <Trash2 size={16} />
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
                                            ))}
                                        </div>
                                    )}
                                </CardContent>
                            </Card>
                        </div>
                    </div>
                )}

                {activeTab === "analytics" && (
                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                        <Card>
                            <CardHeader>
                                <h3 className="text-lg font-medium text-gray-900">Course Performance</h3>
                            </CardHeader>
                            <CardContent>
                                <div className="space-y-4">
                                    <div className="flex justify-between items-center">
                                        <span className="text-sm text-gray-600">Total Enrollments</span>
                                        <span className="font-medium">{course?.totalStudents || 0}</span>
                                    </div>
                                    <div className="flex justify-between items-center">
                                        <span className="text-sm text-gray-600">Average Rating</span>
                                        <div className="flex items-center">
                                            <Star className="h-4 w-4 text-yellow-400 mr-1" />
                                            <span className="font-medium">{course?.rating || 0}/5</span>
                                        </div>
                                    </div>
                                    <div className="flex justify-between items-center">
                                        <span className="text-sm text-gray-600">Total Videos</span>
                                        <span className="font-medium">{videos.length}</span>
                                    </div>
                                    <div className="flex justify-between items-center">
                                        <span className="text-sm text-gray-600">Total Duration</span>
                                        <span className="font-medium">
                      {formatDuration(videos.reduce((total, video) => total + (video.duration || 0), 0))}
                    </span>
                                    </div>
                                    <div className="flex justify-between items-center">
                                        <span className="text-sm text-gray-600">Revenue Potential</span>
                                        <span className="font-medium">
                      ${((course?.price || 0) * (course?.totalStudents || 0)).toFixed(2)}
                    </span>
                                    </div>
                                </div>
                            </CardContent>
                        </Card>

                        <Card>
                            <CardHeader>
                                <h3 className="text-lg font-medium text-gray-900">Course Information</h3>
                            </CardHeader>
                            <CardContent>
                                <div className="space-y-4">
                                    <div>
                                        <span className="text-sm text-gray-600">Course ID</span>
                                        <p className="font-mono text-xs text-gray-900 mt-1">{course?.id}</p>
                                    </div>
                                    <div>
                                        <span className="text-sm text-gray-600">Created Date</span>
                                        <p className="text-sm text-gray-900 mt-1">
                                            {course?.createdAt ? formatDate(course.createdAt) : "Unknown"}
                                        </p>
                                    </div>
                                    <div>
                                        <span className="text-sm text-gray-600">Last Updated</span>
                                        <p className="text-sm text-gray-900 mt-1">
                                            {course?.updatedAt ? formatDate(course.updatedAt) : "Unknown"}
                                        </p>
                                    </div>
                                    <div>
                                        <span className="text-sm text-gray-600">Instructor ID</span>
                                        <p className="font-mono text-xs text-gray-900 mt-1">{course?.instructorId}</p>
                                    </div>
                                </div>
                            </CardContent>
                        </Card>
                    </div>
                )}
            </div>
        </Layout>
    )
}

export default EditCoursePage