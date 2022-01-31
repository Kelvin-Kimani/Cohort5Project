FROM openjdk:11
ADD target/Cohort5Project.jar Cohort5Project.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Cohort5Project.jar"]


#FROM openjdk:11
#COPY ./target/Cohort5Project.jar Cohort5Project.jar
#CMD ["java", "-jar", "Cohort5Project.jar"]