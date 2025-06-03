import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Filter, SlidersHorizontal } from 'lucide-react';
import Layout from '../../components/layout/Layout';
import CourseList from '../../components/courses/CourseList';
import CategoryFilter from '../../components/courses/CategoryFilter';
import LevelFilter from '../../components/courses/LevelFilter';
import Button from '../../components/ui/Button';
import { Course } from '../../types';
import { getAllCourses, getCoursesByCategory } from '../../api/courses';

const CoursesPage: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [courses, setCourses] = useState<Course[]>([]);
  const [filteredCourses, setFilteredCourses] = useState<Course[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [selectedCategory, setSelectedCategory] = useState<string | null>(
    searchParams.get('category')
  );
  const [selectedLevel, setSelectedLevel] = useState<string | null>(
    searchParams.get('level')
  );
  const [showFilters, setShowFilters] = useState(false);

  const categories = [
    'Web Development',
    'Data Science',
    'Mobile Development',
    'Design',
    'Business',
    'Marketing',
  ];

  useEffect(() => {
    const fetchCourses = async () => {
      setIsLoading(true);
      try {
        let fetchedCourses;
        if (selectedCategory) {
          fetchedCourses = await getCoursesByCategory(selectedCategory);
        } else {
          fetchedCourses = await getAllCourses();
        }
        setCourses(fetchedCourses);
        setFilteredCourses(fetchedCourses);
      } catch (error) {
        console.error('Failed to fetch courses:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchCourses();
  }, [selectedCategory]);

  useEffect(() => {
    const params = new URLSearchParams();
    if (selectedCategory) params.set('category', selectedCategory);
    if (selectedLevel) params.set('level', selectedLevel);
    setSearchParams(params);

    let result = [...courses];
    
    if (selectedLevel) {
      result = result.filter(course => course.level === selectedLevel);
    }
    
    setFilteredCourses(result);
  }, [selectedCategory, selectedLevel, courses, setSearchParams]);

  const handleCategorySelect = (category: string | null) => {
    setSelectedCategory(category);
  };

  const handleLevelSelect = (level: string | null) => {
    setSelectedLevel(level);
  };

  const toggleFilters = () => {
    setShowFilters(!showFilters);
  };

  const clearFilters = () => {
    setSelectedCategory(null);
    setSelectedLevel(null);
  };

  return (
    <Layout>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900">All Courses</h1>
          <Button
            variant="outline"
            onClick={toggleFilters}
            className="md:hidden flex items-center"
          >
            <Filter className="h-4 w-4 mr-2" />
            Filters
          </Button>
        </div>

        <div className="flex flex-col md:flex-row">
          {/* Filters - Desktop */}
          <div className="hidden md:block md:w-1/4 pr-8">
            <div className="sticky top-8">
              <div className="flex justify-between items-center mb-6">
                <h2 className="text-xl font-semibold text-gray-800 flex items-center">
                  <SlidersHorizontal className="h-5 w-5 mr-2" />
                  Filters
                </h2>
                {(selectedCategory || selectedLevel) && (
                  <button
                    onClick={clearFilters}
                    className="text-sm text-blue-600 hover:text-blue-800"
                  >
                    Clear all
                  </button>
                )}
              </div>

              <CategoryFilter
                categories={categories}
                selectedCategory={selectedCategory}
                onSelectCategory={handleCategorySelect}
              />

              <LevelFilter
                selectedLevel={selectedLevel}
                onSelectLevel={handleLevelSelect}
              />
            </div>
          </div>

          {/* Filters - Mobile */}
          {showFilters && (
            <div className="md:hidden mb-6 p-4 bg-white rounded-lg shadow-md">
              <div className="flex justify-between items-center mb-4">
                <h2 className="text-lg font-semibold text-gray-800">Filters</h2>
                {(selectedCategory || selectedLevel) && (
                  <button
                    onClick={clearFilters}
                    className="text-sm text-blue-600 hover:text-blue-800"
                  >
                    Clear all
                  </button>
                )}
              </div>

              <CategoryFilter
                categories={categories}
                selectedCategory={selectedCategory}
                onSelectCategory={handleCategorySelect}
              />

              <LevelFilter
                selectedLevel={selectedLevel}
                onSelectLevel={handleLevelSelect}
              />
            </div>
          )}

          {/* Course List */}
          <div className="md:w-3/4">
            {isLoading ? (
              <div className="flex justify-center py-12">
                <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-600"></div>
              </div>
            ) : (
              <>
                <p className="text-gray-600 mb-6">
                  Showing {filteredCourses.length} courses
                  {selectedCategory && ` in ${selectedCategory}`}
                  {selectedLevel && ` for ${selectedLevel} level`}
                </p>
                <CourseList courses={filteredCourses} />
              </>
            )}
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default CoursesPage;