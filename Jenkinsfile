
pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t campus-lost-found:${BUILD_NUMBER} .'
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true,
                  testResults: 'target/surefire-reports/*.xml'

            archiveArtifacts allowEmptyArchive: true,
                            artifacts: 'target/*.jar',
                            fingerprint: true
        }

        success {
            echo 'SUCCESS: Tests passed and Docker image was built.'
        }

        failure {
            echo 'FAILED: Check the Jenkins console output for details.'
        }
    }
}
