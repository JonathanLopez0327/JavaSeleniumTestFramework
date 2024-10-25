package soucedemo.pages;

import base.BaseTest;
import base.page.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    private void openLoginPage() {
        get("https://www.saucedemo.com/");
    }

    private void setUsername(String username) {
        write(usernameField, username);
    }

    private void setPassword(String password) {
        write(passwordField, password);
    }

    private void clickLoginButton() {
        click(loginButton);
    }

    public void login(String username, String password) {
        openLoginPage();
        BaseTest.createStep("Open login page", true, true);
        setUsername(username);
        setPassword(password);
        BaseTest.createStep("Complete", true, true);
        clickLoginButton();
    }

    public boolean isLoginWasSuccessful() {
        return verifyElementPresent(By.xpath("//div[contains(text(), 'Swag Labs')]"), 2);
    }
}
