// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../../../atom/atomStates";
// Styles
import styled from "styled-components";
// Icons
import { BsChat } from "react-icons/bs";
import { IoPaperPlaneOutline } from "react-icons/io5";
// Components
import BookmarkSymbol from "./BookmarkSymbol";
import LikeSymbol from "./LikeSymbol";
import ModalContainer from "../../../modal/ModalContainer";
import { MODAL_TYPE } from "../../../../constants";

const Symbols = ({ post, setTotalLikes }) => {
  const { id } = useRecoilValue(atomUser);

  return (
    <SymbolsStyle>
      <div>
        <button>
          <LikeSymbol
            postId={post.id}
            userId={id}
            setTotalLikes={setTotalLikes}
          />
        </button>
        <ModalContainer
          typeOfModal={MODAL_TYPE.POST}
          contentLabel={"post"}
          buttonContent={
            <button>
              <BsChat />
            </button>
          }
          style={{}}
          obj={post}
        />
        <button>
          <IoPaperPlaneOutline />
        </button>
      </div>
      <div>
        <button>
          <BookmarkSymbol postId={post.id} userId={id} />
        </button>
      </div>
    </SymbolsStyle>
  );
};

export default Symbols;

const SymbolsStyle = styled.div`
  width: 100%;
  display: flex;
  height: 54px;
  div {
    display: flex;
    align-items: center;
    button {
      font-size: 1.5rem;
      padding: 0.5rem;
      cursor: pointer;
      transition: all 250ms ease-out;
      &:hover {
        opacity: 0.6;
      }
    }
  }
  > :first-child {
    flex: 1;
    margin-left: 0.5rem;
  }
  > :last-child {
    svg {
      margin-right: 0.5rem;
    }
  }
`;
