package testingmyframework;

import base.HomeTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testframework.pages.HomePage;
import reportconfig.ExtentManager;

public class Simple extends HomePage {
    private By div = By.xpath("//div[@id='optano']");
    String url = "https://www.orangehrm.com/";
    HomeTest homeTest = new HomeTest();
    public Simple(WebDriver driver) {
        super(driver);
    }

    private void sleepSeconds() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isElementPresent(By by) {
        return driver.findElements(by).size() > 0;
    }

    public boolean simpleTest() {
        homeTest.openBrowser(url);

        if (isElementPresent(div)) {
            return true;
        }
        //sleepSeconds();
        return false;
    }
}
