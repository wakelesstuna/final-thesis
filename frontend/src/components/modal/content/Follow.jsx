// React
import { useNavigate, useParams } from "react-router-dom";
// GrahQL
import { useMutation } from "@apollo/client";
import { unFollowUser_gql } from "../../../graphql/mutation";
// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../../atom/atomStates";
// Styles
import styled from "styled-components";
// Constants
import { MODAL_TYPE } from "../../../constants";

const Follow = ({ follower, type, refetchUser, refetchList }) => {
  const navigate = useNavigate();
  const { profileId } = useParams();
  const user = useRecoilValue(atomUser);
  const [unFollow] = useMutation(unFollowUser_gql);

  const formatUserFamilyName = (follower) => {
    return `${follower.firstName} ${follower.lastName}`;
  };

  const handleUsernamePress = (followerId) => {
    navigate(`/profile/${followerId}`);
  };

  const handleRemoveFollower = async (userId, followerId) => {
    let followInput;
    if (type === MODAL_TYPE.FOLLOWERS) {
      followInput = {
        followId: userId,
        userId: followerId,
      };
    }

    if (type === MODAL_TYPE.FOLLOWING) {
      followInput = {
        followId: followerId,
        userId: userId,
      };
    }

    try {
      await unFollow({
        variables: {
          followInput,
        },
      });
    } catch (e) {
      console.error({ e });
    }

    refetchUser({
      id: user.id,
    });
    refetchList({
      userId: user.id,
    });
  };

  return (
    <FollowStyle>
      <ImageWrapper>
        <Image src={follower.profilePic} alt='profile' />
      </ImageWrapper>
      <UserInfoWrapper>
        <UsernameText onClick={() => handleUsernamePress(follower.id)}>
          {follower.username}
        </UsernameText>
        <NameText>{formatUserFamilyName(follower)}</NameText>
      </UserInfoWrapper>
      <ButtonWrapper>
        {user.id === profileId ? (
          <Button onClick={() => handleRemoveFollower(user.id, follower.id)}>
            {type === MODAL_TYPE.FOLLOWERS ? <>Remove</> : <>UnFollow</>}
          </Button>
        ) : null}
      </ButtonWrapper>
    </FollowStyle>
  );
};

export default Follow;

const FollowStyle = styled.div`
  width: 100%;
  height: 46px;
  display: flex;
  align-items: center;
  padding: 0.5rem;
`;

const ImageWrapper = styled.div`
  width: 30px;
  aspect-ratio: 1;
  margin: 0 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const Image = styled.img`
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
`;

const UserInfoWrapper = styled.div`
  flex: 1;
`;

const UsernameText = styled.p`
  width: fit-content;
  font-weight: 400;
  font-size: 0.9rem;
  text-transform: lowercase;
  cursor: pointer;
  &:hover {
    text-decoration: underline;
  }
`;

const NameText = styled.div`
  font-size: 0.9rem;
  font-weight: 300;
  opacity: 0.7;
`;

const ButtonWrapper = styled.div`
  width: 20%;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const Button = styled.div`
  border: 1px solid rgb(219, 219, 219);
  border-radius: 3px;
  padding: 5px 9px;
  font-size: 0.9rem;
  font-weight: 500;
  color: rgb(38, 38, 38);
  cursor: pointer;
`;
