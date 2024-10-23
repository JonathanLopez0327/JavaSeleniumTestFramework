package base.driverfactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class RemoteEdgeDriverCreator implements WebDriverCreator {

    private String url;

    public RemoteEdgeDriverCreator(String url){
        this.url = url;
    }

    @Override
    public WebDriver createWebDriver() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setAcceptInsecureCerts(true);
        capabilities.setBrowserName("MicrosoftEdge");
        Map<String, Object> edgeOptions = Map.of("binary", "/opt/microsoft/msedge-beta/msedge");
        capabilities.setCapability("ms:edgeOptions", edgeOptions);

        return new RemoteWebDriver(new URL(url),new EdgeOptions().merge(capabilities));
    }
}
