FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD im-basic-service-1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
EXPOSE 8904
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dspring.cloud.config.profile=prod -jar /app.jar" ]