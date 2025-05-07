"use client"

import type React from "react"

import { useState } from "react"
import { Container, Table, Button, Alert, Modal, Form } from "react-bootstrap"
import { getStudentByEmail, createStudent, deleteStudent } from "../../services/studentService"
import type { Student } from "../../types/student"
import LoadingSpinner from "../../components/common/LoadingSpinner"
import { toast } from "react-toastify"

const StudentsPage = () => {
    const [students, setStudents] = useState<Student[]>([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [showModal, setShowModal] = useState(false)
    const [searchEmail, setSearchEmail] = useState("")
    const [formData, setFormData] = useState<Omit<Student, "id">>({
        email: "",
        firstName: "",
        lastName: "",
    })

    const handleSearch = async () => {
        if (!searchEmail.trim()) {
            setError("Please enter an email to search")
            return
        }

        try {
            setLoading(true)
            setError(null)

            const student = await getStudentByEmail(searchEmail)

            // Check if student already exists in the list
            if (!students.some((s) => s.email === student.email)) {
                setStudents([...students, student])
            }

            setSearchEmail("")
        } catch (err) {
            setError(`Student with email ${searchEmail} not found`)
        } finally {
            setLoading(false)
        }
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target
        setFormData({ ...formData, [name]: value })
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        try {
            setLoading(true)
            setError(null)

            const newStudent = await createStudent(formData)
            setStudents([...students, newStudent])

            setShowModal(false)
            setFormData({
                email: "",
                firstName: "",
                lastName: "",
            })

            toast.success("Student created successfully!")
        } catch (err) {
            setError("Failed to create student. Please check your inputs and try again.")
        } finally {
            setLoading(false)
        }
    }

    const handleDelete = async (email: string) => {
        if (window.confirm("Are you sure you want to delete this student?")) {
            try {
                setLoading(true)
                await deleteStudent(email)
                setStudents(students.filter((s) => s.email !== email))
                toast.success("Student deleted successfully!")
            } catch (err) {
                setError("Failed to delete student")
            } finally {
                setLoading(false)
            }
        }
    }

    return (
        <Container>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h1 className="page-title">Students</h1>
                    <p className="page-description">Manage students enrolled in your courses</p>
                </div>
                <Button variant="primary" onClick={() => setShowModal(true)}>
                    Add New Student
                </Button>
            </div>

            {error && <Alert variant="danger">{error}</Alert>}

            <div className="mb-4">
                <div className="d-flex gap-2">
                    <Form.Control
                        type="email"
                        placeholder="Search by email"
                        value={searchEmail}
                        onChange={(e) => setSearchEmail(e.target.value)}
                    />
                    <Button variant="outline-primary" onClick={handleSearch} disabled={loading}>
                        Search
                    </Button>
                </div>
            </div>

            {loading ? (
                <LoadingSpinner />
            ) : students.length === 0 ? (
                <Alert variant="info">No students found. Add a new student or search by email.</Alert>
            ) : (
                <Table striped bordered hover responsive>
                    <thead>
                    <tr>
                        <th>Email</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {students.map((student) => (
                        <tr key={student.email}>
                            <td>{student.email}</td>
                            <td>{student.firstName}</td>
                            <td>{student.lastName}</td>
                            <td>
                                <div className="d-flex gap-2">
                                    <Button variant="outline-danger" size="sm" onClick={() => handleDelete(student.email)}>
                                        Delete
                                    </Button>
                                </div>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            )}

            {/* Add Student Modal */}
            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Add New Student</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Email</Form.Label>
                            <Form.Control type="email" name="email" value={formData.email} onChange={handleChange} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>First Name</Form.Label>
                            <Form.Control type="text" name="firstName" value={formData.firstName} onChange={handleChange} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Last Name</Form.Label>
                            <Form.Control type="text" name="lastName" value={formData.lastName} onChange={handleChange} required />
                        </Form.Group>
                        <div className="d-flex justify-content-end">
                            <Button variant="secondary" className="me-2" onClick={() => setShowModal(false)}>
                                Cancel
                            </Button>
                            <Button variant="primary" type="submit" disabled={loading}>
                                {loading ? "Creating..." : "Create Student"}
                            </Button>
                        </div>
                    </Form>
                </Modal.Body>
            </Modal>
        </Container>
    )
}

export default StudentsPage
