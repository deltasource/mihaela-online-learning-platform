"use client"

import { useState } from "react"
import { useParams } from "react-router-dom"
import { Container, Row, Col, Card, Button, Tabs, Tab, ListGroup, Badge, ProgressBar } from "react-bootstrap"

const CourseDetailPage = () => {
  const { id } = useParams<{ id: string }>()
  const [activeTab, setActiveTab] = useState("overview")

  const course = {
    id,
    title: "Web Development Fundamentals",
    description:
        "Learn the basics of HTML, CSS, and JavaScript to build responsive websites from scratch. This comprehensive course covers everything you need to know to start your journey as a web developer.",
    instructor: {
      name: "John Smith",
      bio: "Senior Web Developer with 3+ years of experience. John has worked with Fortune 500 companies and has taught over 50,000 students online.",
      avatar: "/placeholder.svg?height=100&width=100&query=instructor",
    },
    image: "/placeholder.svg?height=400&width=700&query=web development course",
    category: "Web Development",
    level: "Beginner",
    price: 49.99,
    rating: 4.8,
    students: 1234,
    duration: "10 weeks",
    lectures: 42,
    lastUpdated: "March 2023",
    whatYouWillLearn: [
      "Build responsive websites using HTML5, CSS3, and JavaScript",
      "Understand core web development concepts",
      "Create interactive user interfaces",
      "Deploy websites to production environments",
      "Implement modern CSS frameworks like Bootstrap",
      "Debug and troubleshoot common web development issues",
    ],
    curriculum: [
      {
        title: "Introduction to Web Development",
        lectures: 5,
        duration: "1 hour 30 minutes",
      },
      {
        title: "HTML Fundamentals",
        lectures: 8,
        duration: "2 hours 15 minutes",
      },
      {
        title: "CSS Styling and Layout",
        lectures: 10,
        duration: "3 hours 45 minutes",
      },
      {
        title: "JavaScript Basics",
        lectures: 12,
        duration: "4 hours 20 minutes",
      },
      {
        title: "Building Responsive Websites",
        lectures: 7,
        duration: "2 hours 50 minutes",
      },
    ],
    reviews: [
      {
        user: "Sarah Johnson",
        avatar: "/diverse-group-city.png",
        rating: 5,
        date: "2 months ago",
        comment:
            "This course exceeded my expectations! The instructor explains complex concepts in a way that's easy to understand.",
      },
      {
        user: "Michael Brown",
        avatar: "/diverse-group-city.png",
        rating: 4,
        date: "3 months ago",
        comment:
            "Great content and well-structured. I would have liked more practical exercises, but overall it's excellent.",
      },
      {
        user: "Emily Davis",
        avatar: "/diverse-group-city.png",
        rating: 5,
        date: "1 month ago",
        comment:
            "As a complete beginner, this course was perfect for me. I now feel confident in my web development skills.",
      },
    ],
  }
  const ratingDistribution = {
    5: 78,
    4: 15,
    3: 5,
    2: 1,
    1: 1,
  }

  return (
      <Container className="py-5">
        {/* Course Header */}
        <Row className="mb-5">
          <Col lg={8}>
            <h1 className="mb-3">{course.title}</h1>
            <p className="lead mb-3">{course.description}</p>
            <div className="d-flex flex-wrap align-items-center mb-3">
              <Badge bg="primary" className="me-2 mb-2">
                {course.category}
              </Badge>
              <Badge bg="secondary" className="me-3 mb-2">
                {course.level}
              </Badge>
              <span className="me-3 mb-2">
              <i className="bi bi-star-fill text-warning me-1"></i>
                {course.rating} ({course.students} students)
            </span>
              <span className="me-3 mb-2">
              <i className="bi bi-clock me-1"></i>
                {course.duration}
            </span>
              <span className="me-3 mb-2">
              <i className="bi bi-collection-play me-1"></i>
                {course.lectures} lectures
            </span>
              <span className="mb-2">
              <i className="bi bi-calendar-check me-1"></i>
              Last updated {course.lastUpdated}
            </span>
            </div>
            <div className="d-flex align-items-center">
              <img
                  src={course.instructor.avatar || "/placeholder.svg"}
                  alt={course.instructor.name}
                  className="rounded-circle me-2"
                  width="40"
                  height="40"
              />
              <span>
              Created by <strong>{course.instructor.name}</strong>
            </span>
            </div>
          </Col>
          <Col lg={4}>
            <Card className="border-0 shadow">
              <Card.Img variant="top" src={course.image} alt={course.title} />
              <Card.Body>
                <div className="d-flex justify-content-between align-items-center mb-3">
                  <h3 className="mb-0">${course.price}</h3>
                </div>
                <Button variant="primary" className="w-100 mb-3">
                  Enroll Now
                </Button>
                <Button variant="outline-primary" className="w-100">
                  <i className="bi bi-cart-plus me-2"></i>Add to Cart
                </Button>
                <p className="text-muted mt-3 mb-0 small">
                  <i className="bi bi-shield-check me-1"></i>
                  30-day money-back guarantee
                </p>
              </Card.Body>
            </Card>
          </Col>
        </Row>

        {/* Course Content Tabs */}
        <Tabs activeKey={activeTab} onSelect={(k) => setActiveTab(k || "overview")} className="mb-4">
          <Tab eventKey="overview" title="Overview">
            <Row>
              <Col lg={8}>
                <Card className="border-0 shadow-sm mb-4">
                  <Card.Body>
                    <h3 className="mb-3">What You'll Learn</h3>
                    <Row>
                      {course.whatYouWillLearn.map((item, index) => (
                          <Col md={6} key={index} className="mb-2">
                            <div className="d-flex">
                              <i className="bi bi-check2-circle text-success me-2 fs-5"></i>
                              <span>{item}</span>
                            </div>
                          </Col>
                      ))}
                    </Row>
                  </Card.Body>
                </Card>

                <Card className="border-0 shadow-sm mb-4">
                  <Card.Body>
                    <h3 className="mb-3">Course Content</h3>
                    <p className="text-muted mb-4">
                      {course.lectures} lectures • {course.duration} total length
                    </p>
                    <ListGroup variant="flush">
                      {course.curriculum.map((section, index) => (
                          <ListGroup.Item key={index} className="px-0 py-3 border-bottom">
                            <div className="d-flex justify-content-between align-items-center">
                              <div>
                                <h5 className="mb-1">
                                  Section {index + 1}: {section.title}
                                </h5>
                                <p className="text-muted mb-0 small">
                                  {section.lectures} lectures • {section.duration}
                                </p>
                              </div>
                              <Button variant="link" className="text-decoration-none p-0">
                                <i className="bi bi-chevron-down"></i>
                              </Button>
                            </div>
                          </ListGroup.Item>
                      ))}
                    </ListGroup>
                  </Card.Body>
                </Card>

                <Card className="border-0 shadow-sm">
                  <Card.Body>
                    <h3 className="mb-3">Instructor</h3>
                    <div className="d-flex mb-3">
                      <img
                          src={course.instructor.avatar || "/placeholder.svg"}
                          alt={course.instructor.name}
                          className="rounded-circle me-3"
                          width="80"
                          height="80"
                      />
                      <div>
                        <h4>{course.instructor.name}</h4>
                        <p className="text-muted mb-0">{course.instructor.bio}</p>
                      </div>
                    </div>
                  </Card.Body>
                </Card>
              </Col>
              <Col lg={4}>
                <Card className="border-0 shadow-sm mb-4">
                  <Card.Body>
                    <h3 className="mb-3">This Course Includes</h3>
                    <ListGroup variant="flush">
                      <ListGroup.Item className="px-0 py-2 d-flex border-0">
                        <i className="bi bi-play-circle me-3 fs-5"></i>
                        <span>{course.lectures} on-demand videos</span>
                      </ListGroup.Item>
                      <ListGroup.Item className="px-0 py-2 d-flex border-0">
                        <i className="bi bi-file-earmark-text me-3 fs-5"></i>
                        <span>12 downloadable resources</span>
                      </ListGroup.Item>
                      <ListGroup.Item className="px-0 py-2 d-flex border-0">
                        <i className="bi bi-infinity me-3 fs-5"></i>
                        <span>Full lifetime access</span>
                      </ListGroup.Item>
                      <ListGroup.Item className="px-0 py-2 d-flex border-0">
                        <i className="bi bi-phone me-3 fs-5"></i>
                        <span>Access on mobile and TV</span>
                      </ListGroup.Item>
                      <ListGroup.Item className="px-0 py-2 d-flex border-0">
                        <i className="bi bi-trophy me-3 fs-5"></i>
                        <span>Certificate of completion</span>
                      </ListGroup.Item>
                    </ListGroup>
                  </Card.Body>
                </Card>
                <Card className="border-0 shadow-sm">
                  <Card.Body>
                    <h3 className="mb-3">Share This Course</h3>
                    <div className="d-flex gap-2">
                      <Button variant="outline-primary" className="rounded-circle p-2">
                        <i className="bi bi-facebook"></i>
                      </Button>
                      <Button variant="outline-info" className="rounded-circle p-2">
                        <i className="bi bi-twitter"></i>
                      </Button>
                      <Button variant="outline-danger" className="rounded-circle p-2">
                        <i className="bi bi-pinterest"></i>
                      </Button>
                      <Button variant="outline-secondary" className="rounded-circle p-2">
                        <i className="bi bi-envelope"></i>
                      </Button>
                    </div>
                  </Card.Body>
                </Card>
              </Col>
            </Row>
          </Tab>
          <Tab eventKey="reviews" title="Reviews">
            <Row>
              <Col lg={8}>
                <Card className="border-0 shadow-sm mb-4">
                  <Card.Body>
                    <div className="d-flex align-items-center mb-4">
                      <div className="me-4 text-center">
                        <h2 className="display-4 fw-bold mb-0">{course.rating}</h2>
                        <div className="text-warning mb-1">
                          <i className="bi bi-star-fill"></i>
                          <i className="bi bi-star-fill"></i>
                          <i className="bi bi-star-fill"></i>
                          <i className="bi bi-star-fill"></i>
                          <i className="bi bi-star-half"></i>
                        </div>
                        <p className="text-muted mb-0">Course Rating</p>
                      </div>
                      <div className="flex-grow-1">
                        {[5, 4, 3, 2, 1].map((rating) => (
                            <div key={rating} className="d-flex align-items-center mb-1">
                              <div className="text-muted me-2" style={{ width: "30px" }}>
                                {rating} <i className="bi bi-star-fill text-warning"></i>
                              </div>
                              <ProgressBar
                                  now={ratingDistribution[rating as keyof typeof ratingDistribution]}
                                  className="flex-grow-1 me-2"
                                  style={{ height: "8px" }}
                              />
                              <div className="text-muted" style={{ width: "40px" }}>
                                {ratingDistribution[rating as keyof typeof ratingDistribution]}%
                              </div>
                            </div>
                        ))}
                      </div>
                    </div>
                    <hr />
                    <h3 className="mb-4">Student Reviews</h3>
                    {course.reviews.map((review, index) => (
                        <div key={index} className={index < course.reviews.length - 1 ? "mb-4 pb-4 border-bottom" : ""}>
                          <div className="d-flex mb-2">
                            <img
                                src={review.avatar || "/placeholder.svg"}
                                alt={review.user}
                                className="rounded-circle me-2"
                                width="40"
                                height="40"
                            />
                            <div>
                              <h5 className="mb-0">{review.user}</h5>
                              <div className="d-flex align-items-center">
                                <div className="text-warning me-2">
                                  {[...Array(5)].map((_, i) => (
                                      <i key={i} className={`bi bi-star${i < review.rating ? "-fill" : ""}`}></i>
                                  ))}
                                </div>
                                <span className="text-muted small">{review.date}</span>
                              </div>
                            </div>
                          </div>
                          <p className="mb-0">{review.comment}</p>
                        </div>
                    ))}
                  </Card.Body>
                </Card>
              </Col>
              <Col lg={4}>
                <Card className="border-0 shadow-sm">
                  <Card.Body>
                    <h3 className="mb-3">Write a Review</h3>
                    <p className="text-muted mb-4">Share your experience with this course</p>
                    <Button variant="primary" className="w-100">
                      Write a Review
                    </Button>
                  </Card.Body>
                </Card>
              </Col>
            </Row>
          </Tab>
        </Tabs>
      </Container>
  )
}

export default CourseDetailPage
