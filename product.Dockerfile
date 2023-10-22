FROM gradle:8.4.0-jdk17-alpine AS BUILDER

WORKDIR /build

COPY product-service/src product-service/src

COPY settings.gradle.kts settings.gradle.kts

COPY product-service/build.gradle.kts product-service/build.gradle.kts

COPY gradle/plugins gradle/plugins

COPY common/src common/src

COPY common/build.gradle.kts common/build.gradle.kts

RUN gradle clean assemble

#------------------------------------------------------

FROM bellsoft/liberica-openjdk-alpine:17.0.8

WORKDIR /app

COPY --from=BUILDER /build/product-service/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]