# Flowable Development Training Repository Guide

This repository is designed to assist you in the `Getting Started with Flowable Development` course.

## Prerequisites

Before you begin, ensure you have the following:

1. Access to the commercial Flowable artifacts. If you don't have access, please reach out to your instructor or Customer Success Manager.
2. A Java development environment (Java 17 or newer). We recommend using `IntelliJ IDEA Ultimate` or `Visual Studio Code`.
3. The Maven settings.xml and a license for accessing Flowable's commercial artifacts.

## IDE Setup

### IntelliJ IDEA

1. Download and install IntelliJ IDEA Ultimate (not Community!) from the [official website](https://www.jetbrains.com/idea/download/).
2. Open IntelliJ IDEA and select `Open` or `Import` from the welcome screen.
3. Navigate to the directory where you cloned the training project and select the project's root directory.
4. IntelliJ IDEA will automatically detect and configure the project based on the `pom.xml` files.

### Visual Studio Code

1. Download and install Visual Studio Code from the [official website](https://code.visualstudio.com/download).
2. Install the `Extension Pack for Java` from the Extensions view (`Ctrl+Shift+X`).
3. Install the `Spring Boot Extension Pack` from the Extensions view (`Ctrl+Shift+X`).
4. Open Visual Studio Code and select `Open Folder` from the welcome screen or from the `File` menu.
5. Navigate to the directory where you cloned the training project and select the project's root directory.
6. Visual Studio Code will automatically detect and configure the project based on the `pom.xml` files.

## Setup Instructions

Follow these steps to set up your environment:

1. Place the provided settings.xml in your `.m2` directory. For Windows, this is `C:\Users\YourUser\.m2` and for MacOS, it's `/Users/your.user/.m2`. If there's
   an existing file, make sure to back it up first.
2. Download and place the provided license file in your `.flowable` directory, which is `C:\Users\YourUser\.flowable` (Windows)
   or `/Users/your.user/.flowable` (MacOS).
3. Clone the training project from https://github.com/flowable/flowable-training-getting-started and open it in your Java IDE.
4. After all artifacts have been synchronized, you can start any of the Spring Boot applications.

## Accessing the Applications

Once the application are started, you can access the applications at the following URLs:

- Flowable Work: http://localhost:8090
- Flowable Design: http://localhost:8091
- Flowable Control: http://localhost:8092

## Docker Setup

If you wish to use Docker for setting up your database and Elasticsearch, follow these steps:

1. Ensure Docker is installed and running on your machine.
2. Navigate to the directory containing the `docker-compose.yml` file.
3. Run the command `docker-compose up` to start the services defined in the `docker-compose.yml` file.

To use a Postgres database and Elasticsearch with the Spring Boot applications, you need to start the applications with the `postgres` and `elastic` profiles
respectively.

## Sample User Details

Here are the details for a sample user:

| Username | Password | Role  |
|----------|----------|-------|
| admin    | test     | admin |
