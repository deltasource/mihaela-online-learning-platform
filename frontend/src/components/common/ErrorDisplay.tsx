import { Alert } from "react-bootstrap"

interface ErrorDisplayProps {
    message: string
}

const ErrorDisplay = ({ message }: ErrorDisplayProps) => {
    return (
        <Alert variant="danger">
            <Alert.Heading>Error</Alert.Heading>
            <p>{message}</p>
        </Alert>
    )
}

export default ErrorDisplay
