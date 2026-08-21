pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        SCANNER_HOME = tool 'SonarScanner'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/prempande001/online-voting-system.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat """
                    %SCANNER_HOME%\\bin\\sonar-scanner.bat ^
                    -Dsonar.projectKey=online-voting-app ^
                    -Dsonar.projectName=online-voting-app ^
                    -Dsonar.sources=. ^
                    -Dsonar.java.binaries=target/classes
                    """
                }
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t online-voting-app:latest .'
            }
        }
	
    }
}