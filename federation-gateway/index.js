import { ApolloServer } from "apollo-server";
import { ApolloGateway } from "@apollo/gateway";
import { ApolloServerPluginLandingPageGraphQLPlayground } from "apollo-server-core";

const gateway = new ApolloGateway({
  serviceList: [
    /* { name: "user", url: "https://reactive-traning.herokuapp.com/graphql" }, */
    { name: "user", url: "http://localhost:8000/graphql" },
    { name: "post", url: "http://localhost:8002/graphql" },
  ],
});

const server = new ApolloServer({
  gateway,
  subscriptions: false,
  tracing: true,
  /* plugins: [ApolloServerPluginLandingPageGraphQLPlayground()], */
});

(async () => {
  server
    .listen({ port: process.env.PORT || 4000 })
    .then(({ url }) => console.log(`🚀  Server ready at ${url}`))
    .catch((error) => console.error(error));
})();
