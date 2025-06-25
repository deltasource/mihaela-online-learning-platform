import { Link } from 'react-router-dom';

const NotFoundPage = () => {
  return (
    <div className="min-h-[calc(100vh-64px)] bg-white flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="text-center">
          <h1 className="text-9xl font-extrabold text-primary-600 tracking-widest">404</h1>
          <h1 className="mt-4 text-3xl font-bold text-gray-900">Page Not Found</h1>
          <p className="mt-2 text-base text-gray-500">
            We couldn't find the page you're looking for.
          </p>
          <div className="mt-6">
            <Link 
              to="/"
              className="btn-primary inline-flex items-center"
            >
              Go back home
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NotFoundPage;