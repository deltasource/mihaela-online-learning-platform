"use client"

import type React from "react"
import { useState } from "react"
import { ArrowLeft, Edit, Plus, Linkedin, Github, Instagram } from "lucide-react"
import { mockUserData } from "../../assets/mockData"

interface InstructorPageProps {
    onBackClick: () => void
}

const InstructorPage: React.FC<InstructorPageProps> = ({ onBackClick }) => {
    const [activeTab, setActiveTab] = useState<number>(0)
    const instructor = mockUserData.instructor

    return (
        <div className="container py-4">
            <div className="d-flex align-items-center mb-4">
                <button className="btn btn-light rounded-circle me-3" onClick={onBackClick}>
                    <ArrowLeft size={18} />
                </button>
                <h4 className="mb-0">Instructor Profile</h4>
            </div>

            <div className="card shadow-sm mb-4 overflow-hidden">
                <div
                    className="cover-image"
                    style={{
                        height: "200px",
                        backgroundColor: "#e7f1ff",
                        position: "relative",
                    }}
                >
                    <button className="btn btn-primary btn-sm position-absolute" style={{ top: "16px", right: "16px" }}>
                        <Edit size={16} className="me-2" />
                        Edit Cover
                    </button>
                </div>

                <div className="p-4 pb-0">
                    <div className="row">
                        <div className="col-md-12 d-flex flex-column flex-md-row">
                            <img
                                src={instructor.avatar || "/placeholder.svg?height=120&width=120"}
                                alt={instructor.name}
                                className="rounded-circle"
                                style={{
                                    width: "120px",
                                    height: "120px",
                                    border: "4px solid white",
                                    marginTop: "-60px",
                                    alignSelf: "center",
                                    marginLeft: "0",
                                    boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
                                }}
                            />

                            <div className="ms-md-4 mt-3 mt-md-0 flex-grow-1">
                                <div className="d-flex flex-column flex-md-row justify-content-between align-items-md-center">
                                    <div className="text-center text-md-start">
                                        <h4 className="mb-1">{instructor.name}</h4>
                                        <p className="text-muted mb-0">{instructor.title}</p>
                                    </div>

                                    <div className="d-flex justify-content-center justify-content-md-end gap-2 mt-3 mt-md-0">
                                        {instructor.socialLinks.linkedin && (
                                            <a
                                                href={instructor.socialLinks.linkedin}
                                                target="_blank"
                                                rel="noopener noreferrer"
                                                className="btn btn-light"
                                                style={{ color: "#5390ea" }}
                                            >
                                                <Linkedin size={20} />
                                            </a>
                                        )}
                                        {instructor.socialLinks.github && (
                                            <a
                                                href={instructor.socialLinks.github}
                                                target="_blank"
                                                rel="noopener noreferrer"
                                                className="btn btn-light"
                                                style={{ color: "#5a8ddc" }}
                                            >
                                                <Github size={20} />
                                            </a>
                                        )}
                                        {instructor.socialLinks.instagram && (
                                            <a
                                                href={instructor.socialLinks.instagram}
                                                target="_blank"
                                                rel="noopener noreferrer"
                                                className="btn btn-light"
                                                style={{ color: "#6f9cdf" }}
                                            >
                                                <Instagram size={20} />
                                            </a>
                                        )}
                                    </div>
                                </div>

                                <p className="mt-3 text-center text-md-start">{instructor.bio}</p>
                            </div>
                        </div>
                    </div>

                    <ul className="nav nav-tabs mt-4 justify-content-center">
                        <li className="nav-item">
                            <button className={`nav-link ${activeTab === 0 ? "active" : ""}`} onClick={() => setActiveTab(0)}>
                                Courses
                            </button>
                        </li>
                        <li className="nav-item">
                            <button className={`nav-link ${activeTab === 1 ? "active" : ""}`} onClick={() => setActiveTab(1)}>
                                Teaching Schedule
                            </button>
                        </li>
                        <li className="nav-item">
                            <button className={`nav-link ${activeTab === 2 ? "active" : ""}`} onClick={() => setActiveTab(2)}>
                                Statistics
                            </button>
                        </li>
                    </ul>
                </div>
            </div>

            <div className="mt-4">
                {activeTab === 0 && (
                    <>
                        <div className="d-flex justify-content-between align-items-center mb-4">
                            <h5>Courses ({instructor.courses.length})</h5>
                            <button className="btn btn-primary">
                                <Plus size={18} className="me-2" />
                                Create New Course
                            </button>
                        </div>

                        <div className="row g-4">
                            {instructor.courses.map((course, index) => (
                                <div className="col-12 col-sm-6 col-md-4" key={index}>
                                    <div className="card h-100">
                                        <div className="card-body">
                                            <h5 className="card-title">{course.title}</h5>
                                            <p className="card-text text-muted mb-3">{course.description}</p>
                                            <div className="d-flex align-items-center gap-2 mb-2">
                                                <span className="text-muted">Completion:</span>
                                                <div className="progress flex-grow-1" style={{ height: "8px", borderRadius: "4px" }}>
                                                    <div
                                                        className="progress-bar bg-primary"
                                                        role="progressbar"
                                                        style={{ width: `${course.progress}%` }}
                                                        aria-valuenow={course.progress}
                                                        aria-valuemin={0}
                                                        aria-valuemax={100}
                                                    ></div>
                                                </div>
                                                <span className="text-muted">{course.progress}%</span>
                                            </div>
                                            <div className="d-flex justify-content-end mt-3">
                                                <button className="btn btn-sm btn-outline-primary">Edit</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </>
                )}

                {activeTab === 1 && (
                    <>
                        <h5 className="mb-4">Teaching Schedule</h5>
                        <div className="row g-4">
                            {instructor.teaching.map((schedule, index) => (
                                <div className="col-12" key={index}>
                                    <div className="card">
                                        <div className="card-body">
                                            <div className="d-flex justify-content-between align-items-center flex-wrap">
                                                <h5 className="card-title mb-0">{schedule.courseTitle}</h5>
                                                <span className="badge bg-primary bg-opacity-10 text-primary">
                          Course ID: {schedule.courseId}
                        </span>
                                            </div>
                                            <hr />
                                            <div className="d-flex justify-content-between align-items-center flex-wrap">
                                                <div>
                                                    <p className="text-muted mb-1">Start Date</p>
                                                    <p className="mb-0">{new Date(schedule.startDate).toLocaleDateString()}</p>
                                                </div>
                                                <div>
                                                    <p className="text-muted mb-1">End Date</p>
                                                    <p className="mb-0">{new Date(schedule.endDate).toLocaleDateString()}</p>
                                                </div>
                                                <button className="btn btn-outline-primary btn-sm mt-2 mt-md-0">View Details</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </>
                )}

                {activeTab === 2 && (
                    <>
                        <h5 className="mb-4">Statistics</h5>
                        <div className="row g-4">
                            <div className="col-12 col-sm-6 col-md-3">
                                <div className="card h-100">
                                    <div className="card-body text-center">
                                        <h6 className="card-title text-muted mb-3">Total Students</h6>
                                        <h3 className="mb-0">124</h3>
                                    </div>
                                </div>
                            </div>
                            <div className="col-12 col-sm-6 col-md-3">
                                <div className="card h-100">
                                    <div className="card-body text-center">
                                        <h6 className="card-title text-muted mb-3">Active Courses</h6>
                                        <h3 className="mb-0">{instructor.courses.length}</h3>
                                    </div>
                                </div>
                            </div>
                            <div className="col-12 col-sm-6 col-md-3">
                                <div className="card h-100">
                                    <div className="card-body text-center">
                                        <h6 className="card-title text-muted mb-3">Avg. Rating</h6>
                                        <h3 className="mb-0">4.8</h3>
                                    </div>
                                </div>
                            </div>
                            <div className="col-12 col-sm-6 col-md-3">
                                <div className="card h-100">
                                    <div className="card-body text-center">
                                        <h6 className="card-title text-muted mb-3">Completion Rate</h6>
                                        <h3 className="mb-0">85%</h3>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </>
                )}
            </div>
        </div>
    )
}

export default InstructorPage