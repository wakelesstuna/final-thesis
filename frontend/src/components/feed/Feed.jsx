// GraphQL
import { useQuery } from "@apollo/client";
import { getPosts_gql } from "../../graphql/query";
// Styles
import styled from "styled-components";
// Constants
import { BREAKPOINTS } from "../../constants";
// Icons
import { BiCopyright } from "react-icons/bi";
import { AiOutlineGithub } from "react-icons/ai";
// Components
import UserMiniProfile from "./UserMiniProfile";
import ErrorSwalMessage from "../util/ErrorSwalMessage";
import LoadingDots from "../util/LoadingDots";
import PostsList from "./post/PostsList";
import Stories from "./story/Stories";
import Suggestions from "./Suggestions";
// Util
import useWindowSize from "../../hooks/useWindowSize";
import { handleGitHub } from "../util/utilFunctions";

const Feed = () => {
  const { width } = useWindowSize();

  const { data, error, loading, fetchMore } = useQuery(getPosts_gql, {
    variables: {
      first: 1,
      after: null,
    },
  });

  if (error) return <ErrorSwalMessage error={error} />;
  if (loading || !data) return <LoadingDots />;

  const onScroll = (e) => {
    if (
      Math.trunc(e.target.scrollTop) + e.target.clientHeight ===
      e.target.scrollHeight
    ) {
      console.log("In scroll");
      setTimeout(() => {
        fetchMorePosts();
      }, 300);
    }
  };

  const fetchMorePosts = () => {
    const { endCursor, hasNextPage } = data.paginationPosts.pageInfo;
    if (hasNextPage === false) {
      console.log("No more posts to fetch");
      return;
    }
    fetchMore({
      variables: {
        first: 1,
        after: endCursor,
      },
      updateQuery: (prevResult, { fetchMoreResult }) => {
        fetchMoreResult.paginationPosts.edges = [
          ...prevResult.paginationPosts.edges,
          ...fetchMoreResult.paginationPosts.edges,
        ];
        return fetchMoreResult;
      },
    });
  };

  return (
    <MainStyle width={width}>
      <section>
        <List onScroll={onScroll}>
          <Stories />
          <li>{data && <PostsList posts={data.paginationPosts.edges} />}</li>
          <Margin></Margin>
        </List>
      </section>
      {width > BREAKPOINTS.XL ? (
        <section>
          <SideMenuStyle>
            <UserMiniProfile />
            <Suggestions />

            <LinkStyles>
              About • Help • Press • API • Jobs • Privacy • Terms • Locations
              •Top •Accounts •Hashtags • Language • English
            </LinkStyles>
            <CopyRightWrapper>
              <BiCopyright />
              <p>Copyright 2021 Oscar Forss Final Thesis Instagram Clone </p>
              <GitHubIcon onClick={handleGitHub} />
            </CopyRightWrapper>
          </SideMenuStyle>
        </section>
      ) : null}
    </MainStyle>
  );
};

export default Feed;

const MainStyle = styled.main`
  max-width: 1024px;
  display: flex;
  margin: auto;

  > :first-child {
    width: ${(props) => (props.width > BREAKPOINTS.XL ? "70%" : "825px")};
    margin: ${(props) => (props.width > BREAKPOINTS.XL ? "" : "auto")};
    padding-right: ${(props) => (props.width > BREAKPOINTS.XL ? "2rem" : "")};
  }
  > :last-child {
    width: ${(props) => (props.width > BREAKPOINTS.XL ? "30%" : "825px")};
    position: relative;
  }
`;

const SideMenuStyle = styled.div`
  position: fixed;
  width: 250px;
  height: 400px;
`;

const List = styled.ul`
  height: 92vh;
  max-height: 92vh;
  overflow-y: scroll;
  max-width: 600px;
  margin: auto;
  transform: translateY(-30px);
  padding-top: 30px;

  ::-webkit-scrollbar {
    display: none;
  }

  // To show scroll bar uncomment this
  /* ::-webkit-scrollbar {
    
    width: 12px;
  }

  ::-webkit-scrollbar-track {
    box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
    -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
    border-radius: 10px;
  }

  ::-webkit-scrollbar-thumb {
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.5);
    box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.5);
  } */
`;

const Margin = styled.div`
  min-height: 200px;
`;

const LinkStyles = styled.p`
  margin-top: 1rem;
  color: #d6d6d6;
  font-size: 0.7rem;
  font-weight: 300;
  cursor: pointer;
`;

const CopyRightWrapper = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 1rem;
  color: #d6d6d6;
  font-size: 0.7rem;
  font-weight: 300;
  cursor: pointer;
`;

const GitHubIcon = styled(AiOutlineGithub)`
  margin-left: 1rem;
  font-size: 2rem;
  cursor: pointer;
`;
