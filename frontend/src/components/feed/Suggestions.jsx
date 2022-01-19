// React
import { useEffect, useState } from "react";
// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../atom/atomStates";
// GraphQL
import apolloClient from "../../graphql/apolloClient";
import { getRandomUsers_gql } from "../../graphql/query";
// Styles
import styled from "styled-components";
// Components
import MiniProfile from "./MiniProfile";

const Suggestions = () => {
  const user = useRecoilValue(atomUser);
  const [data, setData] = useState();

  useEffect(() => {
    apolloClient
      .query({
        query: getRandomUsers_gql,
        variables: { howMany: 5, username: user.username },
      })
      .then((resp) => {
        setData(resp.data.fetchRandomUsers);
      });
  }, []);

  return (
    <SuggestionStyle>
      <p>Suggestions</p>
      {data &&
        data.map(({ id, username, profilePic, followers }) => (
          <MiniProfile
            key={id}
            userId={id}
            profilePic={profilePic}
            username={username}
            followers={followers}
            imgSize='30px'
            height='50px'
            padding='0.5rem 1rem'
          />
        ))}
    </SuggestionStyle>
  );
};

const SuggestionStyle = styled.div`
  padding-top: 1rem;
  > :first-child {
    padding-left: 1rem;
    padding-bottom: 0.5rem;
  }
`;

export default Suggestions;
