import { useEffect, useState } from 'react';
import { Check, Clock } from 'lucide-react';
import { Course, CourseEnrollment, Lesson } from '../../types/course';
import { useAuthStore } from '../../stores/authStore';

interface CourseProgressProps {
  course: Course;
  enrollment?: CourseEnrollment;
}

export const CourseProgress = ({ course, enrollment }: CourseProgressProps) => {
  const { user } = useAuthStore();
  const [completedLessons, setCompletedLessons] = useState<string[]>([]);
  
  useEffect(() => {
    if (user && course) {
      const completedLessonsJSON = localStorage.getItem(
        `user_${user.id}_course_${course.id}_lessons`
      );
      
      if (completedLessonsJSON) {
        setCompletedLessons(JSON.parse(completedLessonsJSON));
      }
    }
  }, [user, course]);
  
  if (!course || !course.lessons || course.lessons.length === 0) {
    return null;
  }
  
  const progress = enrollment?.progress || 0;
  
  return (
    <div className="bg-white shadow rounded-lg overflow-hidden">
      <div className="p-4 bg-primary-50 border-b border-primary-100">
        <h3 className="text-lg font-semibold text-primary-800">Course Progress</h3>
        
        <div className="mt-2">
          <div className="w-full bg-gray-200 rounded-full h-2.5">
            <div 
              className="bg-primary-600 h-2.5 rounded-full" 
              style={{ width: `${progress}%` }}
            ></div>
          </div>
          <p className="text-sm text-gray-600 mt-2">
            {progress}% complete
          </p>
        </div>
      </div>
      
      <div className="p-4">
        <h4 className="font-medium text-gray-900 mb-3">Course Content</h4>
        
        <ul className="space-y-3">
          {course.lessons.map((lesson: Lesson) => {
            const isCompleted = completedLessons.includes(lesson.id);
            
            return (
              <li key={lesson.id} className="flex items-start">
                <div className={`flex-shrink-0 h-5 w-5 rounded-full flex items-center justify-center mt-0.5 ${
                  isCompleted ? 'bg-success-500 text-white' : 'bg-gray-200'
                }`}>
                  {isCompleted ? (
                    <Check size={12} />
                  ) : (
                    <Clock size={12} className="text-gray-500" />
                  )}
                </div>
                <div className="ml-3">
                  <p className={`text-sm font-medium ${
                    isCompleted ? 'text-success-600' : 'text-gray-900'
                  }`}>
                    {lesson.title}
                  </p>
                  <p className="text-xs text-gray-500">
                    {lesson.duration} min
                  </p>
                </div>
              </li>
            );
          })}
        </ul>
      </div>
    </div>
  );
};