# Build Stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17
ARG PROFILE=dev
ARG APP_VERSION=0.0.1

WORKDIR /app
COPY --from=build /build/target/StockManagement-*.jar /app/

EXPOSE 8080

ENV DB_URL="jdbc:postgresql://pg-17d474fa-mpnzabera-8a60.i.aivencloud.com:21994/defaultdb?sslmode=require"
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV EMAIL_HOSTNAME=smtp.gmail.com
ENV EMAIL_USER_NAME=nzaberamikepeter@gmail.com
ENV EMAIL_PASSWORD=vyekjidcrtqjrlwf

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} StockManagement-${JAR_VERSION}.jar
