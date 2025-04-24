"use client"

import type React from "react"

import { useState } from "react"
import { useParams, useNavigate } from "react-router-dom"
import { Container, Form, Button, Card, Alert, ProgressBar } from "react-bootstrap"
import { uploadVideo } from "../../services/videoService"
import { toast } from "react-toastify"

const VideoUploadPage = () => {
    const { courseId } = useParams<{ courseId: string }>()
    const navigate = useNavigate()

    const [selectedFile, setSelectedFile] = useState<File | null>(null)
    const [uploading, setUploading] = useState(false)
    const [uploadProgress, setUploadProgress] = useState(0)
    const [error, setError] = useState<string | null>(null)

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            const file = e.target.files[0]

            if (!file.type.startsWith("video/")) {
                setError("Please select a valid video file")
                setSelectedFile(null)
                return
            }

            setSelectedFile(file)
            setError(null)
        }
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        if (!selectedFile || !courseId) {
            setError("Please select a video file to upload")
            return
        }

        try {
            setUploading(true)
            setError(null)

            // Simulate upload progress
            const progressInterval = setInterval(() => {
                setUploadProgress((prev) => {
                    const newProgress = prev + 10
                    if (newProgress >= 90) {
                        clearInterval(progressInterval)
                        return 90
                    }
                    return newProgress
                })
            }, 500)

            await uploadVideo(courseId, selectedFile)

            clearInterval(progressInterval)
            setUploadProgress(100)

            toast.success("Video uploaded successfully!")

            // Navigate back to course details
            setTimeout(() => {
                navigate(`/courses/${courseId}`)
            }, 1500)
        } catch (err) {
            setError("Failed to upload video. Please try again.")
            console.error(err)
        } finally {
            setUploading(false)
        }
    }

    return (
        <Container>
            <h1 className="page-title">Upload Video</h1>
            <p className="page-description">Upload a new video for the course</p>

            {error && <Alert variant="danger">{error}</Alert>}

            <Card className="form-container">
                <Card.Body>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Select Video File</Form.Label>
                            <Form.Control type="file" accept="video/*" onChange={handleFileChange} disabled={uploading} />
                            <Form.Text className="text-muted">Supported formats: MP4, WebM, MOV (max size: 10GB)</Form.Text>
                        </Form.Group>

                        {uploading && (
                            <div className="mb-3">
                                <p>Uploading: {uploadProgress}%</p>
                                <ProgressBar now={uploadProgress} label={`${uploadProgress}%`} />
                            </div>
                        )}

                        <div className="d-flex justify-content-between">
                            <Button variant="outline-secondary" onClick={() => navigate(`/courses/${courseId}`)} disabled={uploading}>
                                Cancel
                            </Button>
                            <Button variant="primary" type="submit" disabled={!selectedFile || uploading}>
                                {uploading ? "Uploading..." : "Upload Video"}
                            </Button>
                        </div>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    )
}

export default VideoUploadPage
