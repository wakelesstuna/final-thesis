// Styles
import styled from "styled-components";

const StoryModal = ({ story }) => {
  return (
    <>
      <VideoStyle
        width='500'
        height='750'
        src={story.storyUrl}
        controls
        autoPlay
      ></VideoStyle>
    </>
  );
};

export default StoryModal;

const VideoStyle = styled.video`
  position: relative;
  border-radius: 3px;
`;
