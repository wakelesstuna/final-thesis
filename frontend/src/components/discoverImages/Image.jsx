// Styles
import styled from "styled-components";
// Icons
import { FaRegComment } from "react-icons/fa";
import { AiFillHeart } from "react-icons/ai";

const Image = ({ post }) => {
  return (
    <ImageStyle>
      <div></div>
      <img src={post.imageUrl} alt='image' />
      <p>
        <AiFillHeart /> {post.totalLikes} {"  "}
        <FaRegComment /> {post.totalComments}
      </p>
    </ImageStyle>
  );
};

export default Image;

const ImageStyle = styled.div`
  position: relative;
  text-align: center;
  cursor: pointer;
  z-index: 0;
  img {
    width: 100%;
    height: 100%;
    object-position: center;
    object-fit: cover;
  }
  p,
  div {
    display: none;
  }

  &:hover {
    div {
      display: block;
      position: absolute;
      inset: 0;
      background-color: hsl(0, 0%, 0%, 0.5);
    }
    p {
      display: block;
      position: absolute;
      color: #fff;
      letter-spacing: 1px;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }
  }
`;
