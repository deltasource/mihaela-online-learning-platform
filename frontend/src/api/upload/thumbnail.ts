import { type NextRequest, NextResponse } from "next/server"
import { writeFile, mkdir } from "fs/promises"
import { join } from "path"
import { existsSync } from "fs"

export async function POST(request: NextRequest) {
    try {
        const formData = await request.formData()
        const file = formData.get("file") as File
        const courseId = formData.get("courseId") as string

        if (!file) {
            return NextResponse.json({ error: "No file provided" }, { status: 400 })
        }

        if (!courseId) {
            return NextResponse.json({ error: "Course ID is required" }, { status: 400 })
        }

        if (!file.type.startsWith("image/")) {
            return NextResponse.json({ error: "Only image files are allowed" }, { status: 400 })
        }

        const maxSize = 5 * 1024 * 1024 // 5MB
        if (file.size > maxSize) {
            return NextResponse.json({ error: "File size must be less than 5MB" }, { status: 400 })
        }

        const uploadDir = join(process.cwd(), "public", "uploads", "thumbnails")
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

        const publicUrl = `/uploads/thumbnails/${fileName}`

        return NextResponse.json({
            success: true,
            url: publicUrl,
            fileName: fileName,
        })
    } catch (error) {
        console.error("Thumbnail upload error:", error)
        return NextResponse.json({ error: "Failed to upload thumbnail" }, { status: 500 })
    }
}
