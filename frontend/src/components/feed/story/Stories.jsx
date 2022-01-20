// Styles
import styled from "styled-components";
// Components
import Story from "./Story";
// GraphQL
import { useQuery } from "@apollo/client";
import { fetchUsersOfStories_gql } from "../../../graphql/query";
// Components
import ErrorSwalMessage from "../../util/ErrorSwalMessage";

const Stories = () => {
  const { data, loading, error } = useQuery(fetchUsersOfStories_gql);

  if (loading) return <div>Loading stories</div>;
  if (error) return <ErrorSwalMessage error={error} />;

  return (
    <StoriesStyle>
      <ul>
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
