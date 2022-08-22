pipeline {
  agent none
  options {
    skipStagesAfterUnstable()
    skipDefaultCheckout()
  }
  stages {
    stage('Prepare Container') {
      agent {
        docker {
          image 'openjdk:8-jdk-alpine'
          args '-v /root/.m2:/root/.m2'
        }
      }
      stages {
        stage('Checkout') {
          steps {
            checkout scm
          }
        }
        stage('Build') {
          steps {
            sh './build -C'
          }
        }
      }
    }

  }
}