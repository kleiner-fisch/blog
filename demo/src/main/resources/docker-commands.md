


- push to Dockerhub
sudo docker login
sudo docker image push heinod/blog

- run locally
sudo docker build --tag heinod/blog .
sudo docker run -p 8080:8080 -d heinod/blog