podTemplate(label: 'mypod',
    volumes: [
        hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock'),
        secretVolume(secretName: 'registry-account', mountPath: '/var/run/secrets/registry-account'),
        configMapVolume(configMapName: 'registry-config', mountPath: '/var/run/configs/registry-config')
    ],
    containers: [
        containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'docker' , image: 'docker:17.11.0-ce', ttyEnabled: true, command: 'cat')
  ]) {
  
  node("mypod") {
    def app

    stage('Clone repository') {
        /* Let's make sure we have the repository cloned to our workspace */

        checkout scm
    }

    stage('Compile code') {

      withMaven(maven: 'maven') {
        sh 'mvn clean package'
      }
    }

    container("docker") {
      stage('Build image') {
        sh """
        #!/bin/sh 
        NAMESPACE=`cat /var/run/configs/registry-config/namespace` 
        REGISTRY=`cat /var/run/configs/registry-config/registry` 
        
        docker build -t \${REGISTRY}/\${NAMESPACE}/wfd-appetizer-spring:${env.BUILD_NUMBER} .
        """

          
      }

      stage('Push image') {
        sh """
        #!/bin/sh
        NAMESPACE=`cat /var/run/configs/registry-config/namespace`
        REGISTRY=`cat /var/run/configs/registry-config/registry`
        
        set +x
        DOCKER_USER=`cat /var/run/secrets/registry-account/username`
        DOCKER_PASSWORD=`cat /var/run/secrets/registry-account/password`
        docker login -u=\${DOCKER_USER} -p=\${DOCKER_PASSWORD} \${REGISTRY}
        
        set -x
        docker push \${REGISTRY}/\${NAMESPACE}/wfd-appetizer-spring:${env.BUILD_NUMBER}
        """
      }
    }
    container("kubectl") {
      stage('Deploy application') {
        sh """
          #!/bin/bash
          NAMESPACE=`cat /var/run/configs/registry-config/namespace`
          REGISTRY=`cat /var/run/configs/registry-config/registry`
          
          DEPLOYMENT=`kubectl get deployments | grep wfd-appetizer | awk '{print \$1}'`

          # Update Deployment
          kubectl set image deployment/\${DEPLOYMENT} wfd-appetizer=\${REGISTRY}/\${NAMESPACE}/wfd-appetizer-spring:${env.BUILD_NUMBER}
          kubectl rollout status deployment/\${DEPLOYMENT}
        """
      }
    }
  }
}
