import { Box, Button, Container, Typography } from "@mui/material"

const Hero = () => {
    return (
        <Box sx={{ bgcolor: "background.paper", py: 8, textAlign: "center" }}>
            <Container maxWidth="sm">
                <Typography component="h1" variant="h2" sx={{ mb: 2, fontWeight: "bold" }}>
                    Discover Your Next Skill
                </Typography>
                <Typography variant="h5" color="text.secondary" sx={{ mb: 4 }}>
                    Learn from experts
                </Typography>
                <Button variant="contained" size="large" color="primary">
                    Explore courses
                </Button>
            </Container>
        </Box>
    )
}

export default Hero
