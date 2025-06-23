import { Link } from 'react-router-dom';
import { Clock, Star, Users, Heart } from 'lucide-react';
import { Course } from '../../types/course';

interface CourseCardProps {
  course: Course;
}

export const CourseCard = ({ course }: CourseCardProps) => {
  const formatDuration = (minutes: number) => {
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return `${hours}h ${mins}m`;
  };

  return (
    <div className="card transition-all duration-300 hover:shadow-md">
      <div className="relative">
        <img 
          src={course.thumbnail} 
          alt={course.title}
          className="h-48 w-full object-cover"
        />
        <div className="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/60 to-transparent p-4">
          <div className="flex justify-between items-end">
            <div className="badge badge-primary">{course.level}</div>
            <div className="flex items-center text-white text-sm">
              <Heart className="h-4 w-4 mr-1 fill-current" />
              <span className="font-medium">FREE</span>
            </div>
          </div>
        </div>
      </div>
      
      <div className="p-4">
        <h3 className="font-bold text-lg mb-2 line-clamp-2">
          <Link to={`/courses/${course.id}`} className="hover:text-primary-600">
            {course.title}
          </Link>
        </h3>
        
        <p className="text-sm text-gray-600 mb-4 line-clamp-2">
          {course.description}
        </p>
        
        <div className="flex items-center text-sm text-gray-500 mb-3">
          <span className="inline-block font-medium">
            {course.instructorName}
          </span>
        </div>
        
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <div className="flex items-center">
              <Star className="w-4 h-4 text-yellow-500 mr-1" />
              <span className="text-sm font-medium">{course.rating.toFixed(1)}</span>
              <span className="text-xs text-gray-500 ml-1">({course.ratingCount})</span>
            </div>
            
            <div className="flex items-center">
              <Users className="w-4 h-4 text-gray-400 mr-1" />
              <span className="text-sm">{course.enrollmentCount}</span>
            </div>
            
            <div className="flex items-center">
              <Clock className="w-4 h-4 text-gray-400 mr-1" />
              <span className="text-sm">{formatDuration(course.duration)}</span>
            </div>
          </div>
        </div>
        
        <div className="mt-4 flex items-center justify-between">
          <div className="text-lg font-bold text-green-600">
            FREE
          </div>
          
          <Link 
            to={`/courses/${course.id}`} 
            className="btn-primary"
          >
            Start Learning
          </Link>
        </div>
      </div>
    </div>
  );
};