import { describe, it, expect, vi, beforeEach } from "vitest"
import { authService } from "./authService"
import apiClient from "./client"

vi.mock("./client", () => ({
  default: {
    post: vi.fn(),
    put: vi.fn(),
    get: vi.fn(),
  },
}))

const localStorageMock = (() => {
  let store: Record<string, string> = {}
  return {
    getItem: (key: string) => store[key] || null,
    setItem: (key: string, value: string) => { store[key] = value },
    removeItem: (key: string) => { delete store[key] },
    clear: () => { store = {} },
  }
})()

Object.defineProperty(global, "localStorage", {
  value: localStorageMock,
  configurable: true,
})

const BASE_URL = import.meta.env.VITE_API_BASE_URL

describe("authService", () => {
  beforeEach(() => {
    vi.clearAllMocks()
    localStorage.clear()
  })

  it("login: should call apiClient.post and return data", async () => {
    const credentials = { email: "a@b.com", password: "123" }
    const mockData = { token: "abc", user: { id: "1" } }
    ;(apiClient.post as any).mockResolvedValue({ data: mockData })

    const result = await authService.login(credentials)
    expect(apiClient.post).toHaveBeenCalledWith(`${BASE_URL}/api/auth/login`, credentials)
    expect(result).toEqual(mockData)
  })

  it("register: should call apiClient.post and return data", async () => {
    const userData = { email: "a@b.com", password: "123", name: "A" }
    const mockData = { token: "abc", user: { id: "1" } }
    ;(apiClient.post as any).mockResolvedValue({ data: mockData })

    const result = await authService.register(userData as any)
    expect(apiClient.post).toHaveBeenCalledWith(`${BASE_URL}/api/auth/register`, userData)
    expect(result).toEqual(mockData)
  })

  it("updateProfile: should call apiClient.put and return data", async () => {
    const userId = "1"
    const userData = { name: "A" }
    const mockData = { id: "1", name: "A" }
    ;(apiClient.put as any).mockResolvedValue({ data: mockData })

    const result = await authService.updateProfile(userId, userData as any)
    expect(apiClient.put).toHaveBeenCalledWith(`${BASE_URL}/api/users/${userId}`, userData)
    expect(result).toEqual(mockData)
  })

  it("getCurrentUser: should return null if no token", async () => {
    localStorage.removeItem("authToken")
    const result = await authService.getCurrentUser()
    expect(result).toBeNull()
  })

  it("getCurrentUser: should call apiClient.get and return data if token exists", async () => {
    localStorage.setItem("authToken", "test-token")
    const mockData = { id: "1", name: "A" }
    ;(apiClient.get as any).mockResolvedValue({ data: mockData })

    const result = await authService.getCurrentUser()
    expect(apiClient.get).toHaveBeenCalledWith(`${BASE_URL}/api/auth/me`)
    expect(result).toEqual(mockData)
  })

  it("getCurrentUser: should return null on error", async () => {
    localStorage.setItem("authToken", "test-token")
    ;(apiClient.get as any).mockRejectedValue(new Error("fail"))

    const result = await authService.getCurrentUser()
    expect(result).toBeNull()
  })
})
