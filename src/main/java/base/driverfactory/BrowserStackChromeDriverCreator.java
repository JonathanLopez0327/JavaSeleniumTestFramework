package base.driverfactory;

import org.openqa.selenium.WebDriver;
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
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("browserVersion", "120.0");

        String username = System.getenv("BROWSERSTACK_USERNAME");
        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");

        if (username == null || accessKey == null) {
            throw new IllegalStateException("BrowserStack credentials are missing");
        }

        String browserstackUrl = "https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub";

        return new RemoteWebDriver(new URL(browserstackUrl), caps);
    }
}