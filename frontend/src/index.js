// React
import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter } from "react-router-dom";
// Recoil
import { RecoilRoot } from "recoil";
// GraphQl
import { ApolloProvider } from "@apollo/client";
import apolloClient from "./graphql/apolloClient";
// Styled Components
import { ThemeProvider } from "styled-components";
import { theme } from "./styles/theme";
import { GlobalCSS } from "./styles/global.css";

// Component
import App from "./App";

ReactDOM.render(
  <React.StrictMode>
    <ApolloProvider client={apolloClient}>
      <BrowserRouter>
        <RecoilRoot>
          <ThemeProvider theme={theme}>
            <GlobalCSS />
            <App />
          </ThemeProvider>
        </RecoilRoot>
      </BrowserRouter>
    </ApolloProvider>
  </React.StrictMode>,
  document.getElementById("root")
);
