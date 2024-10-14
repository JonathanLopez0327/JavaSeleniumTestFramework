pipeline {
    agent any

    tools {
        maven 'MAVEN'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/JonathanLopez0327/JavaSeleniumTestFramework.git'
                sh 'clean test'
            }
        }
        stage('Print') {
            steps {
                echo 'All good!'
            }
        }
    }
}