# Ac Me Project X Flowable Work

## Preparation
### Building the project artifacts
Java and Maven are needed as a prerequisite to build this code.
The Maven configuration of the project tries to get the Flowable dependencies form the Flowable Enterprise Repository.
That Repository needs a Flowable enterprise account to be accessible. Get in contact with Flowable to get your Flowable subscription.

As soon as you got your username and password you can add it to the `settings-flowable.xml` file in the root of the project.
(Keep in mind, that using an unencrypted password in this file is indeed a fast but also not a suggested approach.
Check https://maven.apache.org/guides/mini/guide-encryption.html on how to encrypt your password.)

You can then create the Flowable artifacts by executing `mvn -s settings-flowable.xml clean package`.

### Flowable `infraless` profile
For a setup of a decent development infrastructure dedicated Postgres and Elasticsearch instances are heavily suggested
(e.g. by using Docker, see next chapter).

For participating a Flowable Training course it is possible to run Flowable with an `infraless` profile with an embedded H2
database and in a mode that works without Elasticsearch.
You can activate this `infraless` profile by adding it to the list of activated Spring profiles for Flowable Work, Design
and Control by either using the start configuration of your IDE or by setting `spring.profiles.active=infraless` as a
JVM parameter or environment variable.

**ATTENTION**: Keep in mind that the `infraless` profiles does not provide the full Flowable functionality and is absolutely
not suggested for development, test or even production purposes.

### Setting up the needed Infrastructure
Please check the following links on how to setup the infrastructure for Flowable manually without
using Docker:

- [Postgres Database](https://documentation.flowable.com/latest/admin/installs/engage-full/#database-1)
- [Elasticsearch](https://documentation.flowable.com/latest/admin/installs/engage-full/#elasticsearch-1)

For an easier setup with help of Docker navigate to `/docker`, then execute `docker-compose up`. Needed services such as
Elasticsearch and a database will be started.

## Starting the project
Afterwards you can start the Spring Boot application defined in `com.flowable.training.handson.work.TrainingHandsOnWorkApplication`. In order to achieve this,
you can build the executable war file with maven and execute `java -jar` pointing to the built jar or create a new IDE Run Configuration.
Then open `http://localhost:8090` in the browser and use one of the users specified below in this document.

You can start the Flowable Design and Flowable Control applications accordingly.

## Helpful links
After both docker-compose and the application are started, these are the links for the different applications:

- http://localhost:8090 - Flowable Work
- http://localhost:8091 - Flowable Design
- http://localhost:8092 - Flowable Control

## Setting up Flowable vanilla apps with Docker
The docker-compose.yml file also contains (commented out) preconfigured service definitions to run all Flowable apps as
vanilla docker images. Just uncomment the appropriate parts to start the apps together with the other infrastructure.
These vanilla services are preconfigured to run as a replacement of any of the customized apps within this project.

To be able to use them, log in to docker `docker login artifacts.flowable.com` by using your Flowable login.
You will also need an appropriate Flowable license stored in `~/.flowable/flowable.license`.
Also, a decent amount of memory is needed for your docker VM. At least 4 GB are suggested.
Navigate to `/docker`, then execute `docker-compose up`.

## Infrastructure Containers
If you need to recreate the containers, perform the following actions:
- Make sure the application and the docker containers are stopped.
- Execute `docker-compose down` inside the `/docker` directory. This will remove the created containers.

## Data
Data created by the database and Elasticsearch are stored in docker volumes `data_db` and `data_es`.
This allows you to purge and recreate the containers without loosing any data.

If you need a clean state for the database and elasticsearch just execute `docker-compose down -v`inside the `/docker` directory.
The volumes will be recreated as soon as you restart the containers.  
BE CAREFUL AS WITH THIS YOU WILL LOSE ALL YOUR DATA STORED IN THE DATABASE AND ELASTICSEARCH!

## Sample users
| User | User Definition Key | Login | Password |
| -------------| ------------- | ------------- | ------------- |
| Flowable Administrator | admin-flowable | admin | test |