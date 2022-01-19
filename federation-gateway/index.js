const { ApolloServer } = require("apollo-server");
const { ApolloGateway } = require("@apollo/gateway");

const gateway = new ApolloGateway({
  serviceList: [
    { name: "user", url: "http://localhost:8000/graphql" },
    { name: "post", url: "http://localhost:8002/graphql" },
  ],
});

const server = new ApolloServer({
  gateway,
  subscriptions: false,
  tracing: true,
});

(async () => {
  server.listen().then(({ url }) => console.log(`🚀  Server ready at ${url}`));
})();
