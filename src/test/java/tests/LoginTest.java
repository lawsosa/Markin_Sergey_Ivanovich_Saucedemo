package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;
import utils.TestConfig;

public class LoginTest extends BaseTest {

    @Test(description = "Positive login with standard user")
    public void standardUserCanLogin() {

        InventoryPage inventory = new LoginPage(driver)
                .open()
                .loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD);

        Assert.assertTrue(inventory.isOpen(), "Ожидался URL /inventory.html после успешного входа");
        Assert.assertEquals(inventory.getTitleText(),"Products", "Ожидался заголовок Products после входа");
    }

    @Test(description = "Locked user cannot login")
    public void lockedUserCannotLogin() {

        LoginPage login = new LoginPage(driver)
                .open()
                .loginExpectingError(TestConfig.LOCKED_OUT_USER, TestConfig.PASSWORD);

        Assert.assertTrue(login.getErrorText().contains("locked out"), "Ожидалось сообщение о заблокированном пользователе");
    }
}