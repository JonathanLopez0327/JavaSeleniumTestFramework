package testingmyframework;

import base.HomeTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleTest extends HomeTest {

    @Test(description = "Test framework")
    void simpleTest() {
        Simple simple = new Simple(getDriver());
        Assert.assertTrue(simple.simpleTest());
        //simple.simpleTest();
    }
}
