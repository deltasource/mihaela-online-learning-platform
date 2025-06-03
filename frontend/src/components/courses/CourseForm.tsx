"use client"

import type React from "react"
import { useState, useEffect } from "react"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import { z } from "zod"
import Button from "../ui/Button"
import Input from "../ui/Input"
import type { Course } from "../../types"
import {getAllInstructors, InstructorDTO} from "../../api/instructor.ts";

const baseSchema = z.object({
    name: z.string().min(3, "Course name must be at least 3 characters"),
    description: z.string().min(10, "Description must be at least 10 characters"),
})

const courseWithInstructorSchema = baseSchema.extend({
    instructorId: z.string().uuid("Instructor ID must be a valid UUID"),
})

const courseWithoutInstructorSchema = baseSchema.extend({
    instructorId: z.string().optional(),
})

type CourseFormData = z.infer<typeof courseWithInstructorSchema>

interface CourseFormProps {
    initialData?: Partial<Course>
    onSubmit: (data: any) => Promise<void>
    isLoading: boolean
    submitLabel: string
    currentUserId?: string
    hideInstructorField?: boolean
}

const CourseForm: React.FC<CourseFormProps> = ({
                                                   initialData,
                                                   onSubmit,
                                                   isLoading,
                                                   submitLabel,
                                                   currentUserId,
                                                   hideInstructorField = false,
                                               }) => {
    const [error, setError] = useState<string | null>(null)
    const [instructors, setInstructors] = useState<InstructorDTO[]>([])
    const [isLoadingInstructors, setIsLoadingInstructors] = useState(false)

    const schema = hideInstructorField ? courseWithoutInstructorSchema : courseWithInstructorSchema

    const {
        register,
        handleSubmit,
        setValue,
        watch,
        formState: { errors },
    } = useForm<CourseFormData>({
        resolver: zodResolver(schema),
        defaultValues: {
            name: initialData?.title || initialData?.name || "",
            description: initialData?.description || "",
            instructorId: initialData?.instructorId || currentUserId || "",
        },
    })

    useEffect(() => {
        if (hideInstructorField && currentUserId) {
            setValue("instructorId", currentUserId)
        }
    }, [hideInstructorField, currentUserId, setValue])

    useEffect(() => {
        if (!hideInstructorField) {
            const fetchInstructors = async () => {
                setIsLoadingInstructors(true)
                try {
                    const data = await getAllInstructors()
                    setInstructors(data)

                    if (data.length > 0 && !initialData?.instructorId && !currentUserId) {
                        setValue("instructorId", data[0].id || "")
                    }
                } catch (err) {
                    console.error("Failed to fetch instructors:", err)
                } finally {
                    setIsLoadingInstructors(false)
                }
            }

            fetchInstructors()
        }
    }, [initialData?.instructorId, currentUserId, setValue, hideInstructorField])

    const handleFormSubmit = async (data: CourseFormData) => {
        setError(null)
        try {
            if (hideInstructorField && currentUserId) {
                data.instructorId = currentUserId
            }

            await onSubmit(data)
        } catch (err: any) {
            setError(err.message || "An error occurred. Please try again.")
        }
    }

    return (
        <div>
            {error && <div className="mb-4 p-3 bg-red-50 text-red-700 rounded-md text-sm">{error}</div>}
            <form onSubmit={handleSubmit(handleFormSubmit)} className="space-y-6">
                <Input label="Course Name *" type="text" fullWidth error={errors.name?.message} {...register("name")} />

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Description *</label>
                    <textarea
                        className={`px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-full ${
                            errors.description ? "border-red-500" : "border-gray-300"
                        }`}
                        rows={4}
                        {...register("description")}
                    ></textarea>
                    {errors.description && <p className="mt-1 text-sm text-red-600">{errors.description.message}</p>}
                </div>

                {!hideInstructorField && (
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Instructor *</label>
                        <select
                            className={`px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-full ${
                                errors.instructorId ? "border-red-500" : "border-gray-300"
                            }`}
                            {...register("instructorId")}
                            disabled={isLoadingInstructors}
                        >
                            {isLoadingInstructors ? (
                                <option value="">Loading instructors...</option>
                            ) : instructors.length === 0 ? (
                                <option value="">No instructors available</option>
                            ) : (
                                instructors.map((instructor) => (
                                    <option key={instructor.id} value={instructor.id}>
                                        {instructor.firstName} {instructor.lastName} - {instructor.department}
                                    </option>
                                ))
                            )}
                        </select>
                        {errors.instructorId && <p className="mt-1 text-sm text-red-600">{errors.instructorId.message}</p>}
                        <p className="mt-1 text-xs text-gray-500">Selected instructor ID: {watch("instructorId")}</p>
                    </div>
                )}

                <Button type="submit" fullWidth isLoading={isLoading}>
                    {submitLabel}
                </Button>
            </form>
        </div>
    )
}

export default CourseForm
