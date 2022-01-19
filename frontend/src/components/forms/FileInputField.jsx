// Styles
import styled from "styled-components";

const FileInputField = ({ fileLabel, handleFileSelect, style }) => {
  return (
    <>
      <FileInput
        id='file'
        name='file'
        type='file'
        onChange={(e) => handleFileSelect(e)}
      />
      <FileLabel htmlFor='file' style={style}>
        {fileLabel}
      </FileLabel>
    </>
  );
};

export default FileInputField;

const FileLabel = styled.label`
  font-size: 1.125em;
  font-weight: 300;
  color: white;
  background-color: #3897f0;
  padding: 0.5rem 1rem;
  border-radius: 3px;
  cursor: pointer;
  margin-bottom: 1rem;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  &:hover {
    opacity: 0.7;
  }
`;

const FileInput = styled.input`
  width: 0.1px;
  height: 0.1px;
  opacity: 0;
  overflow: hidden;
  position: absolute;
  z-index: -1;
`;
