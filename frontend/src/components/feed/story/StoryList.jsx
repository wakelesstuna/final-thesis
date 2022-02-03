// Styles
import styled from "styled-components";
// Components
import Story from "./Story";
// GraphQL
import { useQuery } from "@apollo/client";
import { fetchUsersOfStories_gql } from "../../../graphql/query";
// Components
import ErrorSwalMessage from "../../util/ErrorSwalMessage";
import { useRef } from "react";
// Icons
import { BiChevronRight, BiChevronLeft } from "react-icons/bi";

const Stories = () => {
  const storyList = useRef();
  /* const { data, loading, error } = useQuery(fetchUsersOfStories_gql);

  if (loading) return <div>Loading stories</div>;
  if (error) return <ErrorSwalMessage error={error} />; */
  const swipeRight = () => {
    counter++;
    const list = document.querySelector(`#${listId}`);
    const carouselItems = document.querySelectorAll(`#${listId} li`);
    const size = carouselItems[0].clientWidth;
    const calc = size * 4;
    if (counter === 1) {
      const leftBtn = document.querySelector(`#left__btn__${listId}`);
      leftBtn.style.pointerEvents = "auto";
      leftBtn.style.opacity = 1;
    }
    if (counter === carouselItems.length / 4 - 1) {
      const rightBtn = document.querySelector(`#right__btn__${listId}`);
      rightBtn.style.pointerEvents = "none";
      rightBtn.style.opacity = 0;
    }
    list.style.transform = `translateX(-${calc * counter}px)`;
  };
  const swipeLeft = () => {
    counter--;
    const list = document.querySelector(`#${listId}`);
    const carouselItems = document.querySelectorAll(`#${listId} li`);
    const size = carouselItems[0].clientWidth;
    const calc = size * 4;
    if (counter === 0) {
      const leftBtn = document.querySelector(`#left__btn__${listId}`);
      leftBtn.style.pointerEvents = "none";
      leftBtn.style.opacity = 0;
    }
    if (counter < carouselItems.length / 4) {
      const rightBtn = document.querySelector(`#right__btn__${listId}`);
      rightBtn.style.pointerEvents = "auto";
      rightBtn.style.opacity = 1;
    }
    list.style.transform = `translateX(-${calc * counter}px)`;
  };

  const data = [
    {
      id: "123",
      storyUrl: "test-a",
      user: {
        id: "12332113",
        username: "wakelesstuna",
        profilePic: "https://picsum.photos/200/300",
      },
    },
  ];

  return (
    <StoriesStyle>
      <button
        id={`left__btn__${listId}`}
        class='scroll__btn left'
        onClick={swipeLeft}
      >
        <div class='icon icon__left'>
          <BiChevronLeft />
        </div>
      </button>
      <button
        id={`right__btn__${listId}`}
        class='scroll__btn right'
        onClick={swipeRight}
      >
        <div class='icon icon__right'>
          <BiChevronRight />
        </div>
      </button>
      <ul ref={storyList}>
        {data &&
          data.stories.map((story) => (
            <Story key={story.id} story={story} user={story.user} />
          ))}
      </ul>
    </StoriesStyle>
  );
};

export default Stories;

const StoriesStyle = styled.li`
  height: 110px;
  position: relative;
  border: 1px solid #adadadc5;
  margin-bottom: 1.6rem;
  width: 100%;
  overflow-x: scroll;
  border-radius: 3px;

  ::-webkit-scrollbar {
    height: 8px;
  }

  ::-webkit-scrollbar-track {
    background-color: #e4e4e4;
  }

  ::-webkit-scrollbar-thumb {
    background-color: #a3a3a3;
    border-radius: 100px;
  }

  ul {
    display: flex;
  }
`;
