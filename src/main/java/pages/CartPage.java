package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.BasePage;
import io.qameta.allure.Step;
import utils.Locators;


public class CartPage extends BasePage {

    public CartPage(WebDriver driver) {
        super(driver);
    }

    private static By itemByName(String productName) {
        return By.xpath("//div[@data-test='inventory-item'][.//div[@data-test='inventory-item-name'"
                + " and normalize-space()=" + quote(productName) + "]]");
    }

    private static String quote(String value) {
        return "'" + value.replace("'", "&apos;") + "'";
    }

    @Step("Получить названия товаров в корзине")
    public List<String> getItemNames() {
        List<String> names = new ArrayList<>();
        for (WebElement item : elements(Locators.Cart.CART_ITEM)) {
            names.add(item.findElement(Locators.Cart.ITEM_NAME).getText());
        }
        return names;
    }

    @Step("Получить цену товара '{productName}' в корзине")
    public String getItemPrice(String productName) {
        return present(itemByName(productName)).findElement(Locators.Cart.ITEM_PRICE).getText();
    }

    @Step("Получить количество единиц товара '{productName}' в корзине")
    public String getItemQuantity(String productName) {
        return present(itemByName(productName)).findElement(Locators.Cart.QUANTITY).getText();
    }

    @Step("Удалить товар '{productName}' из корзины")
    public CartPage removeItem(String productName) {
        By removeButton = By.xpath("//div[@data-test='inventory-item'][.//div[@data-test='inventory-item-name'"
                + " and normalize-space()=" + quote(productName) + "]]//button[contains(@data-test,'remove')]");
        click(removeButton);
        return this;
    }

    @Step("Проверить, что корзина пуста")
    public boolean isEmpty() {
        return elements(Locators.Cart.CART_ITEM).isEmpty();
    }

    @Step("Получить количество позиций в корзине")
    public int getItemsCount() {
        return elements(Locators.Cart.CART_ITEM).size();
    }

    @Step("Перейти к оформлению заказа")
    public CheckoutPage checkout() {
        click(Locators.Cart.CHECKOUT_BUTTON);
        return new CheckoutPage(driver);
    }

    @Step("Вернуться к покупкам (Continue Shopping)")
    public InventoryPage continueShopping() {
        click(Locators.Cart.CONTINUE_SHOPPING);
        return new InventoryPage(driver);
    }
}
