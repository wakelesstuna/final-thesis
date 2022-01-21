// Styles
import styled from "styled-components";
// Components
import ImageFades from "../components/util/ImageFades";
import Footer from "../components/footer/Footer";
import SignInForm from "../components/forms/SignInForm";

import { BsFillHouseDoorFill } from "react-icons/bs";

const LoginPage = () => {
  return (
    <div>
      <Container>
        <PhoneContainer>
          <PhoneImageContainer>
            <ImageFades />
          </PhoneImageContainer>
          <Phones />
        </PhoneContainer>
        <div>
          <SignInForm />
        </div>
      </Container>
      <Footer />
    </div>
  );
};

export default LoginPage;

const Container = styled.div`
  width: 800px;
  margin: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 3rem;
  div {
    flex: 1;
  }
`;

const PhoneContainer = styled.div`
  position: relative;
`;

const Phones = styled.div`
  background-image: url("https://www.instagram.com/static/images/homepage/home-phones.png/43cc71bb1b43.png");
  background-position: 0 0;
  background-repeat: no-repeat;
  background-size: 454px 618px;
  flex-basis: 454px;
  height: 618px;
  margin-left: -35px;
  margin-right: -15px;
`;

const PhoneImageContainer = styled.div`
  position: absolute;
  left: 116px;
  top: 100px;
`;
