// React
import { useEffect, useState } from "react";
// Recoil
import { useRecoilState, useRecoilValue } from "recoil";
import { atomCurrentFile, atomUser } from "../../atom/atomStates";
// GraphQL
import { useMutation } from "@apollo/client";
import { createPostMutation_gql } from "../../graphql/mutation";
// Style
import styled from "styled-components";
// Components
import ChooseImage from "./content/ChooseImage";
import CreatePost from "./content/CreatePost";
import useFocus from "../../hooks/useFocus";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import { uploadIamge } from "../util/utilFunctions";
import CreateStory from "./content/CreateStory";

const CreatePostModal = ({ closeModal }) => {
  const user = useRecoilValue(atomUser);
  const [currentFile, setCurrentFile] = useRecoilState(atomCurrentFile);
  const [isFile, setIsFile] = useState(false);
  const [fileType, setFileType] = useState("image");
  const [imageUrl, setImageUrl] = useState("");
  const [inputRef, setInputFocus] = useFocus();
  const MySwal = withReactContent(Swal);
  const [createPostMutation] = useMutation(createPostMutation_gql);

  const fileSelected = (e) => {
    const selectedFile = e.target.files[0];

    console.log("Selected file size", selectedFile.size);
    setIsFile(true);
    setImageUrl(URL.createObjectURL(selectedFile));
    setFileType(() => selectedFile.type);
    setCurrentFile(() => selectedFile);
  };

  const handlePost = async () => {
    const caption = inputRef.current.value;
    const resp = await uploadIamge(user.id, currentFile);
    try {
      const response = await createPostMutation({
        variables: {
          input: null,
          createPostInput: {
            userId: user.id,
            caption,
            imageUrl: resp.data,
          },
        },
      });

      if (response.data) {
        window.location.reload(false);
        closeModal();
      }
    } catch (e) {
      console.log({ e });
      console.log(e.message);
    }
  };

  useEffect(() => {
    console.log("File type", fileType);
  }, [fileType]);

  return (
    <ModalStyle>
      <Header>
        Create a new post{" "}
        {isFile ? <PostButton onClick={handlePost}>Post</PostButton> : null}
      </Header>
      <Content>
        {!isFile ? (
          <ChooseImage fileSelected={fileSelected} />
        ) : fileType.startsWith("image") ? (
          <CreatePost
            image={imageUrl}
            inputRef={inputRef}
            setInputFocus={setInputFocus}
          />
        ) : (
          <CreateStory />
        )}
      </Content>
    </ModalStyle>
  );
};

export default CreatePostModal;

const ModalStyle = styled.div`
  position: relative;
`;

const Header = styled.div`
  position: relative;
  padding: 0.5rem 0;
  border-bottom: 1px solid gray;
  text-align: center;
  width: 100%;
`;

const PostButton = styled.button`
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #3897f0;
  font-weight: 500;
  letter-spacing: 0.5px;
`;

const Content = styled.div`
  display: flex;
`;
