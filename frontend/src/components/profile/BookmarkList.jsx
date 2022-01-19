// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../atom/atomStates";
// GraphQl
import { useQuery } from "@apollo/client";
import { fetchBookmarksQuery_gql } from "../../graphql/query";
// Components
import DiscoverImagesTwo from "../discoverImages/DiscoverImagesTwo";
import ErrorSwalMessage from "../util/ErrorSwalMessage";
import LoadingDots from "../util/LoadingDots";

const BookmarkList = () => {
  const { id } = useRecoilValue(atomUser);

  const { data, loading, error } = useQuery(fetchBookmarksQuery_gql, {
    variables: {
      id,
    },
  });

  if (loading) return <LoadingDots />;
  if (error) return <ErrorSwalMessage error={error} />;

  return (
    <>
      <ul>{data && <DiscoverImagesTwo posts={data.user.bookmarks} />}</ul>
    </>
  );
};

export default BookmarkList;
