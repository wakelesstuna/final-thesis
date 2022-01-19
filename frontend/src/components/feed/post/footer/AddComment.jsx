// React
import { useEffect, useState } from "react";
import useFocus from "../../../../hooks/useFocus";
// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../../../atom/atomStates";
// GraphQL
import { useMutation } from "@apollo/client";
import { createComment_gql } from "../../../../graphql/mutation";
// Styles
import styled from "styled-components";
// Icons
import { BsEmojiSmile } from "react-icons/bs";
// Emoji
import Picker from "emoji-picker-react";

const AddComment = ({ postId, refetchComments }) => {
  const { id } = useRecoilValue(atomUser);
  const [inputRef, setInputFocus] = useFocus();
  const [openEmojiPicker, setOpenEmojiPicker] = useState(false);
  const [comment, setComment] = useState("");
  const [disable, setDisable] = useState(false);
  const [createComment] = useMutation(createComment_gql);

  const onEmojiClick = (event, emojiObject) => {
    setComment(comment + emojiObject.emoji);
    setOpenEmojiPicker(!openEmojiPicker);
    setInputFocus();
  };

  const handleComment = async (userId, postId) => {
    try {
      await createComment({
        variables: {
          createCommentInput: {
            userId,
            postId,
            comment,
          },
        },
      });
    } catch (e) {
      console.log({ e });
    }

    console.log("Post id: ", postId);
    refetchComments();
    setComment("");
  };

  useEffect(() => {
    comment === "" ? setDisable(true) : setDisable(false);
  }, [comment]);
  return (
    <AddCommentStyle disable={disable}>
      <div>
        {openEmojiPicker && (
          <EmojiPickerWrapper>
            <Picker onEmojiClick={onEmojiClick} />
          </EmojiPickerWrapper>
        )}
        <BsEmojiSmile onClick={() => setOpenEmojiPicker(!openEmojiPicker)} />
      </div>
      <div>
        <input
          type='text'
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          placeholder='Add a comment...'
          ref={inputRef}
        />
      </div>
      <div>
        <button disabled={disable} onClick={() => handleComment(id, postId)}>
          Post
        </button>
      </div>
    </AddCommentStyle>
  );
};

export default AddComment;

const AddCommentStyle = styled.div`
  width: 100%;
  border-top: 1px solid #adadadc5;
  height: 45px;
  display: flex;
  align-items: center;
  div {
    display: flex;
    align-items: center;
    padding: 1rem;
    svg {
      font-size: 1.5rem;
      cursor: pointer;
    }
    button {
      color: #0095f6;
      opacity: ${(props) => (props.disable ? "0.4" : "1")};
      cursor: ${(props) => (props.disable ? "default" : "pointer")};
    }
  }

  > :first-child {
    position: relative;
  }
  > :nth-child(2) {
    flex: 1;
    input {
      width: 100%;
      outline: none;
      border: none;
    }
  }
`;

const EmojiPickerWrapper = styled.div`
  position: absolute;
  bottom: -1rem;
`;
