"use client"

import { useState, useEffect } from "react"
import { Card, Form, Row, Col, Spinner } from "react-bootstrap"
import ApiCard from "../components/ApiCard"
import ResponseDisplay from "../components/ResponseDisplay"
import { courseApi, type CourseDTO } from "../api/api"

const Courses = () => {
    const [courseId, setCourseId] = useState("")
    const [name, setName] = useState("")
    const [description, setDescription] = useState("")
    const [instructorId, setInstructorId] = useState("")
    const [studentIds, setStudentIds] = useState("")

    const [courses, setCourses] = useState<CourseDTO[]>([])
    const [response, setResponse] = useState<any>(null)
    const [error, setError] = useState<string | null>(null)
    const [loading, setLoading] = useState(false)

    useEffect(() => {
        // Load courses on component mount
        fetchAllCourses()
    }, [])

    const fetchAllCourses = async () => {
        setLoading(true)
        setError(null)

        try {
            const result = await courseApi.getAllCourses()
            setCourses(result)
        } catch (err) {
            setError(err instanceof Error ? err.message : "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }

    const resetForm = () => {
        setCourseId("")
        setName("")
        setDescription("")
        setInstructorId("")
        setStudentIds("")
    }

    const handleCreateCourse = async () => {
        setLoading(true)
        setError(null)

        try {
            const courseData: CourseDTO = {
                name,
                description,
                instructorId: instructorId || undefined,
                studentIds: studentIds ? studentIds.split(",").map((id) => id.trim()) : undefined,
            }

            const result = await courseApi.createCourse(courseData)
            setResponse(result)
            resetForm()
            fetchAllCourses() // Refresh the course list
        } catch (err) {
            setError(err instanceof Error ? err.message : "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }

    const handleGetCourse = async () => {
        if (!courseId) {
            setError("Course ID is required")
            return
        }

        setLoading(true)
        setError(null)

        try {
            const result = await courseApi.getCourseById(courseId)
            setResponse(result)
        } catch (err) {
            setError(err instanceof Error ? err.message : "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }

    const handleUpdateCourse = async () => {
        if (!courseId) {
            setError("Course ID is required")
            return
        }

        setLoading(true)
        setError(null)

        try {
            const courseData: CourseDTO = {
                name,
                description,
                instructorId: instructorId || undefined,
                studentIds: studentIds ? studentIds.split(",").map((id) => id.trim()) : undefined,
            }

            const result = await courseApi.updateCourse(courseId, courseData)
            setResponse(result)
            fetchAllCourses() // Refresh the course list
        } catch (err) {
            setError(err instanceof Error ? err.message : "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }

    const handleDeleteCourse = async () => {
        if (!courseId) {
            setError("Course ID is required")
            return
        }

        setLoading(true)
        setError(null)

        try {
            await courseApi.deleteCourse(courseId)
            setResponse({ message: "Course deleted successfully" })
            resetForm()
            fetchAllCourses() // Refresh the course list
        } catch (err) {
            setError(err instanceof Error ? err.message : "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }

    const handleGetAllCourses = async () => {
        await fetchAllCourses()
        setResponse(courses)
    }

    return (
        <div>
            <h1 className="mb-4">Course Management</h1>

            <Card className="mb-4">
                <Card.Body>
                    <Card.Title>Course Form</Card.Title>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Course ID (for get/update/delete)</Form.Label>
                            <Form.Control
                                type="text"
                                value={courseId}
                                onChange={(e) => setCourseId(e.target.value)}
                                placeholder="UUID format"
                            />
                            {courses.length > 0 && (
                                <Form.Select
                                    className="mt-2"
                                    onChange={(e) => {
                                        const selected = courses.find((c) => c.name === e.target.value)
                                        if (selected) {
                                            setCourseId(selected.instructorId || "")
                                            setName(selected.name)
                                            setDescription(selected.description)
                                            setInstructorId(selected.instructorId || "")
                                            setStudentIds(selected.studentIds?.join(", ") || "")
                                        }
                                    }}
                                >
                                    <option value="">Select a course</option>
                                    {courses.map((course, index) => (
                                        <option key={index} value={course.name}>
                                            {course.name}
                                        </option>
                                    ))}
                                </Form.Select>
                            )}
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Name</Form.Label>
                            <Form.Control
                                type="text"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                                placeholder="Course Name"
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Description</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={3}
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                placeholder="Course Description"
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Instructor ID</Form.Label>
                            <Form.Control
                                type="text"
                                value={instructorId}
                                onChange={(e) => setInstructorId(e.target.value)}
                                placeholder="UUID format"
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Student IDs (comma separated)</Form.Label>
                            <Form.Control
                                type="text"
                                value={studentIds}
                                onChange={(e) => setStudentIds(e.target.value)}
                                placeholder="UUID1, UUID2, ..."
                            />
                        </Form.Group>
                    </Form>
                </Card.Body>
            </Card>

            <h2 className="mb-3">API Endpoints</h2>

            <Row>
                <Col md={6}>
                    <ApiCard
                        title="Create Course"
                        description="Creates a new course with the provided details"
                        method="POST"
                        endpoint="/api/courses"
                        onClick={handleCreateCourse}
                    />
                </Col>

                <Col md={6}>
                    <ApiCard
                        title="Get All Courses"
                        description="Returns all courses"
                        method="GET"
                        endpoint="/api/courses"
                        onClick={handleGetAllCourses}
                    />
                </Col>

                <Col md={6}>
                    <ApiCard
                        title="Get Course by ID"
                        description="Returns a course based on the ID provided"
                        method="GET"
                        endpoint="/api/courses/{courseId}"
                        onClick={handleGetCourse}
                    />
                </Col>

                <Col md={6}>
                    <ApiCard
                        title="Update Course"
                        description="Updates a course's details based on the ID provided"
                        method="PUT"
                        endpoint="/api/courses/{courseId}"
                        onClick={handleUpdateCourse}
                    />
                </Col>

                <Col md={6}>
                    <ApiCard
                        title="Delete Course"
                        description="Deletes a course based on the ID provided"
                        method="DELETE"
                        endpoint="/api/courses/{courseId}"
                        onClick={handleDeleteCourse}
                    />
                </Col>
            </Row>

            {loading && (
                <div className="loading-spinner">
                    <Spinner animation="border" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </Spinner>
                </div>
            )}

            <ResponseDisplay data={response} error={error} loading={loading} />
        </div>
    )
}

export default Courses
