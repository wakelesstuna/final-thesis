// React
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
// GraphQL
import { useQuery } from "@apollo/client";
import {
  fetchUserFollowers_gql,
  fetchUserFollowing_gql,
} from "../../graphql/query";
// Styles
import styled from "styled-components";
// Constants
import { MODAL_TYPE } from "../../constants";
// Icons
import { IoClose } from "react-icons/io5";
// Components
import FollowList from "./content/FollowList";
import ErrorSwalMessage from "../util/ErrorSwalMessage";

const FollowersModal = ({ type, closeModal }) => {
  const { profileId } = useParams();
  const [followList, setFollowList] = useState();

  let fetchMethod;

  if (type === MODAL_TYPE.FOLLOWERS) {
    fetchMethod = fetchUserFollowers_gql;
  }

  if (type === MODAL_TYPE.FOLLOWING) {
    fetchMethod = fetchUserFollowing_gql;
  }

  const { data, loading, error, refetch } = useQuery(fetchMethod, {
    variables: {
      userId: profileId,
    },
  });

  if (error) return <ErrorSwalMessage error={error} />;

  useEffect(() => {
    if (type === MODAL_TYPE.FOLLOWERS) {
      setFollowList(data?.user?.followers);
    }

    if (type === MODAL_TYPE.FOLLOWING) {
      setFollowList(data?.user.following);
    }
  }, [data]);

  return (
    <div>
      <ModalHeader>
        <Spaceing></Spaceing>
        <Title>{type}</Title>
        <CloseButton>
          <IoClose onClick={closeModal} />
        </CloseButton>
      </ModalHeader>
      {followList && (
        <FollowList
          listOfUsers={followList}
          refetchList={refetch}
          type={type}
          closeModal={closeModal}
        />
      )}
    </div>
  );
};

export default FollowersModal;

const ModalHeader = styled.div`
  display: flex;
  align-items: center;
  height: 45px;
  border-bottom: 1px solid rgb(219, 219, 219);
`;

const Spaceing = styled.div`
  width: 50px;
`;

const Title = styled.div`
  flex: 1;
  text-align: center;
  text-transform: lowercase;
`;

const CloseButton = styled.div`
  width: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  svg {
    cursor: pointer;
    font-size: 2rem;
  }
`;
