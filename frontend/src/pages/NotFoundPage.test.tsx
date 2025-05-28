import { render, screen, fireEvent } from "@testing-library/react"
import { MemoryRouter } from "react-router-dom"
import NotFoundPage from "./NotFoundPage";

describe("NotFoundPage", () => {
    test("renders 404 page with heading and description", () => {
        render(
            <MemoryRouter>
                <NotFoundPage />
            </MemoryRouter>
        )

        expect(screen.getByRole("heading", { name: /404/i })).toBeInTheDocument()
        expect(
            screen.getByText(/The page you are looking for might have been removed/i)
        ).toBeInTheDocument()
    })

    test("has a button to go back to the homepage", () => {
        render(
            <MemoryRouter>
                <NotFoundPage />
            </MemoryRouter>
        )

        const button = screen.getByRole("button", { name: /Go to Homepage/i })
        fireEvent.click(button)

        expect(window.location.pathname).toBe("/")
    })
})
