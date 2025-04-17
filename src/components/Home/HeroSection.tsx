import { Button, Container } from "react-bootstrap";
import { Link } from "react-router-dom";

export default function HeroSection() {
    return (
        <Container className="text-center my-5">
            <h1 className="display-4 fw-bold">Welcome to Mihaela’s eLearning Platform</h1>
            <p className="lead">Learn from experts. Teach with ease. All in one place.</p>
            <div className="d-flex justify-content-center gap-3 mt-4">
                <Link to="/student">
                    <Button size="lg">Student Profile</Button>
                </Link>
                <Link to="/instructor">
                    <Button variant="secondary" size="lg">
                        Instructor Profile
                    </Button>
                </Link>
            </div>
        </Container>
    );
}
