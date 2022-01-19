// Styles
import styled from "styled-components";
// Components
import HeaderInfo from "./HeaderInfo";

const ProfileHeader = ({ user }) => {
  return (
    <ProfileHeaderStyle>
      <ImageWrapper>
        <img src={user.profilePic} alt='profile' />
      </ImageWrapper>
      <DescriptionWrapper>
        <HeaderInfo user={user} />
      </DescriptionWrapper>
    </ProfileHeaderStyle>
  );
};

export default ProfileHeader;

const ProfileHeaderStyle = styled.div`
  display: flex;
  padding: 3rem 5rem;
`;

const ImageWrapper = styled.div`
  width: 40%;
  display: flex;
  align-items: center;
  justify-content: center;
  img {
    width: 200px;
    height: 200px;
    object-fit: cover;
    border-radius: 9999px;
  }
`;

const DescriptionWrapper = styled.div`
  width: 60%;
  padding-top: 1rem;
  padding-left: 2rem;
`;
