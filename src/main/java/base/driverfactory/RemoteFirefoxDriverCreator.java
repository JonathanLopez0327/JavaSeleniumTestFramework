package base.driverfactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class RemoteFirefoxDriverCreator implements WebDriverCreator {

    private  boolean headless;
    private String url;

    public RemoteFirefoxDriverCreator(String url, boolean headless){
        this.url = url;
        this.headless = headless;
    }

    @Override
    public WebDriver createWebDriver() throws MalformedURLException {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--start-maximized");
        options.addArguments("--allow-running-insecure-content");
        if(headless){ options.addArguments("--headless=new");}

        return new RemoteWebDriver(new URL(url),options);
    }
}
