// Styles
import styled from "styled-components";

const ErrorMessage = () => {
  return (
    <ErrorStyle>
      <h1>NOT IMPLEMENTED YET!</h1>
    </ErrorStyle>
  );
};

export default ErrorMessage;

const ErrorStyle = styled.div`
  margin-top: 1rem;
  width: 100%;
  display: flex;
  align-items: center;

  h1 {
    color: red;
    margin: 0 auto;
  }
`;
