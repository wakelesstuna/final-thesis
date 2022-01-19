// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../atom/atomStates";
// Styles
import styled from "styled-components";
// Components
import NewMessage from "./NewMessage";

const Messages = () => {
  const profilePicplaceHolder =
    "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8dXNlcnxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&w=1000&q=80";
  const user = useRecoilValue(atomUser);
  const theme = {
    borderColor: "rgba(var(--b6a,219,219,219),1)",
  };
  return (
    <MessageStyle theme={theme}>
      <Header theme={theme}>{user.username}</Header>
      <Friends theme={theme}>{/* Make chat profiles here */}</Friends>
      <Message theme={theme}>
        <NewMessage />
      </Message>
    </MessageStyle>
  );
};

export default Messages;

const MessageStyle = styled.div`
  display: grid;
  grid-template-columns: 350px 1fr;
  grid-template-rows: 60px 1fr;
  border: 1px solid ${(props) => props.theme.borderColor};
  border-radius: 3px;
  width: 100%;
  max-height: 90vh;
  min-height: 600px;
`;

const Header = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  border-right: 1px solid ${(props) => props.theme.borderColor};
`;

const Friends = styled.div`
  overflow-y: scroll;
  border-top: 1px solid ${(props) => props.theme.borderColor};
  border-right: 1px solid ${(props) => props.theme.borderColor};
`;

const Message = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  overflow-y: scroll;
  grid-column-start: 2;
  grid-row-start: 1;
  grid-row-end: 3;
`;
