"use client"

import type React from "react"
import { useState, useRef } from "react"
import { Upload, X, FileText } from "lucide-react"
import Button from "../ui/Button"
import { uploadVideo } from "../../api/videos"

interface VideoUploaderProps {
    courseId: string
    onUploadComplete: () => void
}

const VideoUploader: React.FC<VideoUploaderProps> = ({ courseId, onUploadComplete }) => {
    const [selectedFile, setSelectedFile] = useState<File | null>(null)
    const [isUploading, setIsUploading] = useState(false)
    const [uploadProgress, setUploadProgress] = useState(0)
    const [error, setError] = useState<string | null>(null)
    const fileInputRef = useRef<HTMLInputElement>(null)

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const files = e.target.files
        if (files && files.length > 0) {
            const file = files[0]
            if (!file.type.startsWith("video/")) {
                setError("Please select a video file")
                return
            }
            setSelectedFile(file)
            setError(null)
        }
    }

    const handleUpload = async () => {
        if (!selectedFile) {
            setError("Please select a file to upload")
            return
        }

        setIsUploading(true)
        setUploadProgress(0)
        setError(null)

        try {
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
            setSelectedFile(null)
            onUploadComplete()
        } catch (err: any) {
            setError(err.message || "Failed to upload video. Please try again.")
        } finally {
            setIsUploading(false)
        }
    }

    const handleClearFile = () => {
        setSelectedFile(null)
        if (fileInputRef.current) {
            fileInputRef.current.value = ""
        }
    }

    return (
        <div className="p-6 border border-gray-200 rounded-lg">
            <h3 className="text-lg font-medium text-gray-900 mb-4">Upload Video</h3>

            {error && <div className="mb-4 p-3 bg-red-50 text-red-700 rounded-md text-sm">{error}</div>}

            <div className="mb-4">
                <input
                    type="file"
                    accept="video/*"
                    onChange={handleFileChange}
                    ref={fileInputRef}
                    className="hidden"
                    id="video-upload"
                />

                {!selectedFile ? (
                    <label
                        htmlFor="video-upload"
                        className="flex flex-col items-center justify-center w-full h-32 border-2 border-dashed border-gray-300 rounded-lg cursor-pointer bg-gray-50 hover:bg-gray-100"
                    >
                        <div className="flex flex-col items-center justify-center pt-5 pb-6">
                            <Upload className="w-8 h-8 text-gray-500 mb-2" />
                            <p className="mb-2 text-sm text-gray-500">
                                <span className="font-semibold">Click to upload</span> or drag and drop
                            </p>
                            <p className="text-xs text-gray-500">MP4, WebM, or other video formats</p>
                        </div>
                    </label>
                ) : (
                    <div className="flex items-center justify-between p-4 border border-gray-200 rounded-lg bg-gray-50">
                        <div className="flex items-center">
                            <FileText className="w-6 h-6 text-blue-500 mr-2" />
                            <div>
                                <p className="text-sm font-medium text-gray-700">{selectedFile.name}</p>
                                <p className="text-xs text-gray-500">{(selectedFile.size / (1024 * 1024)).toFixed(2)} MB</p>
                            </div>
                        </div>
                        <button onClick={handleClearFile} className="text-gray-500 hover:text-gray-700" disabled={isUploading}>
                            <X className="w-5 h-5" />
                        </button>
                    </div>
                )}
            </div>

            {selectedFile && (
                <>
                    {isUploading && (
                        <div className="mb-4">
                            <div className="w-full bg-gray-200 rounded-full h-2.5">
                                <div className="bg-blue-600 h-2.5 rounded-full" style={{ width: `${uploadProgress}%` }}></div>
                            </div>
                            <p className="text-sm text-gray-600 mt-1">Uploading: {uploadProgress}%</p>
                        </div>
                    )}

                    <Button onClick={handleUpload} isLoading={isUploading} disabled={isUploading} fullWidth>
                        {isUploading ? "Uploading..." : "Upload Video"}
                    </Button>
                </>
            )}
        </div>
    )
}

export default VideoUploader
