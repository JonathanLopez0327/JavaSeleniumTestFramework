package base.threadsafe;

import org.openqa.selenium.WebDriver;

public class CurrentWebDriver {
    private static CurrentWebDriver instance;
    private static ThreadLocal<WebDriver> webDriverThreadLocal;

    private CurrentWebDriver() {
        createThreadWebdriver();
    }

    public static CurrentWebDriver getInstance(){
        if (instance == null){
            synchronized (CurrentWebDriver.class){
                checkInstance();
            }
        }
        return instance;
    }

    public WebDriver getWebDriver(){
     //   WebDriver driver =
        return  webDriverThreadLocal.get();
    }
    static void checkInstance(){
        if (instance == null){
            instance = new CurrentWebDriver();
        }
    }

    public void setWebDriver(WebDriver webDriver){
        webDriverThreadLocal.set(webDriver);
    }

    public void removeWebDriver(){
        webDriverThreadLocal.remove();
    }

    private static void createThreadWebdriver(){
        webDriverThreadLocal = new ThreadLocal<>();
    }
}
