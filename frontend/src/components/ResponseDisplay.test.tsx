import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import ResponseDisplay from "./ResponseDisplay";

describe('ResponseDisplay', () => {
    it('shows loading message when loading is true', () => {
        render(<ResponseDisplay data={null} error={null} loading={true} />)
        expect(screen.getByText(/loading/i)).toBeInTheDocument()
        expect(screen.getByRole('alert')).toHaveClass('alert-info')
    })

    it('shows error message when error is present', () => {
        render(<ResponseDisplay data={null} error="Something went wrong" loading={false} />)
        expect(screen.getByText(/something went wrong/i)).toBeInTheDocument()
        expect(screen.getByRole('alert')).toHaveClass('alert-danger')
    })

    it('renders JSON response when data is present', () => {
        const mockData = { name: 'Test User', age: 30 }

        render(<ResponseDisplay data={mockData} error={null} loading={false} />)

        expect(screen.getByText(/response:/i)).toBeInTheDocument()
        expect(screen.getByText(/"name": "Test User"/)).toBeInTheDocument()
        expect(screen.getByText(/"age": 30/)).toBeInTheDocument()
    })

    it('renders nothing when no data, no error, and not loading', () => {
        const { container } = render(<ResponseDisplay data={null} error={null} loading={false} />)
        expect(container).toBeEmptyDOMElement()
    })
})
