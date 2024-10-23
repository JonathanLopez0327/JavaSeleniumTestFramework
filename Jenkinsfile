pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/JonathanLopez0327/JavaSeleniumTestFramework.git'
            }
        }
        stage('Pull Maven Image') {
            steps {
                sh 'docker pull maven:latest'
            }
        }
        stage('Start Selenium Grid') {
            steps {
                sh 'docker-compose -f docker-compose.yml up -d'
            }
        }
        stage('Build and Test') {
            steps {
                bat 'mvn clean test'
            }
        }
        stage('Print') {
            steps {
                echo 'All good!'
            }
        }
    }
}