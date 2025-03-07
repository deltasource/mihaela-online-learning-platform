"use client"

import { useState } from "react"
import { useNavigate } from "react-router-dom"
import {
    AppBar,
    Button,
    Toolbar,
    Typography,
    Avatar,
    Menu,
    MenuItem,
    IconButton,
    ListItemIcon,
    ListItemText,
    Divider,
    Box
} from "@mui/material"
import {
    Person,
    Settings,
    ExitToApp,
    Login as LoginIcon,
    PersonAdd as SignUpIcon
} from "@mui/icons-material"

const Header = () => {
    const navigate = useNavigate()
    const [anchorEl, setAnchorEl] = useState(null)

    const isLoggedIn = true
    const user = {
        name: "Mihaela",
        avatar: "/placeholder.svg?height=32&width=32"
    }

    const handleProfileClick = (event) => {
        setAnchorEl(event.currentTarget)
    }

    const handleMenuClose = () => {
        setAnchorEl(null)
    }

    const navigateToProfile = () => {
        navigate("/profile")
        handleMenuClose()
    }

    const navigateToHome = () => {
        navigate("/")
    }

    return (
        <AppBar position="static" color="transparent" elevation={0}>
            <Toolbar>
                <Typography
                    variant="h6"
                    component="div"
                    sx={{
                        flexGrow: 1,
                        fontWeight: "bold",
                        cursor: "pointer"
                    }}
                    onClick={navigateToHome}
                >
                    Elearn
                </Typography>

                <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                    {}
                    <Button
                        color="primary"
                        startIcon={<LoginIcon />}
                        onClick={() => navigate("/login")}
                    >
                        Log in
                    </Button>
                    <Button
                        variant="contained"
                        color="primary"
                        startIcon={<SignUpIcon />}
                        onClick={() => navigate("/register")}
                    >
                        Sign up
                    </Button>

                    {}
                    {isLoggedIn && (
                        <>
                            <IconButton
                                onClick={handleProfileClick}
                                sx={{ padding: "4px", ml: 1 }}
                            >
                                <Avatar
                                    src={user.avatar}
                                    alt={user.name}
                                    sx={{
                                        width: 32,
                                        height: 32,
                                        transition: "transform 0.2s ease-in-out",
                                        "&:hover": {
                                            transform: "scale(1.1)"
                                        }
                                    }}
                                />
                            </IconButton>
                            <Menu
                                anchorEl={anchorEl}
                                open={Boolean(anchorEl)}
                                onClose={handleMenuClose}
                                anchorOrigin={{
                                    vertical: "bottom",
                                    horizontal: "right"
                                }}
                                transformOrigin={{
                                    vertical: "top",
                                    horizontal: "right"
                                }}
                            >
                                <MenuItem onClick={navigateToProfile}>
                                    <ListItemIcon>
                                        <Person fontSize="small" />
                                    </ListItemIcon>
                                    <ListItemText primary="Profile" />
                                </MenuItem>
                                <MenuItem onClick={handleMenuClose}>
                                    <ListItemIcon>
                                        <Settings fontSize="small" />
                                    </ListItemIcon>
                                    <ListItemText primary="Settings" />
                                </MenuItem>
                                <Divider />
                                <MenuItem onClick={handleMenuClose}>
                                    <ListItemIcon>
                                        <ExitToApp fontSize="small" />
                                    </ListItemIcon>
                                    <ListItemText primary="Logout" />
                                </MenuItem>
                            </Menu>
                        </>
                    )}
                </Box>
            </Toolbar>
        </AppBar>
    )
}

export default Header
