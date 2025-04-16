"use client"

import { useState } from "react"
import { Link } from "react-router-dom"
import {
    BookOpen,
    Clock,
    Award,
    Settings,
    Bell,
    Heart,
    ShoppingCart,
    MessageSquare,
    PlayCircle,
    BarChart,
} from "lucide-react"
import { allCourses } from "../assets/data"

const Dashboard = () => {
    const [activeTab, setActiveTab] = useState("my-courses")

    const enrolledCourses = allCourses.slice(0, 3)

    return (
        <div className="container py-5">
            <div className="row">
                <div className="col-lg-3 mb-4">
                    <div className="card shadow-sm mb-4">
                        <div className="card-body">
                            <div className="d-flex align-items-center mb-4">
                                <img
                                    src="/diverse-avatars.png"
                                    alt="Profile"
                                    className="rounded-circle me-3"
                                    style={{ width: "80px", height: "80px" }}
                                />
                                <div>
                                    <h5 className="fw-bold mb-1">John Doe</h5>
                                    <p className="text-muted mb-0">john.doe@example.com</p>
                                </div>
                            </div>

                            <div className="progress mb-2" style={{ height: "8px" }}>
                                <div className="progress-bar bg-primary" style={{ width: "65%" }}></div>
                            </div>
                            <p className="text-muted small mb-0">Profile Completion: 65%</p>
                        </div>
                    </div>

                    <div className="list-group shadow-sm">
                        <button
                            className={`list-group-item list-group-item-action d-flex align-items-center ${activeTab === "my-courses" ? "active" : ""}`}
                            onClick={() => setActiveTab("my-courses")}
                        >
                            <BookOpen size={18} className="me-3" />
                            My Courses
                        </button>
                        <button
                            className={`list-group-item list-group-item-action d-flex align-items-center ${activeTab === "wishlist" ? "active" : ""}`}
                            onClick={() => setActiveTab("wishlist")}
                        >
                            <Heart size={18} className="me-3" />
                            Wishlist
                        </button>
                        <button
                            className={`list-group-item list-group-item-action d-flex align-items-center ${activeTab === "cart" ? "active" : ""}`}
                            onClick={() => setActiveTab("cart")}
                        >
                            <ShoppingCart size={18} className="me-3" />
                            Cart
                        </button>
                        <button
                            className={`list-group-item list-group-item-action d-flex align-items-center ${activeTab === "achievements" ? "active" : ""}`}
                            onClick={() => setActiveTab("achievements")}
                        >
                            <Award size={18} className="me-3" />
                            Achievements
                        </button>
                        <button
                            className={`list-group-item list-group-item-action d-flex align-items-center ${activeTab === "messages" ? "active" : ""}`}
                            onClick={() => setActiveTab("messages")}
                        >
                            <MessageSquare size={18} className="me-3" />
                            Messages
                        </button>
                        <button
                            className={`list-group-item list-group-item-action d-flex align-items-center ${activeTab === "notifications" ? "active" : ""}`}
                            onClick={() => setActiveTab("notifications")}
                        >
                            <Bell size={18} className="me-3" />
                            Notifications
                        </button>
                        <button
                            className={`list-group-item list-group-item-action d-flex align-items-center ${activeTab === "settings" ? "active" : ""}`}
                            onClick={() => setActiveTab("settings")}
                        >
                            <Settings size={18} className="me-3" />
                            Settings
                        </button>
                    </div>
                </div>

                <div className="col-lg-9">
                    {activeTab === "my-courses" && (
                        <div>
                            <div className="d-flex justify-content-between align-items-center mb-4">
                                <h2 className="fw-bold mb-0">My Learning</h2>
                                <div className="d-flex gap-2">
                                    <select className="form-select" style={{ width: "auto" }}>
                                        <option>All Courses</option>
                                        <option>In Progress</option>
                                        <option>Completed</option>
                                        <option>Archived</option>
                                    </select>
                                    <select className="form-select" style={{ width: "auto" }}>
                                        <option>Recently Accessed</option>
                                        <option>Recently Added</option>
                                        <option>Title: A-Z</option>
                                        <option>Title: Z-A</option>
                                    </select>
                                </div>
                            </div>

                            <div className="row g-4 mb-4">
                                <div className="col-md-4">
                                    <div className="card border-0 shadow-sm h-100">
                                        <div className="card-body d-flex flex-column align-items-center justify-content-center text-center p-4">
                                            <Clock size={36} className="text-primary mb-3" />
                                            <h3 className="fw-bold mb-1">12.5 hrs</h3>
                                            <p className="text-muted mb-0">Learning Time</p>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-4">
                                    <div className="card border-0 shadow-sm h-100">
                                        <div className="card-body d-flex flex-column align-items-center justify-content-center text-center p-4">
                                            <BookOpen size={36} className="text-primary mb-3" />
                                            <h3 className="fw-bold mb-1">3</h3>
                                            <p className="text-muted mb-0">Courses in Progress</p>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-4">
                                    <div className="card border-0 shadow-sm h-100">
                                        <div className="card-body d-flex flex-column align-items-center justify-content-center text-center p-4">
                                            <Award size={36} className="text-primary mb-3" />
                                            <h3 className="fw-bold mb-1">2</h3>
                                            <p className="text-muted mb-0">Certificates Earned</p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className="mb-5">
                                <h4 className="fw-bold mb-3">In Progress</h4>
                                {enrolledCourses.map((course, index) => (
                                    <div key={index} className="card mb-3 border-0 shadow-sm">
                                        <div className="row g-0">
                                            <div className="col-md-3">
                                                <img
                                                    src={course.image || `/placeholder.svg?height=150&width=250&query=course ${index + 1}`}
                                                    alt={course.title}
                                                    className="img-fluid rounded-start h-100 object-fit-cover"
                                                    style={{ objectFit: "cover" }}
                                                />
                                            </div>
                                            <div className="col-md-9">
                                                <div className="card-body d-flex flex-column h-100">
                                                    <div className="d-flex justify-content-between mb-2">
                                                        <h5 className="card-title fw-bold mb-0">{course.title}</h5>
                                                        <span className="badge bg-primary">{course.level}</span>
                                                    </div>
                                                    <p className="card-text text-muted mb-3">
                                                        <small>Instructor: {course.instructor}</small>
                                                    </p>
                                                    <div className="progress mb-2" style={{ height: "8px" }}>
                                                        <div className="progress-bar bg-primary" style={{ width: `${30 + index * 20}%` }}></div>
                                                    </div>
                                                    <div className="d-flex justify-content-between mb-3">
                                                        <small className="text-muted">{30 + index * 20}% complete</small>
                                                        <small className="text-muted">
                                                        </small>
                                                    </div>
                                                    <div className="mt-auto d-flex gap-2">
                                                        <Link to={`/course/${course.id}`} className="btn btn-primary">
                                                            <PlayCircle size={18} className="me-1" />
                                                            Continue Learning
                                                        </Link>
                                                        <button className="btn btn-outline-secondary">
                                                            <BarChart size={18} />
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>

                            <div>
                                <h4 className="fw-bold mb-3">Recommended For You</h4>
                                <div className="row g-4">
                                    {allCourses.slice(3, 6).map((course, index) => (
                                        <div key={index} className="col-md-4">
                                            <div className="card h-100 border-0 shadow-sm">
                                                <img
                                                    src={
                                                        course.image ||
                                                        `/placeholder.svg?height=160&width=320&query=recommended course ${index + 1}`
                                                    }
                                                    alt={course.title}
                                                    className="card-img-top"
                                                    style={{ height: "160px", objectFit: "cover" }}
                                                />
                                                <div className="card-body">
                                                    <h5 className="card-title fw-bold mb-2">{course.title}</h5>
                                                    <p className="card-text text-muted mb-3">
                                                        <small>{course.instructor}</small>
                                                    </p>
                                                    <div className="d-flex justify-content-between align-items-center">
                                                        <span className="fw-bold">${course.price}</span>
                                                        <button className="btn btn-sm btn-primary">Add to Cart</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </div>
                    )}

                    {activeTab === "wishlist" && (
                        <div>
                            <h2 className="fw-bold mb-4">My Wishlist</h2>
                            <div className="row g-4">
                                {allCourses.slice(6, 9).map((course, index) => (
                                    <div key={index} className="col-md-4">
                                        <div className="card h-100 border-0 shadow-sm">
                                            <div className="position-relative">
                                                <img
                                                    src={
                                                        course.image || `/placeholder.svg?height=160&width=320&query=wishlist course ${index + 1}`
                                                    }
                                                    alt={course.title}
                                                    className="card-img-top"
                                                    style={{ height: "160px", objectFit: "cover" }}
                                                />
                                                <button className="btn btn-sm btn-light position-absolute top-0 end-0 m-2">
                                                    <Heart size={16} fill="currentColor" className="text-danger" />
                                                </button>
                                            </div>
                                            <div className="card-body">
                                                <h5 className="card-title fw-bold mb-2">{course.title}</h5>
                                                <p className="card-text text-muted mb-3">
                                                    <small>{course.instructor}</small>
                                                </p>
                                                <div className="d-flex justify-content-between align-items-center">
                                                    <span className="fw-bold">${course.price}</span>
                                                    <button className="btn btn-sm btn-primary">Add to Cart</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}

                    {activeTab === "achievements" && (
                        <div>
                            <h2 className="fw-bold mb-4">My Achievements</h2>
                            <div className="row g-4">
                                <div className="col-md-6">
                                    <div className="card border-0 shadow-sm">
                                        <div className="card-body text-center p-4">
                                            <div className="rounded-circle bg-primary p-3 d-inline-flex mb-3">
                                                <Award size={48} className="text-white" />
                                            </div>
                                            <h4 className="fw-bold mb-2">Web Development Fundamentals</h4>
                                            <p className="text-muted mb-3">Completed on April 10, 2025</p>
                                            <button className="btn btn-outline-primary">View Certificate</button>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-md-6">
                                    <div className="card border-0 shadow-sm">
                                        <div className="card-body text-center p-4">
                                            <div className="rounded-circle bg-primary p-3 d-inline-flex mb-3">
                                                <Award size={48} className="text-white" />
                                            </div>
                                            <h4 className="fw-bold mb-2">JavaScript Mastery</h4>
                                            <p className="text-muted mb-3">Completed on March 15, 2025</p>
                                            <button className="btn btn-outline-primary">View Certificate</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    )}

                    {(activeTab === "cart" ||
                        activeTab === "messages" ||
                        activeTab === "notifications" ||
                        activeTab === "settings") && (
                        <div className="text-center py-5">
                            <h3 className="fw-bold mb-3">Coming Soon</h3>
                            <p className="text-muted">This feature is currently under development.</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    )
}

export default Dashboard
