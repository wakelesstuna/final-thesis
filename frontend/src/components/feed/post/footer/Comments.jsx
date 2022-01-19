// Styles
import styled from "styled-components";
// Constants
import { MODAL_TYPE } from "../../../../constants";
// Components
import ModalContainer from "../../../modal/ModalContainer";
import Comment from "./Comment";

const Comments = ({ post, comments, showAll, refetchComments }) => {
  let commentMsg;
  if (post.comments.length < 1) {
    commentMsg = "No comments yet";
  } else {
    commentMsg = `View all ${post.comments.length} comments...`;
  }

  return (
    <CommentsStyle>
      {comments &&
        comments.map((c) => (
          <Comment
            key={c.id}
            comment={c}
            post={post}
            refetchComments={refetchComments}
          />
        ))}
      {showAll === "true" && (
        <ModalContainer
          typeOfModal={MODAL_TYPE.POST}
          contentLabel={commentMsg}
          buttonContent={commentMsg}
          style={{
            color: "#8080808b",
            padding: "1rem",
            cursor: "pointer",
            fontWeight: 400,
          }}
          obj={post}
        />
      )}
    </CommentsStyle>
  );
};

export default Comments;

const CommentsStyle = styled.div`
  p {
    color: #8080808b;
    font-weight: 400;
    padding: 1rem;
    cursor: pointer;
  }
`;
