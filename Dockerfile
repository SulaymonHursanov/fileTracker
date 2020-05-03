FROM openjdk:8-jre-alpine
ADD target/fileTrack-1.0.jar fileTrack.jar

ENTRYPOINT exec java $JAVA_OPTS -jar /fileTrack.jar
