import { render, screen, fireEvent } from "@testing-library/react"
import { MemoryRouter } from "react-router-dom"
import Home from "./Home";

describe("Home Component", () => {
    it("renders the E-Learning Platform title", () => {
        render(
            <MemoryRouter>
                <Home />
            </MemoryRouter>
        )

        expect(screen.getByText("E-Learning Platform API Demo")).toBeInTheDocument()
    })

    it("renders each module card with the correct information", () => {
        render(
            <MemoryRouter>
                <Home />
            </MemoryRouter>
        )

        const modules = [
            { icon: "👨‍🎓", title: "Students", description: "Manage student profiles" },
            { icon: "👨‍🏫", title: "Instructors", description: "Manage instructor profiles" },
            { icon: "📚", title: "Courses", description: "Manage courses and enrollments" },
            { icon: "🎬", title: "Videos", description: "Manage course videos" },
            { icon: "📊", title: "Student Progress", description: "Track student progress in courses" },
        ]

        modules.forEach((module) => {
            expect(screen.getByText(module.icon)).toBeInTheDocument()
            expect(screen.getByText(module.title)).toBeInTheDocument()
            expect(screen.getByText(module.description)).toBeInTheDocument()
        })
    })

    it("links to the correct route when a card is clicked", () => {
        render(
            <MemoryRouter initialEntries={['/']}>  {}
                <Home />
            </MemoryRouter>
        )

        fireEvent.click(screen.getByText("Students"))

        expect(window.location.pathname).toBe("/")
    })
})
