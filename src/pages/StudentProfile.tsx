import { Container, Card } from "react-bootstrap";

const enrolledCourses = [
    { id: 1, title: "React Basics", description: "Intro to components, hooks, JSX" },
    { id: 2, title: "Advanced JavaScript", description: "Closures, async/await, ES6+" },
];

export default function StudentProfile() {
    return (
        <Container className="mt-4">
            <h2>Student Profile</h2>
            <p className="text-muted">My Enrolled Courses:</p>
            <div className="d-flex flex-wrap gap-3">
                {enrolledCourses.map(course => (
                    <Card key={course.id} style={{ width: "18rem" }}>
                        <Card.Body>
                            <Card.Title>{course.title}</Card.Title>
                            <Card.Text>{course.description}</Card.Text>
                        </Card.Body>
                    </Card>
                ))}
            </div>
        </Container>
    );
}
