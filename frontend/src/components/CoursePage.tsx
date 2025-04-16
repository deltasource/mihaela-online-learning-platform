"use client"

import { useState } from "react"
import { Filter, Search, SlidersHorizontal } from "lucide-react"
import CourseCard from "./CourseCard"
import { allCourses } from "../assets/data"

const CoursePage = () => {
    const [showFilters, setShowFilters] = useState(false)

    return (
        <div className="container py-5">
            <div className="row">
                <div className="col-12 mb-4">
                    <h1 className="fw-bold mb-4">All Courses</h1>

                    <div className="d-flex flex-wrap gap-3 justify-content-between align-items-center mb-4">
                        <div className="position-relative" style={{ maxWidth: "500px", width: "100%" }}>
                            <input type="search" className="form-control form-control-lg ps-5" placeholder="Search courses..." />
                            <Search className="position-absolute" style={{ left: "15px", top: "12px" }} size={20} />
                        </div>
                        <button
                            className="btn btn-outline-primary d-md-none d-flex align-items-center gap-2"
                            onClick={() => setShowFilters(!showFilters)}
                        >
                            <SlidersHorizontal size={18} />
                            Filters
                        </button>
                    </div>
                </div>

                <div className={`col-md-3 mb-4 ${showFilters ? "d-block" : "d-none d-md-block"}`}>
                    <div className="card shadow-sm">
                        <div className="card-header bg-white py-3">
                            <h5 className="mb-0 d-flex align-items-center">
                                <Filter size={18} className="me-2" />
                                Filters
                            </h5>
                        </div>
                        <div className="card-body">
                            <div className="mb-4">
                                <h6 className="fw-bold mb-3">Categories</h6>
                                {["Web Development", "Data Science", "Mobile Development", "UI/UX Design", "Business", "Marketing"].map(
                                    (category, index) => (
                                        <div key={index} className="form-check mb-2">
                                            <input className="form-check-input" type="checkbox" id={`category-${index}`} />
                                            <label className="form-check-label" htmlFor={`category-${index}`}>
                                                {category}
                                            </label>
                                        </div>
                                    ),
                                )}
                            </div>

                            <div className="mb-4">
                                <h6 className="fw-bold mb-3">Level</h6>
                                {["Beginner", "Intermediate", "Advanced", "All Levels"].map((level, index) => (
                                    <div key={index} className="form-check mb-2">
                                        <input className="form-check-input" type="checkbox" id={`level-${index}`} />
                                        <label className="form-check-label" htmlFor={`level-${index}`}>
                                            {level}
                                        </label>
                                    </div>
                                ))}
                            </div>

                            <div className="mb-4">
                                <h6 className="fw-bold mb-3">Rating</h6>
                                {[4.5, 4.0, 3.5, 3.0].map((rating, index) => (
                                    <div key={index} className="form-check mb-2">
                                        <input className="form-check-input" type="checkbox" id={`rating-${index}`} />
                                        <label className="form-check-label" htmlFor={`rating-${index}`}>
                                            {rating}+ Stars
                                        </label>
                                    </div>
                                ))}
                            </div>

                            <div className="mb-4">
                                <h6 className="fw-bold mb-3">Duration</h6>
                                {["0-2 Hours", "3-6 Hours", "7-16 Hours", "17+ Hours"].map((duration, index) => (
                                    <div key={index} className="form-check mb-2">
                                        <input className="form-check-input" type="checkbox" id={`duration-${index}`} />
                                        <label className="form-check-label" htmlFor={`duration-${index}`}>
                                            {duration}
                                        </label>
                                    </div>
                                ))}
                            </div>

                            <div>
                                <h6 className="fw-bold mb-3">Price</h6>
                                <div className="form-check mb-2">
                                    <input className="form-check-input" type="checkbox" id="price-free" />
                                    <label className="form-check-label" htmlFor="price-free">
                                        Free
                                    </label>
                                </div>
                                <div className="form-check mb-2">
                                    <input className="form-check-input" type="checkbox" id="price-paid" />
                                    <label className="form-check-label" htmlFor="price-paid">
                                        Paid
                                    </label>
                                </div>
                            </div>

                            <button className="btn btn-primary w-100 mt-3">Apply Filters</button>
                            <button className="btn btn-outline-secondary w-100 mt-2">Reset</button>
                        </div>
                    </div>
                </div>

                <div className="col-md-9">
                    <div className="d-flex justify-content-between align-items-center mb-4">
                        <p className="mb-0">
                            Showing <span className="fw-bold">{allCourses.length}</span> results
                        </p>
                        <select className="form-select" style={{ width: "auto" }}>
                            <option>Most Popular</option>
                            <option>Highest Rated</option>
                            <option>Newest</option>
                            <option>Price: Low to High</option>
                            <option>Price: High to Low</option>
                        </select>
                    </div>

                    <div className="row g-4">
                        {allCourses.map((course, index) => (
                            <div key={index} className="col-lg-4 col-md-6">
                                <CourseCard course={course} />
                            </div>
                        ))}
                    </div>

                    <nav className="mt-5">
                        <ul className="pagination justify-content-center">
                            <li className="page-item disabled">
                                <a className="page-link" href="#" tabIndex={-1}>
                                    Previous
                                </a>
                            </li>
                            <li className="page-item active">
                                <a className="page-link" href="#">
                                    1
                                </a>
                            </li>
                            <li className="page-item">
                                <a className="page-link" href="#">
                                    2
                                </a>
                            </li>
                            <li className="page-item">
                                <a className="page-link" href="#">
                                    3
                                </a>
                            </li>
                            <li className="page-item">
                                <a className="page-link" href="#">
                                    Next
                                </a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
    )
}

export default CoursePage
