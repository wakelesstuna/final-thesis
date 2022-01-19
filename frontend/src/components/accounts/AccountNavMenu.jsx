// React
import { useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
// Styles
import styled from "styled-components";

const AccountNavMenu = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const listRef = useRef();

  const go = (e) => {
    navigate(e.target.id);
  };

  const setActive = () => {
    for (let item of listRef.current.children) {
      if (item.id === location.pathname) {
        item.classList.add("active");
      } else {
        item.classList.add("notActive");
      }
    }
  };

  useEffect(() => {
    setActive();
  }, []);

  return (
    <NavMenu>
      <ListStyle ref={listRef}>
        <ListItemStyle id='/accounts/edit' onClick={(e) => go(e)}>
          <p>Edit Profile</p>
        </ListItemStyle>
        <ListItemStyle id='/accounts/password/change' onClick={(e) => go(e)}>
          <p>Change Password</p>
        </ListItemStyle>
        <ListItemStyle id='/accounts/mange_access' onClick={(e) => go(e)}>
          <p>Apps and Websites</p>
        </ListItemStyle>
        <ListItemStyle id='/accounts/emails/settings' onClick={(e) => go(e)}>
          <p>Email and SMS</p>
        </ListItemStyle>
        <ListItemStyle id='/accounts/push/web/settings' onClick={(e) => go(e)}>
          <p>Push Notifications</p>
        </ListItemStyle>
        <ListItemStyle id='/accounts/contact_history' onClick={(e) => go(e)}>
          <p>Mange Contacts</p>
        </ListItemStyle>
        <ListItemStyle
          id='/accounts/privacy_and_security'
          onClick={(e) => go(e)}
        >
          <p>Privacy and Security</p>
        </ListItemStyle>
        <ListItemStyle
          id='/accounts/session/login_activity'
          onClick={(e) => go(e)}
        >
          <p>Login Activity</p>
        </ListItemStyle>
        <ListItemStyle id='/accounts/emails/emails_sent' onClick={(e) => go(e)}>
          <p>Emails from Instagram</p>
        </ListItemStyle>
        <ListItemStyle id='/accounts/settings/help' onClick={(e) => go(e)}>
          <p>Help</p>
        </ListItemStyle>
      </ListStyle>
      <Block />
    </NavMenu>
  );
};

export default AccountNavMenu;

const NavMenu = styled.div`
  display: flex;
  width: 30%;
  flex-direction: column;
  .active {
    border-color: black;
    font-weight: 400;
  }
  .notActive:hover {
    border-color: #c4c4c4;
    background-color: #fcfcfc;
    cursor: pointer;
  }
`;

const ListStyle = styled.ul`
  display: flex;
  height: 100%;
  flex-direction: column;
  justify-content: space-evenly;
  border-bottom: 1px solid #e6e6e6;
`;

const ListItemStyle = styled.li`
  display: flex;
  align-items: center;
  padding-left: 2rem;
  font-size: 1rem;
  font-weight: 300;
  width: 100%;
  height: 100%;
  border-left: 2px solid transparent;

  cursor: pointer;
  p {
    pointer-events: none;
  }
`;

const Block = styled.div`
  height: 30%;
  width: 100%;
`;
