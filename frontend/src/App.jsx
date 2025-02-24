import { BrowserRouter, Routes, Route } from "react-router-dom"
import { Container, CssBaseline, ThemeProvider, createTheme } from "@mui/material"
import Header from "./components/Header"
import Hero from "./components/Hero"
import Search from "./components/Search"
import Categories from "./components/Categories"
import CourseGrid from "./components/CourseGrid"
import YourCourses from "./components/YourCourses"
import Footer from "./components/Footer"
import ProfilePage from "../src/components/Profile/ProfilePage.jsx"

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

const HomePage = () => {
    return (
        <>
            <Hero />
            <Search />
            <Container sx={{ display: "flex", gap: 4, py: 4 }}>
                <Categories />
                <CourseGrid />
            </Container>
            <YourCourses />
        </>
    )
}

function App() {
    return (
        <BrowserRouter>
            <ThemeProvider theme={theme}>
                <CssBaseline />
                <Container maxWidth={false} disableGutters>
                    <Header />
                    <Routes>
                        <Route path="/" element={<HomePage />} />
                        <Route
                            path="/profile"
                            element={
                                <ProfilePage
                                    onBackClick={() => window.history.back()}
                                />
                            }
                        />
                    </Routes>
                    <Footer />
                </Container>
            </ThemeProvider>
        </BrowserRouter>
    )
}

export default App
