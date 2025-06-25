import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { BookOpen, Award, Users, Check, Heart, Globe, Zap } from 'lucide-react';
import { CourseCard } from '../components/courses/CourseCard';
import { useCourseStore } from '../stores/courseStore';
import { LoadingSpinner } from '../components/ui/LoadingSpinner';

const HomePage = () => {
  const { courses, isLoading, fetchCourses } = useCourseStore();

  useEffect(() => {
    fetchCourses();
  }, [fetchCourses]);

  return (
    <>
      <section className="bg-gradient-to-r from-primary-600 to-primary-800 text-white py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="md:flex md:items-center md:justify-between">
            <div className="md:w-1/2">
              <div className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-primary-100 text-primary-800 mb-4">
                <Heart className="h-4 w-4 mr-2" />
                100% Free Forever
              </div>
              <h1 className="text-4xl md:text-5xl font-extrabold tracking-tight mb-4 text-white">
                Learn Anything, Anytime with LearnIT
              </h1>
              <p className="text-xl md:text-2xl text-primary-50 mb-8">
                Access thousands of high-quality courses completely free. No hidden fees, no subscriptions - just pure learning.
              </p>
              <div className="flex flex-col sm:flex-row space-y-4 sm:space-y-0 sm:space-x-4">
                <Link to="/courses" className="btn bg-white text-primary-700 hover:bg-primary-50 text-base px-6 py-3">
                  Start Learning Free
                </Link>
                <Link to="/register" className="btn border border-white text-white hover:bg-primary-700 text-base px-6 py-3">
                  Join Our Community
                </Link>
              </div>
              <div className="mt-6 flex items-center text-primary-100">
                <Users className="h-5 w-5 mr-2" />
                <span className="text-sm">Join 50,000+ learners worldwide</span>
              </div>
            </div>
            <div className="hidden md:block md:w-1/2">
              <img 
                src="https://images.pexels.com/photos/3184360/pexels-photo-3184360.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" 
                alt="Students learning online" 
                className="rounded-lg shadow-2xl"
              />
            </div>
          </div>
        </div>
      </section>

      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold mb-4">Why Choose LearnIT</h2>
            <p className="text-gray-600 max-w-3xl mx-auto">
              We believe education should be accessible to everyone. That's why all our courses are completely free, forever.
            </p>
          </div>
          
          <div className="grid md:grid-cols-3 gap-8">
            <div className="bg-gray-50 p-6 rounded-lg border border-gray-100">
              <div className="rounded-full bg-green-100 w-12 h-12 flex items-center justify-center mb-4">
                <Heart className="h-6 w-6 text-green-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2">100% Free</h3>
              <p className="text-gray-600">
                All courses, certificates, and features are completely free. No hidden costs, no premium tiers.
              </p>
            </div>
            
            <div className="bg-gray-50 p-6 rounded-lg border border-gray-100">
              <div className="rounded-full bg-primary-100 w-12 h-12 flex items-center justify-center mb-4">
                <BookOpen className="h-6 w-6 text-primary-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Expert Instructors</h3>
              <p className="text-gray-600">
                Learn from industry professionals with real-world experience and proven expertise.
              </p>
            </div>
            
            <div className="bg-gray-50 p-6 rounded-lg border border-gray-100">
              <div className="rounded-full bg-blue-100 w-12 h-12 flex items-center justify-center mb-4">
                <Globe className="h-6 w-6 text-blue-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Global Community</h3>
              <p className="text-gray-600">
                Join learners from around the world and get support through forums and study groups.
              </p>
            </div>
          </div>
        </div>
      </section>

      <section className="py-16 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center mb-8">
            <div>
              <h2 className="text-3xl font-bold">Featured Free Courses</h2>
              <p className="text-gray-600 mt-2">Start your learning journey with these popular courses</p>
            </div>
            <Link to="/courses" className="text-primary-600 hover:text-primary-800 font-medium">
              View All Free Courses →
            </Link>
          </div>
          
          {isLoading ? (
            <div className="flex justify-center py-12">
              <LoadingSpinner size="lg" />
            </div>
          ) : (
            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
              {courses.slice(0, 3).map(course => (
                <CourseCard key={course.id} course={course} />
              ))}
            </div>
          )}
        </div>
      </section>

      <section className="py-16 bg-primary-600">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-8 text-center text-white">
            <div>
              <div className="text-3xl md:text-4xl font-bold mb-2">50K+</div>
              <div className="text-primary-100">Active Learners</div>
            </div>
            <div>
              <div className="text-3xl md:text-4xl font-bold mb-2">500+</div>
              <div className="text-primary-100">Free Courses</div>
            </div>
            <div>
              <div className="text-3xl md:text-4xl font-bold mb-2">100+</div>
              <div className="text-primary-100">Expert Instructors</div>
            </div>
            <div>
              <div className="text-3xl md:text-4xl font-bold mb-2">25+</div>
              <div className="text-primary-100">Course Categories</div>
            </div>
          </div>
        </div>
      </section>

      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold mb-4">What Our Learners Say</h2>
            <p className="text-gray-600 max-w-3xl mx-auto">
              Join thousands of students who have transformed their careers through our free courses.
            </p>
          </div>
          
          <div className="grid md:grid-cols-3 gap-8">
            <div className="bg-gray-50 p-6 rounded-lg shadow-sm">
              <div className="flex items-center mb-4">
                <img
                  className="h-12 w-12 rounded-full"
                  src="https://randomuser.me/api/portraits/women/40.jpg"
                  alt="Student"
                />
                <div className="ml-4">
                  <h4 className="font-semibold">Emma Thompson</h4>
                  <p className="text-sm text-gray-500">Web Developer</p>
                </div>
              </div>
              <p className="text-gray-600">
                "I couldn't believe these courses were free! The quality is amazing and I landed my dream job after completing the web development track."
              </p>
            </div>
            
            <div className="bg-gray-50 p-6 rounded-lg shadow-sm">
              <div className="flex items-center mb-4">
                <img
                  className="h-12 w-12 rounded-full"
                  src="https://randomuser.me/api/portraits/men/32.jpg"
                  alt="Student"
                />
                <div className="ml-4">
                  <h4 className="font-semibold">Michael Chen</h4>
                  <p className="text-sm text-gray-500">Data Scientist</p>
                </div>
              </div>
              <p className="text-gray-600">
                "LearnIT made data science accessible to me. The free courses are comprehensive and the community is incredibly supportive."
              </p>
            </div>
            
            <div className="bg-gray-50 p-6 rounded-lg shadow-sm">
              <div className="flex items-center mb-4">
                <img
                  className="h-12 w-12 rounded-full"
                  src="https://randomuser.me/api/portraits/women/68.jpg"
                  alt="Student"
                />
                <div className="ml-4">
                  <h4 className="font-semibold">Sofia Rodriguez</h4>
                  <p className="text-sm text-gray-500">UX Designer</p>
                </div>
              </div>
              <p className="text-gray-600">
                "The design courses helped me transition careers without breaking the bank. Free education that actually works!"
              </p>
            </div>
          </div>
        </div>
      </section>

      <section className="bg-gradient-to-r from-green-600 to-blue-600 py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-3xl font-extrabold text-white mb-4">
            Ready to Start Your Free Learning Journey?
          </h2>
          <p className="text-xl text-green-50 mb-8 max-w-3xl mx-auto">
            Join our community of learners and start building the skills you need - completely free, forever.
          </p>
          <div className="inline-flex flex-col sm:flex-row space-y-4 sm:space-y-0 sm:space-x-4">
            <Link to="/courses" className="btn bg-white text-green-700 hover:bg-green-50 text-base px-6 py-3">
              Browse Free Courses
            </Link>
            <Link to="/register" className="btn border border-white text-white hover:bg-green-700 text-base px-6 py-3">
              Create Free Account
            </Link>
          </div>
        </div>
      </section>

      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="md:flex md:items-start md:justify-between">
            <div className="md:w-1/2 pr-8">
              <h2 className="text-3xl font-bold mb-6">Why Free Education Matters</h2>
              <ul className="space-y-4">
                <li className="flex">
                  <div className="flex-shrink-0">
                    <Check className="h-6 w-6 text-green-600" />
                  </div>
                  <div className="ml-3">
                    <p className="text-lg font-medium">No Financial Barriers</p>
                    <p className="text-gray-600">Education should be accessible to everyone, regardless of economic background.</p>
                  </div>
                </li>
                <li className="flex">
                  <div className="flex-shrink-0">
                    <Check className="h-6 w-6 text-green-600" />
                  </div>
                  <div className="ml-3">
                    <p className="text-lg font-medium">Learn at Your Own Pace</p>
                    <p className="text-gray-600">Take your time to master concepts without pressure or time limits.</p>
                  </div>
                </li>
                <li className="flex">
                  <div className="flex-shrink-0">
                    <Check className="h-6 w-6 text-green-600" />
                  </div>
                  <div className="ml-3">
                    <p className="text-lg font-medium">Real-World Projects</p>
                    <p className="text-gray-600">Build a portfolio with practical projects that demonstrate your skills.</p>
                  </div>
                </li>
                <li className="flex">
                  <div className="flex-shrink-0">
                    <Check className="h-6 w-6 text-green-600" />
                  </div>
                  <div className="ml-3">
                    <p className="text-lg font-medium">Lifetime Access</p>
                    <p className="text-gray-600">Once enrolled, access your courses forever with all future updates.</p>
                  </div>
                </li>
              </ul>
            </div>
            
            <div className="md:w-1/2 mt-8 md:mt-0">
              <img 
                src="https://images.pexels.com/photos/4145153/pexels-photo-4145153.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" 
                alt="Student learning online" 
                className="rounded-lg shadow-xl"
              />
            </div>
          </div>
        </div>
      </section>
    </>
  );
};

export default HomePage;