package reportconfig;

import com.aventstack.extentreports.ExtentTest;

public class ExtentReport {
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static ExtentTest getExtentTest() {
        return extentTest.get();
    }

    public static void setExtentTest(ExtentTest test) {
        extentTest.set(test);
    }

    private ExtentReport() {
    }
}
