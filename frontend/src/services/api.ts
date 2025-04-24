import axios from "axios"

const api = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        "Content-Type": "application/json",
    },
})

api.interceptors.request.use(
    (config) => {
        return config
    },
    (error) => {
        return Promise.reject(error)
    },
)

api.interceptors.response.use(
    (response) => {
        return response
    },
    (error) => {
        console.error("API Error:", error.response?.data || error.message)
        return Promise.reject(error)
    },
)

export default api
