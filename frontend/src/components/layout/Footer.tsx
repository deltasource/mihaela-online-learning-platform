import { Link } from 'react-router-dom';
import { BookOpen, Facebook, Twitter, Instagram, Linkedin, Mail } from 'lucide-react';

export const Footer = () => {
  const currentYear = new Date().getFullYear();
  
  return (
    <footer className="bg-gray-900 text-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div className="space-y-4">
            <div className="flex items-center">
              <BookOpen className="h-8 w-8 text-primary-400" />
              <span className="ml-2 text-xl font-bold">LearnIT</span>
            </div>
            <p className="text-gray-400 text-sm">
              Transform your skills with our expert-led online courses. Learn at your own pace and achieve your goals - completely free!
            </p>
            <div className="flex space-x-4">
              <a href="#" className="text-gray-400 hover:text-white">
                <Facebook size={20} />
              </a>
              <a href="#" className="text-gray-400 hover:text-white">
                <Twitter size={20} />
              </a>
              <a href="#" className="text-gray-400 hover:text-white">
                <Instagram size={20} />
              </a>
              <a href="#" className="text-gray-400 hover:text-white">
                <Linkedin size={20} />
              </a>
            </div>
          </div>
          
          <div>
            <h3 className="text-lg font-semibold mb-4">Free Courses</h3>
            <ul className="space-y-2">
              <li><Link to="/courses?category=Web Development" className="text-gray-400 hover:text-white">Web Development</Link></li>
              <li><Link to="/courses?category=Data Science" className="text-gray-400 hover:text-white">Data Science</Link></li>
              <li><Link to="/courses?category=Design" className="text-gray-400 hover:text-white">Design</Link></li>
              <li><Link to="/courses?category=Business" className="text-gray-400 hover:text-white">Business</Link></li>
              <li><Link to="/courses" className="text-gray-400 hover:text-white">View All Courses</Link></li>
            </ul>
          </div>
          
          <div>
            <h3 className="text-lg font-semibold mb-4">Company</h3>
            <ul className="space-y-2">
              <li><Link to="/about" className="text-gray-400 hover:text-white">About Us</Link></li>
              <li><Link to="/instructors" className="text-gray-400 hover:text-white">Instructors</Link></li>
              <li><Link to="/careers" className="text-gray-400 hover:text-white">Careers</Link></li>
              <li><Link to="/blog" className="text-gray-400 hover:text-white">Blog</Link></li>
              <li><Link to="/contact" className="text-gray-400 hover:text-white">Contact Us</Link></li>
            </ul>
          </div>
          
          <div>
            <h3 className="text-lg font-semibold mb-4">Support</h3>
            <ul className="space-y-2">
              <li><Link to="/help" className="text-gray-400 hover:text-white">Help Center</Link></li>
              <li><Link to="/faq" className="text-gray-400 hover:text-white">FAQ</Link></li>
              <li><Link to="/terms" className="text-gray-400 hover:text-white">Terms of Service</Link></li>
              <li><Link to="/privacy" className="text-gray-400 hover:text-white">Privacy Policy</Link></li>
              <li>
                <a href="mailto:support@learnit.com" className="text-gray-400 hover:text-white flex items-center">
                  <Mail size={16} className="mr-2" /> support@learnit.com
                </a>
              </li>
            </ul>
          </div>
        </div>
        
        <div className="border-t border-gray-800 mt-12 pt-8 text-gray-400 text-sm text-center">
          <p>© {currentYear} LearnIT. All rights reserved. Free education for everyone.</p>
        </div>
      </div>
    </footer>
  );
};