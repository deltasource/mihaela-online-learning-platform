import { Alert } from "react-bootstrap"

interface ResponseDisplayProps {
    data: any
    error: string | null
    loading: boolean
}

const ResponseDisplay = ({ data, error, loading }: ResponseDisplayProps) => {
    if (loading) {
        return <Alert variant="info">Loading...</Alert>
    }

    if (error) {
        return <Alert variant="danger">{error}</Alert>
    }

    if (!data) {
        return null
    }

    return (
        <div className="mt-3">
            <h5>Response:</h5>
            <pre className="api-response">{JSON.stringify(data, null, 2)}</pre>
        </div>
    )
}

export default ResponseDisplay
