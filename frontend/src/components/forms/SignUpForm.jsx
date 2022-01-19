// React
import { useState } from "react";
import { useNavigate } from "react-router-dom";
// GraphQL
import { useMutation, useQuery } from "@apollo/client";
import { createUserMutation_gql } from "../../graphql/mutation";
// Styles
import styled from "styled-components";
import { ButtonStyle } from "../../styles/layout";
// Components
import TextField from "./TextField";
// Validation
import {
  validateUsername,
  validateEmail,
  formatPhoneNumber,
  validatePassword,
  validatePhone,
} from "./validation";
// SweetAlert
import withReactContent from "sweetalert2-react-content";
import Swal from "sweetalert2";

const SignUpForm = () => {
  const [isSubmitting, setIsSubmiting] = useState(false);
  const [fileLabel, setFileLabel] = useState("Choose a profile pic");
  const [fileInput, setFileInput] = useState();
  const MySwal = withReactContent(Swal);
  const naviagte = useNavigate();
  const [createUserMutation] = useMutation(createUserMutation_gql);

  const handleFileSelect = () => {
    const file = fileInput.files[0];
    setFileLabel(file.name);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmiting(true);
    const formData = Object.fromEntries(new FormData(e.target).entries());
    try {
      await createUserMutation({
        variables: {
          input: fileInput.files[0],
          createUserInput: {
            username: formData.username,
            firstName: formData.firstName,
            lastName: formData.lastName,
            phone: formatPhoneNumber(formData.phone),
            email: formData.email,
            password: formData.password,
          },
        },
      });

      await MySwal.fire({
        title: <strong>User created</strong>,
        html: <i>You can now login with your username and password!</i>,
        icon: "success",
      });

      setIsSubmiting(false);
      form.reset();
      naviagte("/login");
    } catch (e) {
      console.log({ e });
      MySwal.fire({
        title: <strong>Error!</strong>,
        html: <i>{e.message}</i>,
        icon: "error",
      });
      setIsSubmiting(false);
    }
  };

  const handleFakeLinkClick = () => {
    alert("This is just a fake link goes nowhere!");
  };

  return (
    <ContainerStyle>
      <BoxStyle>
        <HeadingStyle></HeadingStyle>
        <FormStyle onSubmit={(e) => handleSubmit(e)}>
          <TextField
            id='username'
            label='Username'
            type='text'
            isRequired={true}
            validate={validateUsername}
          />
          <TextField
            id='firstName'
            label='First name'
            type='text'
            isRequired={false}
          />
          <TextField
            id='lastName'
            label='Last name'
            type='text'
            isRequired={false}
          />
          <TextField
            id='phone'
            label='Phone number'
            type='text'
            isRequired={false}
            validate={validatePhone}
          />
          <TextField
            id='email'
            label='Email'
            type='email'
            isRequired={true}
            validate={validateEmail}
          />
          <TextField
            id='password'
            label='Password'
            type='password'
            isRequired={true}
            validate={validatePassword}
          />
          <FileInput
            id='file'
            name='file'
            type='file'
            onChange={(e) => handleFileSelect(e)}
            ref={(ref) => {
              setFileInput(ref);
            }}
          />
          <FileLabel htmlFor='file'>{fileLabel}</FileLabel>
          <ButtonStyle type='submit' disabled={isSubmitting}>
            Submit
          </ButtonStyle>
        </FormStyle>
        <TermsInfo>
          <p>
            By signing up, you agree to our{" "}
            <span onClick={handleFakeLinkClick}>Terms</span>. Learn how we
            collect, use and share your data in our{" "}
            <span onClick={handleFakeLinkClick}>Data</span> Policy and how we
            use cookies and similar technology in our{" "}
            <span onClick={handleFakeLinkClick}>Cookies Policy</span>.
          </p>
        </TermsInfo>
      </BoxStyle>
      <BoxStyle>
        <BottomBox>
          <p>
            Have an account?{" "}
            <LogIn onClick={() => naviagte("/login")}>Log in</LogIn>
          </p>
        </BottomBox>
      </BoxStyle>
    </ContainerStyle>
  );
};

export default SignUpForm;

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

const HeadingStyle = styled.div`
  margin: 22px auto 12px;
  background-image: url("https://www.instagram.com/static/bundles/es6/sprite_core_b20f2a3cd7e4.png/b20f2a3cd7e4.png");
  background-position: -98px 0;
  min-height: 51px;
  max-height: 51px;
  width: 177px;
  overflow: hidden;
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

const FormStyle = styled.form`
  max-width: 100%;
  padding: 0 2rem;
  margin: auto;

  & button:disabled,
  button[disabled] {
    opacity: 0.3;
  }
`;

const FileLabel = styled.label`
  font-size: 1.125em;
  font-weight: 300;
  color: white;
  background-color: #3897f0;
  padding: 0.5rem 1rem;
  border-radius: 3px;
  cursor: pointer;
  margin-bottom: 1rem;
  display: inline-block;

  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  &:hover {
    opacity: 0.7;
  }
`;

const FileInput = styled.input`
  width: 0.1px;
  height: 0.1px;
  opacity: 0;
  overflow: hidden;
  position: absolute;
  z-index: -1;
`;

const TermsInfo = styled.div`
  max-width: 100%;
  padding: 0 3.3rem;
  margin: 1rem auto;
  font-weight: 300;
  font-size: 12px;
  text-align: center;
  color: #999;
  span {
    font-weight: 500;
    &:hover {
      cursor: pointer;
    }
  }
`;

const LogIn = styled.a`
  color: #3897f0;
  font-weight: 400;
  cursor: pointer;
`;

const BottomBox = styled.div`
  padding: 1rem 0rem;
  p {
    font-weight: 300;
  }
`;
