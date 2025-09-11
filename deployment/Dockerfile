# build
FROM gradle:8.5-jdk21-jammy AS build
WORKDIR /home/gradle/src
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY . .
RUN gradle clean bootJar --no-daemon

# runtime
FROM eclipse-temurin:21-jdk-jammy AS runtime
WORKDIR /opt/app

# Crear usuario no root
ARG USERNAME=user
ARG USER_UID=999
ARG USER_GID=$USER_UID
RUN groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME

# Copiar el jar desde la etapa build
COPY --from=build /home/gradle/src/applications/app-service/build/libs/*.jar app.jar

# Configuración JVM
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError"

# Puerto
EXPOSE 8081

# Ejecutar como usuario no root
USER $USERNAME

ENTRYPOINT ["java","-jar","/opt/app/app.jar"]
