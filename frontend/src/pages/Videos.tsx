"use client"

import { useState, useEffect } from "react"
import { Card, Form, Row, Col, Spinner } from "react-bootstrap"
import ApiCard from "../components/ApiCard"
import ResponseDisplay from "../components/ResponseDisplay"
import { videoApi, type VideoDTO, courseApi, type CourseDTO } from "../api/api"

const Videos = () => {
  const [videoId, setVideoId] = useState("")
  const [courseId, setCourseId] = useState("")
  const [fileName, setFileName] = useState("")
  const [filePath, setFilePath] = useState("")
  const [file, setFile] = useState<File | null>(null)

  const [courses, setCourses] = useState<CourseDTO[]>([])
  const [response, setResponse] = useState<any>(null)
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    // Load courses on component mount for the dropdown
    fetchCourses()
  }, [])

  const fetchCourses = async () => {
    try {
      const result = await courseApi.getAllCourses()
      setCourses(result)
    } catch (err) {
      console.error("Failed to fetch courses:", err)
    }
  }

  const resetForm = () => {
    setVideoId("")
    setCourseId("")
    setFileName("")
    setFilePath("")
    setFile(null)
  }

  const handleUploadVideo = async () => {
    if (!courseId) {
      setError("Course ID is required")
      return
    }

    if (!file) {
      setError("Please select a file to upload")
      return
    }

    setLoading(true)
    setError(null)

    try {
      const result = await videoApi.uploadVideo(courseId, file)
      setResponse(result)
      resetForm()
    } catch (err) {
      setError(err instanceof Error ? err.message : "An unknown error occurred")
    } finally {
      setLoading(false)
    }
  }

  const handleGetVideosByCourse = async () => {
    if (!courseId) {
      setError("Course ID is required")
      return
    }

    setLoading(true)
    setError(null)

    try {
      const result = await videoApi.getVideosByCourse(courseId)
      setResponse(result)
    } catch (err) {
      setError(err instanceof Error ? err.message : "An unknown error occurred")
    } finally {
      setLoading(false)
    }
  }

  const handleUpdateVideo = async () => {
    if (!videoId) {
      setError("Video ID is required")
      return
    }

    setLoading(true)
    setError(null)

    try {
      const videoData: VideoDTO = {
        fileName: fileName || undefined,
        filePath: filePath || undefined,
        courseId,
      }

      const result = await videoApi.updateVideo(videoId, videoData)
      setResponse(result)
    } catch (err) {
      setError(err instanceof Error ? err.message : "An unknown error occurred")
    } finally {
      setLoading(false)
    }
  }

  const handleDeleteVideo = async () => {
    if (!videoId) {
      setError("Video ID is required")
      return
    }

    setLoading(true)
    setError(null)

    try {
      await videoApi.deleteVideo(videoId)
      setResponse({ message: "Video deleted successfully" })
      resetForm()
    } catch (err) {
      setError(err instanceof Error ? err.message : "An unknown error occurred")
    } finally {
      setLoading(false)
    }
  }

  return (
    <div>
      <h1 className="mb-4">Video Management</h1>

      <Card className="mb-4">
        <Card.Body>
          <Card.Title>Video Form</Card.Title>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Video ID (for update/delete)</Form.Label>
              <Form.Control
                type="text"
                value={videoId}
                onChange={(e) => setVideoId(e.target.value)}
                placeholder="UUID format"
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Course ID</Form.Label>
              <Form.Control
                type="text"
                value={courseId}
                onChange={(e) => setCourseId(e.target.value)}
                placeholder="UUID format"
              />
              {courses.length > 0 && (
                <Form.Select
                  className="mt-2"
                  onChange={(e) => {
                    const selected = courses.find((c) => c.name === e.target.value)
                    if (selected && selected.instructorId) {
                      setCourseId(selected.instructorId)
                    }
                  }}
                >
                  <option value="">Select a course</option>
                  {courses.map((course, index) => (
                    <option key={index} value={course.name}>
                      {course.name}
                    </option>
                  ))}
                </Form.Select>
              )}
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>File Name</Form.Label>
              <Form.Control
                type="text"
                value={fileName}
                onChange={(e) => setFileName(e.target.value)}
                placeholder="example.mp4"
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>File Path</Form.Label>
              <Form.Control
                type="text"
                value={filePath}
                onChange={(e) => setFilePath(e.target.value)}
                placeholder="/path/to/video"
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Upload File</Form.Label>
              <Form.Control
                type="file"
                onChange={(e) => {
                  const files = (e.target as HTMLInputElement).files
                  if (files && files.length > 0) {
                    setFile(files[0])
                  }
                }}
              />
            </Form.Group>
          </Form>
        </Card.Body>
      </Card>

      <h2 className="mb-3">API Endpoints</h2>

      <Row>
        <Col md={6}>
          <ApiCard
            title="Upload Video"
            description="Uploads a video file and associates it with a specific course"
            method="POST"
            endpoint="/api/videos/{courseId}/upload"
            onClick={handleUploadVideo}
          />
        </Col>

        <Col md={6}>
          <ApiCard
            title="Get Videos by Course"
            description="Retrieves all videos associated with a specific course"
            method="GET"
            endpoint="/api/videos/courses/{courseId}"
            onClick={handleGetVideosByCourse}
          />
        </Col>

        <Col md={6}>
          <ApiCard
            title="Update Video"
            description="Updates an existing video's metadata"
            method="PUT"
            endpoint="/api/videos/{videoId}"
            onClick={handleUpdateVideo}
          />
        </Col>

        <Col md={6}>
          <ApiCard
            title="Delete Video"
            description="Deletes a video by its ID"
            method="DELETE"
            endpoint="/api/videos/{videoId}"
            onClick={handleDeleteVideo}
          />
        </Col>
      </Row>

      {loading && (
        <div className="loading-spinner">
          <Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
          </Spinner>
        </div>
      )}

      <ResponseDisplay data={response} error={error} loading={loading} />
    </div>
  )
}

export default Videos
