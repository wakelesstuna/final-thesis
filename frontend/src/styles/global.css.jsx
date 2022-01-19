import { createGlobalStyle } from "styled-components";

export const GlobalCSS = createGlobalStyle`
    //@import url('https://fonts.googleapis.com/css2?family=Roboto+Condensed&display=swap');

    body {
      font-family: 'Roboto Condensed', sans-serif;
    }

  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }

  ul {
    list-style: none;
  }

  a {
    text-decoration: none;
    color: black;
  }

  button {
    display: inline-block;
    border: none;
    padding: 0;
    margin: 0;
    text-decoration: none;
    background-color: transparent;
    color: black;
    cursor: pointer;
    text-align: center;
    transition: background 250ms ease-in-out, 
                transform 150ms ease;
    -webkit-appearance: none;
    -moz-appearance: none;
}
`;
