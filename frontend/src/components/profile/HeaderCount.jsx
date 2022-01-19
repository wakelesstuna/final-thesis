// Styles
import styled from "styled-components";
import { MODAL_TYPE } from "../../constants";
import ModalContainer from "../modal/ModalContainer";

const HeaderCount = ({ count, title }) => {
  return (
    <HeaderCountStyle>
      <div>
        <CountText>{count}</CountText>
      </div>
      <div>
        {title === "followers" ? (
          <ModalContainer
            typeOfModal={MODAL_TYPE.FOLLOWERS}
            contentLabel='title'
            buttonContent={<TitleText>{title}</TitleText>}
            style={{ cursor: "pointer" }}
          />
        ) : title === "following" ? (
          <ModalContainer
            typeOfModal={MODAL_TYPE.FOLLOWING}
            contentLabel='title'
            buttonContent={<TitleText>{title}</TitleText>}
            style={{ cursor: "pointer" }}
          />
        ) : (
          <TitleText>{title}</TitleText>
        )}
      </div>
    </HeaderCountStyle>
  );
};

export default HeaderCount;

const HeaderCountStyle = styled.div`
  display: flex;
`;

const CountText = styled.p`
  font-weight: 500;
  padding-right: 0.5rem;
`;

const TitleText = styled.p`
  font-weight: 300;
`;
