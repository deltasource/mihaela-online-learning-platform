import { Container, Row, Col, Card, Button } from "react-bootstrap"
import { Link } from "react-router-dom"

const HomePage = () => {
    const features = [
        {
            title: "Expert Instructors",
            description: "Learn from industry professionals with years of experience.",
            icon: "bi bi-person-check-fill",
        },
        {
            title: "Flexible Learning",
            description: "Study at your own pace, anywhere and anytime.",
            icon: "bi bi-clock-history",
        },
        {
            title: "Interactive Content",
            description: "Engage with quizzes, projects, and hands-on exercises.",
            icon: "bi bi-hand-index-thumb",
        },
    ]

    const popularCourses = [
        {
            id: "1",
            title: "Web Development Fundamentals",
            instructor: "John Smith",
            image: "/coding-workspace.png",
            rating: 4.8,
            students: 1234,
        },
        {
            id: "2",
            title: "Data Science Essentials",
            instructor: "Emily Johnson",
            image: "/data-insights-flow.png",
            rating: 4.7,
            students: 987,
        },
        {
            id: "3",
            title: "Mobile App Development",
            instructor: "Michael Brown",
            image: "/app-development-workflow.png",
            rating: 4.9,
            students: 1567,
        },
    ]

    // @ts-ignore
    return (
        <>
            <div className="bg-primary text-white py-5">
                <Container>
                    <Row className="align-items-center">
                        <Col lg={6} className="mb-4 mb-lg-0">
                            <h1 className="display-4 fw-bold mb-3">Unlock Your Potential with Online Learning</h1>
                            <p className="lead mb-4">
                                Discover thousands of courses taught by expert instructors to help you achieve your goals.
                            </p>
                            <div className="d-flex gap-3">
                                <Button as={Link} to="/courses" variant="light" size="lg">
                                    Explore Courses
                                </Button>
                                <Button as={Link} to="/register" variant="outline-light" size="lg">
                                    Join For Free
                                </Button>
                            </div>
                        </Col>
                        <Col lg={6}>
                            <img src="/connected-learning-globe.png" alt="Online Learning" className="img-fluid rounded shadow" />
                        </Col>
                    </Row>
                </Container>
            </div>
            
            <Container className="py-5">
                <h2 className="text-center mb-5">Why Choose ?</h2>
                <Row>
                    {features.map((feature, index) => (
                        <Col md={4} key={index} className="mb-4">
                            <Card className="h-100 border-0 shadow-sm">
                                <Card.Body className="text-center p-4">
                                    <div className="mb-3">
                                        <i className={`${feature.icon} fs-1 text-primary`}></i>
                                    </div>
                                    <Card.Title>{feature.title}</Card.Title>
                                    <Card.Text className="text-muted">{feature.description}</Card.Text>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>
            </Container>
            
            <div className="bg-light py-5">
                <Container>
                    <div className="d-flex justify-content-between align-items-center mb-4">
                        <h2>Popular Courses</h2>
                        <Button as={Link} to="/courses" variant="outline-primary">
                            View All
                        </Button>
                    </div>
                    <Row>
                        {popularCourses.map((course) => (
                            <Col md={4} key={course.id} className="mb-4">
                                <Card className="h-100 border-0 shadow-sm">
                                    <Card.Img variant="top" src={course.image} alt={course.title} />
                                    <Card.Body>
                                        <Card.Title as={Link} to={`/courses/${course.id}`} className="text-decoration-none text-dark">
                                            {course.title}
                                        </Card.Title>
                                        <Card.Text className="text-muted">Instructor: {course.instructor}</Card.Text>
                                        <div className="d-flex justify-content-between">
                                            <div>
                                                <i className="bi bi-star-fill text-warning me-1"></i>
                                                <span>{course.rating}</span>
                                            </div>
                                            <div>
                                                <i className="bi bi-people-fill me-1"></i>
                                                <span>{course.students} students</span>
                                            </div>
                                        </div>
                                    </Card.Body>
                                </Card>
                            </Col>
                        ))}
                    </Row>
                </Container>
            </div>
            
            <Container className="py-5">
                <h2 className="text-center mb-5">What Our Students Say</h2>
                <Row>
                    <Col md={4} className="mb-4">
                        <Card className="h-100 border-0 shadow-sm">
                            <Card.Body className="p-4">
                                <div className="mb-3 text-warning">
                                    <i className="bi bi-star-fill"></i>
                                    <i className="bi bi-star-fill"></i>
                                    <i className="bi bi-star-fill"></i>
                                    <i className="bi bi-star-fill"></i>
                                    <i className="bi bi-star-fill"></i>
                                </div>
                                <Card.Text className="mb-3">
                                    " has completely transformed my career. The courses are well-structured and the instructors
                                    are top-notch."
                                </Card.Text>
                                <div className="d-flex align-items-center">
                                    <img
                                        src="/diverse-group-city.png"
                                        alt="Student"
                                        className="rounded-circle me-3"
                                        width="50"
                                        height="50"
                                    />
                                    <div>
                                        <h6 className="mb-0">Sarah Johnson</h6>
                                        <small className="text-muted">Web Developer</small>
                                    </div>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={4} className="mb-4">
                        <Card className="h-100 border-0 shadow-sm">
                            <Card.Body className="p-4">
                                <div className="mb-3 text-warning">
                                    <i className="bi bi-star-fill"></i>
                                    <i className="bi bi-star-fill"></i>
                                    <i className="bi bi-star-fill"></i>
                                    <i className="bi bi-star-fill"></i>
                                    <i className="bi bi-star-fill"></i>
                                </div>
                                <Card.Text className="mb-3">
                                    "The flexibility of  allowed me to learn while working full-time. The quality of content is
                                    exceptional."
                                </Card.Text>
                                <div className="d-flex align-items-center">
                                    <img
                                        src="/diverse-group-city.png"
                                        alt="Student"
                                        className="rounded-circle me-3"
                                        width="50"
                                        height="50"
                                    />
                                    <div>
                                        <h6 className="mb-0">David Chen</h6>
                                        <small className="text-muted">Data Analyst</small>
                                    </div>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={4} className="mb-4">
                        <Card className="h-100 border-0 shadow-sm">
                            <Card.Body className="p-4">
                                <div className="mb-3 text-warning">
                                    <i className="bi bi-star-fill"></i>
                                    <i className="bi bi-star-fill"></i>
                                    <i className="bi bi-star-fill"></i>
                                    <i className="bi bi-star-fill"></i>
                                    <i className="bi bi-star-fill"></i>
                                </div>
                                <Card.Text className="mb-3">
                                    "I've tried many platforms, but  stands out with its interactive approach and supportive
                                    community."
                                </Card.Text>
                                <div className="d-flex align-items-center">
                                    <img
                                        src="/diverse-group-city.png"
                                        alt="Student"
                                        className="rounded-circle me-3"
                                        width="50"
                                        height="50"
                                    />
                                    <div>
                                        <h6 className="mb-0">Maria Garcia</h6>
                                        <small className="text-muted">UX Designer</small>
                                    </div>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
            
            <div className="bg-primary text-white py-5">
                <Container className="text-center">
                    <h2 className="mb-3">Ready to Start Learning?</h2>
                    <p className="lead mb-4">Join thousands of students already learning on Elearning </p>
                    <Button as={Link} to="/register" variant="light" size="lg">
                        Get Started Today
                    </Button>
                </Container>
            </div>
        </>
    )
}

export default HomePage
