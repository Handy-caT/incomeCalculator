pipeline {
  agent none
  options {
    skipStagesAfterUnstable()
    skipDefaultCheckout()
  }
  stages {
    stage('Checkout') {
        agent any
        steps {
          checkout scm
          stash(name: 'source',includes: '**')
        }
    }

    stage('Prepare Container') {
      agent {
        docker {
          image 'maven:3.8.6-jdk-8'
          args '-v /root/.m2:/root/.m2'
        }
      }
      stages {
        stage('Build') {
          steps {
            unstash 'source'
            sh './build -C'
            stash(name: 'compiled',includes: '**')
          }
        }
        stage('Test') {
          steps {
            unstash 'compiled'
            sh './test -A'
          }
          post {
            always {
              junit 'CardApi/target/surefire-reports/*.xml'
              junit 'UserApi/target/surefire-reports/*.xml'
              junit 'Core/target/surefire-reports/*.xml'
            }
          }
        }
        stage('End') {
          steps {
            sh 'echo "End"'
          }
        }
      }
    }
  }
 }

