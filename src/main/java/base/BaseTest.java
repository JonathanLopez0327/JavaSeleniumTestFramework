package base;

import base.driverfactory.ChromeDriverCreator;
import base.driverfactory.EdgeDriverCreator;
import base.driverfactory.FirefoxDriverCreator;
import base.driverfactory.OnPromiseDriverCreator;
import base.threadsafe.CurrentWebDriver;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import browserconfiguration.BrowserConfiguration;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import reportconfig.ExtentManager;
import reportconfig.ExtentReport;

import java.lang.reflect.Method;
import java.time.Duration;


public class BaseTest {
    static Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @Getter
    private static ExtentReports extentReports = ExtentManager.getInstance();

    private static ExtentTest scenario;


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


    public static WebDriver browserConfiguration(BrowserConfiguration browser) {
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

                default -> {
                    ChromeDriverCreator chromeDriverCreator = new ChromeDriverCreator();
                    return chromeDriverCreator.createWebDriver();
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
            System.out.println("Error creating step");
        }
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String logText = "Test case: " + methodName;
        if (result.getStatus() == ITestResult.FAILURE) {
            logText += " (Fallido) ";
            ExtentReport.getExtentTest().log(Status.FAIL, logText);
        } else if (result.getStatus() == ITestResult.SKIP) {
            logText += " (Saltado) " + methodName;
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
