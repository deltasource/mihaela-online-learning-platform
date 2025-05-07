"use client"

import { useState } from "react"
import { Card, Form, Row, Col, Spinner } from "react-bootstrap"
import ApiCard from "../components/ApiCard"
import ResponseDisplay from "../components/ResponseDisplay"
import { studentApi, type StudentDTO } from "../api/api"

const Students = () => {
    const [email, setEmail] = useState("")
    const [firstName, setFirstName] = useState("")
    const [lastName, setLastName] = useState("")

    const [response, setResponse] = useState<any>(null)
    const [error, setError] = useState<string | null>(null)
    const [loading, setLoading] = useState(false)

    const resetForm = () => {
        setEmail("")
        setFirstName("")
        setLastName("")
    }

    const handleCreateStudent = async () => {
        setLoading(true)
        setError(null)

        try {
            const studentData: StudentDTO = {
                email,
                firstName,
                lastName,
            }

            const result = await studentApi.createStudent(studentData)
            setResponse(result)
            resetForm()
        } catch (err) {
            setError(err instanceof Error ? err.message : "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }

    const handleGetStudent = async () => {
        if (!email) {
            setError("Email is required")
            return
        }

        setLoading(true)
        setError(null)

        try {
            const result = await studentApi.getStudentByEmail(email)
            setResponse(result)
        } catch (err) {
            setError(err instanceof Error ? err.message : "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }

    const handleUpdateStudent = async () => {
        if (!email) {
            setError("Email is required")
            return
        }

        setLoading(true)
        setError(null)

        try {
            const studentData: StudentDTO = {
                email,
                firstName,
                lastName,
            }

            const result = await studentApi.updateStudent(email, studentData)
            setResponse(result)
        } catch (err) {
            setError(err instanceof Error ? err.message : "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }

    const handleDeleteStudent = async () => {
        if (!email) {
            setError("Email is required")
            return
        }

        setLoading(true)
        setError(null)

        try {
            await studentApi.deleteStudent(email)
            setResponse({ message: "Student deleted successfully" })
            resetForm()
        } catch (err) {
            setError(err instanceof Error ? err.message : "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }

    return (
        <div>
            <h1 className="mb-4">Student Management</h1>

            <Card className="mb-4">
                <Card.Body>
                    <Card.Title>Student Form</Card.Title>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                placeholder="student@example.com"
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>First Name</Form.Label>
                            <Form.Control
                                type="text"
                                value={firstName}
                                onChange={(e) => setFirstName(e.target.value)}
                                placeholder="John"
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Last Name</Form.Label>
                            <Form.Control
                                type="text"
                                value={lastName}
                                onChange={(e) => setLastName(e.target.value)}
                                placeholder="Doe"
                            />
                        </Form.Group>
                    </Form>
                </Card.Body>
            </Card>

            <h2 className="mb-3">API Endpoints</h2>

            <Row>
                <Col md={6}>
                    <ApiCard
                        title="Create Student"
                        description="Creates a new student with the provided details"
                        method="POST"
                        endpoint="/students/v1"
                        onClick={handleCreateStudent}
                    />
                </Col>

                <Col md={6}>
                    <ApiCard
                        title="Get Student"
                        description="Returns a student based on the email provided"
                        method="GET"
                        endpoint="/students/v1/{email}"
                        onClick={handleGetStudent}
                    />
                </Col>

                <Col md={6}>
                    <ApiCard
                        title="Update Student"
                        description="Updates a student's details based on the email provided"
                        method="PUT"
                        endpoint="/students/v1/{email}"
                        onClick={handleUpdateStudent}
                    />
                </Col>

                <Col md={6}>
                    <ApiCard
                        title="Delete Student"
                        description="Deletes a student based on the email provided"
                        method="DELETE"
                        endpoint="/students/v1/{email}"
                        onClick={handleDeleteStudent}
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

export default Students
