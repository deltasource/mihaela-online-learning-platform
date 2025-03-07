import { Card, CardContent, Grid, Typography } from "@mui/material"

const CourseGrid = () => {
  const courses = [
    { title: "Data Science", description: "Learn data analysis and ML" },
    { title: "SEO", description: "Master search optimization" },
    { title: "Web Development", description: "Full-stack web development" },
    { title: "Java Programming", description: "Core Java fundamentals" },
    { title: "Database", description: "SQL and NoSQL databases" },
    { title: "Docker", description: "Container orchestration" },
  ]

  return (
      <Grid container spacing={3} flex={1}>
        {courses.map((course, index) => (
            <Grid item xs={12} sm={6} md={4} key={index}>
              <Card>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    {course.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {course.description}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
        ))}
      </Grid>
  )
}

export default CourseGrid
