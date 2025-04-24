"use client"

import { useState, useEffect } from "react"
import { useParams, Link } from "react-router-dom"
import { Container, Card, ProgressBar, Alert, Button } from "react-bootstrap"
import { getStudentProgress } from "../../services/progressService"
import type { StudentProgress } from "../../types/studentProgress"
import LoadingSpinner from "../../components/common/LoadingSpinner"

const StudentProgressPage = () => {
    const { studentId, courseId } = useParams<{ studentId: string; courseId: string }>()
    const [progress, setProgress] = useState<StudentProgress | null>(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        if (studentId && courseId) {
            fetchStudentProgress(studentId, courseId)
        }
    }, [studentId, courseId])

    const fetchStudentProgress = async (studentId: string, courseId: string) => {
        try {
            setLoading(true)
            const data = await getStudentProgress(studentId, courseId)
            setProgress(data)
            setError(null)
        } catch (err) {
            setError("Failed to fetch student progress. The student may not be enrolled in this course.")
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
                <Button as={Link} to="/students" variant="primary">
                    Back to Students
                </Button>
            </Container>
        )
    }

    if (!progress) {
        return (
            <Container>
                <Alert variant="warning">No progress data found</Alert>
                <Button as={Link} to="/students" variant="primary">
                    Back to Students
                </Button>
            </Container>
        )
    }

    return (
        <Container>
            <h1 className="page-title">Student Progress</h1>
            <p className="page-description">Track student's progress in the course</p>

            <Card className="mb-4">
                <Card.Header>
                    <h4 className="mb-0">Progress Overview</h4>
                </Card.Header>
                <Card.Body>
                    <div className="mb-4">
                        <h5>Student ID: {progress.studentId}</h5>
                        <h5>Course ID: {progress.courseId}</h5>
                    </div>

                    <div className="mb-4">
                        <h5>Overall Progress</h5>
                        <ProgressBar
                            now={progress.progressPercentage}
                            label={`${progress.progressPercentage.toFixed(1)}%`}
                            variant={
                                progress.progressPercentage >= 75 ? "success" : progress.progressPercentage >= 25 ? "info" : "warning"
                            }
                            className="progress-bar-container"
                        />
                    </div>

                    <div className="mb-3">
                        <h5>Videos Watched</h5>
                        <p>
                            {progress.videosWatched} out of {progress.totalVideos} videos completed
                        </p>
                        <ProgressBar
                            now={(progress.videosWatched / progress.totalVideos) * 100}
                            variant="primary"
                            className="progress-bar-container"
                        />
                    </div>
                </Card.Body>
            </Card>

            <div className="d-flex justify-content-between">
                <Button as={Link} to="/students" variant="outline-primary">
                    Back to Students
                </Button>
                <Button as={Link} to={`/courses/${courseId}`} variant="primary">
                    View Course
                </Button>
            </div>
        </Container>
    )
}

export default StudentProgressPage
