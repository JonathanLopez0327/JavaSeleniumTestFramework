package base.driverfactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverCreator implements WebDriverCreator {

    @Override
    public WebDriver createWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--start-maximized");
        options.addArguments("--allow-running-insecure-content");

        return new ChromeDriver(options);
    }
}
