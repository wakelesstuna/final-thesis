// React
import { useEffect } from "react";
// GraphQL
import { useQuery } from "@apollo/client";
import { getUserById_gql } from "../../../graphql/query";
// Recoil
import { useRecoilState } from "recoil";
import { atomUser } from "../../../atom/atomStates";
// Styles
import styled from "styled-components";
// Components
import Follow from "./Follow";

const FollowList = ({ listOfUsers, refetchList, type, closeModal }) => {
  const [user, setUser] = useRecoilState(atomUser);

  const { loading, error, data, refetch } = useQuery(getUserById_gql, {
    variables: { id: user.id },
  });

  useEffect(() => {
    if (data !== undefined) {
      localStorage.setItem("user", JSON.stringify(data.user));
      setUser(data.user);
    }
  }, [data]);
  return (
    <FollowListStyle>
      {listOfUsers &&
        listOfUsers.map((follower) => (
          <Follow
            key={follower.id}
            follower={follower}
            refetchList={refetchList}
            refetchUser={refetch}
            type={type}
            closeModal={closeModal}
          />
        ))}
    </FollowListStyle>
  );
};

export default FollowList;

const FollowListStyle = styled.div`
  width: 400px;
  max-height: 400px;
  min-height: 200px;
`;
