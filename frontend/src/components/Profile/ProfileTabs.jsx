"use client"

import { useState } from "react"
import { Box, Tabs, Tab, Typography, Card, CardContent, LinearProgress } from "@mui/material"
import { School, Star, Assignment, Person } from "@mui/icons-material"

const TabPanel = ({ children, value, index, ...other }) => (
  <div role="tabpanel" hidden={value !== index} id={`profile-tabpanel-${index}`} {...other}>
    {value === index && <Box sx={{ p: 3 }}>{children}</Box>}
  </div>
)

const ProfileTabs = ({ user }) => {
  const [value, setValue] = useState(0)

  const handleChange = (event, newValue) => {
    setValue(newValue)
  }

  return (
    <Box>
      <Tabs
        value={value}
        onChange={handleChange}
        variant="scrollable"
        scrollButtons="auto"
        sx={{
          borderBottom: 1,
          borderColor: "divider",
          "& .MuiTab-root": {
            minHeight: 64,
            fontSize: "0.875rem",
          },
          "& .MuiTab-root.Mui-selected": {
            color: "primary.main",
          },
        }}
      >
        <Tab icon={<School />} label="Courses" />
        <Tab icon={<Star />} label="Achievements" />
        {user.role === "Student" && <Tab icon={<Assignment />} label="Learning" />}
        <Tab icon={<Person />} label="About" />
      </Tabs>

      <TabPanel value={value} index={0}>
        <Box
          sx={{
            display: "grid",
            gridTemplateColumns: {
              xs: "1fr",
              sm: "repeat(2, 1fr)",
              md: "repeat(3, 1fr)",
            },
            gap: 3,
          }}
        >
          {user.courses?.map((course, index) => (
            <Card
              key={index}
              sx={{
                "&:hover": {
                  transform: "translateY(-5px)",
                  boxShadow: "0 8px 16px rgba(0, 0, 0, 0.1)",
                },
                transition: "all 0.3s",
              }}
            >
              <CardContent>
                <Typography variant="h6" gutterBottom color="primary.dark">
                  {course.title}
                </Typography>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  {course.description}
                </Typography>
                <Box sx={{ mt: 2 }}>
                  <Typography variant="body2" color="text.secondary">
                    Progress: {course.progress}%
                  </Typography>
                  <LinearProgress
                    variant="determinate"
                    value={course.progress}
                    sx={{
                      mt: 1,
                      height: 8,
                      borderRadius: 1,
                      bgcolor: "background.default",
                      "& .MuiLinearProgress-bar": {
                        borderRadius: 1,
                      },
                    }}
                  />
                </Box>
              </CardContent>
            </Card>
          ))}
        </Box>
      </TabPanel>

      {}
    </Box>
  )
}

export default ProfileTabs
