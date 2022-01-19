// Styles
import styled from "styled-components";
// Compnents
import PostHeader from "./PostHeader";
import PostContent from "./PostContent";
import PostFooter from "./PostFooter";

const Post = ({ post }) => {
  const postId = post.id;
  return (
    <PostStyle>
      <PostHeader user={post.user} postId={postId} />
      <PostContent image={post.imageUrl} />
      <PostFooter post={post} showAll='true' />
    </PostStyle>
  );
};

export default Post;

const PostStyle = styled.div`
  width: 100%;
  border-radius: 3px;
  border: 1px solid #adadadc5;
  margin-bottom: 1.5em;
`;
