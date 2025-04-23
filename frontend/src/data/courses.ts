import type { Course } from "../types"

export const allCourses: Course[] = [
    {
        id: 1,
        title: "Web Development Fundamentals",
        description: "Learn the basics of HTML, CSS, and JavaScript to build modern websites.",
        price: 49.99,
        duration: "12 hours",
        category: "Web Development",
        rating: 4.7,
        reviewCount: 245,
        instructor: {
            name: "John Smith",
            title: "Senior Web Developer",
            bio: "John has been developing websites for over 10 years and has worked with companies like Google and Facebook.",
        },
        learningOutcomes: [
            "Understand HTML structure and semantics",
            "Create responsive layouts with CSS",
            "Build interactive features with JavaScript",
            "Deploy websites to production",
        ],
        modules: [
            {
                title: "HTML Basics",
                lessons: [
                    {
                        title: "Introduction to HTML",
                        duration: "15 min",
                        videoUrl: "#",
                    },
                    {
                        title: "HTML Elements and Attributes",
                        duration: "20 min",
                        videoUrl: "#",
                    },
                ],
            },
            {
                title: "CSS Styling",
                lessons: [
                    {
                        title: "CSS Selectors",
                        duration: "18 min",
                        videoUrl: "#",
                    },
                    {
                        title: "Box Model and Layout",
                        duration: "25 min",
                        videoUrl: "#",
                    },
                ],
            },
        ],
        reviews: [
            {
                name: "Sarah Johnson",
                rating: 5,
                date: "2 weeks ago",
                comment: "Great course for beginners! The instructor explains everything clearly.",
            },
            {
                name: "Mike Peterson",
                rating: 4,
                date: "1 month ago",
                comment: "Solid content, but some sections could be more in-depth.",
            },
        ],
    },
    {
        id: 2,
        title: "React.js for Beginners",
        description: "Master React.js and build modern, interactive web applications.",
        price: 59.99,
        duration: "15 hours",
        category: "Web Development",
        rating: 4.8,
        reviewCount: 189,
        instructor: {
            name: "Emily Chen",
            title: "Frontend Developer",
            bio: "Emily specializes in React and has built applications for startups and enterprise companies.",
        },
    },
    {
        id: 3,
        title: "Python Programming Masterclass",
        description: "Learn Python from scratch and advance to creating real-world applications.",
        price: 69.99,
        duration: "20 hours",
        category: "Programming",
        rating: 4.9,
        reviewCount: 312,
        instructor: {
            name: "David Wilson",
            title: "Software Engineer",
            bio: "David has been programming in Python for 8 years and has taught over 50,000 students online.",
        },
    },
    {
        id: 4,
        title: "Data Science Essentials",
        description: "Learn the fundamentals of data science, statistics, and machine learning.",
        price: 79.99,
        duration: "18 hours",
        category: "Data Science",
        rating: 4.6,
        reviewCount: 178,
        instructor: {
            name: "Lisa Rodriguez",
            title: "Data Scientist",
            bio: "Lisa has a PhD in Statistics and has worked as a data scientist at Amazon and Netflix.",
        },
    },
    {
        id: 5,
        title: "UI/UX Design Principles",
        description: "Master the principles of user interface and user experience design.",
        price: 54.99,
        duration: "14 hours",
        category: "Design",
        rating: 4.5,
        reviewCount: 156,
        instructor: {
            name: "Alex Johnson",
            title: "UX Designer",
            bio: "Alex has designed interfaces for mobile apps and websites used by millions of people.",
        },
    },
    {
        id: 6,
        title: "Mobile App Development with Flutter",
        description: "Build cross-platform mobile apps for iOS and Android with Flutter.",
        price: 64.99,
        duration: "16 hours",
        category: "Mobile Development",
        rating: 4.7,
        reviewCount: 203,
        instructor: {
            name: "Ryan Park",
            title: "Mobile Developer",
            bio: "Ryan has published over 20 mobile apps and specializes in cross-platform development.",
        },
    },
]

export const featuredCourses = allCourses.slice(0, 3)
