import { Link } from "react-router-dom"
import { ArrowRight, Award, Users, BookOpen, Clock } from "lucide-react"
import CourseCard from "./CourseCard"
import { popularCourses, categories } from "../assets/data"
import { Key } from "react"

const HomePage = () => {
    return (
        <>
            <section className="hero-section">
                <div className="container">
                    <div className="row align-items-center">
                        <div className="col-lg-6 mb-5 mb-lg-0">
                            <h1 className="display-4 fw-bold mb-3">Learn Without Limits</h1>
                            <p className="lead mb-4">
                                Start, switch, or advance your career with thousands of courses from expert instructors.
                            </p>
                            <div className="d-flex flex-wrap gap-2">
                                <Link to="/courses" className="btn  btn-lg px-4">
                                    Explore Courses
                                </Link>
                                <Link to="/signup" className="btn btn-outline-light btn-lg px-4">
                                    Join For Free
                                </Link>
                            </div>
                        </div>
                        <div className="col-lg-6">
                            <img
                                src="/diverse-online-learning.png"
                                alt="Students learning online"
                                className="img-fluid rounded-3 shadow"
                            />
                        </div>
                    </div>
                </div>
            </section>
            <section className="py-5 bg-light">
                <div className="container">
                    <div className="row g-4 text-center">
                        <div className="col-md-3 col-6">
                            <div className="d-flex flex-column align-items-center">
                                <BookOpen size={32} className="text-primary mb-2"/>
                                <h2 className="fw-bold mb-0">10K+</h2>
                                <p className="text-muted">Courses</p>
                            </div>
                        </div>
                        <div className="col-md-3 col-6">
                            <div className="d-flex flex-column align-items-center">
                                <Users size={32} className="text-primary mb-2"/>
                                <h2 className="fw-bold mb-0">5M+</h2>
                                <p className="text-muted">Students</p>
                            </div>
                        </div>
                        <div className="col-md-3 col-6">
                            <div className="d-flex flex-column align-items-center">
                                <Award size={32} className="text-primary mb-2"/>
                                <h2 className="fw-bold mb-0">2K+</h2>
                                <p className="text-muted">Instructors</p>
                            </div>
                        </div>
                        <div className="col-md-3 col-6">
                            <div className="d-flex flex-column align-items-center">
                                <Clock size={32} className="text-primary mb-2"/>
                                <h2 className="fw-bold mb-0">50K+</h2>
                                <p className="text-muted">Hours of Content</p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <section className="py-5">
                <div className="container">
                    <div className="d-flex justify-content-between align-items-center mb-4">
                        <h2 className="fw-bold">Top Categories</h2>
                        <Link to="/categories" className="btn btn-outline-primary d-flex align-items-center gap-1">
                            View All <ArrowRight size={16}/>
                        </Link>
                    </div>
                    <div className="row g-4">
                        {categories.map((category, index) => (
                            <div key={index} className="col-lg-3 col-md-4 col-sm-6">
                                <Link to={`/category/${category.slug}`} className="text-decoration-none">
                                    <div className="card h-100 border-0 shadow-sm">
                                        <div className="card-body d-flex align-items-center p-4">
                                            <div className="rounded-circle bg-light p-3 me-3">{category.icon}</div>
                                            <div>
                                                <h5 className="card-title mb-1">{category.name}</h5>
                                                <p className="card-text text-muted mb-0">{category.courseCount} courses</p>
                                            </div>
                                        </div>
                                    </div>
                                </Link>
                            </div>
                        ))}
                    </div>
                </div>
            </section>
            <section className="py-5 bg-light">
                <div className="container">
                    <div className="d-flex justify-content-between align-items-center mb-4">
                        <h2 className="fw-bold">Popular Courses</h2>
                        <Link to="/courses" className="btn btn-outline-primary d-flex align-items-center gap-1">
                            View All <ArrowRight size={16}/>
                        </Link>
                    </div>
                    <div className="row g-4">
                        {popularCourses.map((course: any, index: Key | null | undefined) => (
                            <div key={index} className="col-lg-3 col-md-6">
                                <CourseCard course={course} />
                            </div>
                        ))}
                    </div>
                </div>
            </section>
            <section className="py-5">
                <div className="container">
                    <div className="row align-items-center">
                        <div className="col-lg-6 mb-4 mb-lg-0">
                            <img src="/online-classroom.png" alt="Become an instructor" className="img-fluid rounded-3 shadow" />
                        </div>
                        <div className="col-lg-6">
                            <h2 className="fw-bold mb-3">Become an Instructor</h2>
                            <p className="lead mb-4">
                                Join thousands of instructors and earn money sharing your knowledge with students around the world.
                            </p>
                            <ul className="list-unstyled mb-4">
                                <li className="d-flex align-items-center mb-3">
                                    <div className="bg-primary rounded-circle p-2 me-3">
                                        <Award size={20} className="text-white" />
                                    </div>
                                    <span>Share your expertise with millions of students</span>
                                </li>
                                <li className="d-flex align-items-center mb-3">
                                    <div className="bg-primary rounded-circle p-2 me-3">
                                        <Users size={20} className="text-white" />
                                    </div>
                                    <span>Build your professional network</span>
                                </li>
                                <li className="d-flex align-items-center">
                                    <div className="bg-primary rounded-circle p-2 me-3">
                                        <Clock size={20} className="text-white" />
                                    </div>
                                    <span>Earn money with every student enrollment</span>
                                </li>
                            </ul>
                            <Link to="/teach" className="btn btn-primary btn-lg px-4">
                                Start Teaching Today
                            </Link>
                        </div>
                    </div>
                </div>
            </section>
        </>
    )
}

export default HomePage
