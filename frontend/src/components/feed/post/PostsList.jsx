// Components
import Post from "./Post";

const PostsList = ({ posts }) => {
  return (
    <ul>
      {posts &&
        posts.map((edge) => (
          <li key={edge.node.id}>
            <Post key={edge.node.id} post={edge.node} />
          </li>
        ))}
    </ul>
  );
};

export default PostsList;
