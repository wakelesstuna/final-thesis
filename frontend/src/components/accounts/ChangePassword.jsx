// Styles
import styled from "styled-components";
// Components
import ChangePasswordForm from "../forms/ChangePasswordForm";

const ChangePassword = () => {
  return (
    <Style>
      <ChangePasswordForm />
    </Style>
  );
};

export default ChangePassword;

const Style = styled.div`
  display: flex;
  align-items: center;
`;
