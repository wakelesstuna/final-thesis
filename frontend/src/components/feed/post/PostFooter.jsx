// React
import { useState } from "react";
// Styles
import styled from "styled-components";
// Components
import CommentSection from "./footer/CommentSection";
import Content from "./footer/Content";
import Likes from "./footer/Likes";
import Symbols from "./footer/Symbols";

const PostFooter = ({ post, showAll }) => {
  const [totalLikes, setTotalLikes] = useState(post.totalLikes);
  return (
    <>
      <PostFooterStyle>
        <Symbols postId={post.id} setTotalLikes={setTotalLikes} />
        <Likes totalLikes={totalLikes} />
        <Content user={post.user} content={post.caption} />
        <CommentSection post={post} showAll={showAll} />
      </PostFooterStyle>
    </>
  );
};

export default PostFooter;

const PostFooterStyle = styled.div`
  display: flex;
  flex-direction: column;
`;
