docker build -t im-basic-server:v1.0 .
docker run -p 8904:8904 -d im-basic-server:v1.0
