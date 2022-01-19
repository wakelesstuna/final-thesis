// GRaphQL
import { useMutation, useQuery } from "@apollo/client";
import { likePost_gql, unLikePost_gql } from "../../../../graphql/mutation";
import { isPostLiked_gql } from "../../../../graphql/query";
// Icons
import { BsHeart, BsHeartFill } from "react-icons/bs";
// Components
import ErrorSwalMessage from "../../../util/ErrorSwalMessage";

const LikeSymbol = ({ userId, postId, setTotalLikes }) => {
  const [likePostMutation] = useMutation(likePost_gql);
  const [unLikePostMutation] = useMutation(unLikePost_gql);
  const LIKED = "liked";
  const NOT_LIKED = "notLiked";

  const { data, loading, error, refetch } = useQuery(isPostLiked_gql, {
    variables: {
      likeInput: {
        userId,
        postId,
      },
    },
  });

  if (loading || !data) return null;
  if (error) return <ErrorSwalMessage error={error} />;

  const handleLike = async (e, userId, postId) => {
    if (e === NOT_LIKED) {
      try {
        await likePostMutation({
          variables: {
            likeInput: {
              userId,
              postId,
            },
          },
        });
        setTotalLikes((prevState) => prevState + 1);
      } catch (e) {
        alert(e.message);
      }
    }
    if (e === LIKED) {
      try {
        await unLikePostMutation({
          variables: {
            likeInput: {
              userId,
              postId,
            },
          },
        });
        setTotalLikes((prevState) => prevState - 1);
      } catch (e) {
        alert({ e });
      }
    }
    refetch();
  };

  return (
    <div>
      {data && data.isPostLiked ? (
        <BsHeartFill
          id='liked'
          style={{ color: "#ed4956" }}
          onClick={() => handleLike(LIKED, userId, postId)}
        />
      ) : (
        <BsHeart
          id='notLiked'
          onClick={() => handleLike(NOT_LIKED, userId, postId)}
        />
      )}
    </div>
  );
};

export default LikeSymbol;
