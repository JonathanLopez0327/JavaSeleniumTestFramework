package reportconfig;

import base.HomeTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class ExtentManager extends HomeTest {
    private static ExtentReports extentReports;
    public static String screenshotName;
    static Logger logger = LoggerFactory.getLogger(ExtentManager.class);

    public static ExtentReports createInstance(String fileName) {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(fileName);

        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle(fileName);
        sparkReporter.config().setEncoding("utf-8");
        sparkReporter.config().setReportName(fileName);

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("Release No", "22");
        extentReports.setSystemInfo("Environment", "SQA");
        extentReports.setSystemInfo("Build No", "B-12673");

        return extentReports;
    }

    public static void captureScreenshot() {
        TakesScreenshot screenshot = (TakesScreenshot)getDriver();

        // Call method to capture screen
        File src = screenshot.getScreenshotAs(OutputType.FILE);

        try {
            Date d = new Date();
            screenshotName =
                    d.toString().replace(":", "_").replace(" ", "-") + ".jpg";

            FileUtils
                    .copyFile(src, new File(System.getProperty("user.dir") + "/Reports/" + screenshotName));

            logger.info("Successfully captured a screenshot");
        } catch (IOException e) {
            logger.error("Error while taking screenshot", e.getMessage());
        }
    }
}
