package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;
import io.qameta.allure.Step;

/**
 * Страница каталога товаров (Products) с сортировкой и кнопками добавления в корзину.
 */
public class InventoryPage extends BasePage {

    private static final String URL_PART = "/inventory.html";

    private final By title = By.cssSelector("[data-test='title']");
    private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By sortSelect = By.cssSelector("[data-test='product-sort-container']");
    private final By inventoryItems = By.cssSelector("[data-test='inventory-item']");
    private final By itemName = By.cssSelector("[data-test='inventory-item-name']");
    private final By itemPrice = By.cssSelector("[data-test='inventory-item-price']");

    public InventoryPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Проверить, что открыт каталог товаров")
    public boolean isOpen() {
        return wait.until(ExpectedConditions.urlContains(URL_PART));
    }

    @Step("Получить заголовок страницы каталога")
    public String getTitleText() {
        return text(title);
    }

    @Step("Добавить товар {productName} в корзину")
    public InventoryPage addProduct(String productName) {
        click(itemButton(productName, "add-to-cart"));
        return this;
    }

    @Step("Удалить товар {productName} из корзины на странице каталога")
    public InventoryPage removeProduct(String productName) {
        click(itemButton(productName, "remove"));
        return this;
    }

    @Step("Открыть корзину")
    public CartPage openCart() {
        click(cartLink);
        return new CartPage(driver, wait);
    }

    @Step("Отсортировать товары (значение сортировки: {value})")
    public InventoryPage selectSort(String value) {
        new Select(visible(sortSelect)).selectByValue(value);
        return this;
    }

    @Step("Получить список названий товаров в каталоге")
    public List<String> getProductNames() {
        present(inventoryItems);
        return textsOf(inventoryItems, itemName);
    }

    @Step("Получить список цен товаров в каталоге")
    public List<Double> getProductPrices() {
        present(inventoryItems);

        List<Double> prices = new ArrayList<>();
        for (String price : textsOf(inventoryItems, itemPrice)) {
            prices.add(Double.parseDouble(price.replace("$", "").trim()));
        }
        return prices;
    }

    @Step("Получить количество товаров в корзине по значку на иконке корзины")
    public String getCartBadgeText() {
        return text(cartBadge);
    }

    @Step("Проверить, отображается ли значок с количеством товаров в корзине")
    public boolean isCartBadgeDisplayed() {
        return isVisible(cartBadge);
    }

    @Step("Дождаться исчезновения значка с количеством товаров в корзине")
    public InventoryPage waitUntilCartBadgeDisappears() {
        waitForAbsence(cartBadge);
        return this;
    }
}
