// Styles
import styled from "styled-components";
// Util
import { formatNumber } from "../../../util/utilFunctions";

const Likes = ({ totalLikes }) => {
  return (
    <LikeStyle>
      <p>
        <span>{formatNumber(totalLikes)}</span> Likes
      </p>
    </LikeStyle>
  );
};

export default Likes;

const LikeStyle = styled.div`
  width: 100%;
  padding-left: 1rem;
  p {
    cursor: pointer;
  }
`;
