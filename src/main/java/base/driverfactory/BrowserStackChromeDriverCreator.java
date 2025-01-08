package base.driverfactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserStackChromeDriverCreator implements WebDriverCreator {
    private final boolean headless;

    public BrowserStackChromeDriverCreator(boolean headless) {
        this.headless = headless;
    }

    @Override
    public WebDriver createWebDriver() throws MalformedURLException {
        // Configuración de ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--start-maximized");
        options.addArguments("--allow-running-insecure-content");

        if (headless) {
            options.addArguments("--headless=new");
        }

        // Configuración de DesiredCapabilities para BrowserStack
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("browserVersion", "latest");
        caps.setCapability("os", "Windows");
        caps.setCapability("osVersion", "10");
        caps.setCapability("resolution", "1920x1080");
        caps.setCapability("name", "BrowserStack Test");
        caps.setCapability("build", "Build #1");
        caps.setCapability("project", "Selenium BrowserStack Integration");

        // Combinar ChromeOptions con DesiredCapabilities
        caps.setCapability(ChromeOptions.CAPABILITY, options);

        // Credenciales y Hub de BrowserStack
        String browserstackUrl = getBrowserStackUrl();
        return new RemoteWebDriver(new URL(browserstackUrl), caps);
    }

    // Método para construir la URL del hub con credenciales
    private String getBrowserStackUrl() {
        String username = System.getenv("BROWSERSTACK_USERNAME");
        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");

        if (username == null || accessKey == null) {
            throw new IllegalStateException("BrowserStack credentials are missing. Set BROWSERSTACK_USERNAME and BROWSERSTACK_ACCESS_KEY as environment variables.");
        }

        return "https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub";
    }
}