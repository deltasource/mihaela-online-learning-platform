import { ReactNode } from 'react';
import { AlertCircle, CheckCircle, Info, XCircle } from 'lucide-react';

type AlertVariant = 'success' | 'error' | 'warning' | 'info';

interface AlertProps {
  variant: AlertVariant;
  title?: string;
  children: ReactNode;
  className?: string;
}

export const Alert = ({ variant, title, children, className = '' }: AlertProps) => {
  const variantClasses = {
    success: 'bg-success-50 text-success-600 border-success-500',
    error: 'bg-error-50 text-error-600 border-error-500',
    warning: 'bg-warning-50 text-warning-600 border-warning-500',
    info: 'bg-primary-50 text-primary-600 border-primary-500'
  };

  const variantIcons = {
    success: <CheckCircle className="h-5 w-5" />,
    error: <XCircle className="h-5 w-5" />,
    warning: <AlertCircle className="h-5 w-5" />,
    info: <Info className="h-5 w-5" />
  };

  return (
    <div className={`p-4 border-l-4 rounded-md ${variantClasses[variant]} ${className}`}>
      <div className="flex">
        <div className="flex-shrink-0">
          {variantIcons[variant]}
        </div>
        <div className="ml-3">
          {title && <h3 className="text-sm font-medium">{title}</h3>}
          <div className="text-sm mt-1">{children}</div>
        </div>
      </div>
    </div>
  );
};