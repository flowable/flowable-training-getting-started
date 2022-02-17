# Flowable Training Hands-On Project

## Preparation
### Building the project artifacts
Java and Maven are needed as a prerequisite to build this code.
The Maven configuration of the project tries to get the Flowable dependencies form the Flowable Enterprise Repository.
That Repository needs a Flowable enterprise account to be accessible. Get in contact with Flowable to get your Flowable subscription.

As soon as you got your username and password you can add it to the `settings-flowable.xml` file in the root of the project.
(Keep in mind, that using an unencrypted password in this file is indeed a fast but also not a suggested approach.
Check https://maven.apache.org/guides/mini/guide-encryption.html on how to encrypt your password.)

You can then create the Flowable artifacts by executing `mvn -s settings-flowable.xml clean package`.

### Setting up the needed Infrastructure (optional)
Please check the following links on how to setup the infrastructure for Flowable manually without
using Docker:

- [Postgres Database](https://documentation.flowable.com/latest/admin/installs/engage-full/#database-1)
- [Elasticsearch](https://documentation.flowable.com/latest/admin/installs/engage-full/#elasticsearch-1)

For an easier setup with help of Docker execute `docker-compose up`. Needed services such as 
Elasticsearch and a database will be started.

## Starting the project
Afterwards you can start the Spring Boot application defined in `TrainingHandsOnWorkApplication`. In order to achieve this,
you can build the executable war file with maven and execute `java -jar` pointing to the built jar or create a new IDE Run Configuration. 
Then open `http://localhost:8090` in the browser and use one of the users specified below in this document.

You can start the Flowable Design and Flowable Control applications accordingly.

## Helpful links
After both docker-compose and the application are started, these are the links for the different applications:

- http://localhost:8090 - Flowable Work
- http://localhost:8888 - Flowable Design
- http://localhost:9988 - Flowable Control


## Infrastructure Containers
If you need to recreate the containers, perform the following actions:
- Make sure the application and the docker containers are stopped.
- Execute `docker-compose down`. This will remove the created containers.

## Sample users
| User  | Login | Password |
| ------------- | ------------- | ------------- |
| Flowable Administrator  | admin | test |

## Change Log

### 0.0.1
- Initial commit