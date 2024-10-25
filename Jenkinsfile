pipeline {
    agent any

    tools {
        maven 'MAVE-3.9.9' // Instalar Maven en el agente
    }

    environment {
        SELENIUM_HUB_IMAGE = 'selenium/hub:4.0.0'  // Versión del Selenium Grid Hub
        SELENIUM_NODE_IMAGE = 'selenium/node-chrome:4.0.0' // Nodos del Grid (Chrome en este caso)
        GIT_REPO = 'https://github.com/JonathanLopez0327/JavaSeleniumTestFramework.git' // Reemplaza con tu repositorio
        REPORT_DIR = 'Reports' // Carpeta donde Maven genera los reportes por defecto
        GRID_URL = 'http://localhost:4444/wd/hub' // URL del Selenium Grid Hub
    }

    stages {

        stage('Preparation') {
            steps {
                script {
                    // Crear red de Docker para los contenedores de Selenium Grid
                    sh 'docker network create selenium-grid'
                }
            }
        }

        stage('Create and Execute Selenium Hub') {
            steps {
                script {
                    // Levantar Selenium Grid Hub y Nodo Chrome usando Docker Compose
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
                    // Levantar Selenium Grid Hub y Nodo Chrome usando Docker Compose
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
                // Clonar el repositorio desde Git
                git branch: 'main', url: "${GIT_REPO}"
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Ejecutar pruebas con Maven, conectando al Selenium Grid Hub
                    sh 'mvn clean test'
                }
            }
        }

         stage('Archive Reports') {

            steps {
                 script {
                    publishHTML([
                        reportDir: "${REPORT_DIR}",
                        reportFiles: sh(script: "ls ${REPORT_DIR}/test-report*.html", returnStdout: true).trim(), // Remover la ruta y usar solo el nombre del archivo
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
