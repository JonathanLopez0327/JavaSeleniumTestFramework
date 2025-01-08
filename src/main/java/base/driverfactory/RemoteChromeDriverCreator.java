package base.driverfactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class RemoteChromeDriverCreator implements WebDriverCreator {

    private String url;
    private boolean headless;

    public RemoteChromeDriverCreator(String url, boolean headless) {
        this.url = url;
        this.headless = headless;
    }

    @Override
    public WebDriver createWebDriver() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--start-maximized");
        options.addArguments("--allow-running-insecure-content");

        if (headless) {
            options.addArguments("--headless=new");
        }

        return new RemoteWebDriver(new URL(url), options);
    }
}
