// Styles
import styled from "styled-components";

const CreateStory = ({ file }) => {
  const binaryData = [];
  binaryData.push(file);
  const video = URL.createObjectURL(
    new Blob(binaryData, { type: "application/zip" })
  );
  return (
    <CreateStoryStyle>
      <video width='500' height='750' src={video} controls />
    </CreateStoryStyle>
  );
};

export default CreateStory;

const CreateStoryStyle = styled.div`
  min-width: 450px;
`;
