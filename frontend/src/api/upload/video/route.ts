import { type NextRequest, NextResponse } from "next/server"
import { writeFile, mkdir } from "fs/promises"
import { join } from "path"
import { existsSync } from "fs"

export async function POST(request: NextRequest) {
    try {
        const formData = await request.formData()
        const file = formData.get("file") as File
        const courseId = formData.get("courseId") as string
        const title = formData.get("title") as string
        const description = formData.get("description") as string

        if (!file) {
            return NextResponse.json({ error: "No file provided" }, { status: 400 })
        }

        if (!courseId) {
            return NextResponse.json({ error: "Course ID is required" }, { status: 400 })
        }

        if (!file.type.startsWith("video/")) {
            return NextResponse.json({ error: "Only video files are allowed" }, { status: 400 })
        }

        const maxSize = 100 * 1024 * 1024 // 100MB
        if (file.size > maxSize) {
            return NextResponse.json({ error: "Video file size must be less than 100MB" }, { status: 400 })
        }

        const uploadDir = join(process.cwd(), "public", "uploads", "videos")
        if (!existsSync(uploadDir)) {
            await mkdir(uploadDir, { recursive: true })
        }

        const timestamp = Date.now()
        const fileExtension = file.name.split(".").pop()
        const fileName = `${courseId}-${timestamp}.${fileExtension}`
        const filePath = join(uploadDir, fileName)

        const bytes = await file.arrayBuffer()
        const buffer = Buffer.from(bytes)
        await writeFile(filePath, buffer)

        const publicUrl = `/uploads/videos/${fileName}`

        return NextResponse.json({
            success: true,
            url: publicUrl,
            fileName: fileName,
            fileSize: file.size,
            title: title || file.name,
            description: description || "",
            status: "ready",
        })
    } catch (error) {
        console.error("Video upload error:", error)
        return NextResponse.json({ error: "Failed to upload video" }, { status: 500 })
    }
}
