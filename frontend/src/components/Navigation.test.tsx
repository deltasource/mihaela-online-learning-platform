import { render, screen } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'
import { describe, it, expect } from 'vitest'
import Navigation from "./Navigation";

describe('Navigation', () => {
    it('renders the brand and all nav links', () => {
        render(
            <MemoryRouter>
                <Navigation />
            </MemoryRouter>
        )

        expect(screen.getByText(/E-Learning Platform/i)).toBeInTheDocument()
        expect(screen.getByRole('link', { name: /home/i })).toHaveAttribute('href', '/')
        expect(screen.getByRole('link', { name: /students/i })).toHaveAttribute('href', '/students')
        expect(screen.getByRole('link', { name: /instructors/i })).toHaveAttribute('href', '/instructors')
        expect(screen.getByRole('link', { name: /courses/i })).toHaveAttribute('href', '/courses')
        expect(screen.getByRole('link', { name: /videos/i })).toHaveAttribute('href', '/videos')
        expect(screen.getByRole('link', { name: /progress/i })).toHaveAttribute('href', '/progress')
    })
})
