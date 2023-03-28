FROM openjdk:8-jre-slim

MAINTAINER zj

ENV PARAMS=""

RUN ln -snf /user/share/zoneinfo/$TZ /etc.localtime && echo $ TZ > /etc/timezone
# 添加应用
ADD /chatbot-api-interfaces/target/chatbot-api.jar /charbot-api.jar
# 执行镜像
ENTRYPOINT ["sh", "-c", "java -jar $JAVA_OPTS /chatbot-api.jar $PARAMS"]
