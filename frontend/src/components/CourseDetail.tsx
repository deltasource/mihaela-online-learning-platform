"use client"

import { useState } from "react"
import { useParams } from "react-router-dom"
import {
    Star,
    Clock,
    Award,
    Globe,
    FileText,
    PlayCircle,
    Download,
    Users,
    MessageSquare,
    Share2,
    Heart,
    ShoppingCart,
    CheckCircle,
} from "lucide-react"
import { allCourses } from "../assets/data"

const CourseDetail = () => {
    const { id } = useParams()
    const [activeTab, setActiveTab] = useState("overview")

    const course = allCourses.find((c) => c.id === Number(id)) || allCourses[0]

    return (
        <div className="container py-5">
            {/* Course Header */}
            <div className="bg-primary text-white p-4 p-md-5 rounded-3 mb-5">
                <div className="row">
                    <div className="col-lg-8">
                        <div className="d-flex align-items-center mb-2">
                            <span className="badge bg-white text-primary me-2">{course.category}</span>
                            <span className="badge bg-white text-primary">{course.level}</span>
                        </div>
                        <h1 className="fw-bold mb-3">{course.title}</h1>
                        <p className="lead mb-4">{course.description}</p>

                        <div className="d-flex flex-wrap align-items-center mb-3 gap-3">
                            <div className="d-flex align-items-center">
                                <span className="fw-bold me-1">{course.rating.toFixed(1)}</span>
                                <div className="rating-stars d-flex me-1">
                                    {[...Array(5)].map((_, i) => (
                                        <Star key={i} size={16} fill={i < Math.floor(course.rating) ? "currentColor" : "none"} />
                                    ))}
                                </div>
                                <span>({course.reviewCount} reviews)</span>
                            </div>
                            <div className="d-flex align-items-center">
                                <Users size={16} className="me-1" />
                                <span>{course.enrollments} students</span>
                            </div>
                            <div className="d-flex align-items-center">
                                <Clock size={16} className="me-1" />
                                <span>{course.duration}</span>
                            </div>
                            <div className="d-flex align-items-center">
                                <Globe size={16} className="me-1" />
                                <span>English</span>
                            </div>
                        </div>

                        <div className="d-flex align-items-center">
                            <img
                                src={course.instructorAvatar || "/placeholder.svg"}
                                alt={course.instructor}
                                className="instructor-avatar me-2"
                            />
                            <span>
                Created by <strong>{course.instructor}</strong>
              </span>
                        </div>
                    </div>
                </div>
            </div>

            <div className="row">
                <div className="col-lg-8 mb-5 mb-lg-0">
                    <ul className="nav nav-tabs mb-4">
                        <li className="nav-item">
                            <button
                                className={`nav-link ${activeTab === "overview" ? "active" : ""}`}
                                onClick={() => setActiveTab("overview")}
                            >
                                Overview
                            </button>
                        </li>
                        <li className="nav-item">
                            <button
                                className={`nav-link ${activeTab === "curriculum" ? "active" : ""}`}
                                onClick={() => setActiveTab("curriculum")}
                            >
                                Curriculum
                            </button>
                        </li>
                        <li className="nav-item">
                            <button
                                className={`nav-link ${activeTab === "instructor" ? "active" : ""}`}
                                onClick={() => setActiveTab("instructor")}
                            >
                                Instructor
                            </button>
                        </li>
                        <li className="nav-item">
                            <button
                                className={`nav-link ${activeTab === "reviews" ? "active" : ""}`}
                                onClick={() => setActiveTab("reviews")}
                            >
                                Reviews
                            </button>
                        </li>
                    </ul>
                    <div className="tab-content">
                        {activeTab === "overview" && (
                            <div>
                                <h3 className="fw-bold mb-4">About This Course</h3>
                                <p className="mb-4">
                                    {course.description}
                                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et
                                    dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut
                                    aliquip ex ea commodo consequat.
                                </p>

                                <h4 className="fw-bold mb-3">What You'll Learn</h4>
                                <div className="row mb-4">
                                    {[
                                        "Master the fundamentals of web development",
                                        "Build responsive websites with HTML, CSS, and JavaScript",
                                        "Deploy your applications to the cloud",
                                        "Implement authentication and authorization",
                                        "Work with databases and APIs",
                                        "Optimize performance and security",
                                    ].map((item, index) => (
                                        <div key={index} className="col-md-6 mb-2">
                                            <div className="d-flex align-items-center">
                                                <CheckCircle size={18} className="text-primary me-2" />
                                                <span>{item}</span>
                                            </div>
                                        </div>
                                    ))}
                                </div>

                                <h4 className="fw-bold mb-3">Requirements</h4>
                                <ul className="mb-4">
                                    <li>Basic computer skills</li>
                                    <li>No prior programming experience required</li>
                                    <li>A computer with internet access</li>
                                </ul>

                                <h4 className="fw-bold mb-3">This Course Includes</h4>
                                <div className="row mb-4">
                                    <div className="col-md-6 mb-2">
                                        <div className="d-flex align-items-center">
                                            <PlayCircle size={18} className="text-primary me-2" />
                                            <span>12 hours on-demand video</span>
                                        </div>
                                    </div>
                                    <div className="col-md-6 mb-2">
                                        <div className="d-flex align-items-center">
                                            <FileText size={18} className="text-primary me-2" />
                                            <span>25 articles</span>
                                        </div>
                                    </div>
                                    <div className="col-md-6 mb-2">
                                        <div className="d-flex align-items-center">
                                            <Download size={18} className="text-primary me-2" />
                                            <span>15 downloadable resources</span>
                                        </div>
                                    </div>
                                    <div className="col-md-6 mb-2">
                                        <div className="d-flex align-items-center">
                                            <MessageSquare size={18} className="text-primary me-2" />
                                            <span>Full lifetime access</span>
                                        </div>
                                    </div>
                                    <div className="col-md-6 mb-2">
                                        <div className="d-flex align-items-center">
                                            <Globe size={18} className="text-primary me-2" />
                                            <span>Access on mobile and TV</span>
                                        </div>
                                    </div>
                                    <div className="col-md-6 mb-2">
                                        <div className="d-flex align-items-center">
                                            <Award size={18} className="text-primary me-2" />
                                            <span>Certificate of completion</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}

                        {activeTab === "curriculum" && (
                            <div>
                                <h3 className="fw-bold mb-4">Course Content</h3>
                                <div className="d-flex justify-content-between mb-3">
                                    <span>10 sections • 42 lectures • 12h 30m total length</span>
                                    <button className="btn btn-link p-0">Expand All Sections</button>
                                </div>

                                <div className="accordion" id="courseContent">
                                    {[1, 2, 3, 4, 5].map((section) => (
                                        <div key={section} className="accordion-item">
                                            <h2 className="accordion-header">
                                                <button
                                                    className="accordion-button collapsed"
                                                    type="button"
                                                    data-bs-toggle="collapse"
                                                    data-bs-target={`#section${section}`}
                                                >
                                                    <div className="w-100 d-flex justify-content-between">
                                                        <span>Section {section}: Introduction to Web Development</span>
                                                        <span className="text-muted">4 lectures • 45 min</span>
                                                    </div>
                                                </button>
                                            </h2>
                                            <div
                                                id={`section${section}`}
                                                className="accordion-collapse collapse"
                                                data-bs-parent="#courseContent"
                                            >
                                                <div className="accordion-body p-0">
                                                    <ul className="list-group list-group-flush">
                                                        <li className="list-group-item d-flex justify-content-between align-items-center py-3">
                                                            <div className="d-flex align-items-center">
                                                                <PlayCircle size={18} className="text-primary me-2" />
                                                                <span>Introduction to HTML</span>
                                                            </div>
                                                            <span className="text-muted">12:30</span>
                                                        </li>
                                                        <li className="list-group-item d-flex justify-content-between align-items-center py-3">
                                                            <div className="d-flex align-items-center">
                                                                <PlayCircle size={18} className="text-primary me-2" />
                                                                <span>Basic HTML Tags</span>
                                                            </div>
                                                            <span className="text-muted">10:15</span>
                                                        </li>
                                                        <li className="list-group-item d-flex justify-content-between align-items-center py-3">
                                                            <div className="d-flex align-items-center">
                                                                <FileText size={18} className="text-primary me-2" />
                                                                <span>HTML Exercise</span>
                                                            </div>
                                                            <span className="text-muted">15:45</span>
                                                        </li>
                                                        <li className="list-group-item d-flex justify-content-between align-items-center py-3">
                                                            <div className="d-flex align-items-center">
                                                                <PlayCircle size={18} className="text-primary me-2" />
                                                                <span>HTML Project</span>
                                                            </div>
                                                            <span className="text-muted">08:30</span>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        )}
                        {activeTab === "instructor" && (
                            <div>
                                <h3 className="fw-bold mb-4">Meet Your Instructor</h3>
                                <div className="d-flex flex-column flex-md-row gap-4 mb-4">
                                    <img
                                        src={
                                            course.instructorAvatar || "/placeholder.svg?height=150&width=150&query=professional instructor"
                                        }
                                        alt={course.instructor}
                                        className="rounded-circle"
                                        style={{ width: "150px", height: "150px", objectFit: "cover" }}
                                    />
                                    <div>
                                        <h4 className="fw-bold mb-2">{course.instructor}</h4>
                                        <p className="text-muted mb-3">Web Development Instructor & Software Engineer</p>
                                        <div className="d-flex gap-3 mb-3">
                                            <div className="d-flex align-items-center">
                                                <Star size={18} className="text-primary me-1" />
                                                <span>4.8 Instructor Rating</span>
                                            </div>
                                            <div className="d-flex align-items-center">
                                                <MessageSquare size={18} className="text-primary me-1" />
                                                <span>2,540 Reviews</span>
                                            </div>
                                            <div className="d-flex align-items-center">
                                                <Users size={18} className="text-primary me-1" />
                                                <span>15,200 Students</span>
                                            </div>
                                        </div>
                                        <p className="mb-3">
                                            John is a full-stack developer with over 10 years of experience in web development. He has worked
                                            with various technologies and frameworks, and has a passion for teaching and helping others learn
                                            to code.
                                        </p>
                                    </div>
                                </div>
                            </div>
                        )}
                        {activeTab === "reviews" && (
                            <div>
                                <h3 className="fw-bold mb-4">Student Reviews</h3>
                                <div className="row mb-4">
                                    <div className="col-md-4 mb-4 mb-md-0">
                                        <div className="text-center">
                                            <h2 className="display-4 fw-bold">{course.rating.toFixed(1)}</h2>
                                            <div className="rating-stars d-flex justify-content-center mb-2">
                                                {[...Array(5)].map((_, i) => (
                                                    <Star
                                                        key={i}
                                                        size={24}
                                                        fill={i < Math.floor(course.rating) ? "currentColor" : "none"}
                                                        className="text-warning"
                                                    />
                                                ))}
                                            </div>
                                            <p className="mb-0">Course Rating</p>
                                        </div>
                                    </div>
                                    <div className="col-md-8">
                                        <div className="mb-3">
                                            <div className="d-flex align-items-center">
                                                <div className="d-flex align-items-center me-2">
                                                    {[...Array(5)].map((_, i) => (
                                                        <Star key={i} size={16} className="text-warning" />
                                                    ))}
                                                </div>
                                                <div className="progress flex-grow-1" style={{ height: "8px" }}>
                                                    <div className="progress-bar bg-warning" style={{ width: "75%" }}></div>
                                                </div>
                                                <span className="ms-2">75%</span>
                                            </div>
                                        </div>
                                        <div className="mb-3">
                                            <div className="d-flex align-items-center">
                                                <div className="d-flex align-items-center me-2">
                                                    {[...Array(4)].map((_, i) => (
                                                        <Star key={i} size={16} className="text-warning" />
                                                    ))}
                                                    <Star size={16} className="text-muted" />
                                                </div>
                                                <div className="progress flex-grow-1" style={{ height: "8px" }}>
                                                    <div className="progress-bar bg-warning" style={{ width: "18%" }}></div>
                                                </div>
                                                <span className="ms-2">18%</span>
                                            </div>
                                        </div>
                                        <div className="mb-3">
                                            <div className="d-flex align-items-center">
                                                <div className="d-flex align-items-center me-2">
                                                    {[...Array(3)].map((_, i) => (
                                                        <Star key={i} size={16} className="text-warning" />
                                                    ))}
                                                    {[...Array(2)].map((_, i) => (
                                                        <Star key={i} size={16} className="text-muted" />
                                                    ))}
                                                </div>
                                                <div className="progress flex-grow-1" style={{ height: "8px" }}>
                                                    <div className="progress-bar bg-warning" style={{ width: "5%" }}></div>
                                                </div>
                                                <span className="ms-2">5%</span>
                                            </div>
                                        </div>
                                        <div className="mb-3">
                                            <div className="d-flex align-items-center">
                                                <div className="d-flex align-items-center me-2">
                                                    {[...Array(2)].map((_, i) => (
                                                        <Star key={i} size={16} className="text-warning" />
                                                    ))}
                                                    {[...Array(3)].map((_, i) => (
                                                        <Star key={i} size={16} className="text-muted" />
                                                    ))}
                                                </div>
                                                <div className="progress flex-grow-1" style={{ height: "8px" }}>
                                                    <div className="progress-bar bg-warning" style={{ width: "1%" }}></div>
                                                </div>
                                                <span className="ms-2">1%</span>
                                            </div>
                                        </div>
                                        <div className="mb-3">
                                            <div className="d-flex align-items-center">
                                                <div className="d-flex align-items-center me-2">
                                                    <Star size={16} className="text-warning" />
                                                    {[...Array(4)].map((_, i) => (
                                                        <Star key={i} size={16} className="text-muted" />
                                                    ))}
                                                </div>
                                                <div className="progress flex-grow-1" style={{ height: "8px" }}>
                                                    <div className="progress-bar bg-warning" style={{ width: "1%" }}></div>
                                                </div>
                                                <span className="ms-2">1%</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="mb-4">
                                    {[1, 2, 3].map((review) => (
                                        <div key={review} className="card mb-3 border-0 shadow-sm">
                                            <div className="card-body">
                                                <div className="d-flex mb-3">
                                                    <img
                                                        src={`/thoughtful-artist.png?height=50&width=50&query=person ${review}`}
                                                        alt="Reviewer"
                                                        className="rounded-circle me-3"
                                                        style={{ width: "50px", height: "50px" }}
                                                    />
                                                    <div>
                                                        <h6 className="mb-1">Student Name {review}</h6>
                                                        <div className="d-flex align-items-center">
                                                            <div className="rating-stars d-flex me-2">
                                                                {[...Array(5)].map((_, i) => (
                                                                    <Star
                                                                        key={i}
                                                                        size={14}
                                                                        fill={i < 5 ? "currentColor" : "none"}
                                                                        className="text-warning"
                                                                    />
                                                                ))}
                                                            </div>
                                                            <span className="text-muted small">3 months ago</span>
                                                        </div>
                                                    </div>
                                                </div>
                                                <p className="mb-0">
                                                    This course exceeded my expectations! The instructor explains complex concepts in a way that's
                                                    easy to understand, and the projects helped me apply what I learned. Highly recommended for
                                                    anyone looking to learn web development.
                                                </p>
                                            </div>
                                        </div>
                                    ))}
                                </div>

                                <div className="text-center">
                                    <button className="btn btn-outline-primary">Load More Reviews</button>
                                </div>
                            </div>
                        )}
                    </div>
                </div>

                <div className="col-lg-4">
                    <div className="card shadow-sm sticky-lg-top" style={{ top: "20px" }}>
                        <img
                            src={course.image || "/placeholder.svg?height=200&width=400&query=web development course"}
                            alt={course.title}
                            className="card-img-top"
                        />
                        <div className="card-body">
                            <div className="d-flex justify-content-between align-items-center mb-3">
                                <h4 className="fw-bold mb-0">${course.price}</h4>
                                {course.originalPrice && (
                                    <span className="text-muted text-decoration-line-through">${course.originalPrice}</span>
                                )}
                            </div>

                            <div className="d-grid gap-2 mb-4">
                                <button className="btn btn-primary btn-lg">
                                    <ShoppingCart size={18} className="me-2" />
                                    Add to Cart
                                </button>
                                <button className="btn btn-outline-primary btn-lg">Buy Now</button>
                            </div>

                            <div className="text-center mb-4">
                                <p className="mb-0 text-muted">30-Day Money-Back Guarantee</p>
                            </div>

                            <div className="mb-4">
                                <h5 className="fw-bold mb-3">This Course Includes:</h5>
                                <ul className="list-unstyled mb-0">
                                    <li className="d-flex align-items-center mb-2">
                                        <PlayCircle size={18} className="text-primary me-2" />
                                        <span>12 hours on-demand video</span>
                                    </li>
                                    <li className="d-flex align-items-center mb-2">
                                        <FileText size={18} className="text-primary me-2" />
                                        <span>25 articles</span>
                                    </li>
                                    <li className="d-flex align-items-center mb-2">
                                        <Download size={18} className="text-primary me-2" />
                                        <span>15 downloadable resources</span>
                                    </li>
                                    <li className="d-flex align-items-center mb-2">
                                        <Globe size={18} className="text-primary me-2" />
                                        <span>Full lifetime access</span>
                                    </li>
                                    <li className="d-flex align-items-center mb-2">
                                        <Award size={18} className="text-primary me-2" />
                                        <span>Certificate of completion</span>
                                    </li>
                                </ul>
                            </div>

                            <div className="d-flex justify-content-center gap-3">
                                <button className="btn btn-outline-secondary">
                                    <Heart size={18} />
                                </button>
                                <button className="btn btn-outline-secondary">
                                    <Share2 size={18} />
                                </button>
                                <button className="btn btn-outline-secondary">Gift This Course</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default CourseDetail
