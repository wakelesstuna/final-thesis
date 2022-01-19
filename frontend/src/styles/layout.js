// Styles
import styled from "styled-components";

export const PageWrapper = styled.div`
  padding-top: 5rem;
  max-width: 925px;
  margin: 0 auto;
`;

export const Page = styled.div`
  position: relative;
  z-index: 0;
`;

export const PrimaryButton = styled.button`
  text-align: center;
  border-radius: 3px;
  padding: 0.5rem;
  border: 1px solid transparent;
  background-color: #3897f0;
  color: #fff;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;

  &:disabled {
    opacity: 0.3;
  }
`;

export const ButtonStyle = styled.button`
  text-align: center;
  width: 100%;
  padding: 0.5rem;
  border: 1px solid transparent;
  background-color: #3897f0;
  color: #fff;
  border-radius: 3px;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
`;

export const AccountPageWrapper = styled.div`
  display: flex;
  border: 1px solid #e6e6e6;
  height: 85vh;
  min-height: 500px;
`;

export const InformationSection = styled.div`
  width: 100%;
  border-left: 1px solid #e6e6e6;
  overflow-y: auto;
  overflow-x: hidden;
`;

export const Center = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const PostOptionModalStyle = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 400px;

  div {
    width: 100%;
    padding: 0.5rem 0;
    text-align: center;
  }

  div:not(:last-child) {
    border-bottom: 1px solid rgba(219, 219, 219, 1);
  }

  div p {
    font-weight: 300;
    cursor: pointer;
    padding: 0.5rem 0;
    letter-spacing: 0.5px;
    font-size: 0.9rem;
  }
`;

export const PostOptionModalRedText = styled.p`
  color: #ed4956;
  font-weight: 600 !important;
`;
