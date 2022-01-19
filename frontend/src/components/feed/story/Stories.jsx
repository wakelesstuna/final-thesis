// Styles
import styled from "styled-components";
// Components
import Story from "./Story";
// Test data
import { users } from "../../../test__data/users";

const Stories = () => {
  // Load stories from database
  const stories = users;
  return (
    <StoriesStyle>
      {stories && stories.map((user) => <Story key={user.id} user={user} />)}
    </StoriesStyle>
  );
};

export default Stories;

const StoriesStyle = styled.div`
  display: flex;
  border: 1px solid #adadadc5;
  margin-bottom: 1.6rem;
  overflow-x: scroll;
  width: 100%;
  overflow-x: scroll;
`;
