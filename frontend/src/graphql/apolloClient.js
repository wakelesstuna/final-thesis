import { ApolloClient, InMemoryCache } from "@apollo/client";

const apolloClient = new ApolloClient({
  cache: new InMemoryCache(),
  uri: `${process.env.REACT_APP_API_GATEWAY_URI}/graphql`,
});

export default apolloClient;
