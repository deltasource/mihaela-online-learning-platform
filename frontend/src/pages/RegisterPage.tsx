"use client"

import type React from "react"

import { useState } from "react"
import { Container, Row, Col, Card, Form, Button, Alert } from "react-bootstrap"
import { Link, useNavigate } from "react-router-dom"

const RegisterPage = () => {
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: "",
        confirmPassword: "",
        role: "student",
    })
    const [error, setError] = useState("")
    const [loading, setLoading] = useState(false)

    const navigate = useNavigate()

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }))
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setError("")

        // Validate form
        if (formData.password !== formData.confirmPassword) {
            setError("Passwords do not match")
            return
        }

        setLoading(true)

        try {

            await new Promise((resolve) => setTimeout(resolve, 1000))

            if (formData.role === "student") {
                navigate("/profile/student")
            } else {
                navigate("/profile/instructor")
            }
        } catch (err) {
            setError("An error occurred. Please try again.")
        } finally {
            setLoading(false)
        }
    }

    return (
        <Container className="py-5">
            <Row className="justify-content-center">
                <Col md={8} lg={6} xl={5}>
                    <Card className="border-0 shadow-sm">
                        <Card.Body className="p-4 p-md-5">
                            <div className="text-center mb-4">
                                <h2 className="fw-bold mb-1">Create an Account</h2>
                                <p className="text-muted">Join LearnHub today</p>
                            </div>

                            {error && <Alert variant="danger">{error}</Alert>}

                            <Form onSubmit={handleSubmit}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Full Name</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="name"
                                        placeholder="Enter your full name"
                                        value={formData.name}
                                        onChange={handleChange}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3">
                                    <Form.Label>Email Address</Form.Label>
                                    <Form.Control
                                        type="email"
                                        name="email"
                                        placeholder="Enter your email"
                                        value={formData.email}
                                        onChange={handleChange}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3">
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control
                                        type="password"
                                        name="password"
                                        placeholder="Create a password"
                                        value={formData.password}
                                        onChange={handleChange}
                                        required
                                    />
                                    <Form.Text className="text-muted">Password must be at least 8 characters long</Form.Text>
                                </Form.Group>

                                <Form.Group className="mb-4">
                                    <Form.Label>Confirm Password</Form.Label>
                                    <Form.Control
                                        type="password"
                                        name="confirmPassword"
                                        placeholder="Confirm your password"
                                        value={formData.confirmPassword}
                                        onChange={handleChange}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-4">
                                    <Form.Label>I am a:</Form.Label>
                                    <div>
                                        <Form.Check
                                            inline
                                            type="radio"
                                            label="Student"
                                            name="role"
                                            id="student"
                                            value="student"
                                            checked={formData.role === "student"}
                                            onChange={handleChange}
                                        />
                                        <Form.Check
                                            inline
                                            type="radio"
                                            label="Instructor"
                                            name="role"
                                            id="instructor"
                                            value="instructor"
                                            checked={formData.role === "instructor"}
                                            onChange={handleChange}
                                        />
                                    </div>
                                </Form.Group>

                                <div className="d-grid mb-4">
                                    <Button type="submit" variant="primary" disabled={loading}>
                                        {loading ? "Creating Account..." : "Create Account"}
                                    </Button>
                                </div>

                                <div className="text-center mb-3">
                                    <p className="text-muted mb-0">Or sign up with</p>
                                </div>

                                <div className="d-grid gap-2 mb-4">
                                    <Button variant="outline-primary" className="d-flex align-items-center justify-content-center">
                                        <i className="bi bi-google me-2"></i> Sign up with Google
                                    </Button>
                                    <Button variant="outline-primary" className="d-flex align-items-center justify-content-center">
                                        <i className="bi bi-facebook me-2"></i> Sign up with Facebook
                                    </Button>
                                </div>

                                <div className="text-center">
                                    <p className="mb-0">
                                        Already have an account?{" "}
                                        <Link to="/login" className="text-decoration-none">
                                            Sign In
                                        </Link>
                                    </p>
                                </div>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    )
}

export default RegisterPage
