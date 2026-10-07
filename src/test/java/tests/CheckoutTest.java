package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;
import utils.TestConfig;

public class CheckoutTest extends BaseTest {
    @Test(description = "User can complete checkout")
    public void userCanCompleteCheckout() {
        InventoryPage inventory = new LoginPage(driver).open().loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        CartPage cart = inventory.addProduct("Sauce Labs Backpack").openCart();
        CheckoutPage checkout = cart.checkout();
        checkout.fillCustomerInfo("Ivan", "Ivanov", "101000").continueToOverview();

        Assert.assertTrue(checkout.getTotalText().startsWith("Total:"), "На странице Overview должен отображаться Total");
        checkout.finish();
        Assert.assertEquals(checkout.getCompleteHeader(), "Thank you for your order!", "После Finish ожидался экран подтверждения заказа");
    }

    @Test(description = "Checkout validates required customer data")
    public void checkoutRequiresCustomerData() {
        InventoryPage inventory = new LoginPage(driver).open().loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        CheckoutPage checkout = inventory.addProduct("Sauce Labs Backpack").openCart().checkout();
        checkout.continueExpectingError();
        Assert.assertTrue(checkout.getErrorText().contains("First Name is required"), "Ожидалась валидация обязательного First Name, фактически: " + checkout.getErrorText());
    }
}
