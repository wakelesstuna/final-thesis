// Styles
import styled from "styled-components";
// Components
import FileInputField from "../../forms/FileInputField";
import IconImageSvg from "./IconImageSvg";

const ChooseImage = ({ fileSelected }) => {
  return (
    <ChooseImageStyle>
      <ContentContainer>
        <IconImageSvg />
        <Text>Select photo and video here</Text>
        <FileInputField
          fileLabel='Select from computer'
          handleFileSelect={fileSelected}
        />
      </ContentContainer>
    </ChooseImageStyle>
  );
};

export default ChooseImage;

const ChooseImageStyle = styled.div`
  width: 800px;
  height: 80vh;
  min-height: 300px;
`;

const ContentContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 90%;
  margin: auto;
`;

const Text = styled.p`
  margin-top: 1rem;
  margin-bottom: 0.5rem;
  font-weight: 300;
  font-size: 1.75rem;
`;
