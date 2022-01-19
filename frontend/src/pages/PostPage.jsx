// React
import { useParams } from "react-router-dom";

const PostPage = () => {
  let { postId } = useParams();
  // fetch post by postId

  return <div>This is the post page, post id == {postId}</div>;
};

export default PostPage;
