// React
import { useNavigate } from "react-router-dom";
// Recoil
import { useSetRecoilState } from "recoil";
import { atomUser } from "../../atom/atomStates";
// Styles
import styled from "styled-components";
// GraphQL
import { useMutation } from "@apollo/client";
import { authUser_gql } from "../../graphql/mutation";
// Components
import ErrorSwalMessage from "../util/ErrorSwalMessage";
import LoadingDots from "../util/LoadingDots";
import TextField from "./TextField";
// Icons
import { FaFacebookSquare } from "react-icons/fa";
// Util
import { formatPhoneNumber } from "./validation";

const SignInForm = () => {
  const [authUserMutation, { loading, error }] = useMutation(authUser_gql);
  const setUser = useSetRecoilState(atomUser);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await authUserMutation({
        variables: {
          authUserInput: {
            username: formatPhoneNumber(e.target.username.value),
            password: e.target.password.value,
          },
        },
      });
      setUserAndNaviageToHomePage(response.data.authUser);
    } catch (e) {
      <ErrorSwalMessage error={e} />;
    }
  };

  const setUserAndNaviageToHomePage = (user) => {
    localStorage.setItem("user", JSON.stringify(user));
    setUser(() => user);
    navigate("/");
  };

  const handleSignUp = () => {
    navigate("/sign-up");
  };

  const handleForgotPassword = () => {
    alert("No implemented yet!");
  };
  const handleLoginWithFacebook = () => {
    alert("No implemented yet!");
  };

  if (loading) return <LoadingDots />;

  return (
    <>
      <ContainerStyle>
        <BoxStyle>
          <HeadingStyle></HeadingStyle>
          <FormStyle onSubmit={handleSubmit}>
            <TextField
              label='Phone number, username, or email'
              id='username'
              type='text'
            />
            <TextField label='Password' id='password' type='password' />
            <ButtonStyle title='login'>Log In</ButtonStyle>
            <Seperator>
              <Line></Line>
              <p>OR</p>
              <Line></Line>
            </Seperator>
            <Other>
              <FbLoginBtn type='button' onClick={handleLoginWithFacebook}>
                <FaFacebookSquare />
                <span>Log in with Facebook</span>
              </FbLoginBtn>
              <ForgotPassword onClick={handleForgotPassword}>
                Forgot password?
              </ForgotPassword>
            </Other>
          </FormStyle>
        </BoxStyle>
        <BoxStyle>
          <AccountText>
            Don't have an account?{" "}
            <SignUp onClick={handleSignUp}>Sign Up</SignUp>
            {error && <ErrorSwalMessage error={error} />}
          </AccountText>
        </BoxStyle>
      </ContainerStyle>
    </>
  );
};

export default SignInForm;

const ContainerStyle = styled.div`
  max-width: 1000px;
  margin: 0 auto;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  margin-top: 3rem;
  font-size: 14px;
`;

const BoxStyle = styled.div`
  max-width: 350px;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  background-color: #ffff;
  border: 1px solid #e6e6e6;
  border-radius: 1px;
  margin: 0 0 10px;
  padding: 10px 0;
  flex-grow: 1;
`;

const HeadingStyle = styled.div`
  margin: 22px auto 12px;
  background-image: url("https://www.instagram.com/static/bundles/es6/sprite_core_b20f2a3cd7e4.png/b20f2a3cd7e4.png");
  background-position: -98px 0;
  min-height: 51px;
  max-height: 51px;
  width: 177px;
  overflow: hidden;
`;

const FormStyle = styled.form`
  width: 250px;
  margin: auto;
`;

const Seperator = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #999;
  margin-top: 6px;
`;

const Line = styled.div`
  height: 1px;
  width: 40%;
  background-color: #dbdbdb;
`;

const ButtonStyle = styled.button`
  text-align: center;
  width: 100%;
  padding: 0.5rem;
  border: 1px solid transparent;
  background-color: #3897f0;
  color: #fff;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
`;

const Other = styled.div`
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
`;

const FbLoginBtn = styled.button`
  margin: 1rem;
  border: 0;
  cursor: pointer;
  font-size: 14px;
  color: #385185;
  font-weight: 600;
  background: transparent;
  display: flex;
  align-items: center;
  svg {
    margin-right: 10px;
  }
`;

const ForgotPassword = styled.p`
  font-size: 11px;
  color: #003569;
  cursor: pointer;
`;

const AccountText = styled.p`
  padding: 1rem 0rem;
`;

const SignUp = styled.a`
  color: #3897f0;
  font-weight: 400;
  cursor: pointer;
`;
