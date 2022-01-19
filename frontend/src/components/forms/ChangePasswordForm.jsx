// React
import { useEffect, useRef, useState } from "react";
// GraphQL
import { useMutation } from "@apollo/client";
import { updatePassword_gql } from "../../graphql/mutation";
// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../atom/atomStates";
// Styles
import styled from "styled-components";
import { PrimaryButton } from "../../styles/layout";
// Components
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";

const ChangePasswordForm = () => {
  const [updatePassword] = useMutation(updatePassword_gql);
  const { id } = useRecoilValue(atomUser);
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errorMsg, setErrorMsg] = useState();
  const buttonRef = useRef();
  const MySwal = withReactContent(Swal);

  const changePasswordHandler = async (e) => {
    e.preventDefault();
    if (passwordMatches()) {
      try {
        const result = await updatePassword({
          variables: {
            updatePasswordInput: {
              userId: id,
              newPassword,
              oldPassword,
            },
          },
        });

        if (result.data.updatePassword === true) {
          await MySwal.fire({
            title: <strong>Success</strong>,
            html: <i>Password updated!</i>,
            icon: "success",
          });
        } else {
          await MySwal.fire({
            title: <strong>error</strong>,
            html: <i>Something went wrong please try again</i>,
            icon: "error",
          });
          setOldPassword("");
          setNewPassword("");
          setConfirmPassword("");
        }
      } catch (e) {
        await MySwal.fire({
          title: <strong>Error!</strong>,
          html: <i>{e.message}</i>,
          icon: "error",
        });
      }
    } else {
      setErrorMsg(true);
    }
  };

  const checkIfDiasble = () => {
    if (
      oldPassword.length > 0 &&
      newPassword.length > 0 &&
      confirmPassword.length > 0
    ) {
      buttonRef.current.disabled = false;
    } else {
      buttonRef.current.disabled = true;
    }
  };

  const passwordMatches = () => {
    return newPassword === confirmPassword;
  };

  useEffect(() => {
    checkIfDiasble();
    setErrorMsg(!passwordMatches);
  }, [oldPassword, newPassword, confirmPassword]);

  return (
    <FormStyle onSubmit={changePasswordHandler}>
      <InputWrapper>
        <LabelStyle>
          <label htmlFor='oldPassword'>Old password</label>
        </LabelStyle>
        <InputStyle>
          <input
            id='oldPassword'
            type='password'
            onChange={(e) => setOldPassword(e.target.value)}
            value={oldPassword}
          />
        </InputStyle>
      </InputWrapper>
      <InputWrapper>
        <LabelStyle>
          <label htmlFor='newPassword'>New password</label>
        </LabelStyle>
        <InputStyle>
          <input
            id='newPassword'
            type='password'
            minLength={6}
            onChange={(e) => setNewPassword(e.target.value)}
            value={newPassword}
          />
          {errorMsg ? <p>Password does not match!</p> : null}
        </InputStyle>
      </InputWrapper>
      <InputWrapper>
        <LabelStyle>
          <label htmlFor='confirmPassword'>Confirm New Password</label>
        </LabelStyle>
        <InputStyle>
          <input
            id='confirmPassword'
            type='password'
            onChange={(e) => setConfirmPassword(e.target.value)}
            value={confirmPassword}
          />
        </InputStyle>
      </InputWrapper>
      <ButtonWrapper>
        <PrimaryButton disabled={true} ref={buttonRef}>
          Change Password
        </PrimaryButton>
      </ButtonWrapper>
    </FormStyle>
  );
};

export default ChangePasswordForm;

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
  padding-inline: 2rem;
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
    font-size: 14px;
    line-height: 30px;

    background: #fafafa;
    border-radius: 3px;
    border: 1px solid #efefef;
    :focus {
      outline: none !important;
      border: 1px solid #a3a3a3;
    }
  }
`;

const ButtonWrapper = styled.div`
  width: 100%;
  display: flex;
  margin-left: 165px;
`;
