// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../../../atom/atomStates";
// GraphQL
import { useMutation } from "@apollo/client";
// Styles
import styled from "styled-components";
// Icons
import { BsTrash } from "react-icons/bs";
import { deleteComment_gql } from "../../../../graphql/mutation";

const Comment = ({ comment, post, refetchComments }) => {
  console.log("Comment: ", post);
  const { id, username } = useRecoilValue(atomUser);

  const [deleteCommentMutation] = useMutation(deleteComment_gql);

  const handleDeleteComment = async () => {
    try {
      const response = await deleteCommentMutation({
        variables: {
          commentInput: {
            commentId: comment.id,
            postId: post.id,
            userId: id,
          },
        },
      });

      if (response.data) {
        refetchComments({
          variables: {
            postId: post.id,
          },
        });
        closeModal();
      }
    } catch (e) {
      console.log({ e });
    }
  };
  return (
    <CommentStyle>
      <p>
        <span>{comment.user.username}</span> {comment.comment}
      </p>
      <div>
        {comment.user.username === username ? (
          <BsTrash onClick={handleDeleteComment} />
        ) : null}
      </div>
    </CommentStyle>
  );
};

export default Comment;

const CommentStyle = styled.div`
  display: flex;

  p {
    flex: 1;
    max-width: 240px;
    word-wrap: break-word;
    span {
      font-weight: 400;
    }
    padding: 0.5rem 1rem;
    font-weight: 300;
  }
  div {
    display: flex;
    justify-content: center;
    align-items: flex-start;
    margin-right: 1rem;
    margin-top: 0.5rem;
    cursor: pointer;
  }
`;
