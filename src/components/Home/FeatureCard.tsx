import { Card } from "react-bootstrap";

interface Props {
    title: string;
    text: string;
    icon: string;
}

export default function FeatureCard({ title, text, icon }: Props) {
    return (
        <Card className="h-100 shadow-sm">
            <Card.Body className="text-center">
                <div style={{ fontSize: "2rem" }}>{icon}</div>
                <Card.Title className="mt-3">{title}</Card.Title>
                <Card.Text>{text}</Card.Text>
            </Card.Body>
        </Card>
    );
}
