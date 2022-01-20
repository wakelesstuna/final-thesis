// React
import { useState } from "react";
// GraphQL
import { useMutation } from "@apollo/client";
import { updateUser_gql } from "../../graphql/mutation";
// Recoil
import { useRecoilState } from "recoil";
import { atomUser } from "../../atom/atomStates";
// Styles
import styled from "styled-components";
import { ButtonStyle } from "../../styles/layout";
// Utils
import { uploadImage } from "../util/utilFunctions";

const EditAccountForm = ({ file }) => {
  const [user, setUser] = useRecoilState(atomUser);
  const [isSubmitting, setIsSubmiting] = useState(false);
  const [firstName, setFirstName] = useState(() => user.firstName);
  const [lastName, setLastName] = useState(() => user.lastName);
  const [username, setUsername] = useState(() => user.username);
  const [bio, setBio] = useState(() => user.description);
  const [email, setEmail] = useState(() => user.email);
  const [phone, setPhone] = useState(() => user.phone);
  const [include, setInclude] = useState();

  const [updateUser] = useMutation(updateUser_gql);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmiting(true);
    console.log("Sending updates to server!");
    let resp;
    if (file !== undefined) {
      resp = await uploadImage(user.id, file);
    }

    const updates = {
      userId: user.id,
      firstName,
      lastName,
      username,
      description: bio,
      email,
      phone,
      profilePic: resp?.data,
    };

    try {
      const response = await updateUser({
        variables: {
          input: null,
          updateUserInput: updates,
        },
      });

      console.log("Response from server: ", { response });

      const updatedUser = response.data.updateUser;
      const newUserInfo = { ...updatedUser };
      localStorage.setItem("user", JSON.stringify(newUserInfo));
      setUser(newUserInfo);

      setIsSubmiting(false);
    } catch (e) {
      console.log({ e });
      setIsSubmiting(false);
    }
  };

  return (
    <div>
      <FormStyle onSubmit={handleSubmit}>
        <InputWrapper>
          <LabelStyle>
            <label htmlFor='firstName'>First name</label>
          </LabelStyle>
          <InputStyle>
            <input
              id='firstName'
              type='text'
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
            />
          </InputStyle>
        </InputWrapper>
        <InputWrapper>
          <LabelStyle>
            <label htmlFor='lastName'>Last name</label>
          </LabelStyle>
          <InputStyle>
            <input
              id='lastName'
              type='text'
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
            />
          </InputStyle>
        </InputWrapper>
        <TextWrapper>
          <p>
            Help people discover your account by using the name you're known by:
            either your full name, nickname, or business name.
          </p>
        </TextWrapper>
        <InputWrapper>
          <LabelStyle>
            <label htmlFor='username'>Username</label>
          </LabelStyle>
          <InputStyle>
            <input
              id='username'
              type='text'
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </InputStyle>
        </InputWrapper>
        <TextWrapper>
          <p>
            In most cases, you'll be able to change your username back to
            forscar for another 14 days.
          </p>
        </TextWrapper>
        <InputWrapper>
          <LabelStyle>
            <label htmlFor='bio'>Bio</label>
          </LabelStyle>
          <InputStyle>
            <textarea
              id='bio'
              type='text'
              value={bio}
              onChange={(e) => setBio(e.target.value)}
            />
          </InputStyle>
        </InputWrapper>
        <TextWrapper>
          <div>
            <span>Personal Information</span>
          </div>
          <p>
            Provide your personal information, even if the account is used for a
            business, a pet or something else. This won't be a part of your
            public profile.
          </p>
        </TextWrapper>
        <InputWrapper>
          <LabelStyle>
            <label htmlFor='email'>Email</label>
          </LabelStyle>
          <InputStyle>
            <input
              id='email'
              type='text'
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </InputStyle>
        </InputWrapper>
        <InputWrapper>
          <LabelStyle>
            <label htmlFor='phone'>Phone Number</label>
          </LabelStyle>
          <InputStyle>
            <input
              id='phone'
              type='text'
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
            />
          </InputStyle>
        </InputWrapper>
        <InputWrapper>
          <LabelStyle>
            <label htmlFor='include'>Similar Account Suggestions</label>
          </LabelStyle>
          <CheckBoxInputStyle>
            <CheckBox
              id='include'
              type='checkbox'
              checked={include}
              onChange={(e) => setInclude(!include)}
            />
            <CheckBoxText>
              Include your account when recommending similar accounts people
              might want to follow.
            </CheckBoxText>
          </CheckBoxInputStyle>
        </InputWrapper>
        <InputWrapper>
          <ButtonWrapper>
            <ButtonStyle type='submit' disabled={isSubmitting}>
              Submit
            </ButtonStyle>
          </ButtonWrapper>
        </InputWrapper>
      </FormStyle>
    </div>
  );
};

export default EditAccountForm;

const FormStyle = styled.form`
  width: 100%;
`;

const InputWrapper = styled.div`
  display: flex;
  padding: 1rem 0;
`;

const LabelStyle = styled.div`
  width: 200px;
  padding-top: 0.3rem;
  padding-right: 2rem;
  text-align: right;
  word-wrap: normal;
`;

const InputStyle = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding-right: 10%;

  input {
    height: 40px;
    width: 100%;
    padding: 9px 0px 7px 9px;
    font-size: 16px;
    line-height: 30px;
    background: #ffffff;
    border-radius: 3px;
    border: 1px solid #efefef;
    :focus {
      outline: none !important;
      border: 2px solid #a3a3a3;
    }
  }

  textarea {
    width: 100%;
    height: 60px;
    border-color: #efefef;
    padding-block: 6px;
    padding-inline: 0.5rem;
    font-size: 1rem;
    font-weight: 300;
    resize: vertical;
    font-family: "Roboto Condensed", sans-serif;
  }
`;

const CheckBoxInputStyle = styled.div`
  display: flex;
  align-items: center;
  justify-content: flex-start;
  width: 100%;
  padding-right: 10%;
`;

const CheckBox = styled.input`
  width: 20px !important;
  height: 20px !important;
`;

const CheckBoxText = styled.p`
  font-size: 0.9rem;
  font-weight: 400;
  color: #313131;
  padding-left: 1rem;
  max-width: 250px;
`;

const TextWrapper = styled.div`
  padding-left: 165px;
  padding-right: 100px;
  font-size: 0.8rem;
  font-weight: 300;
  color: #a3a3a3;

  div {
    margin-bottom: 0.4rem;
    span {
      font-weight: 500;
    }
  }
`;

const ButtonWrapper = styled.div`
  width: 100px;
  display: flex;
  margin-left: 165px;
`;
