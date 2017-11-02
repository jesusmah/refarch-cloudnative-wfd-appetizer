# Microservices Reference Application - What's For Dinner

## Appetizer Service - MicroProfile

This repository contains the **Java MicroProfile** implementation of the **Appetizer Service** which is a part of microservice-based reference application called **What's For Dinner** that can be found in https://github.com/ibm-cloud-architecture/refarch-cloudnative-wfd

<p align="center">
  <a href="https://microprofile.io/">
    <img src="https://github.com/ibm-cloud-architecture/refarch-cloudnative-wfd/blob/microprofile/static/imgs/microprofile_small.png">
  </a>
</p>

### Introduction

This project demonstrates the implementation of Appetizer Microservice. The appetizer microservice retrieves the list of appetizers from its data store.

- Based on [MicroProfile](https://microprofile.io/).
- Integrated with the [MicroService Builder](https://developer.ibm.com/microservice-builder/).
- Deployment options for local, Docker Container-based runtimes, Minikube environment and ICP/BMX.

### How it works

Appetizer Microservice serves [**What's For Dinner**](https://github.com/ibm-cloud-architecture/refarch-cloudnative-wfd), Microservice-based reference application. Though it is a part of a bigger application, Appetizer service is itself an application in turn that gets the data from data store. This service reads the data and returns it through a REST service.

### Endpoints

```
GET     /WfdAppetizer/rest/health/        # Health check of the service
GET     /WfdAppetizer/rest/appetizer/     # Returns the appetizers available
```

### Implementation

#### [MicroProfile](https://microprofile.io/)

MicroProfile is an open platform that optimizes the Enterprise Java for microservices architecture. In this application, we are using [**MicroProfile 1.2**](https://github.com/eclipse/microprofile-bom). This includes

- MicroProfile 1.0 ([JAX-RS 2.0](https://jcp.org/en/jsr/detail?id=339), [CDI 1.2](https://jcp.org/en/jsr/detail?id=346), and [JSON-P 1.0](https://jcp.org/en/jsr/detail?id=353))
- MicroProfile 1.1 (MicroProfile 1.0, [MicroProfile Config 1.0.](https://github.com/eclipse/microprofile-config))
- [MicroProfile Config 1.1](https://github.com/eclipse/microprofile-config) (supercedes MicroProfile Config 1.0), [MicroProfile Fault Tolerance 1.0](https://github.com/eclipse/microprofile-fault-tolerance), [MicroProfile Health Check 1.0](https://github.com/eclipse/microprofile-health), [MicroProfile Metrics 1.0](https://github.com/eclipse/microprofile-metrics), [MicroProfile JWT Authentication 1.0](https://github.com/eclipse/microprofile-jwt-auth).

You can make use of this feature by including this dependency in Maven.

```
<dependency>
<groupId>org.eclipse.microprofile</groupId>
<artifactId>microprofile</artifactId>
<version>1.2</version>
<type>pom</type>
<scope>provided</scope>
</dependency>
```

You should also include a feature in [server.xml](https://github.com/ibm-cloud-architecture/refarch-cloudnative-wfd-appetizer/blob/microprofile/src/main/liberty/config/server.xml).

```
<server description="Sample Liberty server">

  <featureManager>
      <feature>microprofile-1.2</feature>
  </featureManager>

  <httpEndpoint httpPort="${default.http.port}" httpsPort="${default.https.port}"
      id="defaultHttpEndpoint" host="*" />

</server>
```

#### Maven build

Maven is a project management tool that is based on the Project Object Model (POM). Typically, people use Maven for project builds, dependencies, and documentation. Maven simplifies the project build. In this task, you use Maven to build the project.

For Liberty, there is nice tool called [Liberty Accelerator](https://liberty-app-accelerator.wasdev.developer.ibm.com/start/) that generates a simple project based upon your configuration. Using this, you can build and deploy to Liberty either using the Maven or Gradle build.

<p align="center">
  <a href="https://microprofile.io/">
    <img src="https://github.com/ibm-cloud-architecture/refarch-cloudnative-wfd/blob/master/static/imgs/LibertyAcc_Home.png">
  </a>
</p>

Just check the options of your choice and click Generate project. You can either Download it as a zip or you can create git project.

<p align="center">
  <a href="https://microprofile.io/">
    <img src="https://github.com/ibm-cloud-architecture/refarch-cloudnative-wfd/blob/master/static/imgs/LibertyAcc_PrjGen.png">
  </a>
</p>

Once you are done with this, you will have a sample microprofile based application that you can deploy on Liberty.

If you are using Liberty accelerator, look for the generated POM. It may contain unwanted dependencies. Please remove accordingly.

For our application, the following are the required dependencies which are the part of auto-generated POM. Please remove the rest of the dependencies.

```
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.apache.cxf</groupId>
    <artifactId>cxf-rt-rs-client</artifactId>
    <version>3.1.11</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.glassfish</groupId>
    <artifactId>javax.json</artifactId>
    <version>1.0.4</version>
    <scope>test</scope>
</dependency>
```

Using Liberty Accelerator is your choice. You can also create the entire project manually, but using Liberty Accelerator will make things easier.

To make use of MicroProfile 1.2, include the below dependency in your POM.

```
<dependency>
    <groupId>org.eclipse.microprofile</groupId>
    <artifactId>microprofile</artifactId>
    <version>1.2</version>
    <type>pom</type>
    <scope>provided</scope>
</dependency>
```

If you want to enable Zipkin OpenTracing feature, please include the below dependency in your POM.

```
<dependency>
    <groupId>net.wasdev.wlp.tracer</groupId>
    <artifactId>liberty-opentracing-zipkintracer</artifactId>
    <version>1.0</version>
    <type>jar</type>
    <scope>provided</scope>
</dependency>
```

If you are planning to include zipkin tracer in your application, please add the below feature to your [server.xml](https://github.com/ibm-cloud-architecture/refarch-cloudnative-wfd-appetizer/blob/microprofile/src/main/liberty/config/server.xml).

```
<feature>opentracingZipkin-0.30</feature>
```

##### Running the application locally using Maven Build

1. Clone this repository.

   `git clone https://github.com/ibm-cloud-architecture/refarch-cloudnative-wfd-appetizer.git`

2. Checkout MicroProfile branch.

   `git checkout microprofile`

3. `cd refarch-cloudnative-wfd-appetizer/`

4. Run this command. This command builds the project and installs it.

   `mvn install`

   If this runs successfully, you will be able to see the below messages.

```
[INFO] --- maven-failsafe-plugin:2.18.1:verify (verify-results) @ WfdAppetizer ---
[INFO] Failsafe report directory: /Users/Hemankita.Perabathini@ibm.com/PurpleCompute/Microprofile/WhatsForDinner/refarch-cloudnative-wfd-appetizer/target/test-reports/it
[INFO]
[INFO] --- maven-install-plugin:2.4:install (default-install) @ WfdAppetizer ---
[INFO] Installing /Users/Hemankita.Perabathini@ibm.com/PurpleCompute/Microprofile/WhatsForDinner/refarch-cloudnative-wfd-appetizer/target/WfdAppetizer-1.0-SNAPSHOT.war to /Users/Hemankita.Perabathini@ibm.com/.m2/repository/projects/WfdAppetizer/1.0-SNAPSHOT/WfdAppetizer-1.0-SNAPSHOT.war
[INFO] Installing /Users/Hemankita.Perabathini@ibm.com/PurpleCompute/Microprofile/WhatsForDinner/refarch-cloudnative-wfd-appetizer/pom.xml to /Users/Hemankita.Perabathini@ibm.com/.m2/repository/projects/WfdAppetizer/1.0-SNAPSHOT/WfdAppetizer-1.0-SNAPSHOT.pom
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 20.769 s
[INFO] Finished at: 2017-10-31T16:08:12-05:00
[INFO] Final Memory: 19M/305M
[INFO] ------------------------------------------------------------------------
```
5. Now start your server.

   `mvn liberty:start-server`

   You will see the below.
```
[INFO] Starting server defaultServer.
[INFO] Server defaultServer started with process ID 64952.
[INFO] Waiting up to 30 seconds for server confirmation:  CWWKF0011I to be found in /Users/Hemankita.Perabathini@ibm.com/PurpleCompute/Microprofile/WhatsForDinner/refarch-cloudnative-wfd-appetizer/target/liberty/wlp/usr/servers/defaultServer/logs/messages.log
[INFO] CWWKM2010I: Searching for CWWKF0011I in /Users/Hemankita.Perabathini@ibm.com/PurpleCompute/Microprofile/WhatsForDinner/refarch-cloudnative-wfd-appetizer/target/liberty/wlp/usr/servers/defaultServer/logs/messages.log. This search will timeout after 30 seconds.
[INFO] CWWKM2015I: Match number: 1 is [10/31/17 16:12:07:756 CDT] 00000019 com.ibm.ws.kernel.feature.internal.FeatureManager            A CWWKF0011I: The server defaultServer is ready to run a smarter planet..
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 16.650 s
[INFO] Finished at: 2017-10-31T16:12:07-05:00
[INFO] Final Memory: 9M/309M
[INFO] ------------------------------------------------------------------------
```
6. Now, go to your browser and access the REST endpoint at http://localhost:9080/WfdAppetizer/rest/appetizer.

<p align="center">
  <a href="https://microprofile.io/">
    <img src="https://github.com/ibm-cloud-architecture/refarch-cloudnative-wfd/blob/master/static/imgs/AppetizerScreen.png">
  </a>
</p>

   Access URL : `http://<HOST>:<PORT>/<WAR_CONTEXT>/<APPLICATION_PATH>/<ENDPOINT>`

In our sample application, you can get the details of the above URL as follows.

- Since, we are running the application locally on our system, the **HOST** will be `localhost`.
- You can get the **PORT** and **WAR_CONTEXT** from the `<properties> </properties>` section of our POM.

```
<app.name>WfdAppetizer</app.name>
<testServerHttpPort>9080</testServerHttpPort>
<testServerHttpsPort>9443</testServerHttpsPort>
<warContext>${app.name}</warContext>
```
So, **PORT** will be `9080` and **WAR_CONTEXT** will be `WfdAppetizer`.

- **APPLICATION_PATH** can be found in [AppetizerApplication.java](https://github.com/ibm-cloud-architecture/refarch-cloudnative-wfd-appetizer/blob/microprofile/src/main/java/application/rest/AppetizerApplication.java)

```
@ApplicationPath("/rest")
```

In our sample, the **APPLICATION_PATH** will be `rest`.

- Finally you can find the appetizer endpoint at [AppetizerResource.java](https://github.com/ibm-cloud-architecture/refarch-cloudnative-wfd-appetizer/blob/microprofile/src/main/java/application/rest/AppetizerResource.java)

```
@Path("appetizer")
```
So, **ENDPOINT** to access the rest api that exposes the list of appetizers is `appetizer`.

Also, there is one more endpoint defined at [HealthEndpoint.java](https://github.com/ibm-cloud-architecture/refarch-cloudnative-wfd-appetizer/blob/microprofile/src/main/java/application/rest/HealthEndpoint.java)

```
@Path("health")
```

To access the health api, replace the **ENDPOINT** with `health`. This endpoint gives the health of your application. To check this, use http://localhost:9080/WfdAppetizer/rest/health.

7. If you are done accessing the application, you can stop your server using the following command.

   `mvn liberty:stop-server`

Once you do this, you see the below messages.

```
[INFO] CWWKM2001I: Invoke command is [/Users/Hemankita.Perabathini@ibm.com/PurpleCompute/Microprofile/WhatsForDinner/refarch-cloudnative-wfd-appetizer/target/liberty/wlp/bin/server, stop, defaultServer].
[INFO] objc[65225]: Class JavaLaunchHelper is implemented in both /Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/bin/java (0x1048254c0) and /Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/libinstrument.dylib (0x10491f4e0). One of the two will be used. Which one is undefined.
[INFO] Stopping server defaultServer.
[INFO] Server defaultServer stopped.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 1.395 s
[INFO] Finished at: 2017-10-31T16:12:35-05:00
[INFO] Final Memory: 9M/309M
[INFO] ------------------------------------------------------------------------
```

#### Docker file

We are using Docker to containerize the application. With Docker, you can pack, ship, and run applications on a portable, lightweight container that can run anywhere virtually.

```
FROM websphere-liberty:microProfile

MAINTAINER IBM Java engineering at IBM Cloud

COPY /target/liberty/wlp/usr/servers/defaultServer /config/
COPY target/liberty/wlp/usr/shared /opt/ibm/wlp/usr/shared/

RUN wget -t 10 -x -nd -P /opt/ibm/wlp/usr https://repo1.maven.org/maven2/net/wasdev/wlp/tracer/liberty-opentracing-zipkintracer/1.0/liberty-opentracing-zipkintracer-1.0-sample.zip && cd /opt/ibm/wlp/usr && unzip liberty-opentracing-zipkintracer-1.0-sample.zip && rm liberty-opentracing-zipkintracer-1.0-sample.zip

# Install required features if not present
RUN installUtility install --acceptLicense defaultServer

CMD ["/opt/ibm/wlp/bin/server", "run", "defaultServer"]
```

- The `FROM` instruction sets the base image. You're setting the base image to `websphere-liberty:microProfile`.
- The `MAINTAINER` instruction sets the Author field. Here it is `IBM Java engineering at IBM Cloud`.
- The `COPY` instruction copies directories and files from a specified source to a destination in the container file system.
  - You're copying the `/target/liberty/wlp/usr/servers/defaultServer` to the `config` directory in the container.
  - You're replacing the contents of `/opt/ibm/wlp/usr/shared/` with the contents of `target/liberty/wlp/usr/shared`.
- The `RUN` instruction runs the commands.
  - The first instruction gets the Opentracing Zipkin feature and installs it in your server.
  - The second instruction is a precondition to install all the utilities in the server.xml file. You can use the RUN command to install the utilities on the base image.
- The `CMD` instruction provides defaults for an executing container.

##### Running the application locally in a container

1. Build the docker image.

`docker build -t wfd-appetizer:microprofile .`

Once this is done, you will see something similar to the below messages.
```
Successfully built 83722dbad66c
Successfully tagged wfd-appetizer:microprofile
```
You can see the docker images by using this command.

`docker images`

```
REPOSITORY                     TAG                 IMAGE ID            CREATED             SIZE
wfd-appetizer                  microprofile        83722dbad66c        2 minutes ago       379MB
```

2. Run the docker image.

`docker run -p 9080:9080 --name appetizer -t wfd-appetizer:microprofile`

When it is done, you will see the following output.
```
[AUDIT   ] CWWKZ0058I: Monitoring dropins for applications.
[AUDIT   ] CWWKT0016I: Web application available (default_host): http://24d5dd34c35a:9080/ibm/api/
[AUDIT   ] CWWKT0016I: Web application available (default_host): http://24d5dd34c35a:9080/health/
[AUDIT   ] CWWKT0016I: Web application available (default_host): http://24d5dd34c35a:9080/metrics/
[AUDIT   ] CWWKT0016I: Web application available (default_host): http://24d5dd34c35a:9080/jwt/
[AUDIT   ] CWWKT0016I: Web application available (default_host): http://24d5dd34c35a:9080/WfdAppetizer/
[AUDIT   ] CWWKZ0001I: Application WfdAppetizer-1.0-SNAPSHOT started in 1.062 seconds.
[AUDIT   ] CWWKF0012I: The server installed the following features: [microProfile-1.2, mpFaultTolerance-1.0, servlet-3.1, ssl-1.0, jndi-1.0, mpHealth-1.0, appSecurity-2.0, jsonp-1.0, mpConfig-1.1, jaxrs-2.0, jaxrsClient-2.0, concurrent-1.0, jwt-1.0, mpMetrics-1.0, mpJwt-1.0, json-1.0, cdi-1.2, distributedMap-1.0].
[AUDIT   ] CWWKF0011I: The server defaultServer is ready to run a smarter planet
```
3. Now, view the REST endpoint at http://localhost:9080/WfdAppetizer/rest/appetizer.

<p align="center">
  <a href="https://microprofile.io/">
    <img src="https://github.com/ibm-cloud-architecture/refarch-cloudnative-wfd/blob/master/static/imgs/AppetizerScreen.png">
  </a>
</p>

   Access URL : `http://<HOST>:<PORT>/<WAR_CONTEXT>/<APPLICATION_PATH>/<ENDPOINT>`

5. Once you make sure the application is working as expected, you can come out of the process. You can do this by pressing Ctrl+C on the command line where the server was started.

6. You can also remove the container if desired. This can be done in the following way.

`docker ps`

```
CONTAINER ID        IMAGE                        COMMAND                  CREATED             STATUS              PORTS                              NAMES
82865cf36207        wfd-appetizer:microprofile   "/opt/ibm/wlp/bin/..."   About an hour ago   Up About an hour    0.0.0.0:9080->9080/tcp, 9443/tcp   appetizer
```

Grab the container id.

- Do `docker stop <CONTAINER ID>`
In this case it will be, `docker stop 82865cf36207`
- Do `docker rm <CONTAINER ID>`
In this case it will be, `docker rm 82865cf36207`
