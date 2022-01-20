// React
import React, { useState } from "react";
import Modal from "react-modal";
// Constants
import { MODAL_TYPE } from "../../constants";
// Components
import CreatePostModal from "./CreatePostModal";
import FollowersModal from "./FollowersModal";
import PostModal from "./PostModal";
import PostOptionModal from "./PostOptionModal";
import PostUserOptionModal from "./PostUserOptionModal";
import StoryModal from "./StoryModal";

Modal.setAppElement("#root");

const ModalContainer = ({
  typeOfModal,
  afterOpenModalFunc,
  contentLabel,
  buttonContent,
  style,
  obj,
  overlayColor,
  modalBackground,
}) => {
  const [modalIsOpen, setIsOpen] = useState(false);

  const openModal = () => {
    setIsOpen(true);
  };

  const afterOpenModal = () => {
    if (!afterOpenModalFunc) return;
    afterOpenModalFunc();
  };

  const closeModal = () => {
    setIsOpen(false);
  };

  if (overlayColor === undefined) {
    overlayColor = "#161616c7";
  }

  if (modalBackground === undefined) {
    modalBackground = "#fff";
  }

  return (
    <div>
      <div onClick={openModal} style={style}>
        {buttonContent}
      </div>
      <Modal
        isOpen={modalIsOpen}
        onAfterOpen={afterOpenModal}
        onRequestClose={closeModal}
        style={{
          overlay: { zIndex: 1000, background: overlayColor },
          content: {
            top: "50%",
            left: "50%",
            right: "auto",
            bottom: "auto",
            padding: "0",
            marginRight: "-50%",
            transform: "translate(-50%, -50%)",
            background: modalBackground,
            border: "none",
          },
        }}
        contentLabel={contentLabel}
      >
        {typeOfModal === MODAL_TYPE.POST ? <PostModal obj={obj} /> : null}
        {typeOfModal === MODAL_TYPE.POST_OPTION ? (
          <PostOptionModal closeModal={closeModal} />
        ) : null}
        {typeOfModal === MODAL_TYPE.USER_POST_OPTION ? (
          <PostUserOptionModal postId={obj} closeModal={closeModal} />
        ) : null}
        {typeOfModal === MODAL_TYPE.CREATE_POST ? (
          <CreatePostModal closeModal={closeModal} />
        ) : null}
        {typeOfModal === MODAL_TYPE.STORY ? (
          <StoryModal closeModal={closeModal} story={obj} />
        ) : null}
        {typeOfModal === MODAL_TYPE.FOLLOWERS ? (
          <FollowersModal closeModal={closeModal} type={MODAL_TYPE.FOLLOWERS} />
        ) : null}
        {typeOfModal === MODAL_TYPE.FOLLOWING ? (
          <FollowersModal closeModal={closeModal} type={MODAL_TYPE.FOLLOWING} />
        ) : null}
      </Modal>
    </div>
  );
};

export default ModalContainer;
