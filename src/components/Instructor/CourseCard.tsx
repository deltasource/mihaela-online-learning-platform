import { Card } from "react-bootstrap";
import { Course } from "../../types";

interface Props {
    course: Course;
    selected: boolean;
    onSelect: (id: number) => void;
}

function CourseCard({ course, selected, onSelect }: Props) {
    return (
        <Card
            style={{
                width: "18rem",
                cursor: "pointer",
                border: selected ? "2px solid #007bff" : "",
            }}
            onClick={() => onSelect(course.id)}
        >
            <Card.Body>
                <Card.Title>{course.title}</Card.Title>
                <Card.Text>{course.description}</Card.Text>
            </Card.Body>
        </Card>
    );
}
export default CourseCard;