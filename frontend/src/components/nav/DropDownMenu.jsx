// React
import { Fragment } from "react";
import { Link, useNavigate } from "react-router-dom";
// Recoil
import { atomUser } from "../../atom/atomStates";
import { useRecoilState } from "recoil";
// Styles
import styled from "styled-components";
import { Menu, Transition } from "@headlessui/react";
// Icons
import { IoSettingsOutline } from "react-icons/io5";
import { CgProfile } from "react-icons/cg";
import { BsBookmark, BsArrowRepeat } from "react-icons/bs";

const DropDownMenu = ({ component }) => {
  const navigate = useNavigate();
  const [user, setUser] = useRecoilState(atomUser);

  const handleLogOut = () => {
    localStorage.removeItem("user");
    setUser(null);
    navigate("/login");
  };

  const handleProfilePress = (userid) => {
    navigate(`/profile/${userid}`);
  };

  const handleBookMarksPress = (userid) => {
    navigate(`/profile/${userid}/bookmarks`);
  };

  return (
    <div>
      <Menu as='div' style={style} git='true'>
        <div>
          <Menu.Button>{component}</Menu.Button>
        </div>
        <Transition
          as={Fragment}
          enter='transition ease-out duration-100'
          enterFrom='transform opacity-0 scale-95'
          enterTo='transform opacity-100 scale-100'
          leave='transition ease-in duration-75'
          leaveFrom='transform opacity-100 scale-100'
          leaveTo='transform opacity-0 scale-95'
        >
          <MenuButtonStyle>
            <div>
              <Menu.Item>
                <button onClick={() => handleProfilePress(user.id)}>
                  <CgProfile />
                  <p>Profile</p>
                </button>
              </Menu.Item>
              <Menu.Item>
                <button onClick={() => handleBookMarksPress(user.id)}>
                  <BsBookmark />
                  <p>Saved</p>
                </button>
              </Menu.Item>
            </div>
            <div>
              <Menu.Item>
                <Link to='/accounts/edit'>
                  <IoSettingsOutline /> <p>Settings</p>
                </Link>
              </Menu.Item>
              <Menu.Item>
                <button>
                  <BsArrowRepeat />
                  <p>Switch accounts</p>
                </button>
              </Menu.Item>
            </div>
            <div>
              <Menu.Item>
                <button onClick={handleLogOut}>Log Out</button>
              </Menu.Item>
            </div>
          </MenuButtonStyle>
        </Transition>
      </Menu>
    </div>
  );
};

export default DropDownMenu;

const style = {
  position: "relative",
  display: "inline-block",
  textAlign: "left",
  zIndex: "9999",
};

const MenuButtonStyle = styled(Menu.Items)`
  position: absolute;
  top: 20px;
  right: 0;
  width: 12rem;
  margin-top: 1rem;
  transform-origin: top right;
  background-color: #fff;
  border-radius: 0.375rem;
  box-shadow: 0 0 3px grey;

  > div {
    display: flex;
    flex-direction: column;
  }

  > div a,
  > div button {
    font-weight: 400;
    color: #292929;
    display: flex;
    align-items: center;
    padding: 0.5rem 0.5rem;
    width: 100%;
    &:hover {
      background-color: hsl(0, 0%, 63.921568627450974%, 0.1);
    }
    svg {
      font-size: 0.9rem;
    }
    p {
      font-size: 0.6rem;
      margin-left: 5px;
    }
  }
  > :not(:first-child) {
    border-top: 1px solid hsl(0, 0%, 63.921568627450974%, 0.3);
  }
`;
