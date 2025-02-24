import { Box, Card, CardContent, Container, LinearProgress, Typography } from "@mui/material"

const YourCourses = () => {
    return (
        <Box sx={{ bgcolor: "background.paper", py: 6 }}>
            <Container>
                <Typography variant="h4" gutterBottom>
                    Your Courses
                </Typography>
                <Box sx={{ display: "flex", gap: 3 }}>
                    <Card sx={{ flex: 1 }}>
                        <CardContent>
                            <Typography variant="h6" gutterBottom>
                                Continue Learning
                            </Typography>
                            <LinearProgress variant="determinate" value={60} sx={{ height: 8, borderRadius: 4 }} />
                        </CardContent>
                    </Card>
                    <Card sx={{ flex: 1 }}>
                        <CardContent>
                            <Typography variant="h6" gutterBottom>
                                Start New Course
                            </Typography>
                            <LinearProgress variant="determinate" value={0} sx={{ height: 8, borderRadius: 4 }} />
                        </CardContent>
                    </Card>
                </Box>
            </Container>
        </Box>
    )
}

export default YourCourses
