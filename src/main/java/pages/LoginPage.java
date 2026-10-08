package pages;

import org.openqa.selenium.WebDriver;

import base.BasePage;
import io.qameta.allure.Step;
import utils.Locators;
import utils.TestConfig;


public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть страницу авторизации Saucedemo")
    public LoginPage open() {
        driver.get(TestConfig.BASE_URL);
        visible(Locators.Login.USERNAME);
        return this;
    }

    @Step("Ввести логин '{user}' и пароль '{password}'")
    public LoginPage enterCredentials(String user, String password) {
        type(Locators.Login.USERNAME, user);
        type(Locators.Login.PASSWORD, password);
        return this;
    }

    @Step("Нажать кнопку Login")
    public LoginPage submit() {
        click(Locators.Login.LOGIN_BUTTON);
        return this;
    }

    @Step("Авторизоваться как '{user}'")
    public InventoryPage loginAs(String user, String password) {
        enterCredentials(user, password).submit();
        return new InventoryPage(driver);
    }

    @Step("Попытаться авторизоваться как '{user}' (ожидается ошибка)")
    public LoginPage loginExpectingError(String user, String password) {
        enterCredentials(user, password).submit();
        visible(Locators.Login.ERROR_MESSAGE);
        return this;
    }

    @Step("Попытаться авторизоваться без заполнения полей")
    public LoginPage loginWithEmptyCredentials() {
        submit();
        visible(Locators.Login.ERROR_MESSAGE);
        return this;
    }

    @Step("Получить текст сообщения об ошибке авторизации")
    public String getErrorText() {
        return text(Locators.Login.ERROR_MESSAGE);
    }

    @Step("Проверить, что сообщение об ошибке отображается")
    public boolean isErrorDisplayed() {
        return isVisible(Locators.Login.ERROR_MESSAGE);
    }

    @Step("Проверить, что страница авторизации открыта")
    public boolean isOpen() {
        return isVisible(Locators.Login.LOGIN_BUTTON)
                && driver.getCurrentUrl().contains("saucedemo.com");
    }
}
