## Final Thesis - Instagram Clone

This is my source code for my final thesis in school. It's a clone of Instagram with a React front-end and a GraphQL backend.
I been using the DGS framework(by Netflix) to build the GraphQL server. 

To Run the project: 
```
 Docker is needed to run the databases.
```
<ul>
  <li>Clone the project</li>
  <li>Run the docker-compose-database-config</li>
  <li>Login to the pgAdmin on http://localhost:5050/ and create 3 databases with the names "image", "user" and "post"</li>
  <li>Start the cdn-image-server, cdn-story-server, dgs-post and dgs-user projects</li>
  <li>Open up the federation-gateway project and run npm install to download and install all the dependencies</li>
  <li>Run npm start to start the gateway</li>
  <li>Open up the frontend project and run npm install to download and install all the dependencies</li>
  <li>Run npm start to start the frontend</li>
</ul>

You can now access the website on http://localhost:3000/

You can also access the Apollo Gateway on http://localhost:4000/ to check out the GraphQL schema and documentation.
