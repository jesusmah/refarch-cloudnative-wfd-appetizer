FROM websphere-liberty:microProfile

MAINTAINER IBM Java engineering at IBM Cloud

COPY /target/liberty/wlp/usr/servers/defaultServer /config/
COPY target/liberty/wlp/usr/shared /opt/ibm/wlp/usr/shared/

RUN wget -t 10 -x -nd -P /opt/ibm/wlp/usr https://repo1.maven.org/maven2/net/wasdev/wlp/tracer/liberty-opentracing-zipkintracer/1.0/liberty-opentracing-zipkintracer-1.0-sample.zip && cd /opt/ibm/wlp/usr && unzip liberty-opentracing-zipkintracer-1.0-sample.zip && rm liberty-opentracing-zipkintracer-1.0-sample.zip

# Install required features if not present
RUN installUtility install --acceptLicense defaultServer

EXPOSE 9081 9443

CMD ["/opt/ibm/wlp/bin/server", "run", "defaultServer"]
