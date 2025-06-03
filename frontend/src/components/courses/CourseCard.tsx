import type React from "react"
import { Link } from "react-router-dom"
import { Star, Users, Clock, Play } from "lucide-react"
import type { Course } from "../../types"
import Card from "../ui/Card"

interface CourseCardProps {
  course: Course
}

const CourseCard: React.FC<CourseCardProps> = ({ course }) => {
  return (
      <Link to={`/courses/${course.id}`} className="block">
        <Card className="h-full transition-all duration-200 hover:shadow-xl hover:scale-105 cursor-pointer">
          <div className="relative">
            <img src={course.thumbnail || "/placeholder.svg"} alt={course.title} className="w-full h-48 object-cover" />

            {/* Play button overlay */}
            <div className="absolute inset-0 bg-black bg-opacity-0 hover:bg-opacity-30 transition-all duration-200 flex items-center justify-center">
              <Play className="h-12 w-12 text-white opacity-0 hover:opacity-100 transition-opacity duration-200" />
            </div>

            {/* Course level badge */}
            <div className="absolute top-2 right-2 bg-blue-600 text-white text-xs font-bold px-2 py-1 rounded">
              {course.level}
            </div>

            {/* Free badge */}
            {course.price === 0 && (
                <div className="absolute top-2 left-2 bg-green-600 text-white text-xs font-bold px-2 py-1 rounded">
                  FREE
                </div>
            )}
          </div>

          <div className="p-4">
            <h3 className="text-lg font-semibold mb-2 line-clamp-2 hover:text-blue-600 transition-colors">
              {course.title}
            </h3>
            <p className="text-sm text-gray-600 mb-2">By {course.instructorName}</p>
            <p className="text-sm text-gray-500 mb-3 line-clamp-2">{course.description}</p>

            <div className="flex justify-between items-center mb-3">
              <div className="flex items-center">
                <Star className="h-4 w-4 text-yellow-500 mr-1" />
                <span className="text-sm font-medium">{course.rating.toFixed(1)}</span>
              </div>
              <div className="flex items-center">
                <Users className="h-4 w-4 text-gray-500 mr-1" />
                <span className="text-sm text-gray-600">{course.totalStudents}</span>
              </div>
            </div>

            <div className="flex justify-between items-center">
              <div className="flex items-center text-gray-500">
                <Clock className="h-4 w-4 mr-1" />
                <span className="text-sm">2h 30m</span>
              </div>
              <div className="text-lg font-bold text-blue-600">
                {course.price === 0 ? "Free" : `$${course.price.toFixed(2)}`}
              </div>
            </div>
          </div>
        </Card>
      </Link>
  )
}

export default CourseCard
