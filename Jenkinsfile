pipeline {
    agent any

    tools {
        maven 'MAVE-3.9.9' // Instalar Maven en el agente
    }

    environment {
        SELENIUM_HUB_IMAGE = 'selenium/hub:4.0.0'
        SELENIUM_NODE_IMAGE = 'selenium/node-chrome:4.0.0'
        GIT_REPO = 'https://github.com/JonathanLopez0327/JavaSeleniumTestFramework.git'
        REPORT_DIR = 'Reports'
        GRID_URL = 'http://localhost:4444/wd/hub'
        SONARQUBE_SERVER = 'sonarqube'
    }

    stages {
        stage('Preparation') {
            steps {
                script {
                    sh 'docker network create selenium-grid'
                }
            }
        }

        stage('Create and Execute Selenium Hub') {
            steps {
                script {
                    sh '''
                    docker run -d --name selenium-hub --network selenium-grid -p 4444:4444 \
                      -e GRID_MAX_SESSION=16 \
                      -e GRID_BROWSER_TIMEOUT=300 \
                      -e GRID_TIMEOUT=300 \
                      ${SELENIUM_HUB_IMAGE}
                    '''
                }
            }
        }

        stage('Create and Execute Chrome Node') {
            steps {
                script {
                    sh '''
                    docker run -d --name chrome --network selenium-grid --link selenium-hub:hub \
                      -e SE_EVENT_BUS_HOST=selenium-hub \
                      -e SE_EVENT_BUS_PUBLISH_PORT=4442 \
                      -e SE_EVENT_BUS_SUBSCRIBE_PORT=4443 \
                      -e NODE_MAX_SESSION=2 \
                      -e NODE_MAX_INSTANCES=2 \
                      ${SELENIUM_NODE_IMAGE}
                    '''
                }
            }
        }

        stage('Clone Project') {
            steps {
                git branch: 'main', url: "${GIT_REPO}"
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    // Ejecutar el análisis de SonarQube
                    withSonarQubeEnv(SONARQUBE_SERVER) {
                        sh 'mvn clean install sonar:sonar' // Comando para Maven
                    // O usa el comando adecuado para tu proyecto (Gradle, npm, etc.)
                    }
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    sh 'mvn clean test'
                }
            }
        }

        stage('Archive Reports') {
            steps {
                script {
                    publishHTML([
                        reportDir: "${REPORT_DIR}",
                        reportFiles: 'Test-Report-*.html', // Remover la ruta y usar solo el nombre del archivo
                        reportName: 'Test Report',
                        keepAll: true,
                        alwaysLinkToLastBuild: true,
                        allowMissing: true
                    ])
                }
            }
        }
    }

    post {
        always {
            script {
                // Detener y eliminar los contenedores de Selenium Grid
                sh '''
                docker stop selenium-hub chrome
                docker rm selenium-hub chrome
                docker network rm selenium-grid
                '''
            }
        }

        cleanup {
            cleanWs() // Limpiar el workspace de Jenkins después de la ejecución
        }
    }
}
