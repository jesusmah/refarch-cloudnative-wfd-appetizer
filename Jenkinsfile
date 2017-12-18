podTemplate(label: 'mypod',
    volumes: [
        hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock'),
    ],
    containers: [
        containerTemplate(name: 'maven', image: 'maven:3.5.2-alpine', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'docker' , image: 'docker:17.11.0-ce', ttyEnabled: true, command: 'cat')
  ]) {

  node("mypod") {
    def app

    stage('Environment check') {
    /* Check if all environment requirements are present before going ahead */

    sh """
    #!/bin/bash
    if [ "${params.docker_registry}" = "null" ]; then
      echo "[ERROR]: A Docker registry has not been declared. Please declare your Docker registry in your Jenkins docker_registry pipeline variable"
      exit 1
    fi
    if [ "${params.namespace}" = "null" ]; then
      echo "[ERROR]: A namespace has not been declared. Please declare a namespace in your Jenkins docker_registry pipeline variable"
      exit 1
    fi
    if [ "${params.release_name}" = "null" ]; then
      echo "[ERROR]: A release name has not been declared. Please declare a release name in your Jenkins docker_registry pipeline variable"
      exit 1
    fi
    """

    }

    stage('Clone repository') {
        /* Let's make sure we have the repository cloned to our workspace */

        checkout scm
    }

    container("maven") {
      stage('Compile code') {
        sh '''
        #!/bin/bash
        set +x
        printenv | grep MAVEN
          echo "BLABLABLABLA"
          echo "${params.release_name}"
        printenv | grep MAVEN
        exit 1
        mvn clean package
        '''
      }
    }

    container("docker") {
      stage('Build image') {
        sh """
        #!/bin/bash

        docker build -t ${params.docker_registry}/${params.namespace}/wfd-appetizer-spring:${env.BUILD_NUMBER} .
        """

      }

      stage('Push image') {
        withCredentials([usernamePassword(credentialsId: 'icp_credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
          sh """
          #!/bin/bash

          set +x
          docker login -u=${USERNAME} -p=${PASSWORD} ${params.docker_registry}
          set -x

          docker push ${params.docker_registry}/${params.namespace}/wfd-appetizer-spring:${env.BUILD_NUMBER}
          """
        }
      }
    }
    container("kubectl") {
      stage('Deploy application') {
        sh """
          #!/bin/bash

          DEPLOYMENT=`kubectl get deployments | grep ${params.release_name} | grep wfd-appetizer | awk '{print \$1}'`

          # Update Deployment
          kubectl set image deployment/\${DEPLOYMENT} wfd-appetizer=${params.docker_registry}/${params.namespace}/wfd-appetizer-spring:${env.BUILD_NUMBER}
          kubectl rollout status deployment/\${DEPLOYMENT}
        """
      }
    }
  }
}
