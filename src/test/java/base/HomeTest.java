package base;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.EyesRunner;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testframework.browserconfiguration.BrowserConfiguration;
import org.testng.annotations.*;
import java.util.concurrent.TimeUnit;


public class HomeTest {

    private static WebDriver driver;
    private static final String headless = "--headless=new";
    static Logger logger = LoggerFactory.getLogger(HomeTest.class);

    private static EyesRunner runner;
    private static Eyes eyes;
    private static BatchInfo batch;

    public HomeTest() {}


    @BeforeMethod
    @Parameters({"browser"})
    public static void beforeConfig(@Optional String browser) {
        if (browser.isEmpty()) {
            browser = String.valueOf(BrowserConfiguration.NONE);
        }

        setUp(BrowserConfiguration.valueOf(browser));
    }

    public void openBrowser(String url) {
        driver.navigate().to(url);
    }
    public static WebDriver getDriver() {
        return driver;
    }

    public static void setUp(BrowserConfiguration browser) {
        browserConfiguration(browser);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
    }

    public static void setBatch(String batchInfo, String apiKey) {
        // Applitools Eyes
        batch = new BatchInfo(batchInfo);
        // Initialize the runner
        runner = new ClassicRunner();
        // Initialize the eyes SDK
        eyes = new Eyes(runner);
        // Applitools API KEY
        eyes.setApiKey(System.getenv(apiKey));
        // Set batch name
        eyes.setBatch(batch);
    }


    public static WebDriver browserConfiguration(BrowserConfiguration browser) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        DesiredCapabilities remoteCapabilities = new DesiredCapabilities("chrome", "", Platform.ANY);
        desiredCapabilities.setAcceptInsecureCerts(true);
        remoteCapabilities.setAcceptInsecureCerts(true);
        ChromeOptions remoteOptions = new ChromeOptions();
        String sslIgnore = "--ignore-certificate-errors";
        String defaultMaximize = "--start-maximized";
        String allowRunningInsecure = "--allow-running-insecure-content";

        try {
            switch (browser) {
                case CHROME -> {
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments(headless);
                    chromeOptions.merge(desiredCapabilities);
                    driver = new ChromeDriver(chromeOptions);
                    return driver;
                }

                case FIREFOX -> {
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments(headless);
                    firefoxOptions.merge(desiredCapabilities);
                    driver = new FirefoxDriver(firefoxOptions);
                    return driver;
                }

                case EDGE -> {
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.merge(desiredCapabilities);
                    driver = new EdgeDriver(edgeOptions);
                    return driver;
                }

                case OTHER_CHROME_VERSION -> {
                    System.setProperty("webdriver.chrome.drive", "src/main/resources/chromedriver");
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments(headless);
                    options.addArguments(new String[]{sslIgnore});
                    options.addArguments(new String[]{allowRunningInsecure});
                    options.addArguments(new String[]{defaultMaximize});
                    remoteOptions.merge(desiredCapabilities);
                    driver = new ChromeDriver(options);
                    return driver;
                }
            }
        } catch (Exception e) {
            logger.error("Error", e);
        }
        return null;
    }

//    @AfterTest
//    public void closeBrowser() {
//        driver.close();
//    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
