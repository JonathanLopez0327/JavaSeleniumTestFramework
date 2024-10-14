package base.driverfactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class EdgeDriverCreator implements WebDriverCreator {

    @Override
    public WebDriver createWebDriver() {
        EdgeOptions options = new EdgeOptions();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setAcceptInsecureCerts(true);
        options = options.merge(capabilities);
        return new EdgeDriver(options);
    }
}
