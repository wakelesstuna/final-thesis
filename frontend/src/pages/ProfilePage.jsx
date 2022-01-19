// React
import { useLocation, useParams } from "react-router-dom";
// GraphQL
import { useQuery } from "@apollo/client";
import { getUserById_gql } from "../graphql/query";
// Styles
import styled from "styled-components";
// Components
import ProfileContent from "../components/profile/ProfileContent";
import ProfileHeader from "../components/profile/ProfileHeader";
import LoadingDots from "../components/util/LoadingDots";

const ProfilePage = () => {
  let { profileId } = useParams();
  const { pathname } = useLocation();
  const currentPath = pathname.substring(pathname.lastIndexOf("/") + 1);

  const { loading, error, data, refetch } = useQuery(getUserById_gql, {
    variables: { id: profileId },
  });

  let errorMsg;
  if (error) {
    if (error.networkError) {
      errorMsg = "A network error occurred.";
    } else {
      errorMsg = `An error occurred. \n ${error.message}`;
    }
  }

  return (
    <ProfilePageStyle>
      {loading ? (
        <LoadingDots />
      ) : error ? (
        <p>{errorMsg}</p>
      ) : data ? (
        <>
          <div>
            <ProfileHeader user={data.user} />
          </div>
          <div>
            <ProfileContent
              currentPath={currentPath}
              profileId={profileId} /* posts={data.user.posts} */
            />
          </div>{" "}
        </>
      ) : null}
    </ProfilePageStyle>
  );
};

export default ProfilePage;

const ProfilePageStyle = styled.div`
  display: flex;
  flex-direction: column;
  > :last-child {
    border-top: 1px solid #adadadc5;
  }
`;
