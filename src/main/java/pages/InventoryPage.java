package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.BasePage;
import io.qameta.allure.Step;
import utils.Locators;
import utils.TestConfig;


public class InventoryPage extends BasePage {

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    

    private static By itemByName(String productName) {
        return By.xpath("//div[@data-test='inventory-item'][.//div[@data-test='inventory-item-name'"
                + " and normalize-space()=" + quote(productName) + "]]");
    }

    private static By buttonByName(String productName, String buttonDataTest) {
        return By.xpath("//div[@data-test='inventory-item'][.//div[@data-test='inventory-item-name'"
                + " and normalize-space()=" + quote(productName) + "]]"
                + "//button[@data-test='" + buttonDataTest + "']");
    }

    private static String quote(String value) {
        return "'" + value.replace("'", "&apos;") + "'";
    }

    

    @Step("Добавить товар '{productName}' в корзину")
    public InventoryPage addProduct(String productName) {
        By button = buttonByName(productName, "add-to-cart-" + slug(productName));
        scrollTo(button);
        click(button);
        return this;
    }

    @Step("Удалить товар '{productName}' из корзины со страницы каталога")
    public InventoryPage removeProduct(String productName) {
        By button = buttonByName(productName, "remove-" + slug(productName));
        scrollTo(button);
        click(button);
        return this;
    }

    @Step("Выбрать сортировку товаров: '{sortValue}'")
    public InventoryPage selectSort(String sortValue) {
        selectByValue(Locators.Inventory.SORT_SELECT, sortValue);
        return this;
    }

    @Step("Открыть корзину")
    public CartPage openCart() {
        click(Locators.Inventory.CART_LINK);
        return new CartPage(driver);
    }

    @Step("Открыть меню и выйти из аккаунта")
    public LoginPage logout() {
        click(Locators.Inventory.BURGER_MENU);
        
        visible(Locators.Inventory.MENU_WRAPPER);
        click(Locators.Inventory.LOGOUT_LINK);
        return new LoginPage(driver);
    }

    

    @Step("Открыть каталог товаров по прямой ссылке (без авторизации)")
    public InventoryPage openDirectly() {
        driver.get(TestConfig.BASE_URL + "inventory.html");
        return this;
    }

    @Step("Получить заголовок страницы каталога")
    public String getTitleText() {
        return text(Locators.Inventory.TITLE);
    }

    @Step("Проверить, что открыт каталог товаров (/inventory.html)")
    public boolean isOpen() {
        return urlContains("/inventory.html") && isVisible(Locators.Inventory.TITLE);
    }

    @Step("Получить значение счётчика корзины")
    public String getCartBadgeText() {
        return text(Locators.Inventory.CART_BADGE);
    }

    @Step("Дождаться значения счётчика корзины '{expectedValue}'")
    public InventoryPage waitForCartBadge(String expectedValue) {
        textEquals(Locators.Inventory.CART_BADGE, expectedValue);
        return this;
    }

    @Step("Проверить, что счётчик корзины отображается")
    public boolean isCartBadgeDisplayed() {
        return isVisible(Locators.Inventory.CART_BADGE);
    }

    @Step("Получить количество товаров в каталоге")
    public int getProductCount() {
        return elements(Locators.Inventory.ITEM).size();
    }

    @Step("Получить названия товаров в каталоге")
    public List<String> getProductNames() {
        List<String> names = new ArrayList<>();
        for (WebElement item : elements(Locators.Inventory.ITEM)) {
            names.add(item.findElement(Locators.Inventory.ITEM_NAME).getText());
        }
        return names;
    }

    @Step("Получить цены товаров в каталоге")
    public List<Double> getProductPrices() {
        List<Double> prices = new ArrayList<>();
        for (WebElement item : elements(Locators.Inventory.ITEM)) {
            prices.add(parsePrice(item.findElement(Locators.Inventory.ITEM_PRICE).getText()));
        }
        return prices;
    }

    @Step("Получить цену товара '{productName}'")
    public double getProductPrice(String productName) {
        By price = By.xpath("//div[@data-test='inventory-item'][.//div[@data-test='inventory-item-name'"
                + " and normalize-space()=" + quote(productName) + "]]"
                + "//div[@data-test='inventory-item-price']");
        return parsePrice(text(price));
    }

    @Step("Проверить, что для товара '{productName}' доступна кнопка '{buttonDataTest}'")
    public boolean isProductButtonDisplayed(String productName, String buttonDataTest) {
        return isVisible(buttonByName(productName, buttonDataTest));
    }

    private static double parsePrice(String rawPrice) {
        return Double.parseDouble(rawPrice.replace("$", "").replace(",", ".").trim());
    }

    private static String slug(String productName) {
        return productName.toLowerCase().replace(" ", "-");
    }
}
