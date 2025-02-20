import React from 'react';

const YourCourses = () => {
    return (
      <section className="your-courses">
        <h2>Your Courses</h2>
        <div className="enrolled-courses">
          <div className="enrolled-card">
            <h3>Continue Learning</h3>
            <div className="progress-bar">
              <div className="progress" style={{ width: "60%" }}></div>
            </div>
          </div>
          <div className="enrolled-card">
            <h3>Start New Course</h3>
            <div className="progress-bar">
              <div className="progress" style={{ width: "0%" }}></div>
            </div>
          </div>
        </div>
      </section>
    )
  }
  
  export default YourCourses
    