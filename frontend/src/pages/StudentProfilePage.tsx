"use client"

import { useState } from "react"
import { Container, Row, Col, Card, Button, Tabs, Tab, ListGroup, Badge, ProgressBar } from "react-bootstrap"

const StudentProfilePage = () => {
    const [activeTab, setActiveTab] = useState("dashboard")

    const student = {
        id: "s123",
        name: "Alex Johnson",
        email: "alex.johnson@example.com",
        avatar: "/placeholder.svg?height=150&width=150&query=student",
        joinDate: "January 2023",
        bio: "Software engineering student passionate about web development and AI.",
        enrolledCourses: [
            {
                id: "1",
                title: "Web Development Fundamentals",
                instructor: "John Smith",
                image: "/coding-workspace.png",
                progress: 75,
                lastAccessed: "2 days ago",
            },
            {
                id: "2",
                title: "Data Science Essentials",
                instructor: "Emily Johnson",
                image: "/data-insights-flow.png",
                progress: 45,
                lastAccessed: "1 week ago",
            },
            {
                id: "3",
                title: "Mobile App Development",
                instructor: "Michael Brown",
                image: "/app-development-workflow.png",
                progress: 20,
                lastAccessed: "3 days ago",
            },
        ],
        completedCourses: [
            {
                id: "4",
                title: "Python Programming Basics",
                instructor: "Jessica Lee",
                image: "/placeholder.svg?height=80&width=120&query=python programming",
                completedDate: "March 2023",
                certificate: true,
            },
        ],
        achievements: [
            {
                title: "Fast Learner",
                description: "Completed 5 lessons in one day",
                icon: "trophy",
            },
            {
                title: "Perfect Score",
                description: "Scored 100% on a quiz",
                icon: "award",
            },
            {
                title: "Consistent Learner",
                description: "Logged in for 7 consecutive days",
                icon: "calendar-check",
            },
        ],
        wishlist: [
            {
                id: "5",
                title: "Advanced JavaScript Patterns",
                instructor: "Sarah Wilson",
                image: "/placeholder.svg?height=80&width=120&query=javascript",
                price: 69.99,
            },
            {
                id: "6",
                title: "UI/UX Design Principles",
                instructor: "Alex Rodriguez",
                image: "/placeholder.svg?height=80&width=120&query=ui ux design",
                price: 44.99,
            },
        ],
    }

    return (
        <Container className="py-5">
            <Row>
                <Col lg={4} className="mb-4 mb-lg-0">
                    <Card className="border-0 shadow-sm mb-4">
                        <Card.Body className="text-center">
                            <img
                                src={student.avatar || "/placeholder.svg"}
                                alt={student.name}
                                className="rounded-circle mb-3"
                                width="150"
                                height="150"
                            />
                            <h3>{student.name}</h3>
                            <p className="text-muted mb-3">Student</p>
                            <p className="mb-3">{student.bio}</p>
                            <div className="d-flex justify-content-center mb-3">
                                <Badge bg="primary" className="me-2">
                                    Web Development
                                </Badge>
                                <Badge bg="secondary" className="me-2">
                                    Data Science
                                </Badge>
                                <Badge bg="info">Mobile Apps</Badge>
                            </div>
                            <p className="text-muted small mb-0">
                                <i className="bi bi-calendar3 me-1"></i>
                                Joined {student.joinDate}
                            </p>
                        </Card.Body>
                    </Card>

                    <Card className="border-0 shadow-sm">
                        <Card.Body>
                            <h4 className="mb-3">Achievements</h4>
                            <ListGroup variant="flush">
                                {student.achievements.map((achievement, index) => (
                                    <ListGroup.Item key={index} className="px-0 py-3 d-flex align-items-center border-bottom">
                                        <div className="bg-light rounded-circle p-2 me-3">
                                            <i className={`bi bi-${achievement.icon} text-primary fs-4`}></i>
                                        </div>
                                        <div>
                                            <h5 className="mb-0">{achievement.title}</h5>
                                            <p className="text-muted mb-0 small">{achievement.description}</p>
                                        </div>
                                    </ListGroup.Item>
                                ))}
                            </ListGroup>
                        </Card.Body>
                    </Card>
                </Col>

                <Col lg={8}>
                    <Card className="border-0 shadow-sm mb-4">
                        <Card.Body>
                            <Tabs activeKey={activeTab} onSelect={(k) => setActiveTab(k || "dashboard")} className="mb-4">
                                <Tab eventKey="dashboard" title="Dashboard">
                                    <h4 className="mb-3">My Learning</h4>
                                    <Row className="mb-4">
                                        <Col md={4} className="mb-3 mb-md-0">
                                            <Card className="border-0 bg-primary text-white h-100">
                                                <Card.Body className="d-flex flex-column align-items-center justify-content-center text-center">
                                                    <i className="bi bi-book fs-1 mb-2"></i>
                                                    <h2 className="mb-0">{student.enrolledCourses.length}</h2>
                                                    <p className="mb-0">Courses in Progress</p>
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                        <Col md={4} className="mb-3 mb-md-0">
                                            <Card className="border-0 bg-success text-white h-100">
                                                <Card.Body className="d-flex flex-column align-items-center justify-content-center text-center">
                                                    <i className="bi bi-check-circle fs-1 mb-2"></i>
                                                    <h2 className="mb-0">{student.completedCourses.length}</h2>
                                                    <p className="mb-0">Completed Courses</p>
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                        <Col md={4}>
                                            <Card className="border-0 bg-info text-white h-100">
                                                <Card.Body className="d-flex flex-column align-items-center justify-content-center text-center">
                                                    <i className="bi bi-heart fs-1 mb-2"></i>
                                                    <h2 className="mb-0">{student.wishlist.length}</h2>
                                                    <p className="mb-0">Wishlist Courses</p>
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                    </Row>

                                    <h4 className="mb-3">In Progress</h4>
                                    <ListGroup variant="flush" className="mb-4">
                                        {student.enrolledCourses.map((course, index) => (
                                            <ListGroup.Item key={index} className="px-0 py-3 border-bottom">
                                                <div className="d-flex">
                                                    <img
                                                        src={course.image || "/placeholder.svg"}
                                                        alt={course.title}
                                                        className="me-3 rounded"
                                                        width="120"
                                                        height="80"
                                                    />
                                                    <div className="flex-grow-1">
                                                        <div className="d-flex justify-content-between align-items-start mb-2">
                                                            <h5 className="mb-0">{course.title}</h5>
                                                            <Badge
                                                                bg={course.progress >= 75 ? "success" : course.progress >= 25 ? "warning" : "danger"}
                                                            >
                                                                {course.progress}%
                                                            </Badge>
                                                        </div>
                                                        <p className="text-muted mb-2 small">Instructor: {course.instructor}</p>
                                                        <ProgressBar
                                                            now={course.progress}
                                                            variant={course.progress >= 75 ? "success" : course.progress >= 25 ? "warning" : "danger"}
                                                            className="mb-2"
                                                            style={{ height: "8px" }}
                                                        />
                                                        <div className="d-flex justify-content-between align-items-center">
                                                            <small className="text-muted">Last accessed: {course.lastAccessed}</small>
                                                            <Button variant="primary" size="sm">
                                                                Continue
                                                            </Button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </ListGroup.Item>
                                        ))}
                                    </ListGroup>

                                    <h4 className="mb-3">Completed</h4>
                                    <ListGroup variant="flush">
                                        {student.completedCourses.map((course, index) => (
                                            <ListGroup.Item key={index} className="px-0 py-3 border-bottom">
                                                <div className="d-flex">
                                                    <img
                                                        src={course.image || "/placeholder.svg"}
                                                        alt={course.title}
                                                        className="me-3 rounded"
                                                        width="120"
                                                        height="80"
                                                    />
                                                    <div className="flex-grow-1">
                                                        <div className="d-flex justify-content-between align-items-start mb-2">
                                                            <h5 className="mb-0">{course.title}</h5>
                                                            {course.certificate && (
                                                                <Badge bg="success">
                                                                    <i className="bi bi-patch-check-fill me-1"></i>
                                                                    Certificate
                                                                </Badge>
                                                            )}
                                                        </div>
                                                        <p className="text-muted mb-2 small">Instructor: {course.instructor}</p>
                                                        <div className="d-flex justify-content-between align-items-center">
                                                            <small className="text-muted">Completed: {course.completedDate}</small>
                                                            <div>
                                                                {course.certificate && (
                                                                    <Button variant="outline-primary" size="sm" className="me-2">
                                                                        <i className="bi bi-download me-1"></i>
                                                                        Certificate
                                                                    </Button>
                                                                )}
                                                                <Button variant="outline-secondary" size="sm">
                                                                    <i className="bi bi-arrow-repeat me-1"></i>
                                                                    Review
                                                                </Button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </ListGroup.Item>
                                        ))}
                                    </ListGroup>
                                </Tab>

                                <Tab eventKey="wishlist" title="Wishlist">
                                    <h4 className="mb-3">My Wishlist</h4>
                                    {student.wishlist.length > 0 ? (
                                        <ListGroup variant="flush">
                                            {student.wishlist.map((course, index) => (
                                                <ListGroup.Item key={index} className="px-0 py-3 border-bottom">
                                                    <div className="d-flex">
                                                        <img
                                                            src={course.image || "/placeholder.svg"}
                                                            alt={course.title}
                                                            className="me-3 rounded"
                                                            width="120"
                                                            height="80"
                                                        />
                                                        <div className="flex-grow-1">
                                                            <div className="d-flex justify-content-between align-items-start mb-2">
                                                                <h5 className="mb-0">{course.title}</h5>
                                                                <span className="fw-bold">${course.price}</span>
                                                            </div>
                                                            <p className="text-muted mb-3 small">Instructor: {course.instructor}</p>
                                                            <div className="d-flex">
                                                                <Button variant="primary" size="sm" className="me-2">
                                                                    <i className="bi bi-cart-plus me-1"></i>
                                                                    Add to Cart
                                                                </Button>
                                                                <Button variant="outline-danger" size="sm">
                                                                    <i className="bi bi-trash me-1"></i>
                                                                    Remove
                                                                </Button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </ListGroup.Item>
                                            ))}
                                        </ListGroup>
                                    ) : (
                                        <div className="text-center py-5">
                                            <i className="bi bi-heart fs-1 text-muted"></i>
                                            <h5 className="mt-3">Your wishlist is empty</h5>
                                            <p className="text-muted">Save courses you're interested in by clicking the heart icon</p>
                                            <Button variant="primary" as="a" href="/courses">
                                                Browse Courses
                                            </Button>
                                        </div>
                                    )}
                                </Tab>

                                <Tab eventKey="settings" title="Settings">
                                    <h4 className="mb-4">Profile Settings</h4>
                                    <form>
                                        <Row className="mb-3">
                                            <Col md={6} className="mb-3 mb-md-0">
                                                <label className="form-label">Full Name</label>
                                                <input type="text" className="form-control" defaultValue={student.name} />
                                            </Col>
                                            <Col md={6}>
                                                <label className="form-label">Email</label>
                                                <input type="email" className="form-control" defaultValue={student.email} readOnly />
                                            </Col>
                                        </Row>
                                        <div className="mb-3">
                                            <label className="form-label">Bio</label>
                                            <textarea className="form-control" rows={3} defaultValue={student.bio}></textarea>
                                        </div>
                                        <div className="mb-3">
                                            <label className="form-label">Profile Picture</label>
                                            <div className="d-flex align-items-center">
                                                <img
                                                    src={student.avatar || "/placeholder.svg"}
                                                    alt={student.name}
                                                    className="rounded-circle me-3"
                                                    width="60"
                                                    height="60"
                                                />
                                                <Button variant="outline-primary">Change Picture</Button>
                                            </div>
                                        </div>
                                        <div className="mb-3">
                                            <label className="form-label">Interests (Tags)</label>
                                            <input
                                                type="text"
                                                className="form-control"
                                                defaultValue="Web Development, Data Science, Mobile Apps"
                                            />
                                            <div className="form-text">Separate tags with commas</div>
                                        </div>
                                        <hr className="my-4" />
                                        <h4 className="mb-3">Account Settings</h4>
                                        <div className="mb-3">
                                            <label className="form-label">Password</label>
                                            <input type="password" className="form-control" placeholder="••••••••" />
                                        </div>
                                        <div className="mb-3">
                                            <label className="form-label">Confirm Password</label>
                                            <input type="password" className="form-control" placeholder="••••••••" />
                                        </div>
                                        <div className="mb-4">
                                            <div className="form-check form-switch">
                                                <input className="form-check-input" type="checkbox" id="emailNotifications" defaultChecked />
                                                <label className="form-check-label" htmlFor="emailNotifications">
                                                    Email Notifications
                                                </label>
                                            </div>
                                        </div>
                                        <div className="d-grid gap-2 d-md-flex justify-content-md-end">
                                            <Button variant="primary" type="submit">
                                                Save Changes
                                            </Button>
                                        </div>
                                    </form>
                                </Tab>
                            </Tabs>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    )
}

export default StudentProfilePage
