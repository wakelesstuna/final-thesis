// Styles
import styled from "styled-components";
import { MODAL_TYPE } from "../../constants";
import ModalContainer from "../modal/ModalContainer";

const StoryItem = ({ story }) => {
  return (
    <ListItem>
      <ModalContainer
        buttonContent={
          <VideoWrapper>
            <VideoStyle src={story.storyUrl}></VideoStyle>
          </VideoWrapper>
        }
        overlayColor={"#242424"}
        modalBackground={"transparent"}
        typeOfModal={MODAL_TYPE.STORY}
        obj={story}
      ></ModalContainer>
    </ListItem>
  );
};

export default StoryItem;

const ListItem = styled.li`
  width: 30%;
  margin: auto;
  margin-bottom: 2rem;
`;

const VideoWrapper = styled.div`
  width: 250px;
  aspect-ratio: 1;
  overflow: hidden;
  cursor: pointer;
`;

const VideoStyle = styled.video`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;
