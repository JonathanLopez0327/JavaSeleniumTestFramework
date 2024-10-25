package soucedemo.test;

import base.BaseTest;
import org.testng.annotations.Test;
import soucedemo.pages.LoginPage;

import static org.testng.Assert.assertTrue;

public class LoginTest extends BaseTest {

    @Test(description = "Login with valid credentials")
    public void loginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("standard_user", "secret_sauce");
        createStep("Login with valid credentials", true, true);
        assertTrue(loginPage.isLoginWasSuccessful());
    }
}
