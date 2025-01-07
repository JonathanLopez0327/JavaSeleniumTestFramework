package base;

import base.driverfactory.*;
import base.threadsafe.CurrentWebDriver;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import browser.BrowserConfiguration;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import report.ExtentManager;
import report.ExtentReport;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.function.Function;


public class BaseTest {
    static Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @Getter
    private static ExtentReports extentReports = ExtentManager.getInstance();

    private static ExtentTest scenario;

    static String urlOfGrid;

    private static final boolean HEADLESS = false;

    @BeforeMethod
    @Parameters({"browser"})
    public static void beforeConfig(@Optional String browser, ITestResult result, ITestContext context, Method testMethod) {
        String scenarioName  = "";
        scenarioName = context.getCurrentXmlTest().getName();
        scenario = extentReports.createTest(scenarioName + " - " + testMethod.getAnnotation(Test.class).description());
        ExtentReport.setExtentTest(scenario);

        scenario.assignCategory(scenarioName);
        scenario.assignCategory("<b>TOTALS</b>");

        if (browser.isEmpty()) {
            browser = String.valueOf(BrowserConfiguration.NONE);
        }

        setUp(BrowserConfiguration.valueOf(browser));
        scenario.assignDevice(browser);
    }

    public void openBrowser(String url) {
        CurrentWebDriver.getInstance().getWebDriver().navigate().to(url);
    }

    public static void setUp(BrowserConfiguration browser) {
        CurrentWebDriver.getInstance().setWebDriver(browserConfiguration(browser));
        CurrentWebDriver.getInstance().getWebDriver().manage().window().maximize();
        CurrentWebDriver.getInstance().getWebDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    private static String getEnv() {
        return ((Function<String, String>) System::getenv).apply("GRID_URL");
    }

    public static WebDriver browserConfiguration(BrowserConfiguration browser) {
        urlOfGrid = getEnv();
        try {
            switch (browser) {
                case CHROME -> {
                    ChromeDriverCreator chromeDriverCreator = new ChromeDriverCreator();
                    return chromeDriverCreator.createWebDriver();
                }

                case FIREFOX -> {
                    FirefoxDriverCreator firefoxDriverCreator = new FirefoxDriverCreator();
                    return firefoxDriverCreator.createWebDriver();
                }

                case EDGE -> {
                    EdgeDriverCreator edgeDriverCreator = new EdgeDriverCreator();
                    return edgeDriverCreator.createWebDriver();
                }

                case OTHER_CHROME_VERSION -> {
                    OnPromiseDriverCreator onPromiseDriverCreator = new OnPromiseDriverCreator();
                    return onPromiseDriverCreator.createWebDriver();
                }

                case DOCKER_CHROME -> {
                    RemoteChromeDriverCreator remoteChromeDriverCreator = new RemoteChromeDriverCreator(urlOfGrid, HEADLESS);
                    return remoteChromeDriverCreator.createWebDriver();
                }

                case DOCKER_FIREFOX -> {
                    RemoteFirefoxDriverCreator remoteFirefoxDriverCreator = new RemoteFirefoxDriverCreator(urlOfGrid, HEADLESS);
                    return remoteFirefoxDriverCreator.createWebDriver();
                }

                case DOCKER_EDGE -> {
                    RemoteEdgeDriverCreator remoteEdgeDriverCreator = new RemoteEdgeDriverCreator(urlOfGrid);
                    return remoteEdgeDriverCreator.createWebDriver();
                }

                default -> {
                    logger.error("Browser not found");
                    return null;
                }
            }
        } catch (Exception e) {
            logger.error("Error", e);
        }
        return null;
    }

    public static void createStep(String description, boolean decision, boolean isScreenshot) {
        try {
            String screenshot = isScreenshot ? ExtentManager.captureScreenshot(getDriver()) : "";

            scenario = decision && isScreenshot ? ExtentReport.getExtentTest().pass(description, MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build()) : scenario;

            scenario = decision && !isScreenshot ? ExtentReport.getExtentTest().pass(description) : scenario;

            scenario = !decision && isScreenshot ? ExtentReport.getExtentTest().fail(description, MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build()) : scenario;

            scenario = !decision && !isScreenshot ? ExtentReport.getExtentTest().fail(description) : scenario;

        } catch (Exception e) {
            logger.error("Error creating step", e);
        }
    }

    public static void createStep(String description, boolean decision, boolean isScreenshot, BufferedImage image) {
        try {
            String screenshot = isScreenshot ? ExtentManager.captureImage(image) : "";

            scenario = decision && isScreenshot ? ExtentReport.getExtentTest().pass(description, MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build()) : scenario;

            scenario = decision && !isScreenshot ? ExtentReport.getExtentTest().pass(description) : scenario;

            scenario = !decision && isScreenshot ? ExtentReport.getExtentTest().fail(description, MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build()) : scenario;

            scenario = !decision && !isScreenshot ? ExtentReport.getExtentTest().fail(description) : scenario;

        } catch (Exception e) {
            logger.error("Error creating step", e);
        }
    }

    /*
        PDF IMAGE UTILITIES
     */

    public static boolean compareAndHighlight(final BufferedImage img1, final BufferedImage img2,
                                              String fileName, boolean highlight, int colorCode) {

        // Get pixels and dimensions of the images
        final int w = img1.getWidth();
        final int h = img1.getHeight();
        final int[] p1 = img1.getRGB(0, 0, w, h, null, 0, w);
        final int[] p2 = img2.getRGB(0, 0, w, h, null, 0, w);

        // Compare the images
        if (!(java.util.Arrays.equals(p1, p2))) {
            logger.warn("Image compared - does not match");
            if (highlight) {
                for (int i = 0; i < p1.length; i++) {
                    if (p1[i] != p2[i]) {
                        p1[i] = colorCode;
                    }
                }
                final BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                out.setRGB(0, 0, w, h, p1, 0, w);
                saveImage(out, fileName);
            }
            return false;
        }
        return true;
    }

    public static void saveImage(BufferedImage image, String file) {
        try {
            File outputfile = new File(file);
            ImageIO.write(image, "png", outputfile);
        } catch (Exception e) {
            logger.error("Error while saving image: {}", e.getMessage());
        }
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String logText = "Test case: " + methodName;
        
        if (result.getStatus() == ITestResult.FAILURE) {
            logText += " (Failed) ";
            ExtentReport.getExtentTest().log(Status.FAIL, logText);
        } else if (result.getStatus() == ITestResult.SKIP) {
            logText += " (Skipped) " + methodName;
            ExtentReport.getExtentTest().log(Status.SKIP, logText);
        }

        if (CurrentWebDriver.getInstance().getWebDriver() != null) {
            CurrentWebDriver.getInstance().getWebDriver().quit();
            CurrentWebDriver.getInstance().removeWebDriver();
        }
    }

    public void createStep(String textInfo) {
        ExtentReport.getExtentTest().log(Status.PASS, textInfo);
    }

    public static WebDriver getDriver() {
        return CurrentWebDriver.getInstance().getWebDriver();
    }

    @AfterClass
    public void tearDown() {
        if (CurrentWebDriver.getInstance().getWebDriver() != null) {
            CurrentWebDriver.getInstance().getWebDriver().quit();
        }
    }

}
