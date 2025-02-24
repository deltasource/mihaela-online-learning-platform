import { Box, Button, Paper, Typography } from "@mui/material"
import CodeIcon from "@mui/icons-material/Code"
import CloudIcon from "@mui/icons-material/Cloud"
import StorageIcon from "@mui/icons-material/Storage"
import ScienceIcon from "@mui/icons-material/Science"

const Categories = () => {
    const categories = [
        { name: "Web development", icon: <CodeIcon /> },
        { name: "IoT", icon: <CloudIcon /> },
        { name: "AWS", icon: <StorageIcon /> },
        { name: "Data science", icon: <ScienceIcon /> },
    ]

    return (
        <Paper sx={{ width: 240, p: 2 }}>
            <Typography variant="h6" sx={{ mb: 2 }}>
                Categories
            </Typography>
            <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
                {categories.map((category) => (
                    <Button
                        key={category.name}
                        variant="text"
                        color="inherit"
                        startIcon={category.icon}
                        sx={{
                            justifyContent: "flex-start",
                            px: 2,
                            py: 1,
                            "&.active": {
                                bgcolor: "primary.main",
                                color: "primary.contrastText",
                            },
                        }}
                        className={category.name === "Web development" ? "active" : ""}
                    >
                        {category.name}
                    </Button>
                ))}
            </Box>
        </Paper>
    )
}

export default Categories
