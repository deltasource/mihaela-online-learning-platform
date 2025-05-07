"use client"

import type React from "react"

import { useState, type FormEvent } from "react"
import { Form, Button, Card, Alert } from "react-bootstrap"
import { createCourse } from "../../services/courseService"
import type { Course } from "../../types/course"

interface CourseFormProps {
    onCourseAdded: (course: Course) => void
}

const CourseForm = ({ onCourseAdded }: CourseFormProps) => {
    const [formData, setFormData] = useState<Omit<Course, "id">>({
        name: "",
        description: "",
        instructorId: "",
        studentIds: [],
    })
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target
        setFormData({ ...formData, [name]: value })
    }

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault()

        try {
            setLoading(true)
            setError(null)

            const newCourse = await createCourse(formData)
            onCourseAdded(newCourse)
        } catch (err) {
            setError("Failed to create course. Please check your inputs and try again.")
            console.error(err)
        } finally {
            setLoading(false)
        }
    }

    return (
        <Card className="mb-4">
            <Card.Body>
                <Card.Title>Add New Course</Card.Title>

                {error && <Alert variant="danger">{error}</Alert>}

                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                        <Form.Label>Course Name</Form.Label>
                        <Form.Control
                            type="text"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            required
                            placeholder="Enter course name"
                        />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Description</Form.Label>
                        <Form.Control
                            as="textarea"
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            required
                            placeholder="Enter course description"
                            rows={3}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Instructor ID</Form.Label>
                        <Form.Control
                            type="text"
                            name="instructorId"
                            value={formData.instructorId}
                            onChange={handleChange}
                            required
                            placeholder="Enter instructor ID"
                        />
                        <Form.Text className="text-muted">Enter the UUID of the instructor for this course</Form.Text>
                    </Form.Group>

                    <div className="d-flex justify-content-end">
                        <Button variant="primary" type="submit" disabled={loading}>
                            {loading ? "Creating..." : "Create Course"}
                        </Button>
                    </div>
                </Form>
            </Card.Body>
        </Card>
    )
}

export default CourseForm
