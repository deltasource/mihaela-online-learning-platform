"use client"

import { useState } from "react"
import {
    Box,
    Container,
    Typography,
    Grid,
    Card,
    CardContent,
    Avatar,
    Button,
    Tabs,
    Tab,
    Divider,
    LinearProgress,
    IconButton,
    Paper,
    Chip,
} from "@mui/material"
import {
    Edit as EditIcon,
    Add as AddIcon,
    ArrowBack as ArrowBackIcon,
    LinkedIn as LinkedInIcon,
    GitHub as GitHubIcon,
    Instagram as InstagramIcon,
} from "@mui/icons-material"
import { mockUserData } from "../Profile/mockData.jsx"

const InstructorPage = ({ onBackClick }) => {
    const [activeTab, setActiveTab] = useState(0)
    const instructor = mockUserData.instructor

    const handleTabChange = (event, newValue) => {
        setActiveTab(newValue)
    }

    return (
        <Container maxWidth="lg" sx={{ py: 4 }}>
            <Box sx={{ display: "flex", alignItems: "center", mb: 3 }}>
                <IconButton onClick={onBackClick} sx={{ mr: 2 }}>
                    <ArrowBackIcon />
                </IconButton>
                <Typography variant="h4">Instructor Profile</Typography>
            </Box>

            <Paper elevation={2} sx={{ mb: 4, overflow: "hidden" }}>
                <Box
                    sx={{
                        height: 200,
                        bgcolor: "primary.light",
                        position: "relative",
                    }}
                >
                    <Button
                        variant="contained"
                        size="small"
                        startIcon={<EditIcon />}
                        sx={{ position: "absolute", top: 16, right: 16 }}
                    >
                        Edit Cover
                    </Button>
                </Box>

                <Box sx={{ p: 3, pb: 0, display: "flex", flexDirection: { xs: "column", md: "row" } }}>
                    <Avatar
                        src={instructor.avatar || "/placeholder.svg?height=120&width=120"}
                        sx={{
                            width: 120,
                            height: 120,
                            border: "4px solid white",
                            mt: -8,
                            position: { xs: "relative", md: "relative" },
                            left: { xs: "calc(50% - 60px)", md: 0 },
                        }}
                    />

                    <Box sx={{ ml: { xs: 0, md: 3 }, mt: { xs: 2, md: 0 }, flex: 1 }}>
                        <Box
                            sx={{
                                display: "flex",
                                justifyContent: { xs: "center", md: "space-between" },
                                alignItems: "center",
                                flexWrap: "wrap",
                            }}
                        >
                            <Box sx={{ textAlign: { xs: "center", md: "left" } }}>
                                <Typography variant="h4">{instructor.name}</Typography>
                                <Typography variant="subtitle1" color="text.secondary">
                                    {instructor.title}
                                </Typography>
                            </Box>

                            <Box
                                sx={{ display: "flex", gap: 1, mt: { xs: 2, md: 0 }, justifyContent: { xs: "center", md: "flex-end" } }}
                            >
                                {instructor.socialLinks.linkedin && (
                                    <IconButton color="primary" href={instructor.socialLinks.linkedin} target="_blank">
                                        <LinkedInIcon />
                                    </IconButton>
                                )}
                                {instructor.socialLinks.github && (
                                    <IconButton color="primary" href={instructor.socialLinks.github} target="_blank">
                                        <GitHubIcon />
                                    </IconButton>
                                )}
                                {instructor.socialLinks.instagram && (
                                    <IconButton color="primary" href={instructor.socialLinks.instagram} target="_blank">
                                        <InstagramIcon />
                                    </IconButton>
                                )}
                            </Box>
                        </Box>

                        <Typography variant="body1" sx={{ mt: 2, textAlign: { xs: "center", md: "left" } }}>
                            {instructor.bio}
                        </Typography>
                    </Box>
                </Box>

                <Tabs value={activeTab} onChange={handleTabChange} sx={{ mt: 2 }} centered>
                    <Tab label="Courses" />
                    <Tab label="Teaching Schedule" />
                    <Tab label="Statistics" />
                </Tabs>
            </Paper>

            <Box sx={{ mt: 4 }}>
                {activeTab === 0 && (
                    <>
                        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 3 }}>
                            <Typography variant="h5">Courses ({instructor.courses.length})</Typography>
                            <Button variant="contained" startIcon={<AddIcon />}>
                                Create New Course
                            </Button>
                        </Box>

                        <Grid container spacing={3}>
                            {instructor.courses.map((course, index) => (
                                <Grid item xs={12} sm={6} md={4} key={index}>
                                    <Card>
                                        <CardContent>
                                            <Typography variant="h6" gutterBottom>
                                                {course.title}
                                            </Typography>
                                            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                                                {course.description}
                                            </Typography>
                                            <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                                                <Typography variant="body2">Completion:</Typography>
                                                <LinearProgress
                                                    variant="determinate"
                                                    value={course.progress}
                                                    sx={{ height: 8, borderRadius: 4, flexGrow: 1 }}
                                                />
                                                <Typography variant="body2">{course.progress}%</Typography>
                                            </Box>
                                            <Box sx={{ display: "flex", justifyContent: "flex-end", mt: 2 }}>
                                                <Button size="small" color="primary">
                                                    Edit
                                                </Button>
                                            </Box>
                                        </CardContent>
                                    </Card>
                                </Grid>
                            ))}
                        </Grid>
                    </>
                )}

                {activeTab === 1 && (
                    <>
                        <Typography variant="h5" sx={{ mb: 3 }}>
                            Teaching Schedule
                        </Typography>
                        <Grid container spacing={3}>
                            {instructor.teaching.map((schedule, index) => (
                                <Grid item xs={12} key={index}>
                                    <Card>
                                        <CardContent>
                                            <Box
                                                sx={{
                                                    display: "flex",
                                                    justifyContent: "space-between",
                                                    alignItems: "center",
                                                    flexWrap: "wrap",
                                                }}
                                            >
                                                <Typography variant="h6">{schedule.courseTitle}</Typography>
                                                <Chip
                                                    label={`Course ID: ${schedule.courseId}`}
                                                    size="small"
                                                    color="primary"
                                                    variant="outlined"
                                                />
                                            </Box>
                                            <Divider sx={{ my: 2 }} />
                                            <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                                <Box>
                                                    <Typography variant="body2" color="text.secondary">
                                                        Start Date
                                                    </Typography>
                                                    <Typography variant="body1">{new Date(schedule.startDate).toLocaleDateString()}</Typography>
                                                </Box>
                                                <Box>
                                                    <Typography variant="body2" color="text.secondary">
                                                        End Date
                                                    </Typography>
                                                    <Typography variant="body1">{new Date(schedule.endDate).toLocaleDateString()}</Typography>
                                                </Box>
                                                <Button variant="outlined" size="small">
                                                    View Details
                                                </Button>
                                            </Box>
                                        </CardContent>
                                    </Card>
                                </Grid>
                            ))}
                        </Grid>
                    </>
                )}

                {activeTab === 2 && (
                    <>
                        <Typography variant="h5" sx={{ mb: 3 }}>
                            Statistics
                        </Typography>
                        <Grid container spacing={3}>
                            <Grid item xs={12} sm={6} md={3}>
                                <Card>
                                    <CardContent>
                                        <Typography variant="h6" gutterBottom>
                                            Total Students
                                        </Typography>
                                        <Typography variant="h3">124</Typography>
                                    </CardContent>
                                </Card>
                            </Grid>
                            <Grid item xs={12} sm={6} md={3}>
                                <Card>
                                    <CardContent>
                                        <Typography variant="h6" gutterBottom>
                                            Active Courses
                                        </Typography>
                                        <Typography variant="h3">{instructor.courses.length}</Typography>
                                    </CardContent>
                                </Card>
                            </Grid>
                            <Grid item xs={12} sm={6} md={3}>
                                <Card>
                                    <CardContent>
                                        <Typography variant="h6" gutterBottom>
                                            Avg. Rating
                                        </Typography>
                                        <Typography variant="h3">4.8</Typography>
                                    </CardContent>
                                </Card>
                            </Grid>
                            <Grid item xs={12} sm={6} md={3}>
                                <Card>
                                    <CardContent>
                                        <Typography variant="h6" gutterBottom>
                                            Completion Rate
                                        </Typography>
                                        <Typography variant="h3">85%</Typography>
                                    </CardContent>
                                </Card>
                            </Grid>
                        </Grid>
                    </>
                )}
            </Box>
        </Container>
    )
}

export default InstructorPage
