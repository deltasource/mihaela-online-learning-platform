"use client"

import { Card, Button } from "react-bootstrap"

interface ApiCardProps {
    title: string
    description: string
    method: "GET" | "POST" | "PUT" | "DELETE"
    endpoint: string
    onClick: () => void
}

const methodColors = {
    GET: "primary",
    POST: "success",
    PUT: "warning",
    DELETE: "danger",
}

const ApiCard = ({ title, description, method, endpoint, onClick }: ApiCardProps) => {
    return (
        <Card className="endpoint-card">
            <Card.Header className={`bg-${methodColors[method]} text-white`}>
                <strong>{method}</strong> {endpoint}
            </Card.Header>
            <Card.Body>
                <Card.Title>{title}</Card.Title>
                <Card.Text>{description}</Card.Text>
                <Button variant={methodColors[method]} onClick={onClick}>
                    Test Endpoint
                </Button>
            </Card.Body>
        </Card>
    )
}

export default ApiCard
