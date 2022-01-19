// Constants
import { MODAL_TYPE } from "../../constants";
// Styles
import styled from "styled-components";
// Components
import ModalContainer from "../modal/ModalContainer";
import Image from "./Image";

const DiscoverImagesOne = ({ posts }) => {
  return (
    <ImageStyle>
      {posts &&
        posts.map((post) => (
          <ModalContainer
            key={post.id}
            typeOfModal={MODAL_TYPE.POST}
            contentLabel={"Post"}
            buttonContent={<Image post={post} />}
            style={{
              color: "#8080808b",
              padding: "1rem",
              cursior: "pointer",
              fontWeight: 400,
            }}
            obj={post}
          />
        ))}
    </ImageStyle>
  );
};

export default DiscoverImagesOne;

const ImageStyle = styled.div`
  padding: 20px;
  display: grid;
  width: 100%;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 250px 250px;
  grid-gap: 40px;
  > * {
    overflow: hidden;
  }
  > :first-child {
    grid-column: span 2;
    grid-row: span 2;
  }

  > :not(:first-child) {
    grid-column-start: 3;
  }

  img {
    width: 100%;
    aspect-ratio: 1;
    object-fit: cover;
  }
`;
