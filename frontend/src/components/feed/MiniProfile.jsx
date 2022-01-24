// React
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../atom/atomStates";
// GraphQL
import { useMutation } from "@apollo/client";
import { followUser_gql, unFollowUser_gql } from "../../graphql/mutation";
// Styles
import styled from "styled-components";

const MiniProfile = ({ user, height, padding, imgSize, hover }) => {
  const [followText, setFollowText] = useState();
  const FOLLOW = "Follow";
  const UNFOLLOW = "Unfollow";
  const { id: currentUserId } = useRecoilValue(atomUser);
  const navigate = useNavigate();

  const [followUserMutation] = useMutation(followUser_gql);
  const [unFollowUserMutation] = useMutation(unFollowUser_gql);

  const handleProfilePress = (userid) => {
    navigate(`/profile/${userid}`);
  };

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
  };

  const checkIfUserisFollowing = (followers) => {
    const bool = followers?.find((e) => e.id === currentUserId);
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
    <MiniProfileStyle
      height={height}
      padding={padding}
      imgSize={imgSize}
      hover={hover}
    >
      <img src={user.profilePic} alt='profile' />
      <TextInfo onClick={() => handleProfilePress(user.id)}>
        <p>
          <span>{user.username}</span>
        </p>
        <TextFamilyName>
          {user.firstName} {user.lastName}
        </TextFamilyName>
      </TextInfo>
      <div>
        <button onClick={(e) => handleFollow(e, currentUserId, user.id)}>
          {followText}
        </button>
      </div>
    </MiniProfileStyle>
  );
};

export default MiniProfile;

const MiniProfileStyle = styled.div`
  height: 45px;
  width: 100%;
  padding: 0.5rem 1rem;
  margin: auto;
  display: flex;
  align-items: center;
  cursor: pointer;
  img {
    height: 35px;
    aspect-ratio: 1;
    object-fit: cover;
    border-radius: 9999px;
  }

  div {
    button {
      color: #0095f6;
    }
  }
`;

const TextInfo = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;

  p {
    margin-left: 1rem;
    max-width: 110px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    span {
      &:hover {
        text-decoration: underline;
      }
    }
  }

  > :first-child {
    font-weight: 400;
    font-size: 0.8rem;
  }

  > :last-child {
    font-weight: 200;
    font-size: 0.8rem;
  }
`;

const TextFamilyName = styled.p`
  color: #808080;
`;
