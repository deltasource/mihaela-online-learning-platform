import { useState, useEffect } from "react";
import {
    Container,
    Tabs,
    Tab,
    Card,
    ListGroup,
} from "react-bootstrap";
import Loader from "../components/common/Loader";
import ErrorMessage from "../components/common/ErrorMessage";
import { Course, Video } from "../types";

const mockCourses: Course[] = [
    { id: 1, title: "React Basics", description: "Learn core React concepts." },
    { id: 2, title: "TypeScript 101", description: "Intro to TypeScript with React." },
];

const mockVideos: Record<number, Video[]> = {
    1: [
        { id: 1, title: "JSX and Components", duration: "10:34" },
        { id: 2, title: "Using useState", duration: "7:21" },
    ],
    2: [
        { id: 3, title: "Interfaces vs Types", duration: "8:11" },
        { id: 4, title: "Generics in TS", duration: "12:55" },
    ],
};

export default function InstructorProfile() {
    const [tab, setTab] = useState("courses");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [courses, setCourses] = useState<Course[]>([]);
    const [selectedCourseId, setSelectedCourseId] = useState<number | null>(null);

    useEffect(() => {
        const timeout = setTimeout(() => {
            try {
                setCourses(mockCourses);
                setLoading(false);
            } catch {
                setError("Failed to fetch courses.");
                setLoading(false);
            }
        }, 1000);

        return () => clearTimeout(timeout);
    }, []);

    const selectedCourse = courses.find(c => c.id === selectedCourseId);
    const videos = selectedCourseId ? mockVideos[selectedCourseId] || [] : [];

    return (
        <Container className="mt-4">
            <h2>Instructor Profile</h2>

            <Tabs activeKey={tab} onSelect={(k) => setTab(k || "courses")} className="mb-3">
                <Tab eventKey="courses" title="Courses">
                    {loading ? (
                        <Loader />
                    ) : error ? (
                        <ErrorMessage message={error} />
                    ) : (
                        <>
                            <div className="d-flex flex-wrap gap-3">
                                {courses.map((course) => (
                                    <Card
                                        key={course.id}
                                        onClick={() => setSelectedCourseId(course.id)}
                                        style={{
                                            width: "18rem",
                                            cursor: "pointer",
                                            border:
                                                selectedCourseId === course.id ? "2px solid #007bff" : "",
                                        }}
                                    >
                                        <Card.Body>
                                            <Card.Title>{course.title}</Card.Title>
                                            <Card.Text>{course.description}</Card.Text>
                                        </Card.Body>
                                    </Card>
                                ))}
                            </div>

                            {selectedCourse && (
                                <div className="mt-4">
                                    <h4>Videos for: {selectedCourse.title}</h4>
                                    {videos.length > 0 ? (
                                        <ListGroup>
                                            {videos.map((video) => (
                                                <ListGroup.Item key={video.id}>
                                                    ▶ {video.title} <span className="text-muted">({video.duration})</span>
                                                </ListGroup.Item>
                                            ))}
                                        </ListGroup>
                                    ) : (
                                        <p>No videos available for this course.</p>
                                    )}
                                </div>
                            )}
                        </>
                    )}
                </Tab>

                <Tab eventKey="other" title="Other Info">
                    <p>Placeholder for future content</p>
                </Tab>
            </Tabs>
        </Container>
    );
}
