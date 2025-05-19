import { render, screen, fireEvent } from '@testing-library/react'
import ApiCard from './ApiCard'
import {expect, it} from "vitest";

describe('ApiCard', () => {
    const baseProps = {
        title: 'Get Users',
        description: 'Retrieves a list of users',
        method: 'GET' as const,
        endpoint: '/api/users',
        onClick: vi.fn(),
    }

    it('renders title, description, method, and endpoint', () => {
        render(<ApiCard {...baseProps} />)

        expect(screen.getByText('Get Users')).toBeInTheDocument()
        expect(screen.getByText('Retrieves a list of users')).toBeInTheDocument()
        expect(screen.getByText(/GET/)).toBeInTheDocument()
        expect(screen.getByText('/api/users')).toBeInTheDocument()
    })

    it('renders button with correct variant based on method', () => {
        render(<ApiCard {...baseProps} />)
        const button = screen.getByRole('button', { name: /test endpoint/i })

        expect(button).toHaveClass('btn-primary') ;
    })

    it('calls onClick when button is clicked', () => {
        render(<ApiCard {...baseProps} />)
        const button = screen.getByRole('button', { name: /test endpoint/i })

        fireEvent.click(button)
        expect(baseProps.onClick).toHaveBeenCalled()
    })

    it('renders correct color for DELETE method', () => {
        render(<ApiCard {...baseProps} method="DELETE" />)
        expect(screen.getByRole('button')).toHaveClass('btn-danger')
        expect(screen.getByText(/DELETE/)).toBeInTheDocument()
    })
})
