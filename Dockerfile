FROM ibmjava:8-jre

RUN apt-get update \
    && apt-get install -y --no-install-recommends unzip \
    && rm -rf /var/lib/apt/lists/*

# Install WebSphere Liberty
ENV LIBERTY_VERSION 2017.9.0_1
RUN LIBERTY_URL=$(wget -q -O - https://public.dhe.ibm.com/ibmdl/export/pub/software/websphere/wasdev/downloads/wlp/index.yml  | grep $LIBERTY_VERSION -A 3 | sed -n 's/\s*webProfile7:\s//p' | tr -d '\r')  \
    && echo $LIBERTY_URL \
    && wget -q $LIBERTY_URL -U UA-IBM-WebSphere-Liberty-Docker -O /tmp/wlp-beta.zip \
    && unzip -q /tmp/wlp-beta.zip -d /opt/ibm \
    && rm /tmp/wlp-beta.zip
ENV PATH=/opt/ibm/wlp/bin:$PATH

# Set Path Shortcuts
ENV LOG_DIR=/logs \
    WLP_OUTPUT_DIR=/opt/ibm/wlp/output
RUN mkdir /logs \
    && ln -s $WLP_OUTPUT_DIR/defaultServer /output \
    && ln -s /opt/ibm/wlp/usr/servers/defaultServer /config

# Configure WebSphere Liberty
RUN /opt/ibm/wlp/bin/server create \
    && rm -rf $WLP_OUTPUT_DIR/.classCache /output/workarea

COPY target/liberty/wlp/usr/servers/defaultServer /config
COPY target/liberty/wlp/usr/shared /opt/ibm/wlp/usr/shared
# Install required features if not present
RUN installUtility install --acceptLicense defaultServer
EXPOSE 9081 9443

CMD ["/opt/ibm/wlp/bin/server", "run", "defaultServer"]