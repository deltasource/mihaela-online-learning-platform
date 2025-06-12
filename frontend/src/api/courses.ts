import api from "./index"
import type { Course, CourseDetails, Enrollment } from "../types"

const transformCourseDTO = (courseDTO: any): Course => {
  return {
    id: courseDTO.id || "",
    title: courseDTO.name || "Untitled Course",
    description: courseDTO.description || "",
    thumbnail:
        "https://images.pexels.com/photos/1181677/pexels-photo-1181677.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    instructorId: courseDTO.instructorId || "",
    instructorName: "Unknown Instructor",
    category: "General",
    level: "beginner",
    price: 0,
    rating: 4.5,
    totalStudents: courseDTO.studentIds?.length || 0,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
  }
}

const transformToBackendCourseDTO = (course: Partial<Course>): any => {
  console.log("Transforming course data to backend format:", course)

  const dto: any = {
    name: course.title || course.name,
    description: course.description || "",
    instructorId: course.instructorId || undefined,
  }

  if (course.studentIds) {
    dto.studentIds = course.studentIds
  } else {
    dto.studentIds = []
  }

  console.log("Transformed DTO:", dto)
  return dto
}

export const getAllCourses = async (): Promise<Course[]> => {
  try {
    const response = await api.get("/courses")
    console.log("Backend response:", response.data)

    if (Array.isArray(response.data)) {
      return response.data.map(transformCourseDTO)
    }
    return []
  } catch (error) {
    console.error("Error fetching courses:", error)
    throw error
  }
}

export const getCourseById = async (id: string): Promise<CourseDetails> => {
  try {
    const response = await api.get(`/courses/${id}`)
    const course = transformCourseDTO(response.data)

    // Fetch videos for this course
    const videosResponse = await api.get(`/videos/courses/${id}`)
    const videos = videosResponse.data || []

    const sections = [
      {
        id: "1",
        title: "Course Content",
        courseId: id,
        order: 1,
        lessons: videos.map((video: any, index: number) => ({
          id: video.id || video.videoId || `video-${index}`,
          title: video.fileName || `Video ${index + 1}`,
          description: video.description || "Video lesson",
          videoUrl: video.filePath || "",
          duration: video.duration || 300,
          sectionId: "1",
          order: index + 1,
          isPreview: index === 0,
        })),
      },
    ]

    return {
      ...course,
      sections,
    }
  } catch (error) {
    console.error("Error fetching course details:", error)
    throw error
  }
}

export const createCourse = async (courseData: Partial<Course>): Promise<Course> => {
  try {
    console.log("Creating course with data:", courseData)

    const courseDTO = transformToBackendCourseDTO(courseData)
    console.log("Transformed course DTO:", courseDTO)

    const response = await api.post("/courses", courseDTO)
    console.log("Course creation response:", response.data)

    return transformCourseDTO(response.data)
  } catch (error: any) {
    console.error("Error creating course:", error)
    if (error.response) {
      console.error("Response status:", error.response.status)
      console.error("Response data:", error.response.data)
    }
    throw error
  }
}

export const updateCourse = async (id: string, courseData: Partial<Course>): Promise<Course> => {
  try {
    const courseDTO = transformToBackendCourseDTO(courseData)
    const response = await api.put(`/courses/${id}`, courseDTO)
    return transformCourseDTO(response.data)
  } catch (error) {
    console.error("Error updating course:", error)
    throw error
  }
}

export const deleteCourse = async (id: string): Promise<void> => {
  try {
    await api.delete(`/courses/${id}`)
  } catch (error) {
    console.error("Error deleting course:", error)
    throw error
  }
}

export const getCoursesByCategory = async (category: string): Promise<Course[]> => {
  try {
    const allCourses = await getAllCourses()
    return allCourses.filter((course) => course.category.toLowerCase() === category.toLowerCase())
  } catch (error) {
    console.error("Error fetching courses by category:", error)
    throw error
  }
}

export const searchCourses = async (query: string): Promise<Course[]> => {
  try {
    const allCourses = await getAllCourses()
    const lowercaseQuery = query.toLowerCase()

    return allCourses.filter(
        (course) =>
            course.title.toLowerCase().includes(lowercaseQuery) ||
            course.description.toLowerCase().includes(lowercaseQuery) ||
            course.instructorName.toLowerCase().includes(lowercaseQuery),
    )
  } catch (error) {
    console.error("Error searching courses:", error)
    throw error
  }
}

export const getInstructorCourses = async (instructorId?: string): Promise<Course[]> => {
  try {
    let response

    if (instructorId) {
      response = await api.get(`/courses/instructor/${instructorId}`)
    } else {
      response = await api.get("/courses")
    }

    if (Array.isArray(response.data)) {
      return response.data.map(transformCourseDTO)
    }
    return []
  } catch (error) {
    console.error("Error fetching instructor courses:", error)
    return []
  }
}

export const getCoursesByInstructorEmail = async (instructorEmail: string): Promise<Course[]> => {
  try {
    const response = await api.get(`/courses?instructorEmail=${instructorEmail}`)
    if (Array.isArray(response.data)) {
      return response.data.map(transformCourseDTO)
    }
    return []
  } catch (error) {
    console.error("Error fetching courses by instructor email:", error)
    return []
  }
}

export const enrollInCourse = async (courseId: string): Promise<Enrollment> => {
  return {
    id: `enrollment-${Date.now()}`,
    userId: "current-user-id",
    courseId: courseId,
    enrolledAt: new Date().toISOString(),
    progress: 0,
    completedLessons: [],
  }
}

export const getUserEnrollments = async (): Promise<Enrollment[]> => {
  return []
}

export const updateLessonProgress = async (
    enrollmentId: string,
    lessonId: string,
    completed: boolean,
): Promise<Enrollment> => {
  return {
    id: enrollmentId,
    userId: "current-user-id",
    courseId: "course-id",
    enrolledAt: new Date().toISOString(),
    progress: 50,
    completedLessons: completed ? [lessonId] : [],
  }
}
