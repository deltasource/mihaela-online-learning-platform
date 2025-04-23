import 'bootstrap/dist/css/bootstrap.min.css';

interface VideoPlayerProps {
    videoUrl: string
}

const VideoPlayer = ({ videoUrl }: VideoPlayerProps) => {
    return (
        <div className="ratio ratio-16x9 mb-4 bg-dark rounded overflow-hidden">
            {videoUrl ? (
                <video controls className="w-100 h-100" poster="/abstract-thumbnail.png">
                    <source src={videoUrl} type="video/mp4" />
                    Your browser does not support the video tag.
                </video>
            ) : (
                <div className="d-flex align-items-center justify-content-center bg-dark text-white">
                    <div className="text-center">
                        <i className="bi bi-film fs-1 mb-2"></i>
                        <p>Video not available</p>
                    </div>
                </div>
            )}
        </div>
    )
}

export default VideoPlayer
