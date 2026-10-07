package pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.BasePage;

public class CartPage extends BasePage {

    private final By cartItems =
            By.cssSelector("[data-test='inventory-item']");

    private final By checkout =
            By.cssSelector("[data-test='checkout']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getItemNames() {
        return driver.findElements(cartItems)
                .stream()
                .map(item ->
                        item.findElement(
                                By.cssSelector("[data-test='inventory-item-name']")
                        ).getText()
                )
                .collect(Collectors.toList());
    }

    public String getItemPrice(String productName) {

        By item = By.xpath(
                "//div[@data-test='inventory-item']" +
                        "[.//div[@data-test='inventory-item-name' and normalize-space()=" +
                        quote(productName) +
                        "]]"
        );

        return visible(item)
                .findElement(By.cssSelector("[data-test='inventory-item-price']"))
                .getText();
    }

    public CheckoutPage checkout() {
        click(checkout);
        return new CheckoutPage(driver);
    }

    private static String quote(String text) {
        return "'" + text.replace("'", "&apos;") + "'";
    }
}