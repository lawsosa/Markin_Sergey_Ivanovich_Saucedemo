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
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;
import utils.AllureAttachments;
import utils.TestConfig;


@Epic("Saucedemo UI")
@Feature("Оформление заказа")
@Owner("Маркин Сергей")
@Link(name = "Saucedemo", url = "https://www.saucedemo.com/")
public class CheckoutTest extends BaseTest {

    @Test(description = "Позитив: полный сквозной заказ от авторизации до подтверждения")
    @Story("Сквозной заказ")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("SAUCE-14")
    @Description("Логин -> добавление товара -> корзина -> данные покупателя -> обзор заказа -> "
            + "подтверждение. Проверяются суммы Subtotal/Tax/Total и текст Thank you for your order!.")
    public void userCanCompleteCheckout() {

        InventoryPage inventory = new LoginPage(driver)
                .open()
                .loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD)
                .addProduct(TestConfig.BACKPACK);

        inventory.waitForCartBadge("1");
        CartPage cart = inventory.openCart();
        CheckoutPage checkout = cart.checkout();

        checkout.fillCustomerInfo(TestConfig.FIRST_NAME, TestConfig.LAST_NAME, TestConfig.POSTAL_CODE)
                .continueToOverview();

        Assert.assertTrue(checkout.isOverviewOpen(),
                "После заполнения данных должен открыться шаг Checkout: Overview");

        AllureAttachments.text("Суммы заказа",
                checkout.getSubtotalText() + " | " + checkout.getTaxText() + " | " + checkout.getTotalText());

        Assert.assertTrue(checkout.getSubtotalText().contains("29.99"),
                "Subtotal должен соответствовать цене товара, фактически: " + checkout.getSubtotalText());
        Assert.assertTrue(checkout.getTaxText().startsWith("Tax:"),
                "На шаге обзора должен отображаться налог, фактически: " + checkout.getTaxText());
        Assert.assertTrue(checkout.getTotalText().startsWith("Total:"),
                "На шаге обзора должна отображаться итоговая сумма, фактически: " + checkout.getTotalText());
        Assert.assertEquals(checkout.getPaymentInfo(), "SauceCard #31337",
                "Ожидался способ оплаты SauceCard #31337");
        Assert.assertEquals(checkout.getShippingInfo(), "Free Pony Express Delivery!",
                "Ожидался способ доставки Free Pony Express Delivery!");

        AllureAttachments.screenshot(driver, "Обзор заказа перед подтверждением");

        CheckoutPage complete = checkout.finish();

        Assert.assertTrue(complete.isOrderComplete(), "Ожидалась страница подтверждения заказа");
        Assert.assertEquals(complete.getCompleteHeader(), TestConfig.ORDER_COMPLETE_HEADER,
                "Ожидался заголовок подтверждения заказа");

        AllureAttachments.screenshot(driver, "Подтверждение успешного заказа");
    }

    @Test(description = "Негатив: оформление заказа без имени не проходит валидацию")
    @Story("Валидация данных покупателя")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("SAUCE-15")
    @Description("При пустом поле First Name выводится ошибка 'First Name is required', "
            + "переход к обзору заказа не выполняется.")
    public void checkoutRequiresFirstName() {

        CheckoutPage checkout = new LoginPage(driver)
                .open()
                .loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD)
                .addProduct(TestConfig.BACKPACK)
                .openCart()
                .checkout();

        checkout.enterLastName(TestConfig.LAST_NAME);
        checkout.enterPostalCode(TestConfig.POSTAL_CODE);
        checkout.continueExpectingError();

        String errorText = checkout.getErrorText();

        Assert.assertTrue(errorText.contains(TestConfig.FIRST_NAME_REQUIRED_ERROR),
                "Ожидалась ошибка валидации имени, фактически: " + errorText);
        Assert.assertFalse(driver.getCurrentUrl().contains("/checkout-step-two.html"),
                "Без обязательных данных переходить к обзору заказа нельзя");

        AllureAttachments.screenshot(driver, "Ошибка валидации First Name");
    }

    @Test(description = "Негатив: оформление заказа без почтового индекса не проходит валидацию")
    @Story("Валидация данных покупателя")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("SAUCE-16")
    @Description("При пустом поле Postal Code выводится ошибка о обязательном индексе.")
    public void checkoutRequiresPostalCode() {

        CheckoutPage checkout = new LoginPage(driver)
                .open()
                .loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD)
                .addProduct(TestConfig.BACKPACK)
                .openCart()
                .checkout();

        checkout.enterFirstName(TestConfig.FIRST_NAME);
        checkout.enterLastName(TestConfig.LAST_NAME);
        checkout.continueExpectingError();

        String errorText = checkout.getErrorText();

        Assert.assertTrue(errorText.contains("Postal Code is required"),
                "Ожидалась ошибка валидации почтового индекса, фактически: " + errorText);

        AllureAttachments.screenshot(driver, "Ошибка валидации Postal Code");
    }

    @Test(description = "Негатив: заказ с пустой корзиной не должен подтверждаться")
    @Story("Пустая корзина")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("SAUCE-17")
    @Description("Авторизованный пользователь с пустой корзиной переходит на шаг оформления и "
            + "пытается продолжить. Заказ не должен подтверждаться без товаров; фактическое "
            + "поведение стенда фиксируется вложением в Allure (найденный дефект).")
    public void emptyCartOrderCannotBeConfirmed() {

        
        new LoginPage(driver)
                .open()
                .loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD);

        CartPage emptyCart = new InventoryPage(driver).openCart();
        Assert.assertEquals(emptyCart.getItemsCount(), 0,
                "Перед проверкой корзина должна быть пустой");

        CheckoutPage checkout = emptyCart.checkout();
        Assert.assertTrue(driver.getCurrentUrl().contains("/checkout-step-one.html"),
                "Шаг оформления заказа должен открываться из пустой корзины");

        checkout.fillCustomerInfo(TestConfig.FIRST_NAME, TestConfig.LAST_NAME, TestConfig.POSTAL_CODE)
                .continueToOverview();

        boolean orderConfirmed = checkout.isOrderComplete();
        String actualState = "URL после Continue: " + driver.getCurrentUrl()
                + System.lineSeparator() + "Страница подтверждения заказа открыта: " + orderConfirmed;

        AllureAttachments.text("Фактическое поведение при заказе с пустой корзиной", actualState);
        AllureAttachments.screenshot(driver, "Заказ с пустой корзиной");

        Assert.assertFalse(orderConfirmed,
                "Заказ без товаров не должен подтверждаться. Фактическое состояние: " + actualState);
    }

    @Test(description = "Позитив: Cancel на шаге оформления возвращает в корзину с сохранёнными товарами")
    @Story("Отмена оформления")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("SAUCE-18")
    @Description("Кнопка Cancel возвращает пользователя в корзину, товары не теряются.")
    public void cancelCheckoutReturnsToCart() {

        CartPage cart = new LoginPage(driver)
                .open()
                .loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD)
                .addProduct(TestConfig.BIKE_LIGHT)
                .openCart();

        CheckoutPage checkout = cart.checkout();
        CartPage cartAfterCancel = checkout.cancel();

        Assert.assertTrue(driver.getCurrentUrl().contains("/cart.html"),
                "Cancel должен возвращать на страницу корзины");
        Assert.assertEquals(cartAfterCancel.getItemNames().size(), 1,
                "После отмены оформления товар должен остаться в корзине");
        Assert.assertTrue(cartAfterCancel.getItemNames().contains(TestConfig.BIKE_LIGHT),
                "В корзине должен остаться ранее добавленный товар");

        AllureAttachments.screenshot(driver, "Корзина после отмены оформления заказа");
    }
}
