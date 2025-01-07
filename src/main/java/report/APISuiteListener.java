package report;

import base.service.BaseService;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.IExecutionListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Listeners;

@Listeners(APISuiteListener.class)
public class APISuiteListener extends BaseService implements ITestListener, IExecutionListener {

    private ExtentReports extentReports;
    private BaseService baseService;


    public APISuiteListener() {
        // comment
    }

    @Override
    public void onStart(ITestContext context) {
        extentReports = getExtentReports();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReport.getExtentTest().log(Status.PASS, "Test passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentReport.getExtentTest().log(Status.FAIL, "Test failed: " + result.getName());
        ExtentReport.getExtentTest().log(Status.FAIL, "Error: " + result.getThrowable());
    }

    public static void logInfo(String message) {
        ExtentReport.getExtentTest().log(Status.INFO, message);
    }
    public static void logJson(String message) {
        ExtentReport.getExtentTest().info(MarkupHelper.createCodeBlock(message));
    }

    @Override
    public void onExecutionFinish() {
        extentReports.flush();
    }

}
