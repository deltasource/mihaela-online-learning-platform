import type React from "react";
import { useState, useEffect } from "react";
import {
    Container,
    Table,
    Button,
    Alert,
    Modal,
    Form,
    Dropdown,
} from "react-bootstrap";
import {
    getInstructorByEmail,
    createInstructor,
    deleteInstructor,
} from "../../services/instructorService";
import {
    getCoursesByInstructor,
    getVideosByCourse,
} from "../../services/courseService";
import type { Instructor } from "../../types/instructor";
import type { Course } from "../../types/course";
import type { Video } from "../../types/video";
import LoadingSpinner from "../../components/common/LoadingSpinner";
import { toast } from "react-toastify";

const InstructorsPage = () => {
    const [instructors, setInstructors] = useState<Instructor[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [showModal, setShowModal] = useState(false);
    const [searchEmail, setSearchEmail] = useState("");
    const [formData, setFormData] = useState<Omit<Instructor, "id">>({
        email: "",
        firstName: "",
        lastName: "",
        department: "",
    });

    const [courses, setCourses] = useState<Course[]>([]);
    const [selectedCourse, setSelectedCourse] = useState<Course | null>(null);
    const [videos, setVideos] = useState<Video[]>([]);

    const handleSearch = async () => {
        if (!searchEmail.trim()) {
            setError("Please enter an email to search");
            return;
        }
        try {
            setLoading(true);
            setError(null);

            const instructor = await getInstructorByEmail(searchEmail);
            if (!instructors.some((i) => i.email === instructor.email)) {
                setInstructors([...instructors, instructor]);
            }
            setSearchEmail("");
        } catch (err) {
            setError(`Instructor with email ${searchEmail} not found`);
        } finally {
            setLoading(false);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            setLoading(true);
            setError(null);

            const newInstructor = await createInstructor(formData);
            setInstructors([...instructors, newInstructor]);
            setShowModal(false);
            setFormData({
                email: "",
                firstName: "",
                lastName: "",
                department: "",
            });

            toast.success("Instructor created successfully!");
        } catch (err) {
            setError("Failed to create instructor. Please check your inputs and try again.");
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (email: string) => {
        if (window.confirm("Are you sure you want to delete this instructor?")) {
            try {
                setLoading(true);
                await deleteInstructor(email);
                setInstructors(instructors.filter((i) => i.email !== email));
                toast.success("Instructor deleted successfully!");
            } catch (err) {
                setError("Failed to delete instructor");
            } finally {
                setLoading(false);
            }
        }
    };

    const fetchCoursesByInstructor = async (instructorEmail: string) => {
        try {
            setLoading(true);
            const fetchedCourses = await getCoursesByInstructor(instructorEmail);
            setCourses(fetchedCourses);
        } catch (err) {
            setError("Failed to fetch courses");
        } finally {
            setLoading(false);
        }
    };

    const fetchVideosByCourse = async (courseId: string) => {
        try {
            setLoading(true);
            const course = courses.find((c) => c.id === courseId);
            if (course) {
                setSelectedCourse(course);
            }
            const fetchedVideos = await getVideosByCourse(courseId);
            setVideos(fetchedVideos);
        } catch (err) {
            setError("Failed to fetch videos");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (instructors.length > 0) {
            fetchCoursesByInstructor(instructors[0].email);
        }
    }, [instructors]);

    return (
        <Container>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h1 className="page-title">Instructors</h1>
                    <p className="page-description">Manage instructors for your courses</p>
                </div>
                <Button variant="primary" onClick={() => setShowModal(true)}>
                    Add New Instructor
                </Button>
            </div>

            {error && <Alert variant="danger">{error}</Alert>}

            <div className="mb-4 d-flex gap-2">
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

            {loading ? (
                <LoadingSpinner />
            ) : instructors.length === 0 ? (
                <Alert variant="info">No instructors found. Add a new instructor or search by email.</Alert>
            ) : (
                <Table striped bordered hover responsive>
                    <thead>
                    <tr>
                        <th>Email</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Department</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {instructors.map((instructor) => (
                        <tr key={instructor.email}>
                            <td>{instructor.email}</td>
                            <td>{instructor.firstName}</td>
                            <td>{instructor.lastName}</td>
                            <td>{instructor.department}</td>
                            <td>
                                <Button
                                    variant="outline-danger"
                                    size="sm"
                                    onClick={() => handleDelete(instructor.email)}
                                >
                                    Delete
                                </Button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            )}

            {instructors.length > 0 && (
                <div className="mt-4">
                    <h3>Courses</h3>
                    {loading ? (
                        <LoadingSpinner />
                    ) : courses.length === 0 ? (
                        <Alert variant="info">No courses found for this instructor.</Alert>
                    ) : (
                        <Dropdown onSelect={(courseId) => fetchVideosByCourse(courseId!)}>
                            <Dropdown.Toggle variant="success" id="dropdown-basic">
                                Select Course
                            </Dropdown.Toggle>
                            <Dropdown.Menu>
                                {courses.map((course) => (
                                    <Dropdown.Item key={course.id} eventKey={course.id}>
                                        {course.name}
                                    </Dropdown.Item>
                                ))}
                            </Dropdown.Menu>
                        </Dropdown>
                    )}
                </div>
            )}

            {selectedCourse && (
                <div className="mt-4">
                    <h3>Videos for {selectedCourse.name}</h3>
                    {loading ? (
                        <LoadingSpinner />
                    ) : videos.length === 0 ? (
                        <Alert variant="info">No videos available for this course.</Alert>
                    ) : (
                        <Table striped bordered hover responsive>
                            <thead>
                            <tr>
                                <th>Video Title</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            {videos.map((video) => (
                                <tr key={video.id}>
                                    <td>{video.title}</td>
                                    <td>
                                        <Button variant="outline-danger" size="sm">
                                            Delete
                                        </Button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </Table>
                    )}
                </div>
            )}

            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Add New Instructor</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>First Name</Form.Label>
                            <Form.Control
                                type="text"
                                name="firstName"
                                value={formData.firstName}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Last Name</Form.Label>
                            <Form.Control
                                type="text"
                                name="lastName"
                                value={formData.lastName}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Department</Form.Label>
                            <Form.Control
                                type="text"
                                name="department"
                                value={formData.department}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <div className="d-flex justify-content-end">
                            <Button variant="secondary" className="me-2" onClick={() => setShowModal(false)}>
                                Cancel
                            </Button>
                            <Button variant="primary" type="submit" disabled={loading}>
                                {loading ? "Creating..." : "Create Instructor"}
                            </Button>
                        </div>
                    </Form>
                </Modal.Body>
            </Modal>
        </Container>
    );
};

export default InstructorsPage;
