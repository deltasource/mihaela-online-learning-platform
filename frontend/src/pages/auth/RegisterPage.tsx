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

const registerSchema = z
    .object({
      name: z.string().min(2, "Name must be at least 2 characters"),
      email: z.string().email("Please enter a valid email address"),
      password: z.string().min(6, "Password must be at least 6 characters"),
      confirmPassword: z.string(),
      role: z.enum(["student", "instructor"]),
    })
    .refine((data) => data.password === data.confirmPassword, {
      message: "Passwords don't match",
      path: ["confirmPassword"],
    })

type RegisterFormData = z.infer<typeof registerSchema>

const RegisterPage: React.FC = () => {
  const { register: registerUser } = useAuth()
  const navigate = useNavigate()
  const [error, setError] = useState<string | null>(null)
  const [isLoading, setIsLoading] = useState(false)

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      role: "student",
    },
  })

  const onSubmit = async (data: RegisterFormData) => {
    setIsLoading(true)
    setError(null)

    try {
      await registerUser(data.name, data.email, data.password, data.role)
      navigate("/dashboard")
    } catch (err: any) {
      setError(err.message || "Registration failed. Please try again.")
      console.error("Registration error:", err)
    } finally {
      setIsLoading(false)
    }
  }

  return (
      <Layout>
        <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
          <div className="sm:mx-auto sm:w-full sm:max-w-md">
            <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">Create your account</h2>
            <p className="mt-2 text-center text-sm text-gray-600">
              Or{" "}
              <Link to="/login" className="font-medium text-blue-600 hover:text-blue-500">
                sign in to your existing account
              </Link>
            </p>
          </div>

          <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
            <Card>
              <CardHeader>
                <h3 className="text-lg font-medium text-gray-900">Sign up</h3>
              </CardHeader>
              <CardContent>
                {error && <div className="mb-4 p-3 bg-red-50 text-red-700 rounded-md text-sm">{error}</div>}
                <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                  <Input label="Full Name" type="text" fullWidth error={errors.name?.message} {...register("name")} />

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

                  <Input
                      label="Confirm Password"
                      type="password"
                      fullWidth
                      error={errors.confirmPassword?.message}
                      {...register("confirmPassword")}
                  />

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">I want to:</label>
                    <div className="flex space-x-4">
                      <label className="inline-flex items-center">
                        <input
                            type="radio"
                            value="student"
                            className="form-radio h-4 w-4 text-blue-600"
                            {...register("role")}
                        />
                        <span className="ml-2 text-gray-700">Learn (Student)</span>
                      </label>
                      <label className="inline-flex items-center">
                        <input
                            type="radio"
                            value="instructor"
                            className="form-radio h-4 w-4 text-blue-600"
                            {...register("role")}
                        />
                        <span className="ml-2 text-gray-700">Teach (Instructor)</span>
                      </label>
                    </div>
                    {errors.role && <p className="mt-1 text-sm text-red-600">{errors.role.message}</p>}
                  </div>

                  <div className="flex items-center">
                    <input
                        id="terms"
                        name="terms"
                        type="checkbox"
                        className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                        required
                    />
                    <label htmlFor="terms" className="ml-2 block text-sm text-gray-900">
                      I agree to the{" "}
                      <Link to="/terms" className="font-medium text-blue-600 hover:text-blue-500">
                        Terms of Service
                      </Link>{" "}
                      and{" "}
                      <Link to="/privacy" className="font-medium text-blue-600 hover:text-blue-500">
                        Privacy Policy
                      </Link>
                    </label>
                  </div>

                  <Button type="submit" fullWidth isLoading={isLoading}>
                    Create account
                  </Button>
                </form>
              </CardContent>
              <CardFooter className="bg-gray-50">
                <div className="text-sm text-gray-600 text-center w-full">
                  Already have an account?{" "}
                  <Link to="/login" className="font-medium text-blue-600 hover:text-blue-500">
                    Sign in
                  </Link>
                </div>
              </CardFooter>
            </Card>
          </div>

          {/* Registration Info */}
          <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
            <div className="bg-green-50 border border-green-200 rounded-md p-4">
              <h4 className="text-sm font-medium text-green-800 mb-2">Registration Info:</h4>
              <div className="text-xs text-green-700 space-y-1">
                <div>• Choose "Student" to browse and take courses</div>
                <div>• Choose "Instructor" to create and manage courses</div>
                <div>• You can switch roles later in your profile</div>
              </div>
            </div>
          </div>
        </div>
      </Layout>
  )
}

export default RegisterPage
