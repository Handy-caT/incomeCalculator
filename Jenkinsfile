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
            sh 'mvn -B -DskipTests clean package'
          }
        }
        stage('Test') {
          parallel {
            stage('CardApi Tests') {
              steps {
                sh './test -c'
              }
              post {
                always {
                  junit 'target/surefire-reports/*.xml'
                }
              }
            }
            stage('UserApi Tests') {
              steps {
                sh './test -u'
              }
              post {
                always {
                  junit 'target/surefire-reports/*.xml'
                }
              }
            }
            stage('Gateway Tests') {
              steps {
                sh './test -g'
              }
              post {
                always {
                  junit 'target/surefire-reports/*.xml'
                }
              }
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