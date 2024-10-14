pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/JonathanLopez0327/JavaSeleniumTestFramework.git'
                 sh 'mvn clean test'
            },

        }
        stage('Print') {
            steps {
                echo 'All good!'
            }
        }
    }
}