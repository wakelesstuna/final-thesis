// React
import { useState } from "react";
// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../../atom/atomStates";
// Styles
import styled from "styled-components";
// Icons
import { BsEmojiSmile } from "react-icons/bs";
// Emoji
import EmojiPicker from "emoji-picker-react";

const CreatePost = ({ image, inputRef, setInputFocus }) => {
  const user = useRecoilValue(atomUser);
  const [charCount, setCharCount] = useState(0);
  const [openEmojiPicker, setOpenEmojiPicker] = useState(false);

  const onEmojiClick = (event, emojiObject) => {
    setOpenEmojiPicker(!openEmojiPicker);
    inputRef.current.value += emojiObject.emoji;
    setInputFocus();
  };

  return (
    <CreatePostContainer>
      <ImageSection>
        <img src={image} alt='selected' />
      </ImageSection>
      <TextSection>
        <TextHeader>
          <img src={user?.profilePic} alt='user profile' />
          <p>{user?.username}</p>
        </TextHeader>
        <TextArea
          name='caption'
          id='caption'
          placeholder='Write a caption ...'
          maxLength='2200'
          onChange={(e) => setCharCount(e.target.value.length)}
          ref={inputRef}
        ></TextArea>
        <EmojiPickerContainer>
          {openEmojiPicker && (
            <EmojiPickerWrapper>
              <EmojiPicker onEmojiClick={onEmojiClick} />
            </EmojiPickerWrapper>
          )}
          <BsEmojiSmile onClick={() => setOpenEmojiPicker(!openEmojiPicker)} />
        </EmojiPickerContainer>
        <CharCount>{charCount}/2 200</CharCount>
      </TextSection>
    </CreatePostContainer>
  );
};

export default CreatePost;

const CreatePostContainer = styled.div`
  display: flex;
`;

const ImageSection = styled.div`
  max-height: 80vh;
  width: 70%;
  max-width: 1000px;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`;

const TextSection = styled.div`
  width: 285px;
  margin: 0 auto;
`;

const TextHeader = styled.div`
  width: 100%;
  display: flex;
  padding: 1.6rem 1rem;

  img {
    width: 35px;
    height: 35px;
    border-radius: 9999px;
    object-fit: cover;
  }

  p {
    align-self: center;
    padding-left: 1rem;
    flex: 1;
  }
`;

const TextArea = styled.textarea`
  width: 100%;
  height: 200px;
  resize: none;
  border: none;
  outline: none;
  padding: 0 1rem;
  margin: auto;
`;
const CharCount = styled.div`
  border-bottom: 1px solid #c0c0c0;
  width: 100%;
  text-align: end;
  color: #c0c0c0;
  font-size: 0.75rem;
  font-weight: 300;
  padding-bottom: 5px;
  padding-right: 5px;
`;

const EmojiPickerContainer = styled.div`
  position: relative;

  svg {
    position: absolute;
    left: 10px;
    top: -1px;
  }
`;

const EmojiPickerWrapper = styled.div`
  position: absolute;
  top: 20px;
  left: -40px;
`;
