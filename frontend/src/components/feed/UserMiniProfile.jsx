// React
import { useNavigate } from "react-router-dom";
// Recoil
import { useRecoilState } from "recoil";
import { atomUser } from "../../atom/atomStates";
// Styles
import styled from "styled-components";

const UserMiniProfile = () => {
  const [user, setUser] = useRecoilState(atomUser);
  const navigate = useNavigate();

  const handleProfilePress = (userid) => {
    navigate(`/profile/${userid}`);
  };

  const handleLogOut = () => {
    localStorage.removeItem("user");
    setUser(null);
    navigate("/login");
  };

  return (
    <MiniProfileStyle>
      <img src={user?.profilePic} alt='profile' />
      <TextInfo onClick={() => handleProfilePress(user.id)}>
        <p>
          <span>{user?.username}</span>
        </p>
        <TextFamilyName>
          {user?.firstName} {user?.firstName}
        </TextFamilyName>
      </TextInfo>
      <div>
        <button onClick={handleLogOut}>Log out</button>
      </div>
    </MiniProfileStyle>
  );
};

export default UserMiniProfile;

const MiniProfileStyle = styled.div`
  height: 65px;
  width: 100%;
  padding: 0rem 1rem;
  margin: auto;
  display: flex;
  align-items: center;
  cursor: pointer;

  img {
    height: 45px;
    width: 45px;
    object-fit: cover;
    border-radius: 9999px;
  }
  p {
    margin-left: 1rem;
    max-width: 125px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    span {
      &:hover {
        text-decoration: underline;
      }
    }
  }
  div {
    button {
      color: #0095f6;
    }
  }
`;

const TextInfo = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;

  > :first-child {
    font-weight: 400;
    font-size: 0.8rem;
  }

  > :last-child {
    font-weight: 200;
    font-size: 0.8rem;
  }
`;

const TextFamilyName = styled.p`
  color: #808080;
`;
