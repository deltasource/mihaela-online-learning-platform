import React from 'react';

const CourseGrid = () => {
  const courses = [
    { title: "Data Science", description: "Learn data analysis and ML" },
    { title: "SEO", description: "Master search optimization" },
    { title: "Web Development", description: "Full-stack web development" },
    { title: "Java Programming", description: "Core Java fundamentals" },
    { title: "Database", description: "SQL and NoSQL databases" },
    { title: "Docker", description: "Container orchestration" },
  ]

  return (
    <div className="courses-grid">
      {courses.map((course, index) => (
        <div key={index} className="course-card">
          <div className="course-content">
            <h3>{course.title}</h3>
            <p>{course.description}</p>
          </div>
        </div>
      ))}
    </div>
  )
}

export default CourseGrid
