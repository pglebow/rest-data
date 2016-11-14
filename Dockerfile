FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD rest-data-0.0.1-SNAPSHOT.jar .
RUN sh -c 'touch /rest-data-0.0.1-SNAPSHOT.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /rest-data-0.0.1-SNAPSHOT.jar" ]