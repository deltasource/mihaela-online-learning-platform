import { Box, Container, Typography } from "@mui/material"

const Footer = () => {
    return (
        <Box component="footer" sx={{ bgcolor: "background.paper", py: 6, mt: 6 }}>
            <Container>
                <Typography variant="body2" color="text.secondary" align="center">
                    Info like address, contacts
                </Typography>
            </Container>
        </Box>
    )
}

export default Footer
