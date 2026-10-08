package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;
import io.qameta.allure.Step;
import utils.TestConfig;

/**
 * Страница авторизации SauceDemo (https://www.saucedemo.com/).
 */
public class LoginPage extends BasePage {

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Открыть страницу авторизации SauceDemo")
    public LoginPage open() {
        driver.get(TestConfig.BASE_URL);
        visible(usernameInput);
        return this;
    }

    @Step("Войти в систему как пользователь {user}")
    public InventoryPage loginAs(String user, String password) {
        enterCredentials(user, password);
        click(loginButton);
        return new InventoryPage(driver, wait);
    }

    @Step("Попытаться войти как пользователь {user} и дождаться сообщения об ошибке")
    public LoginPage loginExpectingError(String user, String password) {
        enterCredentials(user, password);
        click(loginButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return this;
    }

    /**
     * Заполнение полей формы. Метод не помечен @Step намеренно:
     * значения полей уже логируются шагами ввода из BasePage.
     */
    private void enterCredentials(String user, String password) {
        type(usernameInput, user);
        type(passwordInput, password);
    }

    @Step("Получить текст сообщения об ошибке авторизации")
    public String getErrorText() {
        return text(errorMessage);
    }

    @Step("Проверить, отображается ли сообщение об ошибке авторизации")
    public boolean isErrorDisplayed() {
        return isVisible(errorMessage);
    }
}
