# Flowable AG: Getting Started with Flowable Development - Flowable Work

## Preparation

### Building the Project Artifacts

Java and Maven are prerequisites for building this project. The Maven configuration fetches Flowable dependencies from the Flowable Enterprise Repository, which requires a Flowable enterprise account. You should have received a `settings.xml` file from Flowable's training team. Copy this file into your `YOUR_USER_FOLDER/.m2/` directory.

Alternatively, you can configure access credentials by copying your username and password into the `settings-flowable.xml` file located in the root of the project.  
**Note:** Storing passwords in plain text is not recommended. Refer to [Maven's guide on password encryption](https://maven.apache.org/guides/mini/guide-encryption.html) for secure storage.

Once configured, build the Flowable artifacts by executing the following command:

```sh
mvn -s settings-flowable.xml clean package
```

After building, reload Maven changes in your IDE. In IntelliJ IDEA, click `Reimport All Maven Projects` in the Maven panel (CMD/CTRL + SHIFT + O).

## Starting the Project

Your IDE should automatically create a run configuration for the project. If not, manually create one and point it to the Flowable Work, Design, or Control applications. Use the following URLs to access the running applications:

- [http://localhost:8090](http://localhost:8090) - Flowable Work
- [http://localhost:8091](http://localhost:8091) - Flowable Design
- [http://localhost:8092](http://localhost:8092) - Flowable Control (optional)
- [http://localhost:8093](http://localhost:8093) - Flowable Portal (optional)


## Sample Users

Below are sample user credentials for the Flowable applications:

| User                  | Role                 | Login | Password |
|-----------------------|----------------------|-------|----------|
| Flowable Administrator | Admin Flowable      | admin | test     |


## Running Flowable with a Database and Elasticsearch (optional)

For proper development, a dedicated Postgres and Elasticsearch setup is strongly recommended. The default `infraless` profile lacks full Flowable functionality and is unsuitable for development, testing, or production environments.

This repository is configured with an H2 file-based database and no Elasticsearch for training purposes. To enable `postgres` and `elastic` profiles for Flowable Work, Design, and Control, modify the application’s configuration using your IDE or set the `spring.profiles.active` JVM parameter or environment variable as follows:

```sh
spring.profiles.active=postgres,elastic
```

#### Setting Up the Required Infrastructure

To run with `postgres` or `elastic` profiles, you need the necessary infrastructure. The easiest way is to use Docker, simply execute:

```sh
cd ./docker/; docker compose up
```

This starts services such as Elasticsearch and the database.


## Infrastructure Containers

To recreate the containers, follow these steps:

1. Stop both the application and the Docker containers.
2. Navigate to the `/docker` directory and run:

   ```sh
   cd ./docker; docker compose down
   ```

   This removes the existing containers.

### Data Persistence

Database and Elasticsearch data are stored in Docker volumes (`data_db` and `data_es`). This ensures data persists even if containers are recreated.

To start with a clean state, execute the following command inside the `/docker` directory:

```sh
cd ./docker; docker compose down -v
```

This removes the volumes and creates fresh ones upon restarting the containers.  
**WARNING: This action deletes all stored data in the database and Elasticsearch!**

