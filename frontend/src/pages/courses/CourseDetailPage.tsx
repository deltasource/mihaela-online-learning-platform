"use client"

import { useState, useEffect } from "react"
import { useParams, Link } from "react-router-dom"
import { Container, Row, Col, Card, Button, Alert, Badge, ListGroup } from "react-bootstrap"
import { getCourseById } from "../../services/courseService"
import { getVideosByCourse } from "../../services/videoService"
import type { Course } from "../../types/course"
import type { Video } from "../../types/video"
import LoadingSpinner from "../../components/common/LoadingSpinner"

const CourseDetailPage = () => {
    const { id } = useParams<{ id: string }>()
    const [course, setCourse] = useState<Course | null>(null)
    const [videos, setVideos] = useState<Video[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        if (id) {
            fetchCourseDetails(id)
        }
    }, [id])

    const fetchCourseDetails = async (courseId: string) => {
        try {
            setLoading(true)
            const courseData = await getCourseById(courseId)
            setCourse(courseData)

            const videosData = await getVideosByCourse(courseId)
            setVideos(videosData)

            setError(null)
        } catch (err) {
            setError("Failed to fetch course details. Please try again later.")
            console.error(err)
        } finally {
            setLoading(false)
        }
    }

    if (loading) {
        return <LoadingSpinner />
    }

    if (error) {
        return (
            <Container>
                <Alert variant="danger">{error}</Alert>
                <Button as={Link} to="/courses" variant="primary">
                    Back to Courses
                </Button>
            </Container>
        )
    }

    if (!course) {
        return (
            <Container>
                <Alert variant="warning">Course not found</Alert>
                <Button as={Link} to="/courses" variant="primary">
                    Back to Courses
                </Button>
            </Container>
        )
    }

    return (
        <Container>
            <div className="mb-4">
                <Button as={Link} to="/courses" variant="outline-primary" className="mb-3">
                    &larr; Back to Courses
                </Button>
                <h1 className="page-title">{course.name}</h1>
                <p className="page-description">{course.description}</p>
            </div>

            <Row>
                <Col md={8}>
                    <Card className="mb-4">
                        <Card.Header>
                            <h4 className="mb-0">Course Videos</h4>
                        </Card.Header>
                        <Card.Body>
                            {videos.length === 0 ? (
                                <div className="text-center py-4">
                                    <p className="mb-3">No videos available for this course yet.</p>
                                    <Button as={Link} to={`/videos/upload/${course.id}`} variant="primary">
                                        Upload Video
                                    </Button>
                                </div>
                            ) : (
                                <ListGroup variant="flush">
                                    {videos.map((video) => (
                                        <ListGroup.Item key={video.id} className="d-flex justify-content-between align-items-center">
                                            <div>
                                                <i className="bi bi-play-circle me-2"></i>
                                                {video.fileName}
                                            </div>
                                            <Badge bg="primary" pill>
                                                Video
                                            </Badge>
                                        </ListGroup.Item>
                                    ))}
                                </ListGroup>
                            )}
                        </Card.Body>
                        <Card.Footer>
                            <Button as={Link} to={`/videos/upload/${course.id}`} variant="primary">
                                Upload New Video
                            </Button>
                        </Card.Footer>
                    </Card>
                </Col>

                <Col md={4}>
                    <Card className="mb-4">
                        <Card.Header>
                            <h4 className="mb-0">Course Details</h4>
                        </Card.Header>
                        <ListGroup variant="flush">
                            <ListGroup.Item>
                                <strong>Instructor ID:</strong> {course.instructorId}
                            </ListGroup.Item>
                            <ListGroup.Item>
                                <strong>Total Videos:</strong> {videos.length}
                            </ListGroup.Item>
                            {course.studentIds && course.studentIds.length > 0 && (
                                <ListGroup.Item>
                                    <strong>Enrolled Students:</strong> {course.studentIds.length}
                                </ListGroup.Item>
                            )}
                        </ListGroup>
                        <Card.Footer>
                            <Button variant="outline-primary" className="w-100">
                                Edit Course
                            </Button>
                        </Card.Footer>
                    </Card>
                </Col>
            </Row>
        </Container>
    )
}

export default CourseDetailPage
