import { Link } from "react-router-dom"
import { Star } from "lucide-react"

interface CourseCardProps {
    course: {
        id: number
        title: string
        instructor: string
        instructorAvatar: string
        image: string
        price: number
        originalPrice?: number
        rating: number
        reviewCount: number
        category: string
        level: string
        duration: string
    }
}

const CourseCard = ({ course }: CourseCardProps) => {
    return (
        <div className="card course-card h-100">
            <Link to={`/course/${course.id}`}>
                <img src={course.image || "/placeholder.svg"} className="card-img-top" alt={course.title} />
            </Link>
            <div className="card-body d-flex flex-column">
                <div className="d-flex justify-content-between align-items-center mb-2">
                    <span className="category-badge">{course.category}</span>
                    <span className="badge bg-light text-dark">{course.level}</span>
                </div>
                <Link to={`/course/${course.id}`} className="text-decoration-none">
                    <h5 className="card-title mb-2">{course.title}</h5>
                </Link>
                <div className="d-flex align-items-center mb-2">
                    <img
                        src={course.instructorAvatar || "/placeholder.svg"}
                        alt={course.instructor}
                        className="instructor-avatar me-2"
                    />
                    <small className="text-muted">{course.instructor}</small>
                </div>
                <div className="d-flex align-items-center mb-2">
                    <div className="me-2 d-flex align-items-center">
                        <span className="fw-bold me-1">{course.rating.toFixed(1)}</span>
                        <div className="rating-stars d-flex">
                            {[...Array(5)].map((_, i) => (
                                <Star key={i} size={14} fill={i < Math.floor(course.rating) ? "currentColor" : "none"} />
                            ))}
                        </div>
                    </div>
                    <small className="text-muted">({course.reviewCount})</small>
                </div>
                <div className="d-flex align-items-center mb-2">
                    <small className="text-muted">{course.duration}</small>
                </div>
                <div className="mt-auto d-flex justify-content-between align-items-center">
                    <div>
                        <span className="fw-bold">${course.price}</span>
                        {course.originalPrice && (
                            <small className="text-muted text-decoration-line-through ms-2">${course.originalPrice}</small>
                        )}
                    </div>
                    <button className="btn btn-sm btn-primary">Add to Cart</button>
                </div>
            </div>
        </div>
    )
}

export default CourseCard
