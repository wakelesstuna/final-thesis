### CDN STORY SERVER
This is a cdn server to handle video uploads.
This is a part of my final thesis, "GraphQL - A query language for your API"
The base url for the server is set to "http://localhost:5000" and the max upload size
to 20MB. 

Endpoints: 
```java
"/cdn/story/upload/{userId}"
        
example return
"http://localhost:5000/cdn/story/stream/mp4/45be5bd0-f200-43bb-a880-66101ef8c859+test2/OTI0YzY3LTE0YzktNGM2LThjYy1jMjZmNDY1NGNmZGI="
```
This endpoint is for uploading videos to the server. It needs the video as formdata with <br>
param of "file".
It returns the download url for the video.
```java
"/cdn/story/stream/{fileType}/{fileName}/{path}"
```
This is the endpoint for streaming it takes the filetype och the video, the filname, <br> 
and a encoded path as path variables. 
This is the return string from the server when uploaded.
```java
"/cdn/story/delete/{fileType}/{fileName}/{userId}"
```
This deletes a video from the server sends a http status as return.
it takes the filetype och the video, the filname, <br>
and the user id of the user that uploaded the video as path variables.