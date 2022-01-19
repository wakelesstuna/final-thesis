// React
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import useWindowSize from "../../hooks/useWindowSize";
// Recoil
import { useRecoilValue } from "recoil";
import { atomUser } from "../../atom/atomStates";
// Icons
import logo from "../../img/instagram-logo.png";
import { AiFillHome } from "react-icons/ai";
import { HiMenuAlt3 } from "react-icons/hi";
import { IoClose } from "react-icons/io5";
import { IoPaperPlaneOutline, IoCompassOutline } from "react-icons/io5";
import { BsPlusSquare } from "react-icons/bs";
// Styles
import styled from "styled-components";
import { PageWrapper } from "../../styles/layout";
import { BREAKPOINTS, MODAL_TYPE } from "../../constants";
// Components
import AccountLinks from "./AccountLinks";
import DropDownMenu from "./DropDownMenu";
import ModalContainer from "../modal/ModalContainer";

const NavBar = () => {
  const user = useRecoilValue(atomUser);
  const [open, setOpen] = useState(false);
  const [accountMenu, setAccountMenu] = useState(false);
  const { width } = useWindowSize();
  const navigate = useNavigate();

  const handleLinkPress = () => {
    setOpen(false);
  };

  const handleOpenAccountMenu = () => {
    setAccountMenu(!accountMenu);
  };

  const checkIfAccountMenuOpen = () => {
    if (!accountMenu) return;
    return <AccountLinks />;
  };

  return (
    <HeaderStyle>
      <Wrapper>
        <LogoStyle>
          <img src={logo} alt='logo' onClick={() => navigate("/")} />
        </LogoStyle>
        <MenuButtonStyle onClick={() => setOpen(!open)}>
          {open ? <IoClose /> : <HiMenuAlt3 />}
        </MenuButtonStyle>
        <nav>
          <PrimaryNavStyle id='primary-navigation' open={open}>
            <li>
              <Link to='/' onClick={handleLinkPress}>
                <AiFillHome />
                {width > BREAKPOINTS.MD ? null : <p>Home</p>}
              </Link>
            </li>
            <li>
              <Link to='/messages' onClick={handleLinkPress}>
                <IoPaperPlaneOutline />
                {width > BREAKPOINTS.MD ? null : <p>Messages</p>}
              </Link>
            </li>
            <li>
              <button>
                <ModalContainer
                  typeOfModal={MODAL_TYPE.CREATE_POST}
                  contentLabel='create post'
                  buttonContent={<BsPlusSquare />}
                />

                {width > BREAKPOINTS.MD ? null : <p>Upload Foto</p>}
              </button>
            </li>
            <li>
              <Link to='/discover' onClick={handleLinkPress}>
                <IoCompassOutline />
                {width > BREAKPOINTS.MD ? null : <p>Discover</p>}
              </Link>
            </li>
            <li>
              <div onClick={handleOpenAccountMenu}>
                {width > BREAKPOINTS.MD ? (
                  <DropDownMenu
                    component={<img src={user?.profilePic} alt='proifle' />}
                  />
                ) : (
                  <>
                    <img src={user?.profilePic} alt='proifle' /> <p>Account</p>
                  </>
                )}
              </div>
            </li>
            <li>{width > BREAKPOINTS.MD ? null : checkIfAccountMenuOpen()}</li>
          </PrimaryNavStyle>
        </nav>
      </Wrapper>
    </HeaderStyle>
  );
};

export default NavBar;

const HeaderStyle = styled.header`
  position: fixed;
  width: 100%;
  border-bottom: 1px solid #adadadc5;
  z-index: 50;
`;

const Wrapper = styled(PageWrapper)`
  background-color: #fff;
  display: flex;
  padding: 0.5rem 0;
  align-items: center;
  justify-content: space-between;
`;

const MenuButtonStyle = styled.button`
  position: fixed;
  top: 0.5rem;
  right: 2rem;
  font-size: 1.825rem;
  cursor: pointer;
  // not moblie
  @media (min-width: 745px) {
    display: none;
  }
`;

const PrimaryNavStyle = styled.ul`
  position: fixed;
  inset: 0 0 0 30%;
  display: flex;
  flex-direction: column;
  padding: min(10vh, 10rem) 2rem;
  background-color: hsl(0 0% 50% / 0.3);
  backdrop-filter: blur(12px);
  transition: all 205ms ease-out;
  transform: ${(props) => (props.open ? "translateX(0)" : "translateX(+100%)")};
  opacity: ${(props) => (props.open ? "1" : "0")};
  font-size: 1.125rem;
  font-weight: bolder;

  > li {
    position: relative;
    padding: 1rem 1rem;
  }
  > li a,
  > li button {
    display: flex;
    cursor: pointer;
  }
  > li a p,
  > li button p {
    text-transform: uppercase;
    margin-left: 1rem;
    text-align: center;
    text-align: justify;
  }

  > li a svg,
  > li button svg {
    font-size: 1.5rem;
  }

  img {
    border-radius: 9999px;
    object-fit: cover;
    height: 30px;
    width: 30px;
    aspect-ratio: 1;
  }

  // not mobile
  @media (min-width: 745px) {
    background-color: #fff;
    flex-direction: row;
    position: relative;
    align-items: center;
    inset: 0;
    padding: 0;
    opacity: 1;
    transform: translateX(5px);
    > li {
      padding: 0 1rem;
    }
  }
`;

const LogoStyle = styled.div`
  display: flex;
  align-items: center;
  justify-content: start;
  img {
    margin-top: 0.5rem;
    object-fit: contain;
    height: 1.8rem;
    cursor: pointer;
  }
`;
