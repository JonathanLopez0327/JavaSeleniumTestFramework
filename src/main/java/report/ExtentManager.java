package report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class ExtentManager {
    private static ExtentReports extent;
    private static final String FULL_REPORT_PATH = System.getProperty("user.dir") + "/Reports/";
    static Logger logger = LoggerFactory.getLogger(ExtentManager.class);

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
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(FULL_REPORT_PATH + getReportNameWithTimeStamp());

            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("Reporte de Automatizacion");
            sparkReporter.config().setEncoding("utf-8");
            sparkReporter.config().setReportName("Pruebas");
            sparkReporter.config().thumbnailForBase64(true);
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
        } catch (Exception e) {
            logger.error("Error creating ExtentReports instance: {}", e.getMessage());
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

    public static String captureImage(BufferedImage image) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            logger.error("Error capturing image: {}", e.getMessage());
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
