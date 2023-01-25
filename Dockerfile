FROM openjdk:17
ADD target/programs-0.0.1-SNAPSHOT.jar programs.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /programs.jar" ]