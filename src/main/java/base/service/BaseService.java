package base.service;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import report.ExtentManager;
import report.ExtentReport;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import static report.APISuiteListener.logInfo;
import static report.APISuiteListener.logJson;

@Slf4j
public class BaseService {
    private String baseUrl;

    @Getter
    private static ExtentReports extentReports = ExtentManager.getInstance();

    private static ExtentTest scenario;

    public BaseService() {
    }

    @BeforeMethod
    public static void beforeConfig(ITestResult result, ITestContext context, Method testMethod) {
        String scenarioName  = "";
        scenarioName = context.getCurrentXmlTest().getName();
        scenario = extentReports.createTest(scenarioName + " - " + testMethod.getAnnotation(Test.class).description());
        ExtentReport.setExtentTest(scenario);

        scenario.assignCategory(scenarioName);
        scenario.assignCategory("<b>TOTALS</b>");
    }

    private RequestSpecification getRequestSpecification(String baseUrl, String token) {
        this.baseUrl = baseUrl;
        return RestAssured.given()
                .baseUri(baseUrl)
                .auth()
                .oauth2(token)
                .header("Content-Type", "application/json");
    }


    private void logRequestAndResponse(String method, String endpoint, Object body, Response response) {
        logInfo("Request Method: " + method);
        logInfo("Request URL: " + baseUrl + endpoint);

        if (body != null) {
            logJson("Request Body: " + body);
        }

        logInfo("Response Status Code: " + response.getStatusCode());
        logJson("Response Body: " + response.getBody().asString());
    }

    public Response get(String baseUrl, String token, String endpoint) {
        Response response = getRequestSpecification(baseUrl, token)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
        logRequestAndResponse("GET", endpoint, null, response);
        return response;
    }

    public Response post(String baseUrl, String token, String endpoint, Object body) {
        Response response = getRequestSpecification(baseUrl, token)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
        logRequestAndResponse("POST", endpoint, body, response);
        return response;
    }

    public Response put(String baseUrl, String token, String endpoint, Object body) {
        Response response = getRequestSpecification(baseUrl, token)
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
        logRequestAndResponse("PUT", endpoint, body, response);
        return response;
    }

    public Response delete(String baseUrl, String token, String endpoint) {
        Response response = getRequestSpecification(baseUrl, token)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
        logRequestAndResponse("DELETE", endpoint, null, response);
        return response;
    }

    public Response patch(String baseUrl, String token, String endpoint, Object body) {
        Response response = getRequestSpecification(baseUrl, token)
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .extract()
                .response();
        logRequestAndResponse("PATCH", endpoint, body, response);
        return response;
    }

    public Response getToken(String tokenUrl, String clientId, String clientSecret, String grantType, String scope) {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("client_id", clientId);
        formParams.put("client_secret", clientSecret);
        formParams.put("grant_type", grantType);
        if (scope != null && !scope.isEmpty()) {
            formParams.put("scope", scope);
        }

        Response response = RestAssured.given()
                .baseUri(tokenUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParams(formParams)
                .when()
                .post()
                .then()
                .extract()
                .response();

        logRequestAndResponse("POST", tokenUrl, formParams, response);
        return response;
    }

    public Response getToken(String tokenUrl, String clientId, String clientSecret, String grantType) {
        return getToken(tokenUrl, clientId, clientSecret, grantType, null);
    }
}
