import { Box, Container, Button } from "@mui/material"
import { ArrowBack } from "@mui/icons-material"
import ProfileHeader from "./ProfileHeader"
import ProfileTabs from "./ProfileTabs.jsx"
import { mockUserData } from "./mockData"

const ProfilePage = ({ onBackClick }) => {
  const userData = mockUserData

  return (
    <Box
      sx={{
        minHeight: "100vh",
        bgcolor: "background.default",
        py: 4,
      }}
    >
      <Container maxWidth="lg">
        <Button
          startIcon={<ArrowBack />}
          onClick={onBackClick}
          sx={{
            mb: 2,
            "&:hover": {
              transform: "translateY(-2px)",
              boxShadow: "0 4px 12px rgba(37, 99, 235, 0.2)",
            },
          }}
        >
          Back to Home
        </Button>
        <Box
          sx={{
            bgcolor: "background.paper",
            borderRadius: 2,
            overflow: "hidden",
            boxShadow: "0 4px 6px rgba(0, 0, 0, 0.05)",
          }}
        >
          <ProfileHeader user={userData} />
          <ProfileTabs user={userData} />
        </Box>
      </Container>
    </Box>
  )
}

export default ProfilePage
