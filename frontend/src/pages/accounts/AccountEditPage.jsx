// React
import { useEffect, useState } from "react";
// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../atom/atomStates";
// Styles
import styled from "styled-components";
import { AccountPageWrapper, InformationSection } from "../../styles/layout";
// Components
import AccountNavMenu from "../../components/accounts/AccountNavMenu";
import EditAccountForm from "../../components/forms/EditAccountForm";
import FileInputField from "../../components/forms/FileInputField";

const AccountPage = () => {
  const user = useRecoilValue(atomUser);
  const [fileInput, setFileInput] = useState();

  const handleFileSelect = (e) => {
    setFileInput(e.target.files[0]);
  };

  return (
    <AccountPageWrapper>
      <AccountNavMenu />
      <InformationSection>
        <HeaderStyle>
          <ProfileImage src={user.profilePic} alt={user.username} />
          <TextWrapper>
            <UsernameText>{user.username}</UsernameText>
            <FileInputField
              fileLabel='Change Profile Pick'
              style={fileInputStyle}
              handleFileSelect={handleFileSelect}
              ref={(ref) => {
                setFileInput(ref);
              }}
            />
          </TextWrapper>
        </HeaderStyle>
        <EditAccountForm file={fileInput} />
      </InformationSection>
    </AccountPageWrapper>
  );
};

export default AccountPage;

const HeaderStyle = styled.div`
  display: flex;
  align-items: center;
  margin-top: 2rem;
  margin-bottom: 1rem;
  img {
    margin-left: 6rem;
  }
`;

const TextWrapper = styled.div`
  display: flex;
  flex-direction: column;
`;

const UsernameText = styled.p`
  font-size: 1.4rem;
  font-weight: 400;
  padding-left: 1.8rem;
`;

const ProfileImage = styled.img`
  width: 40px;
  aspect-ratio: 1;
  border-radius: 9999px;
  object-fit: cover;
`;

const fileInputStyle = {
  backgroundColor: "transparent",
  color: "#3897f0",
  cursor: "pointer",
  padding: "0",
  paddingLeft: "1.8rem",
  margin: "0",
  fontSize: "0.9rem",
  fontWeight: "500",
};
