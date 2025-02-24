import { Container, CssBaseline, ThemeProvider, createTheme } from "@mui/material"
import Header from "./components/Header"
import Hero from "./components/Hero"
import Search from "./components/Search"
import Categories from "./components/Categories"
import CourseGrid from "./components/CourseGrid"
import YourCourses from "./components/YourCourses"
import Footer from "./components/Footer"

const theme = createTheme({
    palette: {
        primary: {
            main: "#2196f3",
        },
        secondary: {
            main: "#f50057",
        },
    },
})

function App() {
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Container maxWidth={false} disableGutters>
                <Header />
                <Hero />
                <Search />
                <Container sx={{ display: "flex", gap: 4, py: 4 }}>
                    <Categories />
                    <CourseGrid />
                </Container>
                <YourCourses />
                <Footer />
            </Container>
        </ThemeProvider>
    )
}

export default App
