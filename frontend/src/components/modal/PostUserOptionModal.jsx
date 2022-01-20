// GraphQL
import { useMutation } from "@apollo/client";
import { useRecoilValue } from "recoil";
import { atomUser } from "../../atom/atomStates";
import { deletePostMutation_gql } from "../../graphql/mutation";
// Styles
import {
  PostOptionModalRedText,
  PostOptionModalStyle,
} from "../../styles/layout";

const PostUserOptionModal = ({ closeModal, postId }) => {
  const { id } = useRecoilValue(atomUser);
  const [deletePostMutation] = useMutation(deletePostMutation_gql);
  console.log("User id: ", id);
  console.log("User post: ", postId);
  const handleDeletePost = async () => {
    console.log("User id: ", id);
    console.log("User post: ", postId);
    /* try {
      const response = await deletePostMutation({
        variables: {
          postInput: {
            userId: id,
            postId: postId,
          },
        },
      });

      if (response.data) {
        //window.location.reload(false);
        closeModal();
      }
    } catch (e) {
      console.log({ e });
    } */
  };

  return (
    <PostOptionModalStyle>
      <div onClick={() => handleDeletePost()}>
        <PostOptionModalRedText>Delete Post</PostOptionModalRedText>
      </div>
      <div onClick={closeModal}>
        <p>Cancel</p>
      </div>
    </PostOptionModalStyle>
  );
};

export default PostUserOptionModal;
