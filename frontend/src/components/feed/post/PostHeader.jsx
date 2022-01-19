// React
import { useState } from "react";
import { useNavigate } from "react-router";
// Recoil
import { useRecoilValue, useSetRecoilState } from "recoil";
import { atomPostId, atomUser } from "../../../atom/atomStates";
// Constants
import { MODAL_TYPE } from "../../../constants";
// Styles
import styled from "styled-components";
// Icons
import { BsThreeDots } from "react-icons/bs";
// Components
import ModalContainer from "../../modal/ModalContainer";

const PostHeader = ({ user, style, postId: id }) => {
  const [postId, setPostId] = useState(id);
  const setCurrentPressedPostId = useSetRecoilState(atomPostId);
  const loggedInUser = useRecoilValue(atomUser);
  const navigate = useNavigate();

  const handleUsernamePress = (userId) => {
    navigate(`/profile/${userId}`);
  };

  const getModalType = () => {
    return user?.username === loggedInUser?.username
      ? MODAL_TYPE.USER_POST_OPTION
      : MODAL_TYPE.POST_OPTION;
  };

  return (
    <PostHeaderStyle style={style}>
      <div>
        <img src={user.profilePic} alt={user.username} />
        <p onClick={() => handleUsernamePress(user.id)}>{user.username}</p>
      </div>
      <ModalContainer
        typeOfModal={getModalType()}
        contentLabel='option menu'
        buttonContent={
          <BsThreeDots
            onClick={() => setCurrentPressedPostId(postId)}
            style={{
              cursor: "pointer",
              marginRight: "1rem",
              fontSize: "1rem",
            }}
          />
        }
        obj={postId}
      />
    </PostHeaderStyle>
  );
};

export default PostHeader;

const PostHeaderStyle = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  height: 60px;
  border-bottom: 1px solid #adadadc5;
  > :first-child {
    flex: 1;
    display: flex;
    align-items: center;
    img {
      height: 32px;
      width: 32px;
      border-radius: 9999px;
      margin-left: 1rem;
      cursor: pointer;
    }
    p {
      font-weight: 500;
      letter-spacing: 1px;
      font-size: 0.9rem;
      padding-left: 1rem;
      cursor: pointer;
      transition: all 250ms ease-out;
      &:hover {
        text-decoration: underline;
      }
    }
  }
`;
