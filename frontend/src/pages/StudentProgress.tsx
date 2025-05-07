"use client"

import { useState, useEffect } from "react"
import { Card, Form, Row, Col, Spinner } from "react-bootstrap"
import ApiCard from "../components/ApiCard"
import ResponseDisplay from "../components/ResponseDisplay"
import { progressApi, courseApi, type CourseDTO } from "../api/api"

const StudentProgress = () => {
    const [studentId, setStudentId] = useState("")
    const [courseId, setCourseId] = useState("")
    const [videoId, setVideoId] = useState("")

    const [courses, setCourses] = useState<CourseDTO[]>([])
    const [response, setResponse] = useState<any>(null)
    const [error, setError] = useState<string | null>(null)
    const [loading, setLoading] = useState(false)

    useEffect(() => {
        fetchCourses()
    }, [])

    const fetchCourses = async () => {
        try {
            const result = await courseApi.getAllCourses()
            setCourses(result)
        } catch (err) {
            console.error("Failed to fetch courses:", err)
        }
    }

    const handleGetProgressPercentage = async () => {
        if (!studentId || !courseId) {
            setError("Student ID and Course ID are required")
            return
        }

        setLoading(true)
        setError(null)

        try {
            const result = await progressApi.getProgressPercentage(studentId, courseId)
            setResponse(result)
        } catch (err) {
            setError(err instanceof Error ? err.message : "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }

    const handleUpdateProgress = async () => {
        if (!studentId || !courseId || !videoId) {
            setError("Student ID, Course ID, and Video ID are required")
            return
        }

        setLoading(true)
        setError(null)

        try {
            await progressApi.updateProgress(studentId, courseId, videoId)
            setResponse({ message: "Progress updated successfully" })
        } catch (err) {
            setError(err instanceof Error ? err.message : "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }

    return (
        <div>
            <h1 className="mb-4">Student Progress Tracking</h1>

            <Card className="mb-4">
                <Card.Body>
                    <Card.Title>Progress Form</Card.Title>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Student ID</Form.Label>
                            <Form.Control
                                type="text"
                                value={studentId}
                                onChange={(e) => setStudentId(e.target.value)}
                                placeholder="UUID format"
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Course ID</Form.Label>
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
                                        if (selected && selected.instructorId) {
                                            setCourseId(selected.instructorId)
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
                            <Form.Label>Video ID (for updating progress)</Form.Label>
                            <Form.Control
                                type="text"
                                value={videoId}
                                onChange={(e) => setVideoId(e.target.value)}
                                placeholder="UUID format"
                            />
                        </Form.Group>
                    </Form>
                </Card.Body>
            </Card>

            <h2 className="mb-3">API Endpoints</h2>

            <Row>
                <Col md={6}>
                    <ApiCard
                        title="Get Progress Percentage"
                        description="Returns progress percentage for a student in a specific course"
                        method="GET"
                        endpoint="/students/progress/v1/{studentId}/courses/{courseId}"
                        onClick={handleGetProgressPercentage}
                    />
                </Col>

                <Col md={6}>
                    <ApiCard
                        title="Update Progress"
                        description="Marks a video as watched and updates progress"
                        method="POST"
                        endpoint="/students/progress/v1/{studentId}/courses/{courseId}/videos/{videoId}/update"
                        onClick={handleUpdateProgress}
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

export default StudentProgress
