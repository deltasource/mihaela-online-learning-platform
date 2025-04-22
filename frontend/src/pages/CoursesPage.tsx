"use client"

import { useState } from "react"
import { Container, Row, Col, Card, Form, InputGroup, Button, Badge } from "react-bootstrap"
import { Link } from "react-router-dom"

type Course = {
    id: string
    title: string
    description: string
    instructor: string
    image: string
    category: string
    level: "Beginner" | "Intermediate" | "Advanced"
    price: number
    rating: number
    students: number
}

const CoursesPage = () => {
    const [searchTerm, setSearchTerm] = useState("")
    const [selectedCategory, setSelectedCategory] = useState("")
    const [selectedLevel, setSelectedLevel] = useState("")

    const courses: Course[] = [
        {
            id: "1",
            title: "Web Development Fundamentals",
            description: "Learn the basics of HTML, CSS, and JavaScript to build responsive websites.",
            instructor: "John Smith",
            image: "/coding-workspace.png",
            category: "Web Development",
            level: "Beginner",
            price: 49.99,
            rating: 4.8,
            students: 1234,
        },
        {
            id: "2",
            title: "Data Science Essentials",
            description: "Master the fundamentals of data analysis, visualization, and machine learning.",
            instructor: "Emily Johnson",
            image: "/data-insights-flow.png",
            category: "Data Science",
            level: "Intermediate",
            price: 59.99,
            rating: 4.7,
            students: 987,
        },
        {
            id: "3",
            title: "Mobile App Development",
            description: "Build cross-platform mobile applications using React Native.",
            instructor: "Michael Brown",
            image: "/app-development-workflow.png",
            category: "Mobile Development",
            level: "Intermediate",
            price: 54.99,
            rating: 4.9,
            students: 1567,
        },
        {
            id: "4",
            title: "Advanced JavaScript Patterns",
            description: "Deep dive into advanced JavaScript concepts and design patterns.",
            instructor: "Sarah Wilson",
            image: "/placeholder.svg?height=200&width=350&query=javascript",
            category: "Web Development",
            level: "Advanced",
            price: 69.99,
            rating: 4.9,
            students: 876,
        },
        {
            id: "5",
            title: "UI/UX Design Principles",
            description: "Learn the fundamentals of user interface and experience design.",
            instructor: "Alex Rodriguez",
            image: "/placeholder.svg?height=200&width=350&query=ui ux design",
            category: "Design",
            level: "Beginner",
            price: 44.99,
            rating: 4.6,
            students: 1432,
        },
        {
            id: "6",
            title: "Python for Beginners",
            description: "Start your programming journey with Python, one of the most popular languages.",
            instructor: "Jessica Lee",
            image: "/placeholder.svg?height=200&width=350&query=python programming",
            category: "Programming",
            level: "Beginner",
            price: 39.99,
            rating: 4.8,
            students: 2345,
        },
    ]

    const categories = [...new Set(courses.map((course) => course.category))]
    const levels = ["Beginner", "Intermediate", "Advanced"]

    const filteredCourses = courses.filter((course) => {
        const matchesSearch =
            course.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
            course.description.toLowerCase().includes(searchTerm.toLowerCase())
        const matchesCategory = selectedCategory === "" || course.category === selectedCategory
        const matchesLevel = selectedLevel === "" || course.level === selectedLevel

        return matchesSearch && matchesCategory && matchesLevel
    })

    return (
        <Container className="py-5">
            <h1 className="mb-4">Explore Courses</h1>

            {/* Search and Filters */}
            <Row className="mb-4">
                <Col md={6} className="mb-3 mb-md-0">
                    <InputGroup>
                        <Form.Control
                            placeholder="Search courses..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                        />
                        <Button variant="primary">
                            <i className="bi bi-search"></i>
                        </Button>
                    </InputGroup>
                </Col>
                <Col md={3} className="mb-3 mb-md-0">
                    <Form.Select value={selectedCategory} onChange={(e) => setSelectedCategory(e.target.value)}>
                        <option value="">All Categories</option>
                        {categories.map((category, index) => (
                            <option key={index} value={category}>
                                {category}
                            </option>
                        ))}
                    </Form.Select>
                </Col>
                <Col md={3}>
                    <Form.Select value={selectedLevel} onChange={(e) => setSelectedLevel(e.target.value)}>
                        <option value="">All Levels</option>
                        {levels.map((level, index) => (
                            <option key={index} value={level}>
                                {level}
                            </option>
                        ))}
                    </Form.Select>
                </Col>
            </Row>

            {/* Courses Grid */}
            <Row>
                {filteredCourses.length > 0 ? (
                    filteredCourses.map((course) => (
                        <Col md={4} key={course.id} className="mb-4">
                            <Card className="h-100 border-0 shadow-sm">
                                <Card.Img variant="top" src={course.image} alt={course.title} />
                                <Card.Body>
                                    <div className="d-flex justify-content-between mb-2">
                                        <Badge bg="primary">{course.category}</Badge>
                                        <Badge bg="secondary">{course.level}</Badge>
                                    </div>
                                    <Card.Title as={Link} to={`/courses/${course.id}`} className="text-decoration-none text-dark">
                                        {course.title}
                                    </Card.Title>
                                    <Card.Text className="text-muted mb-2">Instructor: {course.instructor}</Card.Text>
                                    <Card.Text className="mb-3">{course.description}</Card.Text>
                                    <div className="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i className="bi bi-star-fill text-warning me-1"></i>
                                            <span>
                        {course.rating} ({course.students} students)
                      </span>
                                        </div>
                                        <div className="fw-bold">${course.price}</div>
                                    </div>
                                </Card.Body>
                                <Card.Footer className="bg-white border-0">
                                    <Button as={Link} to={`/courses/${course.id}`} variant="primary" className="w-100">
                                        View Course
                                    </Button>
                                </Card.Footer>
                            </Card>
                        </Col>
                    ))
                ) : (
                    <Col className="text-center py-5">
                        <i className="bi bi-search fs-1 text-muted"></i>
                        <h3 className="mt-3">No courses found</h3>
                        <p className="text-muted">Try adjusting your search or filters</p>
                    </Col>
                )}
            </Row>
        </Container>
    )
}

export default CoursesPage
