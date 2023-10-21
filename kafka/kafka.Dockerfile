FROM eclipse-temurin:17-jre-alpine

ADD https://dlcdn.apache.org/kafka/3.6.0/kafka_2.13-3.6.0.tgz kafka.tgz

RUN tar -xvzf kafka.tgz

RUN rm kafka.tgz

RUN mv /kafka_2.13-3.6.0 /kafka

ENV PATH=${PATH}:/kafka/bin

WORKDIR /start

COPY run.sh run.sh

CMD [ "sh", "run.sh" ]