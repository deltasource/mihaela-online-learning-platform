"use client"

import { useState } from "react"
import { Container, Row, Col, Card, Button, Tabs, Tab, ListGroup, Badge, ProgressBar } from "react-bootstrap"

const InstructorProfilePage = () => {
    const [activeTab, setActiveTab] = useState("dashboard")

    const instructor = {
        id: "i456",
        name: "John Smith",
        email: "john.smith@example.com",
        avatar: "/placeholder.svg?height=150&width=150&query=instructor",
        joinDate: "March 2022",
        bio: "Senior Web Developer with 10+ years of experience. Passionate about teaching and helping others learn to code.",
        expertise: ["Web Development", "JavaScript", "React", "Node.js"],
        socialLinks: {
            website: "https://johnsmith.dev",
            twitter: "https://twitter.com/johnsmith",
            linkedin: "https://linkedin.com/in/johnsmith",
            github: "https://github.com/johnsmith",
        },
        courses: [
            {
                id: "1",
                title: "Web Development Fundamentals",
                image: "/coding-workspace.png",
                students: 1234,
                rating: 4.8,
                reviews: 256,
                revenue: 12450,
                published: true,
                lastUpdated: "2 months ago",
            },
            {
                id: "7",
                title: "Advanced React Patterns",
                image: "/placeholder.svg?height=80&width=120&query=react",
                students: 876,
                rating: 4.9,
                reviews: 189,
                revenue: 8760,
                published: true,
                lastUpdated: "1 month ago",
            },
            {
                id: "8",
                title: "Node.js API Development",
                image: "/placeholder.svg?height=80&width=120&query=nodejs",
                students: 543,
                rating: 4.7,
                reviews: 98,
                revenue: 5430,
                published: true,
                lastUpdated: "3 months ago",
            },
        ],
        draftCourses: [
            {
                id: "draft1",
                title: "Full Stack JavaScript Bootcamp",
                image: "/placeholder.svg?height=80&width=120&query=javascript bootcamp",
                progress: 75,
                lastEdited: "2 days ago",
            },
        ],
        earnings: {
            total: 26640,
            thisMonth: 3450,
            lastMonth: 4120,
            history: [
                { month: "Jan", amount: 2100 },
                { month: "Feb", amount: 2350 },
                { month: "Mar", amount: 3200 },
                { month: "Apr", amount: 4120 },
                { month: "May", amount: 3450 },
            ],
        },
        students: {
            total: 2653,
            newThisMonth: 234,
            countries: [
                { name: "United States", count: 876 },
                { name: "India", count: 543 },
                { name: "United Kingdom", count: 321 },
                { name: "Canada", count: 234 },
                { name: "Australia", count: 187 },
            ],
        },
    }

    // @ts-ignore
    // @ts-ignore
    return (
        <Container className="py-5">
            <Row>
                <Col lg={4} className="mb-4 mb-lg-0">
                    <Card className="border-0 shadow-sm mb-4">
                        <Card.Body className="text-center">
                            <img
                                src={instructor.avatar || "/placeholder.svg"}
                                alt={instructor.name}
                                className="rounded-circle mb-3"
                                width="150"
                                height="150"
                            />
                            <h3>{instructor.name}</h3>
                            <p className="text-muted mb-3">Instructor</p>
                            <p className="mb-3">{instructor.bio}</p>
                            <div className="d-flex justify-content-center flex-wrap mb-3">
                                {instructor.expertise.map((skill, index) => (
                                    <Badge key={index} bg="primary" className="me-2 mb-2">
                                        {skill}
                                    </Badge>
                                ))}
                            </div>
                            <div className="d-flex justify-content-center mb-3">
                                <a
                                    href={instructor.socialLinks.website}
                                    className="btn btn-outline-secondary btn-sm rounded-circle me-2"
                                >
                                    <i className="bi bi-globe"></i>
                                </a>
                                <a
                                    href={instructor.socialLinks.twitter}
                                    className="btn btn-outline-secondary btn-sm rounded-circle me-2"
                                >
                                    <i className="bi bi-twitter"></i>
                                </a>
                                <a
                                    href={instructor.socialLinks.linkedin}
                                    className="btn btn-outline-secondary btn-sm rounded-circle me-2"
                                >
                                    <i className="bi bi-linkedin"></i>
                                </a>
                                <a href={instructor.socialLinks.github} className="btn btn-outline-secondary btn-sm rounded-circle">
                                    <i className="bi bi-github"></i>
                                </a>
                            </div>
                            <p className="text-muted small mb-0">
                                <i className="bi bi-calendar3 me-1"></i>
                                Joined {instructor.joinDate}
                            </p>
                        </Card.Body>
                    </Card>

                    <Card className="border-0 shadow-sm">
                        <Card.Body>
                            <h4 className="mb-3">Statistics</h4>
                            <ListGroup variant="flush">
                                <ListGroup.Item className="px-0 py-3 d-flex justify-content-between align-items-center border-bottom">
                                    <div>
                                        <h5 className="mb-0">Total Students</h5>
                                        <p className="text-muted mb-0 small">Across all courses</p>
                                    </div>
                                    <Badge bg="primary" pill className="fs-6">
                                        {instructor.students.total}
                                    </Badge>
                                </ListGroup.Item>
                                <ListGroup.Item className="px-0 py-3 d-flex justify-content-between align-items-center border-bottom">
                                    <div>
                                        <h5 className="mb-0">Total Courses</h5>
                                        <p className="text-muted mb-0 small">Published courses</p>
                                    </div>
                                    <Badge bg="success" pill className="fs-6">
                                        {instructor.courses.length}
                                    </Badge>
                                </ListGroup.Item>
                                <ListGroup.Item className="px-0 py-3 d-flex justify-content-between align-items-center border-bottom">
                                    <div>
                                        <h5 className="mb-0">Average Rating</h5>
                                        <p className="text-muted mb-0 small">Based on all reviews</p>
                                    </div>
                                    <div className="d-flex align-items-center">
                                        <i className="bi bi-star-fill text-warning me-1"></i>
                                        <span className="fw-bold">4.8</span>
                                    </div>
                                </ListGroup.Item>
                                <ListGroup.Item className="px-0 py-3 d-flex justify-content-between align-items-center">
                                    <div>
                                        <h5 className="mb-0">Total Revenue</h5>
                                        <p className="text-muted mb-0 small">Lifetime earnings</p>
                                    </div>
                                    <span className="fw-bold">${instructor.earnings.total.toLocaleString()}</span>
                                </ListGroup.Item>
                            </ListGroup>
                        </Card.Body>
                    </Card>
                </Col>

                <Col lg={8}>
                    <Card className="border-0 shadow-sm mb-4">
                        <Card.Body>
                            <Tabs activeKey={activeTab} onSelect={(k) => setActiveTab(k || "dashboard")} className="mb-4">
                                <Tab eventKey="dashboard" title="Dashboard">
                                    <h4 className="mb-3">Overview</h4>
                                    <Row className="mb-4">
                                        <Col md={3} className="mb-3 mb-md-0">
                                            <Card className="border-0 bg-primary text-white h-100">
                                                <Card.Body className="d-flex flex-column align-items-center justify-content-center text-center">
                                                    <i className="bi bi-people fs-1 mb-2"></i>
                                                    <h2 className="mb-0">{instructor.students.total}</h2>
                                                    <p className="mb-0">Total Students</p>
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                        <Col md={3} className="mb-3 mb-md-0">
                                            <Card className="border-0 bg-success text-white h-100">
                                                <Card.Body className="d-flex flex-column align-items-center justify-content-center text-center">
                                                    <i className="bi bi-book fs-1 mb-2"></i>
                                                    <h2 className="mb-0">{instructor.courses.length}</h2>
                                                    <p className="mb-0">Courses</p>
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                        <Col md={3} className="mb-3 mb-md-0">
                                            <Card className="border-0 bg-info text-white h-100">
                                                <Card.Body className="d-flex flex-column align-items-center justify-content-center text-center">
                                                    <i className="bi bi-star fs-1 mb-2"></i>
                                                    <h2 className="mb-0">4.8</h2>
                                                    <p className="mb-0">Avg. Rating</p>
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                        <Col md={3}>
                                            <Card className="border-0 bg-warning text-white h-100">
                                                <Card.Body className="d-flex flex-column align-items-center justify-content-center text-center">
                                                    <i className="bi bi-currency-dollar fs-1 mb-2"></i>
                                                    <h2 className="mb-0">${instructor.earnings.thisMonth.toLocaleString()}</h2>
                                                    <p className="mb-0">This Month</p>
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                    </Row>

                                    <h4 className="mb-3">My Courses</h4>
                                    <ListGroup variant="flush" className="mb-4">
                                        {instructor.courses.map((course, index) => (
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
                                                            <Badge bg={course.published ? "success" : "secondary"}>
                                                                {course.published ? "Published" : "Draft"}
                                                            </Badge>
                                                        </div>
                                                        <div className="d-flex flex-wrap mb-2">
                                                            <div className="me-3">
                                                                <i className="bi bi-people-fill me-1"></i>
                                                                <span>{course.students} students</span>
                                                            </div>
                                                            <div className="me-3">
                                                                <i className="bi bi-star-fill text-warning me-1"></i>
                                                                <span>
                                  {course.rating} ({course.reviews} reviews)
                                </span>
                                                            </div>
                                                            <div>
                                                                <i className="bi bi-clock-history me-1"></i>
                                                                <span>Updated {course.lastUpdated}</span>
                                                            </div>
                                                        </div>
                                                        <div className="d-flex justify-content-between align-items-center">
                                                            <span className="fw-bold">${course.revenue.toLocaleString()} earned</span>
                                                            <div>
                                                                <Button variant="outline-primary" size="sm" className="me-2">
                                                                    <i className="bi bi-pencil me-1"></i>
                                                                    Edit
                                                                </Button>
                                                                <Button variant="outline-secondary" size="sm">
                                                                    <i className="bi bi-bar-chart me-1"></i>
                                                                    Analytics
                                                                </Button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </ListGroup.Item>
                                        ))}
                                    </ListGroup>

                                    <h4 className="mb-3">Draft Courses</h4>
                                    {instructor.draftCourses.length > 0 ? (
                                        <ListGroup variant="flush" className="mb-4">
                                            {instructor.draftCourses.map((course, index) => (
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
                                                                <Badge bg="secondary">Draft</Badge>
                                                            </div>
                                                            <p className="text-muted mb-2 small">Last edited: {course.lastEdited}</p>
                                                            <ProgressBar
                                                                now={course.progress}
                                                                variant="primary"
                                                                className="mb-2"
                                                                style={{ height: "8px" }}
                                                            />
                                                            <div className="d-flex justify-content-between align-items-center">
                                                                <span>{course.progress}% complete</span>
                                                                <Button variant="primary" size="sm">
                                                                    Continue Editing
                                                                </Button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </ListGroup.Item>
                                            ))}
                                        </ListGroup>
                                    ) : (
                                        <Card className="border-0 bg-light mb-4">
                                            <Card.Body className="text-center py-4">
                                                <i className="bi bi-pencil-square fs-1 text-muted mb-3"></i>
                                                <h5>No draft courses</h5>
                                                <p className="text-muted mb-3">Start creating a new course today</p>
                                                <Button variant="primary">
                                                    <i className="bi bi-plus-circle me-2"></i>
                                                    Create New Course
                                                </Button>
                                            </Card.Body>
                                        </Card>
                                    )}

                                    <div className="d-flex justify-content-between align-items-center mb-3">
                                        <h4 className="mb-0">Earnings</h4>
                                        <div className="text-muted">
                                            This Month:{" "}
                                            <span className="fw-bold text-success">${instructor.earnings.thisMonth.toLocaleString()}</span>
                                        </div>
                                    </div>
                                    <Card className="border-0 bg-light mb-4">
                                        <Card.Body>
                                            <div className="d-flex justify-content-between mb-4">
                                                {instructor.earnings.history.map((item, index) => (
                                                    <div key={index} className="text-center">
                                                        <div className="position-relative">
                                                            <div
                                                                className="bg-primary rounded"
                                                                style={{
                                                                    height: `${(item.amount / Math.max(...instructor.earnings.history.map((i) => i.amount))) * 100}px`,
                                                                    width: "30px",
                                                                }}
                                                            ></div>
                                                        </div>
                                                        <div className="mt-2">{item.month}</div>
                                                    </div>
                                                ))}
                                            </div>
                                        </Card.Body>
                                    </Card>

                                    <h4 className="mb-3">Student Demographics</h4>
                                    <Row>
                                        <Col md={6}>
                                            <Card className="border-0 bg-light h-100">
                                                <Card.Body>
                                                    <h5 className="mb-3">Top Countries</h5>
                                                    <ListGroup variant="flush">
                                                        {instructor.students.countries.map((country, index) => (
                                                            <ListGroup.Item
                                                                key={index}
                                                                className="px-0 py-2 d-flex justify-content-between align-items-center border-0 bg-transparent"
                                                            >
                                                                <span>{country.name}</span>
                                                                <Badge bg="primary" pill>
                                                                    {country.count}
                                                                </Badge>
                                                            </ListGroup.Item>
                                                        ))}
                                                    </ListGroup>
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                        <Col md={6}>
                                            <Card className="border-0 bg-light h-100">
                                                <Card.Body className="d-flex flex-column justify-content-center align-items-center text-center">
                                                    <div className="mb-3">
                                                        <i className="bi bi-people-fill fs-1 text-primary"></i>
                                                    </div>
                                                    <h5 className="mb-2">New Students This Month</h5>
                                                    <h2 className="mb-0">{instructor.students.newThisMonth}</h2>
                                                    <p className="text-muted">
                                                        {((instructor.students.newThisMonth / instructor.students.total) * 100).toFixed(1)}% growth
                                                    </p>
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                    </Row>
                                </Tab>

                                <Tab eventKey="courses" title="Courses">
                                    <div className="d-flex justify-content-between align-items-center mb-4">
                                        <h4 className="mb-0">My Courses</h4>
                                        <Button variant="primary">
                                            <i className="bi bi-plus-circle me-2"></i>
                                            Create New Course
                                        </Button>
                                    </div>

                                    <Row>
                                        {instructor.courses.concat(instructor.draftCourses).map((course, index) => (
                                            <Col md={6} key={index} className="mb-4">
                                                <Card className="h-100 border-0 shadow-sm">
                                                    <Card.Img variant="top" src={course.image} alt={course.title} height="160" />
                                                    <Card.Body>
                                                        <div className="d-flex justify-content-between align-items-start mb-2">
                                                            <Card.Title>{course.title}</Card.Title>
                                                            <Badge
                                                                bg={"published" in course ? (course.published ? "success" : "secondary") : "secondary"}
                                                            >
                                                                {"published" in course ? (course.published ? "Published" : "Draft") : "Draft"}
                                                            </Badge>
                                                        </div>
                                                        {"students" in course ? (
                                                            <div className="d-flex flex-wrap mb-3">
                                                                <div className="me-3">
                                                                    <i className="bi bi-people-fill me-1"></i>
                                                                    <span>{course.students} students</span>
                                                                </div>
                                                                <div>
                                                                    <i className="bi bi-star-fill text-warning me-1"></i>
                                                                    <span>
                                    {course.rating} ({course.reviews} reviews)
                                  </span>
                                                                </div>
                                                            </div>
                                                        ) : (
                                                            <div className="mb-3">
                                                                <ProgressBar
                                                                    now={"progress" in course ? course.progress : 0}
                                                                    variant="primary"
                                                                    className="mb-2"
                                                                    style={{ height: "8px" }}
                                                                />
                                                                <small className="text-muted">
                                                                    {"progress" in course ? `${course.progress}% complete` : ""}
                                                                </small>
                                                            </div>
                                                        )}
                                                        <div className="d-flex justify-content-between align-items-center">
                                                            {"revenue" in course ? (
                                                                <span className="fw-bold">${course.revenue.toLocaleString()}</span>
                                                            ) : (
                                                                <span className="text-muted">
                                  Last edited: {"lastEdited" in course ? course.lastEdited : ""}
                                </span>
                                                            )}
                                                            <Button variant="outline-primary" size="sm">
                                                                <i className="bi bi-pencil me-1"></i>
                                                                Edit
                                                            </Button>
                                                        </div>
                                                    </Card.Body>
                                                </Card>
                                            </Col>
                                        ))}
                                    </Row>
                                </Tab>

                                <Tab eventKey="analytics" title="Analytics">
                                    <h4 className="mb-4">Course Analytics</h4>

                                    <Row className="mb-4">
                                        <Col md={3} className="mb-3 mb-md-0">
                                            <Card className="border-0 bg-light h-100">
                                                <Card.Body className="text-center">
                                                    <div className="mb-2">
                                                        <i className="bi bi-eye fs-1 text-primary"></i>
                                                    </div>
                                                    <h5>Course Views</h5>
                                                    <h2 className="mb-0">12,456</h2>
                                                    <p className="text-success mb-0">
                                                        <i className="bi bi-arrow-up"></i> 12% this month
                                                    </p>
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                        <Col md={3} className="mb-3 mb-md-0">
                                            <Card className="border-0 bg-light h-100">
                                                <Card.Body className="text-center">
                                                    <div className="mb-2">
                                                        <i className="bi bi-graph-up fs-1 text-success"></i>
                                                    </div>
                                                    <h5>Conversion Rate</h5>
                                                    <h2 className="mb-0">8.3%</h2>
                                                    <p className="text-success mb-0">
                                                        <i className="bi bi-arrow-up"></i> 2.1% this month
                                                    </p>
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                        <Col md={3} className="mb-3 mb-md-0">
                                            <Card className="border-0 bg-light h-100">
                                                <Card.Body className="text-center">
                                                    <div className="mb-2">
                                                        <i className="bi bi-people fs-1 text-info"></i>
                                                    </div>
                                                    <h5>New Students</h5>
                                                    <h2 className="mb-0">{instructor.students.newThisMonth}</h2>
                                                    <p className="text-success mb-0">
                                                        <i className="bi bi-arrow-up"></i> 8% this month
                                                    </p>
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                        <Col md={3}>
                                            <Card className="border-0 bg-light h-100">
                                                <Card.Body className="text-center">
                                                    <div className="mb-2">
                                                        <i className="bi bi-star fs-1 text-warning"></i>
                                                    </div>
                                                    <h5>New Reviews</h5>
                                                    <h2 className="mb-0">42</h2>
                                                    <p className="text-success mb-0">
                                                        <i className="bi bi-arrow-up"></i> 15% this month
                                                    </p>
                                                </Card.Body>
                                            </Card>
                                        </Col>
                                    </Row>

                                    <Card className="border-0 shadow-sm mb-4">
                                        <Card.Body>
                                            <h5 className="mb-3">Course Performance</h5>
                                            <table className="table">
                                                <thead>
                                                <tr>
                                                    <th>Course</th>
                                                    <th>Students</th>
                                                    <th>Rating</th>
                                                    <th>Revenue</th>
                                                    <th>Completion Rate</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                {instructor.courses.map((course, index) => (
                                                    <tr key={index}>
                                                        <td>{course.title}</td>
                                                        <td>{course.students}</td>
                                                        <td>
                                                            <i className="bi bi-star-fill text-warning me-1"></i>
                                                            {course.rating}
                                                        </td>
                                                        <td>${course.revenue.toLocaleString()}</td>
                                                        <td>
                                                            <div className="d-flex align-items-center">
                                                                <ProgressBar
                                                                    now={75 - index * 5}
                                                                    variant="success"
                                                                    className="flex-grow-1 me-2"
                                                                    style={{ height: "8px" }}
                                                                />
                                                                <span>{75 - index * 5}%</span>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                ))}
                                                </tbody>
                                            </table>
                                        </Card.Body>
                                    </Card>

                                    <h4 className="mb-3">Student Engagement</h4>
                                    <Card className="border-0 shadow-sm">
                                        <Card.Body>
                                            <h5 className="mb-3">Most Active Students</h5>
                                            <ListGroup variant="flush">
                                                {[1, 2, 3, 4, 5].map((_, index) => (
                                                    <ListGroup.Item key={index} className="px-0 py-3 d-flex align-items-center border-bottom">
                                                        <img
                                                            src={`/diverse-group-city.png?height=50&width=50&query=person${index}`}
                                                            alt="Student"
                                                            className="rounded-circle me-3"
                                                            width="50"
                                                            height="50"
                                                        />
                                                        <div className="flex-grow-1">
                                                            <h6 className="mb-0">Student Name {index + 1}</h6>
                                                            <p className="text-muted mb-0 small">Enrolled in {3 - (index % 3)} courses</p>
                                                        </div>
                                                        <div className="text-end">
                                                            <div className="mb-1">
                                                                <i className="bi bi-chat-left-text me-1"></i>
                                                                <span>{20 - index * 3} comments</span>
                                                            </div>
                                                            <div>
                                                                <i className="bi bi-star-fill text-warning me-1"></i>
                                                                <span>{5 - (index % 2)} reviews</span>
                                                            </div>
                                                        </div>
                                                    </ListGroup.Item>
                                                ))}
                                            </ListGroup>
                                        </Card.Body>
                                    </Card>
                                </Tab>

                                <Tab eventKey="settings" title="Settings">
                                    <h4 className="mb-4">Profile Settings</h4>
                                    <form>
                                        <Row className="mb-3">
                                            <Col md={6} className="mb-3 mb-md-0">
                                                <label className="form-label">Full Name</label>
                                                <input type="text" className="form-control" defaultValue={instructor.name} />
                                            </Col>
                                            <Col md={6}>
                                                <label className="form-label">Email</label>
                                                <input type="email" className="form-control" defaultValue={instructor.email} readOnly />
                                            </Col>
                                        </Row>
                                        <div className="mb-3">
                                            <label className="form-label">Bio</label>
                                            <textarea className="form-control" rows={3} defaultValue={instructor.bio}></textarea>
                                            <div className="form-text">Tell students about yourself and your teaching experience</div>
                                        </div>
                                        <div className="mb-3">
                                            <label className="form-label">Profile Picture</label>
                                            <div className="d-flex align-items-center">
                                                <img
                                                    src={instructor.avatar || "/placeholder.svg"}
                                                    alt={instructor.name}
                                                    className="rounded-circle me-3"
                                                    width="60"
                                                    height="60"
                                                />
                                                <Button variant="outline-primary">Change Picture</Button>
                                            </div>
                                        </div>
                                        <div className="mb-3">
                                            <label className="form-label">Areas of Expertise</label>
                                            <input type="text" className="form-control" defaultValue={instructor.expertise.join(", ")} />
                                            <div className="form-text">Separate tags with commas</div>
                                        </div>
                                        <hr className="my-4" />
                                        <h4 className="mb-3">Social Links</h4>
                                        <Row className="mb-3">
                                            <Col md={6} className="mb-3 mb-md-0">
                                                <label className="form-label">Website</label>
                                                <input type="url" className="form-control" defaultValue={instructor.socialLinks.website} />
                                            </Col>
                                            <Col md={6}>
                                                <label className="form-label">Twitter</label>
                                                <input type="url" className="form-control" defaultValue={instructor.socialLinks.twitter} />
                                            </Col>
                                        </Row>
                                        <Row className="mb-4">
                                            <Col md={6} className="mb-3 mb-md-0">
                                                <label className="form-label">LinkedIn</label>
                                                <input type="url" className="form-control" defaultValue={instructor.socialLinks.linkedin} />
                                            </Col>
                                            <Col md={6}>
                                                <label className="form-label">GitHub</label>
                                                <input type="url" className="form-control" defaultValue={instructor.socialLinks.github} />
                                            </Col>
                                        </Row>
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

export default InstructorProfilePage
