FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD favorapp-api-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="C:\Program Files\Java\jdk1.8.0_92\bin\javac.exe"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]