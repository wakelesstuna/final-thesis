// React
import { useEffect, useState } from "react";
// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../atom/atomStates";
// GraphQL
import { useMutation } from "@apollo/client";
import { followUser_gql, unFollowUser_gql } from "../../graphql/mutation";
// Styles
import styled from "styled-components";
// Components
import HeaderCount from "./HeaderCount";

const HeaderInfo = ({ user }) => {
  const { id } = useRecoilValue(atomUser);
  const [followText, setFollowText] = useState();
  const FOLLOW = "Follow";
  const UNFOLLOW = "Unfollow";
  const [followUserMutation] = useMutation(followUser_gql);
  const [unFollowUserMutation] = useMutation(unFollowUser_gql);

  const handleFollow = async (e, userId, userToFollowId) => {
    if (e.target.innerText === FOLLOW) {
      await followUserMutation({
        variables: {
          followInput: { userId: userId, followId: userToFollowId },
        },
      });
      setFollowText(UNFOLLOW);
    } else {
      await unFollowUserMutation({
        variables: {
          followInput: { userId: userId, followId: userToFollowId },
        },
      });
      setFollowText(FOLLOW);
    }
    location.reload();
  };

  const checkIfUserisFollowing = (followers) => {
    const bool = followers?.find((e) => e.id === id);
    if (bool) {
      setFollowText(UNFOLLOW);
    } else {
      setFollowText(FOLLOW);
    }
  };

  useEffect(() => {
    checkIfUserisFollowing(user.followers);
  }, []);

  return (
    <HeaderInfoStyle>
      <div>
        <UsernameWrapper>
          <h3>{user.username}</h3>
          {id === user.id ? null : (
            <FollowButton onClick={(e) => handleFollow(e, id, user.id)}>
              {followText}
            </FollowButton>
          )}
        </UsernameWrapper>
        <p>{user.description}</p>
      </div>
      <HeadCountStyle>
        <HeaderCount count={user.totalPosts} title='posts' />
        <HeaderCount count={user.totalFollowers} title='followers' />
        <HeaderCount count={user.totalFollowing} title='following' />
      </HeadCountStyle>
    </HeaderInfoStyle>
  );
};

export default HeaderInfo;

const HeaderInfoStyle = styled.div`
  display: flex;
  flex-direction: column;
  div {
    h3 {
      font-weight: lighter;
      font-size: 2rem;
      letter-spacing: 0.5px;
    }
  }
  > :not(:first-child) {
    margin-top: 2rem;
  }
`;

const UsernameWrapper = styled.div`
  display: flex;
  margin-bottom: 1em;
`;

const FollowButton = styled.button`
  margin-left: 2em;
  background-color: #3897f0;
  color: #fff;
  padding: 0.5em 2em;
  border-radius: 3px;
`;

const HeadCountStyle = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
`;
