// Styles
import styled from "styled-components";
// TestData
import video from "../../test__data/test-video.mp4";

const StoryModal = () => {
  // fetch the story that you pressed on

  return (
    <>
      <VideoStyle
        width='500'
        height='750'
        src={video}
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
