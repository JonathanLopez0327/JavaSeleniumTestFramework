package report;

import base.BaseTest;
import base.service.BaseService;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.testng.IExecutionListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Listeners;

@Listeners(UISuiteListener.class)
public class UISuiteListener extends BaseTest implements ITestListener, IExecutionListener {

    private ExtentReports extentReports;
    private BaseService baseService;


    public UISuiteListener() {
        // comment
    }

    @Override
    public void onStart(ITestContext context) {
        extentReports = getExtentReports();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        try {
            ExtentReport.getExtentTest().pass("<b><font color=\"green\">Test Passed</font></b>");
        } catch (Exception e) {
            System.out.println("Error while capturing screenshot on test success");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            ExtentReport.getExtentTest().fail("<b><font color=\"red\">Test Failed</font></b><br>");
        } catch (Exception e) {
            System.out.println("Error " + e);
        }
    }

    @Override
    public void onExecutionFinish() {
        extentReports.flush();
    }
}
