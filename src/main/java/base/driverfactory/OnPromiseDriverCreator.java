package base.driverfactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class OnPromiseDriverCreator implements WebDriverCreator {
    @Override
    public WebDriver createWebDriver() {
        System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--start-maximized");
        options.addArguments("--allow-running-insecure-content");

        return new ChromeDriver(options);
    }
}
