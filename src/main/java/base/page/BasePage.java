package base.page;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    public final JavascriptExecutor js;
    WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        js = (JavascriptExecutor)this.driver;
        wait = (WebDriverWait) new WebDriverWait(this.driver, Duration.ofSeconds(60))
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class);
    }

    public void get(String url) {
        driver.get(url);
        driver.manage().window().maximize();
    }

    public void findElement(By locator){
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        wait.until(d -> driver.findElement(locator).isDisplayed());
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        wait.until(d -> driver.findElement(locator).isEnabled());
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean verifyElementPresent(By locator, int timeout) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeout))
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(ElementNotInteractableException.class)
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public void click(By locator) {
        findElement(locator);
        wait.until(
                d -> {
                    driver.findElement(locator).click();
                    return true;
                });
    }

    public void sendKeys(By locator, Keys keys) {
        findElement(locator);
        wait.until(
                d -> {
                    driver.findElement(locator).sendKeys(keys);
                    return true;
                });
    }

    public void write(By locator, String text) {
        findElement(locator);
        wait.until(
                d -> {
                    driver.findElement(locator).click();
                    driver.findElement(locator).clear();
                    driver.findElement(locator).sendKeys(text);
                    //Retries writing for cases where field auto-deletes
                    return driver.findElement(locator).getAttribute("value").contains(text);
                });
    }

    public void selectByText(By locator, String text){
        findElement(locator);
        wait.until(
                d -> {
                    new Select(driver.findElement(locator)).selectByVisibleText(text);
                    return true;
                });
    }

    public void jsClick(By locator){
        findElement(locator);
        wait.until(
                d -> {
                    js.executeScript("arguments[0].click();", driver.findElement(locator));
                    return true;
                });

    }

    public void jsWrite(By locator, String text){
        findElement(locator);
        wait.until(
                d -> {
                    driver.findElement(locator).click();
                    driver.findElement(locator).clear();
                    js.executeScript(
                            "arguments[0].setAttribute(arguments[1],arguments[2])",
                            driver.findElement(locator), "value", text);

                    return true;
                });
    }
}
