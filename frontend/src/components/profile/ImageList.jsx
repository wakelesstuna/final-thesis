// Styles
import styled from "styled-components";
// Constants
import { BREAKPOINTS } from "../../constants";

const ImageList = ({ imageList }) => {
  return (
    <ImageListStyle breakPoints={BREAKPOINTS}>
      {imageList && imageList.map((image) => <img src={image.url} />)}
    </ImageListStyle>
  );
};

export default ImageList;

const ImageListStyle = styled.div`
  display: grid;
  grid-gap: 20px;
  grid-template-columns: repeat(auto-fit, 225px);
  max-width: 715px;
  margin: 0 auto; //max-width: ${(props) => props.breakPoints.xl};
  img {
    width: 225px;
    aspect-ratio: 1;
    object-fit: cover;
  }
`;
