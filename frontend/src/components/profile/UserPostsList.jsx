// GraphQl
import { useQuery } from "@apollo/client";
import { getPostsOfUser_gql } from "../../graphql/query";
// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../atom/atomStates";
// Components
import DiscoverImagesTwo from "../discoverImages/DiscoverImagesTwo";
import ErrorSwalMessage from "../util/ErrorSwalMessage";
import LoadingDots from "../util/LoadingDots";

const UserPostsList = ({ profileId }) => {
  const { data, loading, error } = useQuery(getPostsOfUser_gql, {
    variables: {
      id: profileId,
    },
  });

  if (loading) return <LoadingDots />;
  if (error) return <ErrorSwalMessage error={error} />;

  return <>{data && <DiscoverImagesTwo posts={data.user.posts} />}</>;
};

export default UserPostsList;
