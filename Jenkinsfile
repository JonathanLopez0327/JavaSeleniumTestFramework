pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/JonathanLopez0327/JavaSeleniumTestFramework.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean test'
            }
        }
        stage('Imprimo') {
            steps {
                echo 'All good!'
            }
        }
    }
}