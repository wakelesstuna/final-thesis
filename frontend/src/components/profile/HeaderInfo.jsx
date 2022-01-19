// Styles
import styled from "styled-components";
// Components
import HeaderCount from "./HeaderCount";

const HeaderInfo = ({ user }) => {
  console.log("user: ", { user });
  return (
    <HeaderInfoStyle>
      <div>
        <h3>{user.username}</h3>
        <p>{user.description}</p>
      </div>
      <HeadCountStyle>
        <HeaderCount count={user.totalPosts} title='posts' />
        <HeaderCount count={user.totalFollowers} title='followers' />
        <HeaderCount count={user.totalFollowing} title='following' />
      </HeadCountStyle>
    </HeaderInfoStyle>
  );
};

export default HeaderInfo;

const HeaderInfoStyle = styled.div`
  display: flex;
  flex-direction: column;
  div {
    h3 {
      font-weight: lighter;
      font-size: 2rem;
      letter-spacing: 0.5px;
    }
  }
  > :not(:first-child) {
    margin-top: 2rem;
  }
`;

const HeadCountStyle = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
`;
