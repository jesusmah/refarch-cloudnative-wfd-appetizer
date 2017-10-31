###### refarch-cloudnative-wfd-appetizer

### Microservices Reference Application - What's For Dinner Appetizer Service

*This project is part of the 'IBM Cloud Architecture - Microservices Reference Application for Netflix OSS' suite, available at
https://github.com/ibm-cloud-architecture/refarch-cloudnative-netflix*

#### Introduction

This project is built to demonstrate how to build a Spring Boot application for use in a microservices-based architecture:
 - Leverage Spring Boot & Spring Cloud frameworks to build a microservices-based application.
 - Integrate with the [Netflix Eureka](https://github.com/Netflix/eureka) framework.
 - Deployment options for local, Cloud Foundry, or Docker Container-based runtimes (including [IBM Container Service](https://console.ng.bluemix.net/docs/containers/container_index.html) on [Bluemix](https://new-console.ng.bluemix.net/#overview)).

Additionally, this microservice leverages robust resiliency capabilities, provided in this implementation by the Spring Cloud Config Server client libraries.  The microservice will retry multiple times to contact the Config Server for the most up-to-date configuration data upon startup.  Should the Config Server still be unavailable, after a configurable period of retries, the microservice will fail to start and can be diagnosed for additional connectivity problems or configured to be started with static built-in configuration data.  

For more information on Spring Cloud Config Server retry capabilities, you can reference the [Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-config/spring-cloud-config.html#config-client-retry) documentation.  Regardless of framework and tooling selection, resiliency capabilities, like this configurable retry logic, are critical in microservice-based applications.

#### APIs in this application
You can use cURL or Chrome POSTMAN to send get/post/put/delete requests to the application.
- Get available options for Appetizers  
`http://<hostname>/appetizers`

#### Configuration Options in this application
These options can be set on the command-line, in a Docker Compose environment file, or any other way that [Spring Boot supports](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html):  
- Disable Config Server retry logic, allowing startup without remote configuration data:  
  `spring.cloud.config.failFast=false`

#### Pre-requisite:
- You need a Docker machine running on localhost to host container(s). [Click for instructions](https://docs.docker.com/machine/get-started/).

#### Build the application
1. Clone git repository.
    ```
    git clone https://github.com/ibm-cloud-architecture/refarch-cloudnative-wfd-appetizer
    cd refarch-cloudnative-wfd-appetizer
    ```

2. Build the application.  A utility script is provided to easily build using either Gradle (default) or Maven.  You can optionally specify the `-d` parameter to build the associated Docker image as well.  The default Gradle build instructions use a Gradle wrapper requiring no further installation.  The Maven build instructions require Maven to be installed locally.

  2.1 Build the application using Gradle:
    ```
    ./build-microservice.sh [-d]
    ```

  2.2 Build the application using Maven:
    ```
    ./build-microservice.sh -m [-d]
    ```

#### Run Appetizer Service on localhost
In this section you will deploy the Spring Boot application to run on your localhost.

1.  [Setup Eureka](https://github.com/ibm-cloud-architecture/refarch-cloudnative-netflix-eureka#run-the-application-component-locally) to run locally.

2. Run the application on localhost (assuming default Gradle build).  If Eureka is not running locally, you will need to pass the location of the Eureka server as a command-line paramter.
    ```
    java [-Deureka.client.serviceUrl.defaultZone=http://eureka-host:port/eureka/] -jar build/libs/wfd-appetizer-0.0.1-SNAPSHOT.jar
    ```

3. Validate.
    ```
    curl http://localhost:8082/appetizers
    {"order":1,"menu":["Hummus","Crab Cakes","Mozzerella Sticks"],"type":"appetizer"}
    ```

#### Run Appetizer Service application on local Docker container
In this section you will deploy the Spring Boot application to run in a local docker container.

1. Build service and container image:
    ```
    ./build-microservice.sh -d
    ```

2. If not done so already, [Setup Eureka](https://github.com/ibm-cloud-architecture/refarch-cloudnative-netflix-eureka#run-the-application-component-locally) to run locally.

3. Start the application in docker container.
    ```
    docker run -d -p 8082:8082 --name wfd-appetizer --rm --env "eureka.client.serviceUrl.defaultZone=http://eureka-host:port/eureka/"  wfd-appetizer
    ```

4. Validate.
    ```
    curl http://{docker-host}:8082/appetizers
    {"order":1,"menu":["Hummus","Crab Cakes","Mozzerella Sticks"],"type":"appetizer"}
    ```

#### Deploy Appetizer Service to Cloud Foundry runtime, on IBM Bluemix

1. Log in to your Bluemix account.
    ```
    cf login -a <bluemix-api-endpoint> -u <your-bluemix-user-id>
    ```

2. Set target to use your Bluemix Org and Space.
    ```
    cf target -o <your-bluemix-org> -s <your-bluemix-space>
    ```

3. [Setup Eureka on Bluemix](https://github.com/ibm-cloud-architecture/refarch-cloudnative-netflix-eureka#run-the-application-component-on-bluemix).

4.  Create a user-provided service for Eureka, so the services running in Cloud Foundry can bind to it:

    ```
    cf create-user-provided-service eureka-service-discovery -p "{\"uri\": \"http://{eureka-host}/eureka/\"}"
    ```

5. Start the application in a Cloud Foundry runtime on IBM Bluemix.

  The following commands push the service code to Bluemix and creates a Cloud Foundry application.  It then sets the desired Spring Boot profile for the application to configure itself correctly, as well as binds the user-provided service with the Eureka endpoint information.  Finally, it restages the application code to ensure it receives all the configuration changes and then starts the application.  

  _NOTE_: There is no need for a port in the Eureka parameter, as the Container Group running Eureka is listening on port 80 (the default HTTP port) and will forward to the necessary port of 8761 that Eureka is listening on.  

    ```
    cf push -p build/libs/wfd-appetizer-0.0.1-SNAPSHOT.jar -d mybluemix.net -n wfd-appetizer-{your-bluemix-user-id} --no-start

    cf set-env wfd-appetizer SPRING_PROFILES_ACTIVE cloud

    cf bind-service wfd-appetizer eureka-service-discovery

    cf restage wfd-appetizer

    cf start wfd-appetizer
    ```

6. Validate.  
    ```
    curl http://wfd-appetizer-{your-bluemix-user-id}.mybluemix.net/appetizers
    {"order":1,"menu":["Hummus","Crab Cakes","Mozzerella Sticks"],"type":"appetizer"}
    ```


#### Deploy Appetizer Service to Docker Container, on IBM Bluemix

1. Log in to your Bluemix account.
    ```
    cf login -a <bluemix-api-endpoint> -u <your-bluemix-user-id>
    ```

2. Set target to use your Bluemix Org and Space.
    ```
    cf target -o <your-bluemix-org> -s <your-bluemix-space>
    ```

3. Log in to IBM Containers plugin.
    ```
    cf ic init
    ```

4. Tag and push the local docker image to bluemix private registry.
    ```
    docker tag wfd-appetizer registry.ng.bluemix.net/$(cf ic namespace get)/wfd-appetizer:latest
    docker push registry.ng.bluemix.net/$(cf ic namespace get)/wfd-appetizer:latest
    ```

5. [Setup Eureka on Bluemix](https://github.com/ibm-cloud-architecture/refarch-cloudnative-netflix-eureka#run-the-application-component-on-bluemix).

6. Start the application in an IBM Bluemix Container. Replace `{eureka-host}` with the public route configured in the deployment of Eureka to Bluemix.  

  _NOTE_: There is no need for a port in the Eureka parameter, as the Container Group running Eureka is listening on port 80 (the default HTTP port) and will forward to the necessary port of 8761 that Eureka is listening on.  
    ```
    cf ic group create -p 8082 -m 256 --min 1 --auto --name wfd-appetizer-group -e "--env "eureka.client.serviceUrl.defaultZone=http://eureka-host/eureka/" -n wfd-appetizer-$(cf ic namespace get) -d mybluemix.net registry.ng.bluemix.net/$(cf ic namespace get)/wfd-appetizer:latest
    ```

7. Validate.  
    ```
    curl http://wfd-appetizer-$(cf ic namespace get).mybluemix.net/appetizers
    {"order":1,"menu":["Hummus","Crab Cakes","Mozzerella Sticks"],"type":"appetizer"}
    ```
