import "./App.css"
import Header from "./components/Header"
import Hero from "./components/Hero"
import Search from "./components/Search"
import Categories from "./components/Categories"
import CourseGrid from "./components/CourseGrid"
import YourCourses from "./components/YourCourses"
import Footer from "./components/Footer"

function App() {
  return (
    <div className="container">
      <Header />
      <Hero />
      <Search />
      <div className="main-content">
        <Categories />
        <CourseGrid />
      </div>
      <YourCourses />
      <Footer />
    </div>
  )
}

export default App
