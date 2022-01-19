// Styles
import styled from "styled-components";

const AccountLinks = () => {
  return (
    <div>
      <ListStyle>
        <li>Profile</li>
        <li>BookMarked</li>
        <li>Settings</li>
        <li>Change Account</li>
        <li>Log out</li>
      </ListStyle>
    </div>
  );
};

export default AccountLinks;

const ListStyle = styled.ul`
  > :last-child {
    border-top: 1px solid black;
  }
`;
