import { Alert } from "react-bootstrap";

export default function ErrorMessage({ message }: { message: string }) {
    return (
        <Alert variant="danger" className="my-3">
            {message}
        </Alert>
    );
}
