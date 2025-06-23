import { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { useCourseStore } from '../../stores/courseStore';
import { useAuthStore } from '../../stores/authStore';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { Alert } from '../../components/ui/Alert';
import { Quiz, QuizQuestion, QuizAnswer } from '../../types/course';
import { CheckCircle, XCircle, Clock, AlertTriangle } from 'lucide-react';

const QuizPage = () => {
  const { courseId, quizId } = useParams<{ courseId: string; quizId: string }>();
  const { user } = useAuthStore();
  const { currentCourse, currentQuiz, isLoading, error, fetchCourseById, fetchQuizById, submitQuiz } = useCourseStore();
  const [userAnswers, setUserAnswers] = useState<Record<string, string>>({});
  const [quizSubmitted, setQuizSubmitted] = useState(false);
  const [quizResult, setQuizResult] = useState<{ score: number; isPassed: boolean } | null>(null);
  const [timeRemaining, setTimeRemaining] = useState<number | null>(null);
  const [confirmSubmit, setConfirmSubmit] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const loadQuiz = async () => {
      if (!courseId || !quizId) return;

      try {
        if (!currentCourse || currentCourse.id !== courseId) {
          await fetchCourseById(courseId);
        }
        
        await fetchQuizById(courseId, quizId);
      } catch (err) {
        console.error('Failed to load quiz:', err);
      }
    };

    loadQuiz();
  }, [courseId, quizId, fetchCourseById, fetchQuizById, currentCourse]);

  useEffect(() => {
    if (currentQuiz?.timeLimit && !quizSubmitted && !timeRemaining) {
      setTimeRemaining(currentQuiz.timeLimit * 60);
    }
  }, [currentQuiz, quizSubmitted, timeRemaining]);

  useEffect(() => {
    if (!timeRemaining || timeRemaining <= 0 || quizSubmitted) return;

    const timer = setInterval(() => {
      setTimeRemaining(prevTime => {
        if (prevTime && prevTime > 0) {
          return prevTime - 1;
        }
        return 0;
      });
    }, 1000);

    return () => clearInterval(timer);
  }, [timeRemaining, quizSubmitted]);

  useEffect(() => {
    if (timeRemaining === 0 && !quizSubmitted) {
      handleSubmitQuiz();
    }
  }, [timeRemaining, quizSubmitted]);

  const handleAnswerChange = (questionId: string, optionId: string) => {
    setUserAnswers(prev => ({
      ...prev,
      [questionId]: optionId
    }));
  };

  const handleSubmitQuiz = async () => {
    if (!user || !currentQuiz) return;
    
    if (!confirmSubmit && !timeRemaining) {
      setConfirmSubmit(true);
      return;
    }

    try {
        const answers: QuizAnswer[] = currentQuiz.questions.map(question => {
        const selectedOptionId = userAnswers[question.id];
        const selectedOption = question.options?.find(o => o.id === selectedOptionId);
        
        return {
          questionId: question.id,
          selectedOptionId,
          isCorrect: !!selectedOption?.isCorrect
        };
      });

      const result = await submitQuiz(user.id, currentQuiz.id, answers);
      setQuizResult({
        score: result.score,
        isPassed: result.isPassed
      });
      setQuizSubmitted(true);
    } catch (err) {
      console.error('Failed to submit quiz:', err);
    }
  };

  const formatTime = (totalSeconds: number) => {
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;
    return `${minutes}:${seconds < 10 ? '0' + seconds : seconds}`;
  };

  if (isLoading) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  if (error) {
    return (
      <div className="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        <Alert variant="error" title="Error loading quiz">
          {error}
        </Alert>
        <div className="mt-6 text-center">
          <Link to={`/courses/${courseId}`} className="btn-primary">
            Back to Course
          </Link>
        </div>
      </div>
    );
  }

  if (!currentQuiz || !currentCourse) {
    return (
      <div className="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        <div className="text-center py-12">
          <h2 className="text-2xl font-semibold text-gray-900">Quiz not found</h2>
          <p className="mt-2 text-gray-500">The quiz you're looking for doesn't exist or has been removed.</p>
          <div className="mt-6">
            <Link to={`/courses/${courseId}`} className="btn-primary">
              Back to Course
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-gray-50 min-h-[calc(100vh-64px)]">
      <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Quiz Header */}
        <div className="bg-white shadow rounded-lg overflow-hidden mb-8">
          <div className="p-6">
            <div className="flex justify-between items-center">
              <h1 className="text-2xl font-bold text-gray-900">{currentQuiz.title}</h1>
              {timeRemaining !== null && !quizSubmitted && (
                <div className={`flex items-center ${
                  timeRemaining < 60 ? 'text-error-600' : 'text-gray-700'
                }`}>
                  <Clock className="h-5 w-5 mr-2" />
                  <span className="font-medium">{formatTime(timeRemaining)}</span>
                </div>
              )}
            </div>
            <p className="text-gray-600 mt-2">{currentQuiz.description}</p>
            <div className="mt-4 flex items-center text-sm text-gray-500">
              <span className="font-medium">Passing score: {currentQuiz.passingScore}%</span>
              <span className="mx-2">•</span>
              <span>{currentQuiz.questions.length} questions</span>
            </div>
          </div>
        </div>

        {quizSubmitted ? (
          <div className="bg-white shadow rounded-lg overflow-hidden">
            <div className="p-6">
              <div className="text-center mb-6">
                {quizResult?.isPassed ? (
                  <div className="mb-4">
                    <CheckCircle className="mx-auto h-16 w-16 text-success-500" />
                    <h2 className="mt-4 text-2xl font-bold text-gray-900">Quiz Passed!</h2>
                    <p className="mt-1 text-lg text-gray-600">
                      Your score: {quizResult.score}%
                    </p>
                  </div>
                ) : (
                  <div className="mb-4">
                    <XCircle className="mx-auto h-16 w-16 text-error-500" />
                    <h2 className="mt-4 text-2xl font-bold text-gray-900">Quiz Failed</h2>
                    <p className="mt-1 text-lg text-gray-600">
                      Your score: {quizResult?.score}% (Passing: {currentQuiz.passingScore}%)
                    </p>
                  </div>
                )}
                
                <div className="mt-8 space-x-4">
                  <Link
                    to={`/courses/${courseId}`}
                    className="btn-outline"
                  >
                    Back to Course
                  </Link>
                  {!quizResult?.isPassed && (
                    <button
                      onClick={() => {
                        setQuizSubmitted(false);
                        setUserAnswers({});
                        setTimeRemaining(currentQuiz.timeLimit ? currentQuiz.timeLimit * 60 : null);
                      }}
                      className="btn-primary"
                    >
                      Retry Quiz
                    </button>
                  )}
                </div>
              </div>

              <div className="mt-8 border-t border-gray-200 pt-8">
                <h3 className="text-lg font-medium text-gray-900 mb-4">Quiz Results</h3>
                
                <div className="space-y-6">
                  {currentQuiz.questions.map((question, index) => {
                    const userAnswerId = userAnswers[question.id];
                    const userAnswer = question.options?.find(o => o.id === userAnswerId);
                    const correctOption = question.options?.find(o => o.isCorrect);
                    const isCorrect = userAnswer?.isCorrect;
                    
                    return (
                      <div key={question.id} className="border-b border-gray-200 pb-6 last:border-b-0 last:pb-0">
                        <div className="flex items-start">
                          <span className="flex-shrink-0 h-6 w-6 rounded-full bg-gray-100 flex items-center justify-center text-gray-800 font-medium mr-3">
                            {index + 1}
                          </span>
                          <div className="flex-1">
                            <h4 className="text-base font-medium text-gray-900 mb-2">{question.question}</h4>
                            
                            {question.options && (
                              <div className="space-y-2">
                                {question.options.map(option => {
                                  const isSelected = option.id === userAnswerId;
                                  
                                  let optionClass = "block w-full text-left px-4 py-2 border rounded-md";
                                  
                                  if (option.isCorrect) {
                                    optionClass += " border-success-500 bg-success-50 text-success-700";
                                  } else if (isSelected && !option.isCorrect) {
                                    optionClass += " border-error-500 bg-error-50 text-error-700";
                                  } else {
                                    optionClass += " border-gray-300 text-gray-700";
                                  }
                                  
                                  return (
                                    <div key={option.id} className={optionClass}>
                                      {option.text}
                                      {isSelected && !option.isCorrect && (
                                        <div className="mt-1 text-sm text-error-600">
                                          Your answer (incorrect)
                                        </div>
                                      )}
                                      {option.isCorrect && (
                                        <div className="mt-1 text-sm text-success-600">
                                          Correct answer
                                        </div>
                                      )}
                                    </div>
                                  );
                                })}
                              </div>
                            )}
                          </div>
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>
            </div>
          </div>
        ) : (
          <div className="bg-white shadow rounded-lg overflow-hidden">
            <div className="p-6">
              {confirmSubmit && (
                <Alert variant="warning" className="mb-6">
                  <div className="flex flex-col space-y-2">
                    <p>Are you sure you want to submit your quiz?</p>
                    <div className="flex space-x-3 mt-2">
                      <button
                        onClick={() => setConfirmSubmit(false)}
                        className="btn-outline py-1 px-3 text-sm"
                      >
                        Cancel
                      </button>
                      <button
                        onClick={handleSubmitQuiz}
                        className="btn-primary py-1 px-3 text-sm"
                      >
                        Yes, Submit
                      </button>
                    </div>
                  </div>
                </Alert>
              )}
              
              {timeRemaining !== null && timeRemaining < 60 && !confirmSubmit && (
                <Alert variant="warning" className="mb-6">
                  <div className="flex items-center">
                    <AlertTriangle className="mr-2 h-5 w-5 text-warning-600" />
                    <span>Less than a minute remaining!</span>
                  </div>
                </Alert>
              )}
              
              <form>
                <div className="space-y-8">
                  {currentQuiz.questions.map((question: QuizQuestion, index) => (
                    <div key={question.id} className="border-b border-gray-200 pb-8 last:border-b-0 last:pb-0">
                      <div className="flex items-start">
                        <span className="flex-shrink-0 h-6 w-6 rounded-full bg-primary-100 flex items-center justify-center text-primary-800 font-medium mr-3">
                          {index + 1}
                        </span>
                        <div className="flex-1">
                          <h3 className="text-lg font-medium text-gray-900 mb-4">{question.question}</h3>
                          
                          {question.questionType === 'multiple-choice' && question.options && (
                            <div className="space-y-3">
                              {question.options.map(option => (
                                <label 
                                  key={option.id} 
                                  className={`flex items-center p-3 border rounded-md cursor-pointer transition-colors ${
                                    userAnswers[question.id] === option.id
                                      ? 'bg-primary-50 border-primary-500'
                                      : 'hover:bg-gray-50 border-gray-300'
                                  }`}
                                >
                                  <input
                                    type="radio"
                                    name={`question-${question.id}`}
                                    className="h-4 w-4 text-primary-600 border-gray-300"
                                    checked={userAnswers[question.id] === option.id}
                                    onChange={() => handleAnswerChange(question.id, option.id)}
                                  />
                                  <span className="ml-3 text-gray-700">{option.text}</span>
                                </label>
                              ))}
                            </div>
                          )}
                          
                          {question.questionType === 'true-false' && (
                            <div className="space-y-3">
                              {[
                                { id: `${question.id}-true`, text: 'True' },
                                { id: `${question.id}-false`, text: 'False' },
                              ].map(option => (
                                <label 
                                  key={option.id} 
                                  className={`flex items-center p-3 border rounded-md cursor-pointer transition-colors ${
                                    userAnswers[question.id] === option.id
                                      ? 'bg-primary-50 border-primary-500'
                                      : 'hover:bg-gray-50 border-gray-300'
                                  }`}
                                >
                                  <input
                                    type="radio"
                                    name={`question-${question.id}`}
                                    className="h-4 w-4 text-primary-600 border-gray-300"
                                    checked={userAnswers[question.id] === option.id}
                                    onChange={() => handleAnswerChange(question.id, option.id)}
                                  />
                                  <span className="ml-3 text-gray-700">{option.text}</span>
                                </label>
                              ))}
                            </div>
                          )}
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
                
                <div className="mt-8 flex justify-between">
                  <Link 
                    to={`/courses/${courseId}`} 
                    className="btn-outline"
                  >
                    Cancel
                  </Link>
                  
                  <button
                    type="button"
                    onClick={() => setConfirmSubmit(true)}
                    className="btn-primary"
                  >
                    Submit Quiz
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default QuizPage;