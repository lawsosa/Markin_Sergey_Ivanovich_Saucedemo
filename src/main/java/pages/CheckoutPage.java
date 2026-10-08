package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import base.BasePage;
import io.qameta.allure.Step;
import utils.Locators;


public class CheckoutPage extends BasePage {

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    

    @Step("Заполнить данные покупателя: {firstName} {lastName}, индекс {postalCode}")
    public CheckoutPage fillCustomerInfo(String firstName, String lastName, String postalCode) {
        type(Locators.Checkout.FIRST_NAME, firstName);
        type(Locators.Checkout.LAST_NAME, lastName);
        type(Locators.Checkout.POSTAL_CODE, postalCode);
        return this;
    }

    @Step("Ввести имя: '{firstName}'")
    public CheckoutPage enterFirstName(String firstName) {
        type(Locators.Checkout.FIRST_NAME, firstName);
        return this;
    }

    @Step("Ввести фамилию: '{lastName}'")
    public CheckoutPage enterLastName(String lastName) {
        type(Locators.Checkout.LAST_NAME, lastName);
        return this;
    }

    @Step("Ввести почтовый индекс: '{postalCode}'")
    public CheckoutPage enterPostalCode(String postalCode) {
        type(Locators.Checkout.POSTAL_CODE, postalCode);
        return this;
    }

    @Step("Нажать Continue (переход к обзору заказа)")
    public CheckoutPage continueToOverview() {
        click(Locators.Checkout.CONTINUE_BUTTON);
        
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/checkout-step-two.html"),
                ExpectedConditions.visibilityOfElementLocated(Locators.Checkout.TOTAL_LABEL)));
        return this;
    }

    @Step("Нажать Continue без заполнения данных (ожидается ошибка)")
    public CheckoutPage continueExpectingError() {
        click(Locators.Checkout.CONTINUE_BUTTON);
        visible(Locators.Checkout.ERROR_MESSAGE);
        return this;
    }

    @Step("Нажать Cancel (возврат в корзину)")
    public CartPage cancel() {
        click(Locators.Checkout.CANCEL_BUTTON);
        return new CartPage(driver);
    }

    

    @Step("Проверить, что открыт шаг обзора заказа (Checkout: Overview)")
    public boolean isOverviewOpen() {
        return urlContains("/checkout-step-two.html") && isVisible(Locators.Checkout.TOTAL_LABEL);
    }

    @Step("Получить итоговую сумму заказа")
    public String getTotalText() {
        return text(Locators.Checkout.TOTAL_LABEL);
    }

    @Step("Получить сумму товаров без налога")
    public String getSubtotalText() {
        return text(Locators.Checkout.SUBTOTAL_LABEL);
    }

    @Step("Получить сумму налога")
    public String getTaxText() {
        return text(Locators.Checkout.TAX_LABEL);
    }

    @Step("Получить способ оплаты")
    public String getPaymentInfo() {
        return text(Locators.Checkout.PAYMENT_INFO);
    }

    @Step("Получить способ доставки")
    public String getShippingInfo() {
        return text(Locators.Checkout.SHIPPING_INFO);
    }

    @Step("Получить текст ошибки на странице оформления заказа")
    public String getErrorText() {
        return text(Locators.Checkout.ERROR_MESSAGE);
    }

    

    @Step("Нажать Finish (завершить заказ)")
    public CheckoutPage finish() {
        click(Locators.Checkout.FINISH_BUTTON);
        
        visible(Locators.Checkout.COMPLETE_HEADER);
        return this;
    }

    @Step("Получить заголовок страницы подтверждения заказа")
    public String getCompleteHeader() {
        return text(Locators.Checkout.COMPLETE_HEADER);
    }

    @Step("Получить текст страницы подтверждения заказа")
    public String getCompleteText() {
        return text(Locators.Checkout.COMPLETE_TEXT);
    }

    @Step("Проверить, что заказ успешно оформлен")
    public boolean isOrderComplete() {
        return isVisible(Locators.Checkout.COMPLETE_HEADER);
    }

    @Step("Вернуться в каталог товаров (Back Home)")
    public InventoryPage backToProducts() {
        click(Locators.Checkout.BACK_HOME_BUTTON);
        return new InventoryPage(driver);
    }

    
    @Step("Оформить заказ: {firstName} {lastName}, индекс {postalCode}")
    public CheckoutPage completeOrder(String firstName, String lastName, String postalCode) {
        fillCustomerInfo(firstName, lastName, postalCode);
        continueToOverview();
        finish();
        return this;
    }
}
