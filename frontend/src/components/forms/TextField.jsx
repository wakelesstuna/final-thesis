// React
import { useEffect, useRef, useState } from "react";
// Styles
import styled from "styled-components";
// Icons
import {
  IoCloseCircleOutline,
  IoCheckmarkCircleOutline,
} from "react-icons/io5";

const TextField = ({ id, label, type, validate, isRequired }) => {
  const [isValidated, setIsValidated] = useState(true);
  const [isTouched, setIsTouched] = useState(false);
  const [showPasswordText, setShowPasswordText] = useState("Show");
  const [currentLenghtOfInput, setCurrentLenghtOfInput] = useState(0);
  const [value, setValue] = useState();
  const inputRef = useRef();

  const validateField = async () => {
    if (validate !== undefined) {
      setIsValidated(await validate(inputRef));
      setIsTouched(true);
    }
  };

  const handleShowPassword = () => {
    if (inputRef.current.type === "password") {
      setShowPasswordText("Hide");
      inputRef.current.type = "text";
    } else {
      setShowPasswordText("Show");
      inputRef.current.type = "password";
    }
  };

  useEffect(() => {
    if (inputRef.current.type === "password") {
      validateField();
    }
  }, [value]);
  return (
    <FieldStyle>
      <ValidateStyle>
        {isTouched ? (
          isValidated ? (
            <IoCheckmarkCircleOutline />
          ) : (
            <IoCloseCircleOutline />
          )
        ) : null}
        {type === "password" && currentLenghtOfInput !== 0 ? (
          <PasswordShowButton onClick={handleShowPassword}>
            {showPasswordText}
          </PasswordShowButton>
        ) : null}
      </ValidateStyle>

      <InputStyle
        placeholder={label}
        type={type}
        id={id}
        name={id}
        value={value}
        required={isRequired}
        onChange={(e) => {
          setValue(e.target.value);
          setCurrentLenghtOfInput(e.target.value.length);
        }}
        onBlur={(e) => validateField(e)}
        ref={inputRef}
      />
      <LabelStyle htmlFor='username'>{label}</LabelStyle>
    </FieldStyle>
  );
};

export default TextField;

const FieldStyle = styled.div`
  margin: 10px 0;
  position: relative;
  font-size: 14px;
  width: 100%;
  text-overflow: ellipsis;
`;

const ValidateStyle = styled.div`
  position: absolute;
  right: 5px;
  top: 50%;
  transform: translateY(-50%);
  color: grey;
  font-size: 1.5rem;
  display: flex;
  align-items: center;
`;

const InputStyle = styled.input`
  padding: 9px 0px 7px 9px;
  font-size: 12px;
  width: 100%;
  height: 2.5rem;
  outline: none;
  background: #fafafa;
  border-radius: 3px;
  border: 1px solid #efefef;
  ::placeholder {
    visibility: hidden;
  }

  :-moz-placeholder {
    color: transparent;
  }

  :not(:placeholder-shown) + label {
    transform: translateY(2px);
    font-size: 10px;
  }

  :not(:placeholder-shown) {
    padding-top: 14px;
    padding-bottom: 2px;
  }
`;

const LabelStyle = styled.label`
  position: absolute;
  pointer-events: none;
  left: 10px;
  padding-bottom: 15px;
  transform: translateY(14px);
  line-height: 6px;
  transition: all ease-out 0.1s;
  font-size: 12px;
  color: #999;
  padding-top: 6px;
`;

const PasswordShowButton = styled.p`
  font-size: 0.8rem;
  color: #202020;
  cursor: pointer;
`;
