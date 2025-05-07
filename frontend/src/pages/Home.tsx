import { Card, Row, Col } from "react-bootstrap"
import { Link } from "react-router-dom"

const Home = () => {
    const modules = [
        {
            title: "Students",
            description: "Manage student profiles",
            link: "/students",
            icon: "👨‍🎓",
        },
        {
            title: "Instructors",
            description: "Manage instructor profiles",
            link: "/instructors",
            icon: "👨‍🏫",
        },
        {
            title: "Courses",
            description: "Manage courses and enrollments",
            link: "/courses",
            icon: "📚",
        },
        {
            title: "Videos",
            description: "Manage course videos",
            link: "/videos",
            icon: "🎬",
        },
        {
            title: "Student Progress",
            description: "Track student progress in courses",
            link: "/progress",
            icon: "📊",
        },
    ]

    return (
        <div>
            <div className="text-center mb-5">
                <h1>E-Learning Platform API Demo</h1>
                <p className="lead">This application demonstrates all the API endpoints from the Swagger specification.</p>
            </div>

            <Row>
                {modules.map((module, index) => (
                    <Col md={4} key={index} className="mb-4">
                        <Card as={Link} to={module.link} className="h-100 text-decoration-none endpoint-card">
                            <Card.Body className="text-center">
                                <div className="display-4 mb-3">{module.icon}</div>
                                <Card.Title>{module.title}</Card.Title>
                                <Card.Text>{module.description}</Card.Text>
                            </Card.Body>
                        </Card>
                    </Col>
                ))}
            </Row>
        </div>
    )
}

export default Home
