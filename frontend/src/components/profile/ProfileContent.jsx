// React
import { useEffect, useRef, useState } from "react";
// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../atom/atomStates";
// Styles
import styled from "styled-components";
// Constants
import { PROFILE_CHOICE } from "../../constants";
// Icons
import { BsGrid3X3, BsPlayCircle, BsBookmark } from "react-icons/bs";
import { BiUserPin } from "react-icons/bi";
// Components
import ErrorMessage from "../util/ErrorMessage";
import BookmarkList from "./BookmarkList";
import UserPostsList from "./UserPostsList";
import VideoList from "./VideoList";

const ProfileContent = ({ currentPath, profileId }) => {
  const { id } = useRecoilValue(atomUser);

  const ref1 = useRef();
  const ref2 = useRef();
  const ref3 = useRef();
  const ref4 = useRef();
  const [choice, setChoice] = useState();

  const setActive = (e) => {
    removeActives();
    e.target.classList.add("active");
    setChoice(e.target.id);
  };

  const removeActives = () => {
    ref1.current.classList.remove("active");
    ref2.current.classList.remove("active");
    ref3.current?.classList.remove("active");
    ref4.current.classList.remove("active");
  };
  useEffect(() => {
    removeActives();
    if (currentPath === "bookmarks") {
      ref3.current?.classList.add("active");
      setChoice(PROFILE_CHOICE.SAVED);
    } else {
      ref1.current.classList.add("active");
      setChoice(PROFILE_CHOICE.POSTS);
    }
  }, [currentPath]);

  return (
    <ProfileContentStyle>
      <TextWrapper>
        <Text id={PROFILE_CHOICE.POSTS} ref={ref1} onClick={setActive}>
          <BsGrid3X3 />
          Posts
        </Text>
        <Text id={PROFILE_CHOICE.VIDEOS} ref={ref2} onClick={setActive}>
          <BsPlayCircle />
          Videos
        </Text>
        {profileId === id ? (
          <Text id={PROFILE_CHOICE.SAVED} ref={ref3} onClick={setActive}>
            <BsBookmark />
            Saved
          </Text>
        ) : null}
        <Text id={PROFILE_CHOICE.TAGGED} ref={ref4} onClick={setActive}>
          <BiUserPin /> Tagged
        </Text>
      </TextWrapper>
      <div>
        {choice === PROFILE_CHOICE.POSTS && (
          <UserPostsList profileId={profileId} />
        )}
        {choice === PROFILE_CHOICE.VIDEOS && <VideoList />}
        {choice === PROFILE_CHOICE.SAVED && <BookmarkList />}
        {choice === PROFILE_CHOICE.TAGGED && <ErrorMessage />}
      </div>
    </ProfileContentStyle>
  );
};

export default ProfileContent;

const ProfileContentStyle = styled.div`
  > :first-child {
    display: flex;
  }
  .active {
    border-top: 1px solid black;
    color: black;
  }
`;

const TextWrapper = styled.div`
  width: 55%;
  margin: auto;
  display: flex;
  justify-content: space-between;
`;

const Text = styled.p`
  border-top: 1px solid transparent;
  padding-top: 1rem;
  align-items: center;
  transform: translateY(-1px);
  cursor: pointer;
  text-transform: uppercase;
  font-weight: 400;
  font-size: 0.9rem;
  letter-spacing: 1px;
  color: #adadadc5;
  svg {
    pointer-events: none;
    margin-right: 5px;
    transform: translateY(2px);
  }
`;
