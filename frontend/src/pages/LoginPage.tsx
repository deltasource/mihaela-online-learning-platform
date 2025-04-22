"use client"

import type React from "react"

import { useState } from "react"
import { Container, Row, Col, Card, Form, Button, Alert } from "react-bootstrap"
import { Link, useNavigate } from "react-router-dom"

const LoginPage = () => {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [error, setError] = useState("")
    const [loading, setLoading] = useState(false)

    const navigate = useNavigate()

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setError("")
        setLoading(true)

        try {

            await new Promise((resolve) => setTimeout(resolve, 1000))

            if (email === "student@example.com" && password === "password") {

                navigate("/profile/student")
            } else if (email === "instructor@example.com" && password === "password") {

                navigate("/profile/instructor")
            } else {
                setError("Invalid email or password")
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
                                <h2 className="fw-bold mb-1">Welcome Back</h2>
                                <p className="text-muted">Sign in to continue to LearnHub</p>
                            </div>

                            {error && <Alert variant="danger">{error}</Alert>}

                            <Form onSubmit={handleSubmit}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Email Address</Form.Label>
                                    <Form.Control
                                        type="email"
                                        placeholder="Enter your email"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-4">
                                    <div className="d-flex justify-content-between align-items-center">
                                        <Form.Label>Password</Form.Label>
                                        <Link to="#" className="text-decoration-none small">
                                            Forgot Password?
                                        </Link>
                                    </div>
                                    <Form.Control
                                        type="password"
                                        placeholder="Enter your password"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        required
                                    />
                                </Form.Group>

                                <div className="d-grid mb-4">
                                    <Button type="submit" variant="primary" disabled={loading}>
                                        {loading ? "Signing In..." : "Sign In"}
                                    </Button>
                                </div>

                                <div className="text-center mb-3">
                                    <p className="text-muted mb-0">Or sign in with</p>
                                </div>

                                <div className="d-grid gap-2 mb-4">
                                    <Button variant="outline-primary" className="d-flex align-items-center justify-content-center">
                                        <i className="bi bi-google me-2"></i> Sign in with Google
                                    </Button>
                                    <Button variant="outline-primary" className="d-flex align-items-center justify-content-center">
                                        <i className="bi bi-facebook me-2"></i> Sign in with Facebook
                                    </Button>
                                </div>

                                <div className="text-center">
                                    <p className="mb-0">
                                        Don't have an account?{" "}
                                        <Link to="/register" className="text-decoration-none">
                                            Sign Up
                                        </Link>
                                    </p>
                                </div>
                            </Form>
                        </Card.Body>
                    </Card>

                    <div className="text-center mt-3">
                        <p className="text-muted small">
                            For demo purposes, use:
                            <br />
                            Student: student@example.com / password
                            <br />
                            Instructor: instructor@example.com / password
                        </p>
                    </div>
                </Col>
            </Row>
        </Container>
    )
}

export default LoginPage
