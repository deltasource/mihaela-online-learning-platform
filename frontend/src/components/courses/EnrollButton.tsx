import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../../stores/authStore';
import { useCourseStore } from '../../stores/courseStore';
import { LoadingSpinner } from '../ui/LoadingSpinner';
import { BookOpen, Heart } from 'lucide-react';

interface EnrollButtonProps {
  courseId: string;
  isEnrolled: boolean;
  firstLessonId?: string;
}

export const EnrollButton = ({ courseId, isEnrolled, firstLessonId }: EnrollButtonProps) => {
  const { user, isAuthenticated } = useAuthStore();
  const { enrollInCourse } = useCourseStore();
  const [isEnrolling, setIsEnrolling] = useState(false);
  const navigate = useNavigate();

  const handleEnroll = async () => {
    if (!isAuthenticated) {
      navigate('/login', { state: { from: `/courses/${courseId}` } });
      return;
    }
    
    if (isEnrolled && firstLessonId) {
      navigate(`/courses/${courseId}/lessons/${firstLessonId}`);
      return;
    }
    
    try {
      setIsEnrolling(true);
      await enrollInCourse(courseId, user!.id);
      setIsEnrolling(false);
      
      if (firstLessonId) {
        navigate(`/courses/${courseId}/lessons/${firstLessonId}`);
      }
    } catch (error) {
      setIsEnrolling(false);
      console.error('Failed to enroll in course:', error);
    }
  };

  return (
    <button
      onClick={handleEnroll}
      disabled={isEnrolling}
      className={`btn ${
        isEnrolled ? 'btn-secondary' : 'btn-primary'
      } w-full flex items-center justify-center py-3`}
    >
      {isEnrolling ? (
        <LoadingSpinner size="sm" color="text-white" />
      ) : (
        <>
          {isEnrolled ? (
            <>
              <BookOpen className="h-5 w-5 mr-2" />
              {firstLessonId ? 'Continue Learning' : 'Go to Course'}
            </>
          ) : (
            <>
              <Heart className="h-5 w-5 mr-2" />
              Start Learning Free
            </>
          )}
        </>
      )}
    </button>
  );
};