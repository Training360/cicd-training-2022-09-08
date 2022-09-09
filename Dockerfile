FROM openjdk:17.0.2
WORKDIR app
COPY target/*.jar employees.jar
ENTRYPOINT [ "java", "-jar", "employees.jar" ]
