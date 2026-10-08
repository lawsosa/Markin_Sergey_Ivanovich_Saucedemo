package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;
import io.qameta.allure.Step;

/**
 * Оформление заказа: шаг 1 (Checkout: Your Information), шаг 2 (Checkout: Overview)
 * и экран подтверждения (Checkout: Complete!).
 */
public class CheckoutPage extends BasePage {

    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");

    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By finishButton = By.id("finish");

    private final By title = By.cssSelector("[data-test='title']");
    private final By completeHeader = By.cssSelector("[data-test='complete-header']");
    private final By error = By.cssSelector("[data-test='error']");
    private final By total = By.cssSelector("[data-test='total-label']");

    public CheckoutPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Заполнить данные покупателя: {first} {last}, индекс {zip}")
    public CheckoutPage fillCustomerInfo(String first, String last, String zip) {
        type(firstName, first);
        type(lastName, last);
        type(postalCode, zip);
        return this;
    }

    @Step("Перейти к шагу Overview")
    public CheckoutPage continueToOverview() {
        click(continueButton);
        waitForUrl("checkout-step-two");
        return this;
    }

    @Step("Нажать Continue с пустой формой и дождаться ошибки валидации")
    public CheckoutPage continueExpectingError() {
        click(continueButton);
        visible(error);
        return this;
    }

    @Step("Получить текст ошибки валидации данных покупателя")
    public String getErrorText() {
        return text(error);
    }

    @Step("Получить итоговую сумму заказа")
    public String getTotalText() {
        return text(total);
    }

    @Step("Получить заголовок текущего шага оформления заказа")
    public String getTitleText() {
        return text(title);
    }

    @Step("Подтвердить заказ (Finish)")
    public CheckoutPage finish() {
        click(finishButton);
        waitForUrl("checkout-complete");
        return this;
    }

    @Step("Получить заголовок экрана подтверждения заказа")
    public String getCompleteHeader() {
        return text(completeHeader);
    }

    @Step("Отменить оформление заказа (Cancel)")
    public CartPage cancel() {
        click(cancelButton);
        return new CartPage(driver, wait);
    }
}
