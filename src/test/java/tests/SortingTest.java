package tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;
import utils.TestConfig;

public class SortingTest extends BaseTest {
    @Test(description = "Products can be sorted by price low to high")
    public void productsCanBeSortedByPriceAscending() {
        InventoryPage inventory = new LoginPage(driver).open().loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD);
        inventory.selectSort("lohi");
        List<Double> actual = inventory.getProductPrices();
        List<Double> expected = new ArrayList<>(actual);
        Collections.sort(expected);
        Assert.assertEquals(actual, expected, "Цены должны быть отсортированы по возрастанию");
    }
}
