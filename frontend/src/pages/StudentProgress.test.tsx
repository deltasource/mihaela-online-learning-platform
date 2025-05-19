import { render, screen, fireEvent, waitFor } from "@testing-library/react"
import StudentProgress from "../pages/StudentProgress"
import { vi } from "vitest"
import * as api from "../api/api"

vi.mock("../components/ApiCard", () => ({
    default: ({ title, onClick }: any) => (
        <button onClick={onClick}>{title}</button>
    )
}))
vi.mock("../components/ResponseDisplay", () => ({
    default: ({ data, error }: any) => (
        <div>
            {error && <div data-testid="error">{error}</div>}
            {data && <div data-testid="response">{JSON.stringify(data)}</div>}
        </div>
    )
}))

const mockCourses = [
    { name: "Course 1", instructorId: "course-1-id", description: "Description 1" },
    { name: "Course 2", instructorId: "course-2-id", description:  "Description 2" }
]

describe("StudentProgress", () => {
    beforeEach(() => {
        vi.spyOn(api.courseApi, "getAllCourses").mockResolvedValue(mockCourses)
        vi.spyOn(api.progressApi, "getProgressPercentage").mockResolvedValue({
            studentId: "student123",
            courseId: "course456",
            progressPercentage: 85,
            totalVideos: 20,
            videosWatched: 17
        })
    })

    it("renders correctly and fetches courses", async () => {
        render(<StudentProgress />)

        expect(await screen.findByText("Get Progress Percentage")).toBeInTheDocument()
        expect(await screen.findByText("Update Progress")).toBeInTheDocument()
        expect(await screen.findByText("Course 1")).toBeInTheDocument()
        expect(screen.getByText("Course 2")).toBeInTheDocument()
    })

    it("handles Get Progress Percentage click", async () => {
        render(<StudentProgress />)

        fireEvent.change(screen.getByLabelText("Student ID"), {
            target: { value: "student-123" }
        })
        fireEvent.change(screen.getByLabelText("Course ID"), {
            target: { value: "course-1-id" }
        })

        fireEvent.click(await screen.findByText("Get Progress Percentage"))

        await waitFor(() =>
            expect(screen.getByTestId("response")).toHaveTextContent("85")
        )
    })

    it("shows error if fields are missing", async () => {
        render(<StudentProgress />)

        fireEvent.click(await screen.findByText("Get Progress Percentage"))

        await waitFor(() =>
            expect(screen.getByTestId("error")).toHaveTextContent(
                "Student ID and Course ID are required"
            )
        )
    })

    it("handles Update Progress click", async () => {
        render(<StudentProgress />)

        fireEvent.change(screen.getByLabelText("Student ID"), {
            target: { value: "student-123" }
        })
        fireEvent.change(screen.getByLabelText("Course ID"), {
            target: { value: "course-1-id" }
        })
        fireEvent.change(screen.getByLabelText("Video ID (for updating progress)"), {
            target: { value: "video-789" }
        })

        fireEvent.click(await screen.findByText("Update Progress"))

        await waitFor(() =>
            expect(screen.getByTestId("response")).toHaveTextContent(
                "Progress updated successfully"
            )
        )
    })
})
