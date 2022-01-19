// GraphQL
import { useQuery } from "@apollo/client";
import { getCommentsOfPostById_gql } from "../../../../graphql/query";
// Components
import ErrorSwalMessage from "../../../util/ErrorSwalMessage";
import AddComment from "./AddComment";
import Comments from "./Comments";

const CommentSection = ({ post, showAll }) => {
  const { loading, error, data, refetch } = useQuery(
    getCommentsOfPostById_gql,
    {
      variables: {
        postId: post.id,
      },
    }
  );

  if (loading) return <div>Loading....</div>;
  if (error) return <ErrorSwalMessage error={error} />;

  return (
    <>
      {data && (
        <Comments
          post={post}
          comments={data.comments}
          showAll={showAll}
          postId={post.id}
        />
      )}
      <AddComment postId={post.id} refetchComments={refetch} />
    </>
  );
};

export default CommentSection;
