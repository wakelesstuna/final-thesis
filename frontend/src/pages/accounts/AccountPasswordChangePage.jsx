// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../atom/atomStates";
// Styles
import styled from "styled-components";
import { AccountPageWrapper, InformationSection } from "../../styles/layout";
// Compnents
import AccountNavMenu from "../../components/accounts/AccountNavMenu";
import ChangePasswordForm from "../../components/forms/ChangePasswordForm";

const AccountPasswordChangePage = () => {
  const user = useRecoilValue(atomUser);
  return (
    <AccountPageWrapper>
      <AccountNavMenu />
      <InformationSection>
        <HeaderStyle>
          <ProfileImage src={user.profilePic} alt={user.username} />
          <p>{user.username}</p>
        </HeaderStyle>
        <ChangePasswordForm />
      </InformationSection>
    </AccountPageWrapper>
  );
};

export default AccountPasswordChangePage;

const HeaderStyle = styled.div`
  display: flex;
  align-items: center;
  margin-top: 2rem;
  margin-bottom: 1rem;
  img {
    margin-left: 6rem;
  }
  p {
    font-size: 1.4rem;
    font-weight: 400;
    padding-left: 1.8rem;
  }
`;

const ProfileImage = styled.img`
  width: 40px;
  aspect-ratio: 1;
  border-radius: 9999px;
  object-fit: cover;
`;
