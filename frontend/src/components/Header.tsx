"use client"

import type React from "react"

import { useState } from "react"
import { Link } from "react-router-dom"
import { Search, ShoppingCart, User, Menu, X } from "lucide-react"

interface HeaderProps {
    onProfileClick: () => void
    onInstructorClick: () => void
}

const Header: React.FC<HeaderProps> = ({ onProfileClick, onInstructorClick }) => {
    const [isMenuOpen, setIsMenuOpen] = useState(false)

    return (
        <header className="sticky-top">
            <nav className="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
                <div className="container">
                    <Link className="navbar-brand d-flex align-items-center" to="/">
            <span className="h4 mb-0 fw-bold" style={{ color: "#60a5fa" }}>
              Elearning
            </span>
                    </Link>

                    <button className="navbar-toggler border-0" type="button" onClick={() => setIsMenuOpen(!isMenuOpen)}>
                        {isMenuOpen ? <X size={24} /> : <Menu size={24} />}
                    </button>

                    <div className={`collapse navbar-collapse ${isMenuOpen ? "show" : ""}`}>
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                            <li className="nav-item dropdown">
                                <a className="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                    Categories
                                </a>
                                <ul className="dropdown-menu">
                                    <li>
                                        <a className="dropdown-item" href="#">
                                            Web Development
                                        </a>
                                    </li>
                                    <li>
                                        <a className="dropdown-item" href="#">
                                            Data Science
                                        </a>
                                    </li>
                                    <li>
                                        <a className="dropdown-item" href="#">
                                            Mobile Development
                                        </a>
                                    </li>
                                    <li>
                                        <a className="dropdown-item" href="#">
                                            UI/UX Design
                                        </a>
                                    </li>
                                    <li>
                                        <hr className="dropdown-divider" />
                                    </li>
                                    <li>
                                        <a className="dropdown-item" href="#">
                                            All Categories
                                        </a>
                                    </li>
                                </ul>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/courses">
                                    Courses
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link className="nav-link" to="/dashboard">
                                    My Learning
                                </Link>
                            </li>
                        </ul>

                        <form className="d-flex me-auto position-relative" style={{ maxWidth: "400px" }}>
                            <input className="form-control me-2 ps-4" type="search" placeholder="Search for anything" />
                            <Search className="position-absolute" style={{ left: "10px", top: "10px" }} size={18} />
                        </form>

                        <div className="d-flex align-items-center gap-3 ms-lg-3">
                            <Link to="/cart" className="btn btn-outline-primary position-relative">
                                <ShoppingCart size={20} />
                                <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-primary">
                  3
                </span>
                            </Link>
                            <div className="dropdown">
                                <button
                                    className="btn btn-primary d-flex align-items-center gap-2"
                                    id="userDropdown"
                                    data-bs-toggle="dropdown"
                                    aria-expanded="false"
                                >
                                    <User size={18} />
                                    <span className="d-none d-md-inline">Account</span>
                                </button>
                                <ul className="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                                    <li>
                                        <a className="dropdown-item" href="#" onClick={onProfileClick}>
                                            Student Profile
                                        </a>
                                    </li>
                                    <li>
                                        <a className="dropdown-item" href="#" onClick={onInstructorClick}>
                                            Instructor Profile
                                        </a>
                                    </li>
                                    <li>
                                        <hr className="dropdown-divider" />
                                    </li>
                                    <li>
                                        <Link className="dropdown-item" to="/dashboard">
                                            Dashboard
                                        </Link>
                                    </li>
                                    <li>
                                        <Link className="dropdown-item" to="/settings">
                                            Settings
                                        </Link>
                                    </li>
                                    <li>
                                        <hr className="dropdown-divider" />
                                    </li>
                                    <li>
                                        <a className="dropdown-item" href="#">
                                            Logout
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </nav>
        </header>
    )
}

export default Header
