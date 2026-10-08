package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import pages.InventoryPage;
import pages.LoginPage;
import utils.AllureAttachments;
import utils.TestConfig;


@Epic("Saucedemo UI")
@Feature("Авторизация")
@Owner("Маркин Сергей")
@Link(name = "Saucedemo", url = "https://www.saucedemo.com/")
public class LoginTest extends BaseTest {

    @Test(description = "Позитив: standard_user успешно авторизуется и попадает в каталог товаров")
    @Story("Успешный вход")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("SAUCE-1")
    @Description("Проверяем, что валидные учётные данные переводят пользователя "
            + "на страницу /inventory.html с заголовком Products.")
    public void standardUserCanLogin() {

        InventoryPage inventory = new LoginPage(driver)
                .open()
                .loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD);

        Assert.assertTrue(inventory.isOpen(),
                "Ожидался переход на /inventory.html после успешного входа");
        Assert.assertEquals(inventory.getTitleText(), TestConfig.PRODUCTS_TITLE,
                "Ожидался заголовок каталога товаров");
        Assert.assertTrue(inventory.getProductCount() > 0,
                "В каталоге должен отображаться хотя бы один товар");

        AllureAttachments.screenshot(driver, "Каталог товаров после входа");
    }

    @Test(description = "Негатив: locked_out_user получает сообщение о блокировке")
    @Story("Блокировка пользователя")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("SAUCE-2")
    @Description("Заблокированный пользователь не должен попадать в каталог, "
            + "ему выводится сообщение о блокировке.")
    public void lockedOutUserCannotLogin() {

        LoginPage loginPage = new LoginPage(driver)
                .open()
                .loginExpectingError(TestConfig.LOCKED_OUT_USER, TestConfig.PASSWORD);

        String errorText = loginPage.getErrorText();

        Assert.assertTrue(errorText.toLowerCase().contains(TestConfig.LOCKED_OUT_ERROR),
                "Ожидалось сообщение о заблокированном пользователе, фактически: " + errorText);
        Assert.assertFalse(driver.getCurrentUrl().contains("/inventory.html"),
                "Заблокированный пользователь не должен попадать в каталог товаров");

        AllureAttachments.screenshot(driver, "Сообщение о блокировке пользователя");
    }

    @Test(description = "Негатив: неверный пароль не пропускает пользователя в систему")
    @Story("Неверные учётные данные")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("SAUCE-3")
    @Description("Проверяем валидацию пары логин/пароль: при неверном пароле "
            + "выводится ошибка 'Username and password do not match'.")
    public void userCannotLoginWithInvalidPassword() {

        LoginPage loginPage = new LoginPage(driver)
                .open()
                .loginExpectingError(TestConfig.STANDARD_USER, TestConfig.INVALID_PASSWORD);

        String errorText = loginPage.getErrorText();

        Assert.assertTrue(errorText.contains(TestConfig.INVALID_CREDENTIALS_ERROR),
                "Ожидалась ошибка о несовпадении логина и пароля, фактически: " + errorText);

        AllureAttachments.screenshot(driver, "Ошибка неверных учётных данных");
    }

    @Test(description = "Негатив: пустая форма авторизации показывает ошибку о обязательном логине")
    @Story("Валидация формы входа")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("SAUCE-4")
    @Description("При отправке пустой формы выводится сообщение о необходимости указать логин.")
    public void emptyCredentialsShowValidationError() {

        LoginPage loginPage = new LoginPage(driver)
                .open()
                .loginWithEmptyCredentials();

        String errorText = loginPage.getErrorText();

        Assert.assertTrue(errorText.toLowerCase().contains("username is required"),
                "Ожидалась ошибка о обязательном поле Username, фактически: " + errorText);

        AllureAttachments.screenshot(driver, "Ошибка валидации пустой формы");
    }
}
