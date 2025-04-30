"use client"

import { useState } from "react"
import { Card, Form, Row, Col, Spinner } from "react-bootstrap"
import ApiCard from "../components/ApiCard"
import ResponseDisplay from "../components/ResponseDisplay"
import { instructorApi, type InstructorDTO } from "../api/api"

const Instructors = () => {
    const [email, setEmail] = useState("")
    const [firstName, setFirstName] = useState("")
    const [lastName, setLastName] = useState("")
    const [department, setDepartment] = useState("")

    const [response, setResponse] = useState<any>(null)
    const [error, setError] = useState<string | null>(null)
    const [loading, setLoading] = useState(false)

    const resetForm = () => {
        setEmail("")
        setFirstName("")
        setLastName("")
        setDepartment("")
    }

    const handleCreateInstructor = async () => {
        setLoading(true)
        setError(null)

        try {
            const instructorData: InstructorDTO = {
                email,
                firstName,
                lastName,
                department,
            }

            const result = await instructorApi.createInstructor(instructorData)
            setResponse(result)
            resetForm()
        } catch (err) {
            setError(err instanceof Error ? err.message : "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }

    const handleGetInstructor = async () => {
        if (!email) {
            setError("Email is required")
            return
        }

        setLoading(true)
        setError(null)

        try {
            const result = await instructorApi.getInstructorByEmail(email)
            setResponse(result)
        } catch (err) {
            setError(err instanceof Error ? err.message : "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }

    const handleUpdateInstructor = async () => {
        if (!email) {
            setError("Email is required")
            return
        }

        setLoading(true)
        setError(null)

        try {
            const instructorData: InstructorDTO = {
                email,
                firstName,
                lastName,
                department,
            }

            const result = await instructorApi.updateInstructor(email, instructorData)
            setResponse(result)
        } catch (err) {
            setError(err instanceof Error ? err.message : "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }

    const handleDeleteInstructor = async () => {
        if (!email) {
            setError("Email is required")
            return
        }

        setLoading(true)
        setError(null)

        try {
            await instructorApi.deleteInstructor(email)
            setResponse({ message: "Instructor deleted successfully" })
            resetForm()
        } catch (err) {
            setError(err instanceof Error ? err.message : "An unknown error occurred")
        } finally {
            setLoading(false)
        }
    }

    return (
        <div>
            <h1 className="mb-4">Instructor Management</h1>

            <Card className="mb-4">
                <Card.Body>
                    <Card.Title>Instructor Form</Card.Title>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                placeholder="instructor@example.com"
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

                        <Form.Group className="mb-3">
                            <Form.Label>Department</Form.Label>
                            <Form.Control
                                type="text"
                                value={department}
                                onChange={(e) => setDepartment(e.target.value)}
                                placeholder="Computer Science"
                            />
                        </Form.Group>
                    </Form>
                </Card.Body>
            </Card>

            <h2 className="mb-3">API Endpoints</h2>

            <Row>
                <Col md={6}>
                    <ApiCard
                        title="Create Instructor"
                        description="Creates a new instructor with the provided details"
                        method="POST"
                        endpoint="/instructors/v1"
                        onClick={handleCreateInstructor}
                    />
                </Col>

                <Col md={6}>
                    <ApiCard
                        title="Get Instructor"
                        description="Returns an instructor based on the email provided"
                        method="GET"
                        endpoint="/instructors/v1/{email}"
                        onClick={handleGetInstructor}
                    />
                </Col>

                <Col md={6}>
                    <ApiCard
                        title="Update Instructor"
                        description="Updates an instructor's details based on the email provided"
                        method="PUT"
                        endpoint="/instructors/v1/{email}"
                        onClick={handleUpdateInstructor}
                    />
                </Col>

                <Col md={6}>
                    <ApiCard
                        title="Delete Instructor"
                        description="Deletes an instructor based on the email provided"
                        method="DELETE"
                        endpoint="/instructors/v1/{email}"
                        onClick={handleDeleteInstructor}
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

export default Instructors
