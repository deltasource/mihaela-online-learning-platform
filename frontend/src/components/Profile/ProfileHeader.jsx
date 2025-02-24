import { Box, Avatar, Typography, Button, Chip, IconButton } from "@mui/material"
import { Edit, LinkedIn, Instagram, GitHub } from "@mui/icons-material"

const ProfileHeader = ({ user }) => {
    return (
        <Box>
            <Box
                sx={{
                    height: 200,
                    bgcolor: "primary.light",
                    position: "relative",
                }}
            >
                {user.coverImage && (
                    <Box
                        component="img"
                        src={user.coverImage}
                        alt="Cover"
                        sx={{
                            width: "100%",
                            height: "100%",
                            objectFit: "cover",
                        }}
                    />
                )}
            </Box>

            <Box sx={{ px: 3, pb: 3 }}>
                <Box sx={{ display: "flex", alignItems: "flex-end", mb: 2 }}>
                    <Avatar
                        src={user.avatar}
                        alt={user.name}
                        sx={{
                            width: 120,
                            height: 120,
                            border: 4,
                            borderColor: "background.paper",
                            marginTop: "-50px",
                            boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
                        }}
                    />
                    <Box sx={{ ml: "auto", mt: 2 }}>
                        <Button
                            variant="outlined"
                            startIcon={<Edit />}
                            sx={{
                                mr: 1,
                                "&:hover": {
                                    transform: "translateY(-2px)",
                                    boxShadow: "0 4px 12px rgba(37, 99, 235, 0.2)",
                                },
                            }}
                        >
                            Edit Profile
                        </Button>
                    </Box>
                </Box>

                <Box sx={{ display: "flex", alignItems: "center", mb: 1 }}>
                    <Typography variant="h5" component="h1" sx={{ fontWeight: "bold", color: "primary.dark" }}>
                        {user.name}
                    </Typography>
                    <Chip
                        label={user.role}
                        size="small"
                        color="primary"
                        sx={{
                            ml: 2,
                            bgcolor: "primary.main",
                            color: "white",
                        }}
                    />
                </Box>

                <Typography color="text.secondary" sx={{ mb: 2 }}>
                    {user.title}
                </Typography>

                <Typography sx={{ mb: 2, color: "text.secondary" }}>{user.bio}</Typography>

                <Box sx={{ display: "flex", gap: 1 }}>
                    {user.socialLinks?.linkedin && (
                        <IconButton
                            href={user.socialLinks.linkedin}
                            target="_blank"
                            sx={{
                                color: "primary.main",
                                "&:hover": {
                                    bgcolor: "primary.light",
                                    color: "white",
                                },
                            }}
                        >
                            <LinkedIn />
                        </IconButton>
                    )}
                    {user.socialLinks?.instagram && (
                        <IconButton
                            href={user.socialLinks.instagram}
                            target="_blank"
                            sx={{
                                color: "primary.main",
                                "&:hover": {
                                    bgcolor: "primary.light",
                                    color: "white",
                                },
                            }}
                        ><Instagram/>
                        </IconButton>
                    )}
                    {user.socialLinks?.github && (
                        <IconButton
                            href={user.socialLinks.github}
                            target="_blank"
                            sx={{
                                color: "primary.main",
                                "&:hover": {
                                    bgcolor: "primary.light",
                                    color: "white",
                                },
                            }}
                        >
                            <GitHub />
                        </IconButton>
                    )}
                </Box>
            </Box>
        </Box>
    )
}

export default ProfileHeader
