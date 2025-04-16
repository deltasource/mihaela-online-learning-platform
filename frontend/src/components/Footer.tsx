import { Link } from "react-router-dom"
import { Facebook, Twitter, Instagram, Linkedin, Youtube } from "lucide-react"

const Footer = () => {
    return (
        <footer className="footer mt-auto">
            <div className="container py-5">
                <div className="row">
                    <div className="col-12 col-md-4 mb-4 mb-md-0">
                        <h5 className="text-white fw-bold mb-4">Elearning</h5>
                        <p className="text-light">
                            Empowering individuals and organizations through high-quality online learning experiences.
                        </p>
                        <div className="d-flex gap-3 mt-4">
                            <a href="#" className="text-white">
                                <Facebook size={20} />
                            </a>
                            <a href="#" className="text-white">
                                <Twitter size={20} />
                            </a>
                            <a href="#" className="text-white">
                                <Instagram size={20} />
                            </a>
                            <a href="#" className="text-white">
                                <Linkedin size={20} />
                            </a>
                            <a href="#" className="text-white">
                                <Youtube size={20} />
                            </a>
                        </div>
                    </div>

                    <div className="col-6 col-md-2 mb-3">
                        <h6 className="text-white fw-bold mb-3">Learn</h6>
                        <ul className="nav flex-column">
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    Popular Courses
                                </Link>
                            </li>
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    Categories
                                </Link>
                            </li>
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    Certificates
                                </Link>
                            </li>
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    Free Courses
                                </Link>
                            </li>
                        </ul>
                    </div>

                    <div className="col-6 col-md-2 mb-3">
                        <h6 className="text-white fw-bold mb-3">Company</h6>
                        <ul className="nav flex-column">
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    About Us
                                </Link>
                            </li>
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    Careers
                                </Link>
                            </li>
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    Blog
                                </Link>
                            </li>
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    Press
                                </Link>
                            </li>
                        </ul>
                    </div>

                    <div className="col-6 col-md-2 mb-3">
                        <h6 className="text-white fw-bold mb-3">Support</h6>
                        <ul className="nav flex-column">
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    Contact Us
                                </Link>
                            </li>
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    Help Center
                                </Link>
                            </li>
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    FAQ
                                </Link>
                            </li>
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    Accessibility
                                </Link>
                            </li>
                        </ul>
                    </div>

                    <div className="col-6 col-md-2 mb-3">
                        <h6 className="text-white fw-bold mb-3">Legal</h6>
                        <ul className="nav flex-column">
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    Terms
                                </Link>
                            </li>
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    Privacy
                                </Link>
                            </li>
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    Cookie Settings
                                </Link>
                            </li>
                            <li className="nav-item mb-2">
                                <Link to="#" className="nav-link p-0 text-light">
                                    Sitemap
                                </Link>
                            </li>
                        </ul>
                    </div>
                </div>

                <div className="d-flex flex-column flex-sm-row justify-content-between pt-4 mt-4 border-top border-secondary">
                    <p className="text-light">© 2025 Elearning, Inc. All rights reserved.</p>
                    <div className="d-flex gap-3">
                        <select
                            className="form-select form-select-sm bg-dark text-light border-secondary"
                            style={{ width: "auto" }}
                        >
                            <option value="en">English</option>
                            <option value="es">Español</option>
                            <option value="fr">Français</option>
                            <option value="de">Deutsch</option>
                        </select>
                    </div>
                </div>
            </div>
        </footer>
    )
}

export default Footer
