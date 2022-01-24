// GraphQL
import { useQuery } from "@apollo/client";
import { fetchStoriesForUser_gql } from "../../graphql/query";
// Recoil
import { atomUser } from "../../atom/atomStates";
import { useRecoilValue } from "recoil";

// Components
import ErrorSwalMessage from "../util/ErrorSwalMessage";
import LoadingDots from "../util/LoadingDots";
import StoryItem from "./StoryItem";
import styled from "styled-components";

const VideoList = () => {
  const { id } = useRecoilValue(atomUser);

  const { data, loading, error } = useQuery(fetchStoriesForUser_gql, {
    variables: {
      id,
    },
  });

  if (loading) return <LoadingDots />;
  if (error) return <ErrorSwalMessage error={error} />;

  return (
    <ListStyle>
      {data &&
        data.user.stories.map((story) => (
          <StoryItem key={story.id} story={story} />
        ))}
    </ListStyle>
  );
};

export default VideoList;

const ListStyle = styled.ul`
  display: flex;
  flex-wrap: wrap;
  margin-top: 2rem;
  max-width: 100%;
`;
