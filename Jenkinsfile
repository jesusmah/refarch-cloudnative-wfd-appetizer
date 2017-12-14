podTemplate(label: 'mypod',
    volumes: [
        hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock'),
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
        docker build -t ${params.docker_registry}/${params.namespace}/wfd-appetizer-spring:${env.BUILD_NUMBER} .
        """

      }

      stage('Push image') {
        withCredentials([usernamePassword(credentialsId: 'icp_credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
          sh """
          #!/bin/sh

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

          DEPLOYMENT=`kubectl get deployments | grep wfd-appetizer | awk '{print \$1}'`

          # Update Deployment
          kubectl set image deployment/\${DEPLOYMENT} wfd-appetizer=${params.docker_registry}/${params.namespace}/wfd-appetizer-spring:${env.BUILD_NUMBER}
          kubectl rollout status deployment/\${DEPLOYMENT}
        """
      }
    }
  }
}
