// React
import { useNavigate } from "react-router-dom";
// Recoil
import { useRecoilValue } from "recoil";
import { atomPostId } from "../../atom/atomStates";
// Styles
import styled from "styled-components";
import {
  PostOptionModalRedText,
  PostOptionModalStyle,
} from "../../styles/layout";

const PostOptionModal = ({ closeModal, postId }) => {
  const currentPressedPostId = useRecoilValue(atomPostId);
  const navigate = useNavigate();

  const handleReport = () => {
    alert("error: not yet implemented");
  };
  const handleUnFollow = () => {
    alert("error: not yet implemented");
  };
  const handleGoToPost = (postId) => {
    navigate(`/post/${postId}`);
  };
  const handleShareTo = () => {
    alert("error: not yet implemented");
  };
  const handleCopyLink = () => {
    alert("error: not yet implemented");
  };
  const handleEmbed = () => {
    alert("error: not yet implemented");
  };

  return (
    <PostOptionModalStyle>
      <div onClick={handleReport}>
        <PostOptionModalRedText>Report</PostOptionModalRedText>
      </div>
      <div onClick={handleUnFollow}>
        <PostOptionModalRedText>Unfollow</PostOptionModalRedText>
      </div>
      <div onClick={() => handleGoToPost(currentPressedPostId)}>
        <p>Go to post</p>
      </div>
      <div onClick={handleShareTo}>
        <p>Share to...</p>
      </div>
      <div onClick={handleCopyLink}>
        <p>Copy link</p>
      </div>
      <div onClick={handleEmbed}>
        <p>Embed</p>
      </div>
      <div onClick={closeModal}>
        <p>Cancel</p>
      </div>
    </PostOptionModalStyle>
  );
};

export default PostOptionModal;
