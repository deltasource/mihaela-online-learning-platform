import { Container, Row, Col } from "react-bootstrap";
import FeatureCard from "./FeatureCard";

export default function FeatureGrid() {
    const features = [
        {
            title: "Wide Range of Courses",
            text: "Explore hundreds of curated courses in development, business, design, and more.",
            icon: "📚",
        },
        {
            title: "Engaging Video Lessons",
            text: "Learn at your own pace with high-quality video content and interactive lessons.",
            icon: "🎥",
        },
        {
            title: "Instructor Tools",
            text: "Create and manage courses, track student progress, and grow your audience.",
            icon: "🧑‍🏫",
        },
    ];

    return (
        <Container className="my-5">
            <Row className="text-center mb-4">
                <h2 className="fw-bold">Why Choose Our Platform?</h2>
                <p className="text-muted">Built for learners and instructors alike</p>
            </Row>
            <Row className="g-4">
                {features.map((feature, index) => (
                    <Col md={4} key={index}>
                        <FeatureCard {...feature} />
                    </Col>
                ))}
            </Row>
        </Container>
    );
}
