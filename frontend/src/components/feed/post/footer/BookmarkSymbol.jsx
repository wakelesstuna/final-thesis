// GraphQL
import { useMutation, useQuery } from "@apollo/client";
import { isBookmarked_gql } from "../../../../graphql/query";
import {
  bookmarkPost_gql,
  unbookmarkPost_gql,
} from "../../../../graphql/mutation";
// Icons
import { BsBookmark, BsBookmarkFill } from "react-icons/bs";
// Components
import ErrorSwalMessage from "../../../util/ErrorSwalMessage";

const BookmarkSymbol = ({ postId, userId }) => {
  const [bookmarkPostMutation] = useMutation(bookmarkPost_gql);
  const [unBookmarkPostMutation] = useMutation(unbookmarkPost_gql);
  const BOOKMARKED = "bookmarked";
  const NOT_BOOKMARKED = "notBookmarked";

  const { data, loading, error, refetch } = useQuery(isBookmarked_gql, {
    variables: {
      bookmarkInput: {
        userId,
        postId,
      },
    },
  });

  const handleBookmark = async (e, userId, postId) => {
    if (e === NOT_BOOKMARKED) {
      try {
        await bookmarkPostMutation({
          variables: {
            bookmarkInput: {
              userId,
              postId,
            },
          },
        });
      } catch (e) {
        alert(e.message);
      }
    }
    if (e === BOOKMARKED) {
      try {
        await unBookmarkPostMutation({
          variables: {
            bookmarkInput: {
              userId,
              postId,
            },
          },
        });
      } catch (e) {
        alert({ e });
      }
    }
    refetch();
  };

  if (loading || !data) return null;
  if (error) return <ErrorSwalMessage error={error} />;

  return (
    <div>
      {data.isBookmarked ? (
        <BsBookmarkFill
          style={{ color: "#262626" }}
          onClick={() => handleBookmark(BOOKMARKED, userId, postId)}
        />
      ) : (
        <BsBookmark
          onClick={() => handleBookmark(NOT_BOOKMARKED, userId, postId)}
        />
      )}
    </div>
  );
};

export default BookmarkSymbol;
