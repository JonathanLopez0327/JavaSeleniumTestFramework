pipeline {
    agent any

    environment {
        SELENIUM_HUB_IMAGE = 'selenium/hub:4.0.0'  // Versión del Selenium Grid Hub
        SELENIUM_NODE_IMAGE = 'selenium/node-chrome:4.0.0' // Nodos del Grid (Chrome en este caso)
        GIT_REPO = 'https://github.com/JonathanLopez0327/JavaSeleniumTestFramework.git' // Reemplaza con tu repositorio
        REPORT_DIR = 'target/surefire-reports' // Carpeta donde Maven genera los reportes por defecto
    }

    stages {
        stage('Set Up Selenium Grid') {
            steps {
                script {
                    // Levantar Selenium Grid Hub y Nodo Chrome usando Docker Compose
                    sh '''
                    docker network create selenium-grid
                    docker run -d -p 4444:4444 --network selenium-grid --name selenium-hub ${SELENIUM_HUB_IMAGE}
                    docker run -d --network selenium-grid --link selenium-hub:hub ${SELENIUM_NODE_IMAGE}
                    '''
                }
            }
        }

        stage('Clone Project') {
            steps {
                // Clonar el repositorio desde Git
                git branch: 'main', url: "${GIT_REPO}"
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Ejecutar pruebas con Maven, conectando al Selenium Grid Hub
                    sh 'mvn clean test -Dselenium.hub=http://localhost:4444/wd/hub'
                }
            }
        }
    }

    post {
        always {
            script {
                // Detener y eliminar los contenedores de Selenium Grid
                sh '''
                docker stop selenium-hub
                docker rm selenium-hub
                docker network rm selenium-grid
                '''
            }
        }
        cleanup {
            cleanWs() // Limpiar el workspace de Jenkins después de la ejecución
        }
    }
}
