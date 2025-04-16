"use client"

import type React from "react"
import { ArrowLeft } from "lucide-react"
import ProfileHeader from "./ProfileHeader"
import ProfileTabs from "./ProfileTabs"
import { mockUserData } from "../../assets/mockData"

interface ProfilePageProps {
    onBackClick: () => void
}

const ProfilePage: React.FC<ProfilePageProps> = ({ onBackClick }) => {
    const userData = mockUserData

    return (
        <div className="min-vh-100 bg-light py-4">
            <div className="container">
                <button
                    className="btn btn-link text-decoration-none mb-3"
                    onClick={onBackClick}
                    style={{
                        transition: "all 0.3s",
                    }}
                    onMouseOver={(e) => {
                        e.currentTarget.style.transform = "translateY(-2px)"
                        e.currentTarget.style.boxShadow = "0 4px 12px rgba(13, 110, 253, 0.2)"
                    }}
                    onMouseOut={(e) => {
                        e.currentTarget.style.transform = "translateY(0)"
                        e.currentTarget.style.boxShadow = "none"
                    }}
                >
                    <ArrowLeft size={18} className="me-2" />
                    Back to Home
                </button>
                <div className="bg-white rounded shadow-sm overflow-hidden">
                    <ProfileHeader user={userData} />
                    <ProfileTabs user={userData} />
                </div>
            </div>
        </div>
    )
}

export default ProfilePage
