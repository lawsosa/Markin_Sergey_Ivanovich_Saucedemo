package pages;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import base.BasePage;

public class InventoryPage extends BasePage {

    private final By title = By.cssSelector("[data-test='title']");
    private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By sortSelect = By.cssSelector("[data-test='product-sort-container']");
    private final By inventoryItems = By.cssSelector("[data-test='inventory-item']");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public String getTitleText() {
        return visible(title).getText();
    }

    public boolean isOpen() {
        return wait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/inventory.html"));
    }

    public String getCartBadgeText() {
        return visible(cartBadge).getText();
    }

    public InventoryPage addProduct(String productName) {

        By addButton = By.xpath(
                "//div[@data-test='inventory-item']" +
                        "[.//div[@data-test='inventory-item-name' and normalize-space()=" +
                        quote(productName) + "]]" + "//button[contains(@data-test,'add-to-cart')]"
        );

        click(addButton);

        return this;
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    public InventoryPage selectSort(String value) {
        new Select(visible(sortSelect)).selectByValue(value);
        return this;
    }

    public List<String> getProductNames() {
        return driver.findElements(inventoryItems)
                .stream()
                .map(item ->
                        item.findElement(By.cssSelector("[data-test='inventory-item-name']")).getText())
                .collect(Collectors.toList());
    }

    public List<Double> getProductPrices() {

        List<Double> prices = new ArrayList<>();

        for (WebElement item : driver.findElements(inventoryItems)) {
            String price = item.findElement(By.cssSelector("[data-test='inventory-item-price']")).getText();
            prices.add(Double.parseDouble(price.replace("$", "")));
        }

        return prices;
    }

    private static String quote(String text) {
        return "'" + text.replace("'", "&apos;") + "'";
    }
}