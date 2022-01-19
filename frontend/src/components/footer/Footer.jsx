// Icons
import { BiCopyright } from "react-icons/bi";
import { AiOutlineGithub } from "react-icons/ai";
// Styles
import styled from "styled-components";

const Footer = () => {
  const handleGitHub = () => {};

  return (
    <Container>
      <InfoChoice className='flex space-x-4 flex-wrap text-sm text-gray-400 text-center justify-center mx-[10%] m-auto mt-16 childsCursorPointer childsHoverUnderline'>
        <p className='ml-4'>About</p>
        <p>Blog</p>
        <p>Jobs</p>
        <p>Help</p>
        <p>API</p>
        <p>Privacy</p>
        <p>Terms</p>
        <p>TopAccounts</p>
        <p>HashTags</p>
        <p>Locations</p>
        <p>IntagramLite</p>
        <p>Beauty</p>
        <p>Dance</p>
        <p>Fitness</p>
        <p>Food & Drink</p>
        <p>Home & Garden</p>
        <p>Music</p>
        <p>Visual Atrs</p>
      </InfoChoice>
      <CopyRightContainer>
        <BiCopyright />
        <p>Copyright 2021 Oscar Forss Master Thesis Instagram Clone </p>
        <GitHubIcon onClick={handleGitHub} />
      </CopyRightContainer>
    </Container>
  );
};

export default Footer;

const Container = styled.footer`
  max-width: 1024px;
`;

const InfoChoice = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  font-weight: 300;
  text-align: center;
  padding: 0 7rem;
  margin: auto;

  p {
    font-size: 0.75rem;
    color: #999;
    padding: 8px 12px;
    cursor: pointer;
    &:hover {
      text-decoration: underline;
    }
  }
`;

const CopyRightContainer = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 2rem;
  color: #999;

  svg {
    height: 2rem;
  }
  p {
    padding-left: 5px;
    align-self: center;
  }
`;

const GitHubIcon = styled(AiOutlineGithub)`
  margin-left: 1rem;
  cursor: pointer;
`;
