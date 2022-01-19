// Styles
import styled from "styled-components";
// GraphQl
import { useQuery } from "@apollo/client";
import { getPostById_gql } from "../../graphql/query";
// Components
import PostContent from "../feed/post/PostContent";
import PostFooter from "../feed/post/PostFooter";
import PostHeader from "../feed/post/PostHeader";
import Comment from "../feed/post/footer/Comment";
import ErrorSwalMessage from "../util/ErrorSwalMessage";

const PostModal = ({ obj: post }) => {
  const { loading, error, data, refetch } = useQuery(getPostById_gql, {
    variables: {
      postId: post.id,
    },
  });

  if (loading) return <div>Loading</div>;
  if (error) return <ErrorSwalMessage error={error} />;

  return (
    <PostModalStyle>
      {data.post && (
        <>
          <PostContent image={data.post.imageUrl} />
          <SidePanelStyle>
            <PostHeader
              user={data.post.user}
              style={{ borderBottom: "1px solid #adadadc5" }}
            />
            <Comments>
              {data.post.comments.map((c) => (
                <Comment
                  key={c.id}
                  comment={c}
                  post={post}
                  refetchComments={refetch}
                />
              ))}
            </Comments>
            <PostFooter
              post={data.post}
              owner={data.post.user}
              content={data.post.caption}
              comments={data.post.comments}
              showAll={false}
              style={{ borderTop: "1px solid #adadadc5" }}
            />
          </SidePanelStyle>
        </>
      )}
    </PostModalStyle>
  );
};

export default PostModal;

const PostModalStyle = styled.div`
  display: flex;
  overflow: hidden;
  min-height: 500px;
  max-height: 80%;
`;

const SidePanelStyle = styled.div`
  min-width: 300px;
  max-width: 300px;
  display: flex;
  flex-direction: column;
`;

const Comments = styled.div`
  flex: 1;
  max-height: 350px;
  overflow-y: scroll;
  ::-webkit-scrollbar {
    width: 0; /* Remove scrollbar space */
    background: transparent; /* Optional: just make scrollbar invisible */
  }
`;
