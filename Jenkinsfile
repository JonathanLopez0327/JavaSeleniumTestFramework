pipeline {
    agent any

    tools {
        maven 'MAVEN'
    }

     stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/JonathanLopez0327/JavaSeleniumTestFramework.git'
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