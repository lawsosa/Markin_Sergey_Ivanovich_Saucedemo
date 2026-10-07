package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.InventoryPage;
import pages.LoginPage;
import utils.TestConfig;

public class CartTest extends BaseTest {
    @Test(description = "User can add Sauce Labs Backpack to cart")
    public void userCanAddBackpackToCart() {
        InventoryPage inventory = new LoginPage(driver).open().loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        inventory.addProduct("Sauce Labs Backpack");
        Assert.assertEquals(inventory.getCartBadgeText(), "1", "Ожидался счётчик корзины 1");

        CartPage cart = inventory.openCart();
        Assert.assertTrue(cart.getItemNames().contains("Sauce Labs Backpack"), "Корзина должна содержать Sauce Labs Backpack");
        Assert.assertEquals(cart.getItemPrice("Sauce Labs Backpack"), "$29.99", "Ожидалась цена Backpack $29.99");
    }
}
