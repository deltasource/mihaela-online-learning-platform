import { ListGroup } from "react-bootstrap";
import { Video } from "../../types";

interface Props {
  videos: Video[];
}

function VideoList({ videos }: Props) {
  if (videos.length === 0) {
    return <p>No videos available for this course.</p>;
  }

  return (
    <ListGroup>
      {videos.map(video => (
        <ListGroup.Item key={video.id}>
          ▶ {video.title} <span className="text-muted">({video.duration})</span>
        </ListGroup.Item>
      ))}
    </ListGroup>
  );
}
export default VideoList;