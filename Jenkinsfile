pipeline {
    agent any

    tools {
        maven 'Maven 3.9.9'
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