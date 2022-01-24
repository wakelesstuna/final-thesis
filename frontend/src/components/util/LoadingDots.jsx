// Components
import { Loading } from "react-loading-dot/lib";
// Styles
import styled from "styled-components";

/**
 * @author https://www.npmjs.com/package/react-loading-dot
 *  Properties	Default	        Description	                                Type
 *  dots	    3	            Number of dots displayed	                number
 *  size	    1.5rem	        The width and height of each dot	        string
 *  margin	    1rem	        The horizontal distance between each dot	string
 *  background	#ca3939The    color of the dot	                          string
 *  duration	0.8s	        The duration of the animation	            string
 *
 */
const LoadingDots = () => {
  return (
    <Wrapper>
      <Loading dots='5' size='0.5rem' background='grey' />
    </Wrapper>
  );
};

const Wrapper = styled.div`
  position: fixed;
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
`;

export default LoadingDots;
