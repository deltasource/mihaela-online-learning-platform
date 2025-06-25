import { useState, useEffect } from 'react';
import { Search, Filter, X } from 'lucide-react';
import { CourseFilters } from '../../types/course';
import { useCourseStore } from '../../stores/courseStore';

interface CourseFilterProps {
  onFilterChange: (filters: CourseFilters) => void;
}

export const CourseFilter = ({ onFilterChange }: CourseFilterProps) => {
  const { categories, fetchCategories } = useCourseStore();
  const [filters, setFilters] = useState<CourseFilters>({
    search: '',
    category: '',
    level: '',
    priceRange: [0, 1000],
    sortBy: 'popular'
  });
  const [isExpanded, setIsExpanded] = useState(false);

  useEffect(() => {
    fetchCategories();
  }, [fetchCategories]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  const handlePriceRangeChange = (e: React.ChangeEvent<HTMLInputElement>, index: number) => {
    const value = parseInt(e.target.value);
    setFilters(prev => {
      const newPriceRange = [...prev.priceRange!];
      newPriceRange[index] = value;
      return { ...prev, priceRange: newPriceRange as [number, number] };
    });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onFilterChange(filters);
  };

  const handleReset = () => {
    setFilters({
      search: '',
      category: '',
      level: '',
      priceRange: [0, 1000],
      sortBy: 'popular'
    });
    onFilterChange({});
  };

  return (
    <div className="bg-white shadow rounded-lg overflow-hidden mb-6">
      <div className="p-4">
        <div className="flex items-center justify-between">
          <h2 className="text-lg font-semibold">Filter Courses</h2>
          <button 
            onClick={() => setIsExpanded(!isExpanded)} 
            className="md:hidden text-gray-500 hover:text-gray-700"
          >
            {isExpanded ? <X size={20} /> : <Filter size={20} />}
          </button>
        </div>

        <form onSubmit={handleSubmit} className="mt-4">
          <div className="flex flex-col md:flex-row gap-4 mb-4">
            <div className="flex-1 relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <Search className="h-5 w-5 text-gray-400" />
              </div>
              <input
                type="text"
                name="search"
                placeholder="Search courses..."
                value={filters.search}
                onChange={handleInputChange}
                className="input pl-10"
              />
            </div>
            
            <button type="submit" className="btn-primary">
              Apply Filters
            </button>
            
            <button 
              type="button" 
              onClick={handleReset}
              className="btn-outline"
            >
              Reset
            </button>
          </div>

          <div className={`grid grid-cols-1 md:grid-cols-4 gap-4 ${isExpanded ? 'block' : 'hidden md:grid'}`}>
            <div>
              <label htmlFor="category" className="label">Category</label>
              <select
                id="category"
                name="category"
                value={filters.category}
                onChange={handleInputChange}
                className="input"
              >
                <option value="">All Categories</option>
                {categories.map(category => (
                  <option key={category} value={category}>
                    {category}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label htmlFor="level" className="label">Level</label>
              <select
                id="level"
                name="level"
                value={filters.level}
                onChange={handleInputChange}
                className="input"
              >
                <option value="">All Levels</option>
                <option value="beginner">Beginner</option>
                <option value="intermediate">Intermediate</option>
                <option value="advanced">Advanced</option>
              </select>
            </div>

            <div>
              <label htmlFor="priceRange" className="label">Price Range</label>
              <div className="flex items-center space-x-2">
                <input
                  type="number"
                  min="0"
                  max={filters.priceRange?.[1]}
                  value={filters.priceRange?.[0]}
                  onChange={e => handlePriceRangeChange(e, 0)}
                  className="input w-24"
                />
                <span>to</span>
                <input
                  type="number"
                  min={filters.priceRange?.[0]}
                  value={filters.priceRange?.[1]}
                  onChange={e => handlePriceRangeChange(e, 1)}
                  className="input w-24"
                />
              </div>
            </div>

            <div>
              <label htmlFor="sortBy" className="label">Sort By</label>
              <select
                id="sortBy"
                name="sortBy"
                value={filters.sortBy}
                onChange={handleInputChange}
                className="input"
              >
                <option value="popular">Most Popular</option>
                <option value="newest">Newest</option>
                <option value="price-asc">Price: Low to High</option>
                <option value="price-desc">Price: High to Low</option>
                <option value="rating">Highest Rated</option>
              </select>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};