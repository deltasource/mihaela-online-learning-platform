import { useEffect, useState } from 'react';
import { useCourseStore } from '../../stores/courseStore';
import { CourseCard } from '../../components/courses/CourseCard';
import { CourseFilter } from '../../components/courses/CourseFilter';
import { CourseFilters } from '../../types/course';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { AlertCircle } from 'lucide-react';

const CourseCatalogPage = () => {
  const { courses, isLoading, error, fetchCourses } = useCourseStore();
  const [filters, setFilters] = useState<CourseFilters>({});

  useEffect(() => {
    fetchCourses(filters);
  }, [fetchCourses, filters]);

  const handleFilterChange = (newFilters: CourseFilters) => {
    setFilters(newFilters);
  };

  return (
    <div className="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
      <div className="text-center mb-10">
        <h1 className="text-3xl font-extrabold text-gray-900 sm:text-4xl">
          Course Catalog
        </h1>
        <p className="mt-3 max-w-2xl mx-auto text-xl text-gray-500 sm:mt-4">
          Browse our collection of high-quality courses designed to help you learn new skills and advance your career.
        </p>
      </div>

      <CourseFilter onFilterChange={handleFilterChange} />

      {isLoading ? (
        <div className="flex justify-center py-20">
          <LoadingSpinner size="lg" />
        </div>
      ) : error ? (
        <div className="text-center py-20">
          <AlertCircle className="mx-auto h-12 w-12 text-error-500" />
          <h3 className="mt-2 text-sm font-medium text-gray-900">Error loading courses</h3>
          <p className="mt-1 text-sm text-gray-500">{error}</p>
          <div className="mt-6">
            <button
              onClick={() => fetchCourses(filters)}
              className="btn-primary"
            >
              Try again
            </button>
          </div>
        </div>
      ) : courses.length === 0 ? (
        <div className="text-center py-20">
          <h3 className="mt-2 text-lg font-medium text-gray-900">No courses found</h3>
          <p className="mt-1 text-sm text-gray-500">
            Try adjusting your filters or check back later for new courses.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-6">
          {courses.map((course) => (
            <CourseCard key={course.id} course={course} />
          ))}
        </div>
      )}
    </div>
  );
};

export default CourseCatalogPage;