import { AppBar, Button, Toolbar, Typography } from "@mui/material"

const Header = () => {
    return (
        <AppBar position="static" color="transparent" elevation={0}>
            <Toolbar>
                <Typography variant="h6" component="div" sx={{ flexGrow: 1, fontWeight: "bold" }}>
                    Elearn
                </Typography>
                <Button color="primary" sx={{ mr: 2 }}>
                    Log in
                </Button>
                <Button variant="contained" color="primary">
                    Sign up
                </Button>
            </Toolbar>
        </AppBar>
    )
}

export default Header
