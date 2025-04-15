# Use the official Java 21 image from Docker Hub
FROM openjdk:21-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Copy the Maven configuration file
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B || { echo 'Maven dependency resolution failed'; exit 1; }

# Copy the rest of the application files
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests || { echo 'Maven build failed'; exit 1; }

# Run the application
ENTRYPOINT ["java", "-jar", "target/foodshare-0.0.1-SNAPSHOT.jar", "--spring.devtools.restart.enabled=true"]
