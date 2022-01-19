// Icons
import { IoPaperPlaneOutline } from "react-icons/io5";
// Styles
import styled from "styled-components";

const NewMessage = () => {
  return (
    <NewMessageStyle>
      <div>
        <IoPaperPlaneOutline />
      </div>
      <div>
        <h4>Your messages</h4>
        <p>Send private photos and messages to a friend or group.</p>
      </div>
      <div>
        <button>Send Message</button>
      </div>
    </NewMessageStyle>
  );
};

export default NewMessage;

const NewMessageStyle = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  > * {
    margin-top: 1rem;
  }
  > :first-child {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100px;
    aspect-ratio: 1;
    border: 2px solid black;
    border-radius: 9999px;
    svg {
      font-size: 3.5rem;
    }
  }

  > :nth-child(2) {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    h4 {
      font-weight: 400;
      padding-bottom: 0.3rem;
    }
    p {
      font-weight: 200;
      color: #808080b9;
    }
  }

  > div button {
    padding: 0.5rem 1rem;
    border-radius: 3px;
    font-size: 1rem;
    font-weight: 700;
    color: #fff;
    background-color: #0095f6;
  }
`;
