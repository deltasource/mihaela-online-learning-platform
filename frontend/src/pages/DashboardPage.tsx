"use client"

import { useContext, useState } from "react"
import { Navigate } from "react-router-dom"
import { UserContext } from "../context/UserContext"
import { allCourses } from "../data/courses"

const DashboardPage = () => {
    const { user } = useContext(UserContext)
    const [activeTab, setActiveTab] = useState("overview")

    if (!user) {
        return <Navigate to="/login" />
    }

    // Mock data - in a real app, this would come from an API
    const enrolledCourses = allCourses.slice(0, 3)
    const completedCourses = allCourses.slice(3, 4)

    // Instructor-specific data
    const instructorCourses = user.role === "instructor" ? allCourses.slice(0, 2) : []
    const studentStats = {
        totalStudents: 245,
        averageRating: 4.8,
        totalRevenue: 12580,
    }

    return (
        <div className="container py-5">
            <div className="row mb-4">
                <div className="col-md-8">
                    <h1>Dashboard</h1>
                    <p className="text-muted">
                        Welcome back, {user.name}!{" "}
                        {user.role === "student" ? "Continue your learning journey." : "Manage your courses and students."}
                    </p>
                </div>
                <div className="col-md-4 text-md-end">
                    {user.role === "instructor" && (
                        <button className="btn btn-primary">
                            <i className="bi bi-plus-circle me-2"></i> Create New Course
                        </button>
                    )}
                </div>
            </div>

            <div className="row mb-4">
                <div className="col-md-4">
                    <div className="card border-0 shadow-sm h-100">
                        <div className="card-body">
                            <div className="d-flex align-items-center mb-3">
                                <div className="bg-primary bg-opacity-10 p-3 rounded-circle me-3">
                                    <i className="bi bi-book fs-4 text-primary"></i>
                                </div>
                                <div>
                                    <h6 className="mb-0 text-muted">Enrolled Courses</h6>
                                    <h3 className="mb-0">{enrolledCourses.length}</h3>
                                </div>
                            </div>
                            <div className="progress" style={{ height: "6px" }}>
                                <div className="progress-bar bg-primary" style={{ width: "60%" }}></div>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="col-md-4">
                    <div className="card border-0 shadow-sm h-100">
                        <div className="card-body">
                            <div className="d-flex align-items-center mb-3">
                                <div className="bg-success bg-opacity-10 p-3 rounded-circle me-3">
                                    <i className="bi bi-check-circle fs-4 text-success"></i>
                                </div>
                                <div>
                                    <h6 className="mb-0 text-muted">Completed</h6>
                                    <h3 className="mb-0">{completedCourses.length}</h3>
                                </div>
                            </div>
                            <div className="progress" style={{ height: "6px" }}>
                                <div className="progress-bar bg-success" style={{ width: "25%" }}></div>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="col-md-4">
                    <div className="card border-0 shadow-sm h-100">
                        <div className="card-body">
                            <div className="d-flex align-items-center mb-3">
                                <div className="bg-warning bg-opacity-10 p-3 rounded-circle me-3">
                                    <i className="bi bi-clock fs-4 text-warning"></i>
                                </div>
                                <div>
                                    <h6 className="mb-0 text-muted">Hours Spent</h6>
                                    <h3 className="mb-0">24.5</h3>
                                </div>
                            </div>
                            <div className="progress" style={{ height: "6px" }}>
                                <div className="progress-bar bg-warning" style={{ width: "40%" }}></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

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
                        className={`nav-link ${activeTab === "courses" ? "active" : ""}`}
                        onClick={() => setActiveTab("courses")}
                    >
                        My Courses
                    </button>
                </li>
                {user.role === "instructor" && (
                    <li className="nav-item">
                        <button
                            className={`nav-link ${activeTab === "students" ? "active" : ""}`}
                            onClick={() => setActiveTab("students")}
                        >
                            Students
                        </button>
                    </li>
                )}
                <li className="nav-item">
                    <button
                        className={`nav-link ${activeTab === "settings" ? "active" : ""}`}
                        onClick={() => setActiveTab("settings")}
                    >
                        Settings
                    </button>
                </li>
            </ul>

            <div className="tab-content">
                {activeTab === "overview" && (
                    <div>
                        <h3 className="mb-4">Continue Learning</h3>
                        <div className="row g-4">
                            {enrolledCourses.map((course) => (
                                <div key={course.id} className="col-md-6 col-lg-4">
                                    <div className="card h-100 border-0 shadow-sm">
                                        <div className="position-relative">
                                            <img
                                                src={course.thumbnail || `/placeholder.svg?height=160&width=400&query=${course.title} course`}
                                                className="card-img-top"
                                                alt={course.title}
                                                style={{ height: "160px", objectFit: "cover" }}
                                            />
                                            <div className="position-absolute bottom-0 start-0 w-100 p-3 bg-dark bg-opacity-75 text-white">
                                                <div className="d-flex justify-content-between align-items-center">
                                                    <span>Progress: 60%</span>
                                                    <span>12/20 lessons</span>
                                                </div>
                                                <div className="progress mt-2" style={{ height: "6px" }}>
                                                    <div className="progress-bar bg-primary" style={{ width: "60%" }}></div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="card-body">
                                            <h5 className="card-title">{course.title}</h5>
                                            <p className="card-text text-muted small">{course.description}</p>
                                            <button className="btn btn-primary w-100">Continue</button>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>

                        {user.role === "instructor" && (
                            <div className="mt-5">
                                <h3 className="mb-4">Instructor Stats</h3>
                                <div className="row g-4">
                                    <div className="col-md-4">
                                        <div className="card border-0 shadow-sm">
                                            <div className="card-body text-center">
                                                <h5 className="text-muted mb-1">Total Students</h5>
                                                <h2 className="mb-0">{studentStats.totalStudents}</h2>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-md-4">
                                        <div className="card border-0 shadow-sm">
                                            <div className="card-body text-center">
                                                <h5 className="text-muted mb-1">Average Rating</h5>
                                                <h2 className="mb-0">{studentStats.averageRating}</h2>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-md-4">
                                        <div className="card border-0 shadow-sm">
                                            <div className="card-body text-center">
                                                <h5 className="text-muted mb-1">Total Revenue</h5>
                                                <h2 className="mb-0">${studentStats.totalRevenue}</h2>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>
                )}

                {activeTab === "courses" && (
                    <div>
                        <div className="d-flex justify-content-between align-items-center mb-4">
                            <h3>My Courses</h3>
                            {user.role === "instructor" && (
                                <button className="btn btn-primary">
                                    <i className="bi bi-plus-circle me-2"></i> Create New Course
                                </button>
                            )}
                        </div>

                        <div className="table-responsive">
                            <table className="table table-hover">
                                <thead className="table-light">
                                <tr>
                                    <th>Course</th>
                                    <th>Category</th>
                                    <th>Progress</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                {(user.role === "instructor" ? instructorCourses : enrolledCourses).map((course) => (
                                    <tr key={course.id}>
                                        <td>
                                            <div className="d-flex align-items-center">
                                                <img
                                                    src={course.thumbnail || `/placeholder.svg?height=40&width=60&query=${course.title} course`}
                                                    alt={course.title}
                                                    className="me-3 rounded"
                                                    width="60"
                                                    height="40"
                                                    style={{ objectFit: "cover" }}
                                                />
                                                <div>
                                                    <h6 className="mb-0">{course.title}</h6>
                                                    <small className="text-muted">{course.instructor.name}</small>
                                                </div>
                                            </div>
                                        </td>
                                        <td>{course.category}</td>
                                        <td>
                                            <div className="d-flex align-items-center">
                                                <div className="progress flex-grow-1 me-2" style={{ height: "6px" }}>
                                                    <div
                                                        className="progress-bar bg-primary"
                                                        style={{ width: user.role === "instructor" ? "100%" : "60%" }}
                                                    ></div>
                                                </div>
                                                <span>{user.role === "instructor" ? "Published" : "60%"}</span>
                                            </div>
                                        </td>
                                        <td>
                        <span
                            className={`badge bg-${user.role === "instructor" ? "success" : "primary"} bg-opacity-10 text-${user.role === "instructor" ? "success" : "primary"} px-3 py-2`}
                        >
                          {user.role === "instructor" ? "Active" : "In Progress"}
                        </span>
                                        </td>
                                        <td>
                                            <div className="d-flex gap-2">
                                                <button className="btn btn-sm btn-outline-primary">
                                                    {user.role === "instructor" ? "Edit" : "Continue"}
                                                </button>
                                                {user.role === "instructor" && (
                                                    <button className="btn btn-sm btn-outline-danger">Delete</button>
                                                )}
                                            </div>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}

                {activeTab === "students" && user.role === "instructor" && (
                    <div>
                        <h3 className="mb-4">Students</h3>
                        <div className="table-responsive">
                            <table className="table table-hover">
                                <thead className="table-light">
                                <tr>
                                    <th>Student</th>
                                    <th>Enrolled Courses</th>
                                    <th>Progress</th>
                                    <th>Last Active</th>
                                    <th>Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                {[1, 2, 3, 4, 5].map((index) => (
                                    <tr key={index}>
                                        <td>
                                            <div className="d-flex align-items-center">
                                                <img
                                                    src={`/thoughtful-artist.png?height=40&width=40&query=person ${index}`}
                                                    alt={`Student ${index}`}
                                                    className="me-3 rounded-circle"
                                                    width="40"
                                                    height="40"
                                                />
                                                <div>
                                                    <h6 className="mb-0">Student {index}</h6>
                                                    <small className="text-muted">student{index}@example.com</small>
                                                </div>
                                            </div>
                                        </td>
                                        <td>{Math.floor(Math.random() * 3) + 1}</td>
                                        <td>
                                            <div className="d-flex align-items-center">
                                                <div className="progress flex-grow-1 me-2" style={{ height: "6px" }}>
                                                    <div
                                                        className="progress-bar bg-primary"
                                                        style={{ width: `${Math.floor(Math.random() * 100)}%` }}
                                                    ></div>
                                                </div>
                                                <span>{Math.floor(Math.random() * 100)}%</span>
                                            </div>
                                        </td>
                                        <td>{Math.floor(Math.random() * 24)} hours ago</td>
                                        <td>
                                            <button className="btn btn-sm btn-outline-primary">Message</button>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )}

                {activeTab === "settings" && (
                    <div>
                        <h3 className="mb-4">Account Settings</h3>
                        <div className="card border-0 shadow-sm">
                            <div className="card-body">
                                <form>
                                    <div className="mb-4">
                                        <div className="d-flex align-items-center">
                                            <img
                                                src={`/placeholder.svg?height=100&width=100&query=person`}
                                                alt="Profile"
                                                className="rounded-circle me-4"
                                                width="100"
                                                height="100"
                                            />
                                            <div>
                                                <h5 className="mb-1">{user.name}</h5>
                                                <p className="text-muted mb-3">{user.role}</p>
                                                <button type="button" className="btn btn-sm btn-outline-primary">
                                                    Change Photo
                                                </button>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="row mb-3">
                                        <div className="col-md-6 mb-3 mb-md-0">
                                            <label htmlFor="firstName" className="form-label">
                                                First Name
                                            </label>
                                            <input
                                                type="text"
                                                className="form-control"
                                                id="firstName"
                                                defaultValue={user.name.split(" ")[0]}
                                            />
                                        </div>
                                        <div className="col-md-6">
                                            <label htmlFor="lastName" className="form-label">
                                                Last Name
                                            </label>
                                            <input
                                                type="text"
                                                className="form-control"
                                                id="lastName"
                                                defaultValue={user.name.split(" ")[1] || ""}
                                            />
                                        </div>
                                    </div>

                                    <div className="mb-3">
                                        <label htmlFor="email" className="form-label">
                                            Email
                                        </label>
                                        <input
                                            type="email"
                                            className="form-control"
                                            id="email"
                                            defaultValue={`${user.name.toLowerCase().replace(" ", ".")}@example.com`}
                                        />
                                    </div>

                                    <div className="mb-3">
                                        <label htmlFor="bio" className="form-label">
                                            Bio
                                        </label>
                                        <textarea
                                            className="form-control"
                                            id="bio"
                                            rows={4}
                                            defaultValue={`I am a ${user.role} interested in online learning.`}
                                        ></textarea>
                                    </div>

                                    <div className="mb-4">
                                        <label htmlFor="password" className="form-label">
                                            Password
                                        </label>
                                        <input type="password" className="form-control" id="password" placeholder="Enter new password" />
                                        <div className="form-text">Leave blank to keep your current password.</div>
                                    </div>

                                    <div className="d-flex justify-content-end">
                                        <button type="button" className="btn btn-outline-secondary me-2">
                                            Cancel
                                        </button>
                                        <button type="submit" className="btn btn-primary">
                                            Save Changes
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    )
}

export default DashboardPage
