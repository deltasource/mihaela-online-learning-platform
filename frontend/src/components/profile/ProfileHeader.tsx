"use client"

import type React from "react"
import { Linkedin, Instagram, Github } from "lucide-react"
import type { User } from "../../assets/mockData"

interface ProfileHeaderProps {
    user: User
}

const ProfileHeader: React.FC<ProfileHeaderProps> = ({ user }) => {
    return (
        <div className="profile-header">
            <div
                className="cover-image"
                style={{
                    height: "200px",
                    backgroundColor: "#e7f1ff",
                    position: "relative",
                }}
            >
                {user.coverImage && (
                    <img
                        src={user.coverImage || "/placeholder.svg"}
                        alt="Cover"
                        style={{
                            width: "100%",
                            height: "100%",
                            objectFit: "cover",
                        }}
                    />
                )}
            </div>

            <div className="px-4 pb-4">
                <div className="d-flex align-items-end mb-3">
                    <img
                        src={user.avatar || "/placeholder.svg?height=120&width=120&query=person"}
                        alt={user.name}
                        className="rounded-circle"
                        style={{
                            width: "120px",
                            height: "120px",
                            border: "4px solid white",
                            marginTop: "-50px",
                            boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
                        }}
                    />
                    <div className="ms-auto mt-3">
                        <button
                            className="btn btn-outline-primary"
                            style={{
                                transition: "all 0.3s",
                            }}
                            onMouseOver={(e) => {
                                e.currentTarget.style.transform = "translateY(-2px)"
                                e.currentTarget.style.boxShadow = "0 4px 12px rgba(37, 99, 235, 0.2)"
                            }}
                            onMouseOut={(e) => {
                                e.currentTarget.style.transform = "translateY(0)"
                                e.currentTarget.style.boxShadow = "none"
                            }}
                        >
                            <i className="bi bi-pencil me-2"></i>
                            Edit Profile
                        </button>
                    </div>
                </div>

                <div className="d-flex align-items-center mb-2">
                    <h4 className="fw-bold text-primary mb-0">{user.name}</h4>
                    <span className="badge bg-primary ms-2">{user.role}</span>
                </div>

                <p className="text-muted mb-3">{user.title}</p>

                <p className="mb-3 text-muted">{user.bio}</p>

                <div className="d-flex gap-2">
                    {user.socialLinks?.linkedin && (
                        <a
                            href={user.socialLinks.linkedin}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="btn btn-light"
                            style={{
                                color: "#6a95d5",
                                transition: "all 0.3s",
                            }}
                            onMouseOver={(e) => {
                                e.currentTarget.style.backgroundColor = "#729ddd"
                                e.currentTarget.style.color = "white"
                            }}
                            onMouseOut={(e) => {
                                e.currentTarget.style.backgroundColor = ""
                                e.currentTarget.style.color = "#6893d1"
                            }}
                        >
                            <Linkedin size={20} />
                        </a>
                    )}
                    {user.socialLinks?.instagram && (
                        <a
                            href={user.socialLinks.instagram}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="btn btn-light"
                            style={{
                                color: "#719ad6",
                                transition: "all 0.3s",
                            }}
                            onMouseOver={(e) => {
                                e.currentTarget.style.backgroundColor = "#6188c3"
                                e.currentTarget.style.color = "white"
                            }}
                            onMouseOut={(e) => {
                                e.currentTarget.style.backgroundColor = ""
                                e.currentTarget.style.color = "#6f99d8"
                            }}
                        >
                            <Instagram size={20} />
                        </a>
                    )}
                    {user.socialLinks?.github && (
                        <a
                            href={user.socialLinks.github}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="btn btn-light"
                            style={{
                                color: "#6796da",
                                transition: "all 0.3s",
                            }}
                            onMouseOver={(e) => {
                                e.currentTarget.style.backgroundColor = "#577aae"
                                e.currentTarget.style.color = "white"
                            }}
                            onMouseOut={(e) => {
                                e.currentTarget.style.backgroundColor = ""
                                e.currentTarget.style.color = "#739bd5"
                            }}
                        >
                            <Github size={20} />
                        </a>
                    )}
                </div>
            </div>
        </div>
    )
}

export default ProfileHeader
