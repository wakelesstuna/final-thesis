// Styles
import styled from "styled-components";
// Constants
import { MODAL_TYPE } from "../../../constants";
// Components
import ModalContainer from "../../modal/ModalContainer";

const Story = ({ user, story }) => {
  return (
    <StoryStyle>
      <ModalContainer
        buttonContent={
          <StoryButtonStyle>
            <StoryRingOutter>
              <StoryRingInner>
                <StoryImg src={user.profilePic} alt={user.username} />
              </StoryRingInner>
            </StoryRingOutter>
          </StoryButtonStyle>
        }
        overlayColor={"#242424"}
        modalBackground={"transparent"}
        typeOfModal={MODAL_TYPE.STORY}
        obj={story}
      ></ModalContainer>
      <UsernameStyle>{user.username}</UsernameStyle>
    </StoryStyle>
  );
};

export default Story;

const StoryStyle = styled.li`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-width: 90px;
  max-width: 90px;
  margin-top: 0.5rem;
`;

const StoryButtonStyle = styled.div`
  cursor: pointer;
  height: 65px;
  background: linear-gradient(45deg, blue, red);
  border-radius: 9999px;
`;

const StoryRingOutter = styled.div`
  height: 65px;
  width: 65px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 9999px;
  border: 2px solid transparent;
`;

const StoryRingInner = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  border-radius: 9999px;
  border: 2px solid #fff;
`;

const StoryImg = styled.img`
  height: 65px;
  aspect-ratio: 1;
  object-fit: cover;
  border-radius: 9999px;
  padding: 2px;
  border: 2px solid transparent;
`;

const UsernameStyle = styled.p`
  font-weight: 300;
  text-transform: lowercase;
  font-size: 0.9rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 90px;
`;
