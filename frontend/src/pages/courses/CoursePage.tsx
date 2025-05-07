"use client"

import { useState, useEffect } from "react"
import { Container, Row, Col, Card, Button, Alert } from "react-bootstrap"
import { Link } from "react-router-dom"
import { getCourses } from "../../services/courseService"
import type { Course } from "../../types/course"
import LoadingSpinner from "../../components/common/LoadingSpinner"
import CourseForm from "./CourseForm"

const CoursesPage = () => {
    const [courses, setCourses] = useState<Course[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const [showForm, setShowForm] = useState(false)

    useEffect(() => {
        fetchCourses()
    }, [])

    const fetchCourses = async () => {
        try {
            setLoading(true)
            const data = await getCourses()
            setCourses(data)
            setError(null)
        } catch (err) {
            setError("Failed to fetch courses. Please try again later.")
            console.error(err)
        } finally {
            setLoading(false)
        }
    }

    const handleCourseAdded = (newCourse: Course) => {
        setCourses([...courses, newCourse])
        setShowForm(false)
    }

    return (
        <Container>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h1 className="page-title">Courses</h1>
                    <p className="page-description">Browse all available courses</p>
                </div>
                <Button variant="primary" onClick={() => setShowForm(!showForm)}>
                    {showForm ? "Cancel" : "Add New Course"}
                </Button>
            </div>

            {error && <Alert variant="danger">{error}</Alert>}

            {showForm && (
                <div className="mb-4">
                    <CourseForm onCourseAdded={handleCourseAdded} />
                </div>
            )}

            {loading ? (
                <LoadingSpinner />
            ) : courses.length === 0 ? (
                <Alert variant="info">No courses available. Add a new course to get started.</Alert>
            ) : (
                <Row xs={1} md={2} lg={3} className="g-4">
                    {courses.map((course) => (
                        <Col key={course.id}>
                            <Card className="h-100 shadow-sm">
                                <Card.Body>
                                    <Card.Title>{course.name}</Card.Title>
                                    <Card.Text>{course.description}</Card.Text>
                                </Card.Body>
                                <Card.Footer className="bg-white">
                                    <Button as={Link} to={`/courses/${course.id}`} variant="outline-primary" className="w-100">
                                        View Details
                                    </Button>
                                </Card.Footer>
                            </Card>
                        </Col>
                    ))}
                </Row>
            )}
        </Container>
    )
}

export default CoursesPage
