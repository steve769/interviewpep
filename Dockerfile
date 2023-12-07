# Use the official OpenJDK 21 base image
FROM openjdk:21

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY interviewpep.jar /app/

# Expose the port your application will run on
EXPOSE 9011

# Command to run the application
CMD ["java", "-jar", "interviewpep.jar"]