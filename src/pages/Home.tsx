import FeatureGrid from "../components/Home/FeatureGrid";
import HeroSection from "../components/Home/HeroSection";

export default function Home() {
    return (
        <div>
            <HeroSection />
            <FeatureGrid />
            <footer className="text-center text-muted mt-5 mb-4">
                &copy; {new Date().getFullYear()} Mihaela eLearning. All rights reserved.
            </footer>
        </div>
    );
}
