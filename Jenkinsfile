pipeline {
    agent any

    tools {
        maven 'MAVEN'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/JonathanLopez0327/JavaSeleniumTestFramework.git'
                sh 'mvn clean test'
            }
        }
        stage('Print') {
            steps {
                echo 'All good!'
            }
        }
    }
}