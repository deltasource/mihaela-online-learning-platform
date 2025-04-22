import { Link } from "react-router-dom"
import type { Course } from "../types/Course"

interface CourseCardProps {
    course: Course
}

const CourseCard = ({ course }: CourseCardProps) => {
    return (
        <div className="card h-100 shadow-sm border-0 hover-shadow transition-all">
            <img
                src={course.thumbnailUrl || "/placeholder.svg"}
                className="card-img-top"
                alt={course.title}
                style={{ height: "160px", objectFit: "cover" }}
            />
            <div className="card-body d-flex flex-column">
                <div className="d-flex justify-content-between align-items-start mb-2">
                    <span className="badge bg-primary-subtle text-primary">{course.category}</span>
                    <div className="d-flex align-items-center">
                        <i className="bi bi-star-fill text-warning me-1"></i>
                        <span>{course.rating.toFixed(1)}</span>
                    </div>
                </div>
                <h5 className="card-title">{course.title}</h5>
                <p className="card-text text-muted small mb-3">{course.description.substring(0, 80)}...</p>
                <div className="mt-auto">
                    <div className="d-flex justify-content-between align-items-center mb-2">
                        <div className="d-flex align-items-center">
                            <img
                                src={course.instructor.avatarUrl || "/placeholder.svg"}
                                alt={course.instructor.name}
                                className="rounded-circle me-2"
                                width="24"
                                height="24"
                            />
                            <span className="small text-muted">{course.instructor.name}</span>
                        </div>
                    </div>
                    <div className="d-flex justify-content-between align-items-center">
                        <span className="fw-bold">${course.price.toFixed(2)}</span>
                        <Link to={`/courses/${course.id}`} className="btn btn-sm btn-outline-primary">
                            View Course
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default CourseCard
