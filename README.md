# Java Selenium Test Framework

Este proyecto es un marco de pruebas automatizadas para aplicaciones web utilizando Selenium WebDriver con Java. Está diseñado para facilitar la creación y ejecución de pruebas automatizadas, garantizando la calidad y funcionalidad de las aplicaciones web.

## Características

- **Selenium WebDriver**: Para la automatización de navegadores web. :contentReference[oaicite:0]{index=0}
- **TestNG**: Framework de pruebas utilizado para la gestión y ejecución de casos de prueba.
- **Maven**: Gestión de dependencias y construcción del proyecto.
- **Jenkins**: Integración continua para la ejecución automatizada de pruebas.
- **Docker**: Contenedorización del entorno de pruebas para garantizar la consistencia.

## Estructura del Proyecto

- **src/**: Contiene el código fuente del proyecto.
    - **main/**: Código principal.
    - **test/**: Casos de prueba automatizados.
- **pom.xml**: Archivo de configuración de Maven que gestiona las dependencias del proyecto.
- **TestSuite.xml**: Archivo de configuración de TestNG que define la suite de pruebas a ejecutar.
- **Jenkinsfile**: Script para la configuración de Jenkins y la integración continua.
- **docker-compose.yml**: Archivo para la configuración de los servicios Docker necesarios.

## Requisitos Previos

- **Java**: JDK 8 o superior.
- **Maven**: Para la gestión del proyecto y dependencias.
- **Docker**: Para la ejecución de contenedores.
- **Jenkins**: (Opcional) Para la integración continua.

## Instalación

1. Clona este repositorio:

   ```bash
   git clone https://github.com/JonathanLopez0327/JavaSeleniumTestFramework.git

2. Navega al directorio del proyecto:

   ```bash
   cd JavaSeleniumTestFramework

3. Construye el proyecto utilizando Maven:

   ```bash
   mvn clean install

## Ejecución de Pruebas
1. Con Maven:
   ```bash
   mvn test
```
2. Con TestNG:
   ```bash
   mvn test -DsuiteXmlFile=TestSuite.xml
```
