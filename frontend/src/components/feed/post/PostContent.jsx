// Styles
import styled from "styled-components";

const PostContent = ({ image }) => {
  return (
    <PostContentStyle>
      <img src={image} alt='post' />
    </PostContentStyle>
  );
};

export default PostContent;

const PostContentStyle = styled.div`
  width: 100%;
  height: fit-content;
  max-height: 700px;
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  overflow: hidden;
`;
