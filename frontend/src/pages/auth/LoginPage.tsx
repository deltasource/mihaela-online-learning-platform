"use client"

import type React from "react"
import { useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import { useForm } from "react-hook-form"
import { z } from "zod"
import { zodResolver } from "@hookform/resolvers/zod"
import { useAuth } from "../../context/AuthContext"
import Layout from "../../components/layout/Layout"
import Input from "../../components/ui/Input"
import Button from "../../components/ui/Button"
import Card, { CardContent, CardHeader, CardFooter } from "../../components/ui/Card"

const loginSchema = z.object({
  email: z.string().email("Please enter a valid email address"),
  password: z.string().min(6, "Password must be at least 6 characters"),
})

type LoginFormData = z.infer<typeof loginSchema>

const LoginPage: React.FC = () => {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [error, setError] = useState<string | null>(null)
  const [isLoading, setIsLoading] = useState(false)

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  })

  const onSubmit = async (data: LoginFormData) => {
    setIsLoading(true)
    setError(null)

    try {
      await login(data.email, data.password)
      navigate("/dashboard")
    } catch (err: any) {
      setError(err.message || "Login failed. Please try again.")
      console.error("Login error:", err)
    } finally {
      setIsLoading(false)
    }
  }

  return (
      <Layout>
        <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
          <div className="sm:mx-auto sm:w-full sm:max-w-md">
            <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">Sign in to your account</h2>
            <p className="mt-2 text-center text-sm text-gray-600">
              Or{" "}
              <Link to="/register" className="font-medium text-blue-600 hover:text-blue-500">
                create a new account
              </Link>
            </p>
          </div>

          <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
            <Card>
              <CardHeader>
                <h3 className="text-lg font-medium text-gray-900">Sign in</h3>
              </CardHeader>
              <CardContent>
                {error && <div className="mb-4 p-3 bg-red-50 text-red-700 rounded-md text-sm">{error}</div>}
                <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                  <Input
                      label="Email address"
                      type="email"
                      fullWidth
                      error={errors.email?.message}
                      {...register("email")}
                  />

                  <Input
                      label="Password"
                      type="password"
                      fullWidth
                      error={errors.password?.message}
                      {...register("password")}
                  />

                  <div className="flex items-center justify-between">
                    <div className="flex items-center">
                      <input
                          id="remember-me"
                          name="remember-me"
                          type="checkbox"
                          className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                      />
                      <label htmlFor="remember-me" className="ml-2 block text-sm text-gray-900">
                        Remember me
                      </label>
                    </div>

                    <div className="text-sm">
                      <Link to="/forgot-password" className="font-medium text-blue-600 hover:text-blue-500">
                        Forgot your password?
                      </Link>
                    </div>
                  </div>

                  <Button type="submit" fullWidth isLoading={isLoading}>
                    Sign in
                  </Button>
                </form>
              </CardContent>
              <CardFooter className="bg-gray-50">
                <div className="text-sm text-gray-600 text-center w-full">
                  Don't have an account?{" "}
                  <Link to="/register" className="font-medium text-blue-600 hover:text-blue-500">
                    Sign up
                  </Link>
                </div>
              </CardFooter>
            </Card>
          </div>

          {/* Demo Users Info */}
          <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
            <div className="bg-blue-50 border border-blue-200 rounded-md p-4">
              <h4 className="text-sm font-medium text-blue-800 mb-2">Demo Users:</h4>
              <div className="text-xs text-blue-700 space-y-1">
                <div>
                  <strong>Instructor:</strong> eray.ali@example.com
                </div>
                <div>
                  <strong>Student:</strong> mihaela.kolarova@example.com
                </div>
                <div className="text-blue-600 mt-1">Use any password for demo users</div>
              </div>
            </div>
          </div>
        </div>
      </Layout>
  )
}

export default LoginPage
