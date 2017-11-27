node {
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


    stage('Build image') {

        app = docker.build("${params.namespace}/wfd-appetizer-spring")
    }

    stage('Push image') {
      /* Finally, we'll push the image with two tags:
      * First, the incremental build number from Jenkins
      * Second, the 'latest' tag.
      * Pushing multiple tags is cheap, as all the layers are reused. */
     docker.withRegistry('https://mycluster.icp:8500', 'icp-credentials') {
         app.push("${env.BUILD_NUMBER}")
         app.push("latest")
     }
   }

    stage('Deploy application') {
      sh '''
        #!/bin/bash
        DEPLOYMENT=`kubectl get deployments | grep wfd-appetizer | awk '{print \$1}'`

        # Update Deployment
        kubectl set image deployment/\${DEPLOYMENT} wfd-appetizer=mycluster.icp:8500/${params.namespace}/wfd-appetizer-spring:${env.BUILD_NUMBER}
        kubectl rollout status deployment/\${DEPLOYMENT}
      '''
    }
}
