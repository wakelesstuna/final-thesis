// Styles
import { useNavigate } from "react-router-dom";
import styled from "styled-components";

const Content = ({ user, content }) => {
  const navigate = useNavigate();

  const handleUsernameClick = (userId) => {
    navigate(`profile/${userId}`);
  };

  return (
    <ContentStyle>
      <p>
        <span onClick={() => handleUsernameClick(user.id)}>
          {user.username}
        </span>
        <span>&nbsp;</span>
        {content}
      </p>
    </ContentStyle>
  );
};

export default Content;

const ContentStyle = styled.div`
  width: 100%;
  padding: 1rem;
  font-size: 0.9rem;
  span {
    font-weight: 500;
    cursor: pointer;
    :hover {
      text-decoration: underline;
    }
  }
  p {
    font-weight: 300;
  }
`;
