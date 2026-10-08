package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;
import io.qameta.allure.Step;

/**
 * Страница корзины (Your Cart).
 */
public class CartPage extends BasePage {

    private static final String URL_PART = "/cart.html";

    private final By title = By.cssSelector("[data-test='title']");
    private final By cartItems = By.cssSelector("[data-test='inventory-item']");
    private final By itemName = By.cssSelector("[data-test='inventory-item-name']");
    private final By itemPrice = By.cssSelector("[data-test='inventory-item-price']");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By checkoutButton = By.cssSelector("[data-test='checkout']");
    private final By continueShoppingButton = By.cssSelector("[data-test='continue-shopping']");

    public CartPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Проверить, что открыта страница корзины")
    public boolean isOpen() {
        return wait.until(ExpectedConditions.urlContains(URL_PART));
    }

    @Step("Получить заголовок страницы корзины")
    public String getTitleText() {
        return text(title);
    }

    @Step("Получить список товаров в корзине")
    public List<String> getItemNames() {
        visible(title);
        return textsOf(cartItems, itemName);
    }

    @Step("Получить цену товара {productName} в корзине")
    public String getItemPrice(String productName) {
        return visible(itemContainer(productName)).findElement(itemPrice).getText();
    }

    @Step("Удалить товар {productName} из корзины")
    public CartPage removeProduct(String productName) {
        click(itemButton(productName, "remove"));
        waitForAbsence(itemContainer(productName));
        return this;
    }

    @Step("Проверить, что корзина пуста")
    public boolean isEmpty() {
        return driver.findElements(cartItems).isEmpty();
    }

    @Step("Проверить, отображается ли значок с количеством товаров в корзине")
    public boolean isCartBadgeDisplayed() {
        return isVisible(cartBadge);
    }

    @Step("Вернуться в каталог товаров (Continue Shopping)")
    public InventoryPage continueShopping() {
        click(continueShoppingButton);
        return new InventoryPage(driver, wait);
    }

    @Step("Перейти к оформлению заказа (Checkout)")
    public CheckoutPage checkout() {
        click(checkoutButton);
        return new CheckoutPage(driver, wait);
    }
}
