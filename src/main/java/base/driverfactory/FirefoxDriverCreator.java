package base.driverfactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxDriverCreator implements WebDriverCreator {

    @Override
    public WebDriver createWebDriver() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--start-maximized");
        options.addArguments("--allow-running-insecure-content");
        return new FirefoxDriver(options);
    }
}
