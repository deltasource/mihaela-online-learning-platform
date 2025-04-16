"use client"

import type React from "react"
import { useState } from "react"
import { GraduationCap, Star, BookOpen, User } from "lucide-react"
import type { User as UserType } from "../../assets/mockData"

interface TabPanelProps {
    children: React.ReactNode
    value: number
    index: number
}

const TabPanel: React.FC<TabPanelProps> = ({ children, value, index }) => (
    <div role="tabpanel" hidden={value !== index} id={`profile-tabpanel-${index}`}>
        {value === index && <div className="p-4">{children}</div>}
    </div>
)

interface ProfileTabsProps {
    user: UserType
}

const ProfileTabs: React.FC<ProfileTabsProps> = ({ user }) => {
    const [value, setValue] = useState<number>(0)

    const handleChange = (newValue: number) => {
        setValue(newValue)
    }

    return (
        <div>
            <ul className="nav nav-tabs" role="tablist">
                <li className="nav-item" role="presentation">
                    <button
                        className={`nav-link d-flex align-items-center ${value === 0 ? "active" : ""}`}
                        onClick={() => handleChange(0)}
                        style={{ minHeight: "64px", fontSize: "0.875rem", color: value === 0 ? "#6a98dc" : "" }}
                    >
                        <GraduationCap className="me-2" size={18} />
                        Courses
                    </button>
                </li>
                <li className="nav-item" role="presentation">
                    <button
                        className={`nav-link d-flex align-items-center ${value === 1 ? "active" : ""}`}
                        onClick={() => handleChange(1)}
                        style={{ minHeight: "64px", fontSize: "0.875rem", color: value === 1 ? "#6591d3" : "" }}
                    >
                        <Star className="me-2" size={18} />
                        Achievements
                    </button>
                </li>
                {user.role === "Student" && (
                    <li className="nav-item" role="presentation">
                        <button
                            className={`nav-link d-flex align-items-center ${value === 2 ? "active" : ""}`}
                            onClick={() => handleChange(2)}
                            style={{ minHeight: "64px", fontSize: "0.875rem", color: value === 2 ? "#6793d3" : "" }}
                        >
                            <BookOpen className="me-2" size={18} />
                            Learning
                        </button>
                    </li>
                )}
                <li className="nav-item" role="presentation">
                    <button
                        className={`nav-link d-flex align-items-center ${value === 3 ? "active" : ""}`}
                        onClick={() => handleChange(3)}
                        style={{ minHeight: "64px", fontSize: "0.875rem", color: value === 3 ? "#6f98d5" : "" }}
                    >
                        <User className="me-2" size={18} />
                        About
                    </button>
                </li>
            </ul>

            <TabPanel value={value} index={0}>
                <div className="row g-4">
                    {user.courses?.map((course, index) => (
                        <div className="col-12 col-sm-6 col-md-4" key={index}>
                            <div
                                className="card h-100"
                                style={{
                                    transition: "all 0.3s",
                                }}
                                onMouseOver={(e) => {
                                    e.currentTarget.style.transform = "translateY(-5px)"
                                    e.currentTarget.style.boxShadow = "0 8px 16px rgba(0, 0, 0, 0.1)"
                                }}
                                onMouseOut={(e) => {
                                    e.currentTarget.style.transform = "translateY(0)"
                                    e.currentTarget.style.boxShadow = ""
                                }}
                            >
                                <div className="card-body">
                                    <h5 className="card-title text-primary">{course.title}</h5>
                                    <p className="card-text text-muted">{course.description}</p>
                                    <div className="mt-3">
                                        <p className="text-muted mb-1">Progress: {course.progress}%</p>
                                        <div className="progress" style={{ height: "8px", borderRadius: "4px" }}>
                                            <div
                                                className="progress-bar bg-primary"
                                                role="progressbar"
                                                style={{ width: `${course.progress}%` }}
                                                aria-valuenow={course.progress}
                                                aria-valuemin={0}
                                                aria-valuemax={100}
                                            ></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </TabPanel>

            <TabPanel value={value} index={1}>
                <div className="text-center py-5">
                    <h4>Achievements</h4>
                    <p className="text-muted">Your achievements will appear here.</p>
                </div>
            </TabPanel>

            {user.role === "Student" && (
                <TabPanel value={value} index={2}>
                    <div className="text-center py-5">
                        <h4>Learning Progress</h4>
                        <p className="text-muted">Your learning statistics will appear here.</p>
                    </div>
                </TabPanel>
            )}

            <TabPanel value={value} index={3}>
                <div className="text-center py-5">
                    <h4>About {user.name}</h4>
                    <p className="text-muted">Additional information will appear here.</p>
                </div>
            </TabPanel>
        </div>
    )
}

export default ProfileTabs
