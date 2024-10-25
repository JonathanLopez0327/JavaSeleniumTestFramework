package reportconfig;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentManager {
    private static ExtentReports extent;
    private static final String FULL_REPORT_PATH = System.getProperty("user.dir") + "/Reports/";

    private ExtentManager() {
    }

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    public static ExtentReports createInstance() {
        try {
            // createReportDirectory();
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(FULL_REPORT_PATH + getReportNameWithTimeStamp());

            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("Reporte de Automatizacion");
            sparkReporter.config().setEncoding("utf-8");
            sparkReporter.config().setReportName("Pruebas");
            sparkReporter.config().thumbnailForBase64(true);
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extent;
    }

    public static String captureScreenshot(WebDriver driver) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            return screenshot.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getReportNameWithTimeStamp() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String timeStamp = dateTimeFormatter.format(localDateTime);
        return "Test-Report-" + timeStamp + ".html";
    }
}
