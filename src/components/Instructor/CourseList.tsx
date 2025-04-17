import CourseCard from "./CourseCard";
import { Course } from "../../types";

interface Props {
    courses: Course[];
    selectedCourseId: number | null;
    onSelect: (id: number) => void;
}
 function CourseList({ courses, selectedCourseId, onSelect }: Props) {
    return (
        <div className="d-flex flex-wrap gap-3">
            {courses.map(course => (
                <CourseCard
                    key={course.id}
                    course={course}
                    selected={course.id === selectedCourseId}
                    onSelect={onSelect}
                />
            ))}
        </div>
    );
}
export default CourseList;
