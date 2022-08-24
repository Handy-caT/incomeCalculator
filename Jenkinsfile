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
          args '-v /home/.m2:/root/.m2'
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
          parallel {
            stage('CardApi Tests') {
              steps {
                unstash 'compiled'
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
                unstash 'compiled'
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
                unstash 'compiled'
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

