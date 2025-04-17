import { Spinner } from "react-bootstrap";

export default function Loader() {
    return (
        <div className="text-center my-4">
            <Spinner animation="border" role="status" />
        </div>
    );
}
