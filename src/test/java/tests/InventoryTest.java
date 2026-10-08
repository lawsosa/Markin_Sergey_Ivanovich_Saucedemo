package tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import pages.InventoryPage;
import pages.LoginPage;
import utils.AllureAttachments;
import utils.TestConfig;


@Epic("Saucedemo UI")
@Feature("Каталог товаров")
@Owner("Маркин Сергей")
@Link(name = "Saucedemo", url = "https://www.saucedemo.com/")
public class InventoryTest extends BaseTest {

    @Test(description = "Сортировка каталога по цене от дешёвых к дорогим (lohi)")
    @Story("Сортировка товаров")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("SAUCE-5")
    @Description("Проверяем, что после выбора сортировки Price (low to high) "
            + "цены в каталоге идут по возрастанию.")
    public void productsCanBeSortedByPriceAscending() {

        InventoryPage inventory = new LoginPage(driver)
                .open()
                .loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD)
                .selectSort(TestConfig.SORT_PRICE_ASC);

        List<Double> actualPrices = inventory.getProductPrices();
        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices);

        AllureAttachments.text("Цены после сортировки low->high", actualPrices.toString());

        Assert.assertEquals(actualPrices, expectedPrices,
                "Цены должны быть отсортированы по возрастанию");
        Assert.assertEquals(actualPrices.size(), inventory.getProductCount(),
                "Количество цен должно совпадать с количеством товаров");
    }

    @Test(description = "Сортировка каталога по цене от дорогих к дешёвым (hilo)")
    @Story("Сортировка товаров")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("SAUCE-6")
    @Description("Проверяем обратную сортировку по цене: первым должен идти самый дорогой товар.")
    public void productsCanBeSortedByPriceDescending() {

        InventoryPage inventory = new LoginPage(driver)
                .open()
                .loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD)
                .selectSort(TestConfig.SORT_PRICE_DESC);

        List<Double> actualPrices = inventory.getProductPrices();
        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        expectedPrices.sort(Collections.reverseOrder());

        AllureAttachments.text("Цены после сортировки high->low", actualPrices.toString());

        Assert.assertEquals(actualPrices, expectedPrices,
                "Цены должны быть отсортированы по убыванию");
    }

    @Test(description = "Сортировка каталога по названию от A до Z (az)")
    @Story("Сортировка товаров")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("SAUCE-7")
    @Description("Проверяем алфавитную сортировку названий товаров.")
    public void productsCanBeSortedByNameAscending() {

        InventoryPage inventory = new LoginPage(driver)
                .open()
                .loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD)
                .selectSort(TestConfig.SORT_NAME_ASC);

        List<String> actualNames = inventory.getProductNames();
        List<String> expectedNames = new ArrayList<>(actualNames);
        Collections.sort(expectedNames);

        AllureAttachments.text("Названия после сортировки A->Z", String.join(" | ", actualNames));

        Assert.assertEquals(actualNames, expectedNames,
                "Названия товаров должны быть отсортированы по алфавиту");
        Assert.assertEquals(actualNames.get(0), TestConfig.BACKPACK,
                "Первым по алфавиту должен идти Sauce Labs Backpack");
    }

    @Test(description = "Счётчик корзины суммирует все добавленные товары")
    @Story("Счётчик корзины")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("SAUCE-8")
    @Description("Проверяем, что после добавления двух товаров счётчик корзины равен 2, "
            + "а кнопки Add to cart заменяются на Remove.")
    public void cartBadgeCountsAllAddedProducts() {

        InventoryPage inventory = new LoginPage(driver)
                .open()
                .loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD);

        inventory.addProduct(TestConfig.BACKPACK);
        inventory.addProduct(TestConfig.BIKE_LIGHT).waitForCartBadge("2");

        Assert.assertEquals(inventory.getCartBadgeText(), "2",
                "Счётчик корзины должен быть равен количеству добавленных товаров");
        Assert.assertTrue(inventory.isProductButtonDisplayed(TestConfig.BACKPACK, "remove-sauce-labs-backpack"),
                "После добавления товара кнопка должна называться Remove");
        Assert.assertTrue(inventory.isProductButtonDisplayed(TestConfig.BIKE_LIGHT, "remove-sauce-labs-bike-light"),
                "После добавления товара кнопка должна называться Remove");

        AllureAttachments.screenshot(driver, "Каталог со счётчиком корзины = 2");
    }

    @Test(description = "Позитив: выход из аккаунта возвращает на страницу авторизации")
    @Story("Выход из аккаунта")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("SAUCE-9")
    @Description("Проверяем, что пункт Logout в бургер-меню завершает сессию "
            + "и пользователь возвращается на страницу входа.")
    public void userCanLogout() {

        LoginPage loginPage = new LoginPage(driver)
                .open()
                .loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD)
                .logout();

        Assert.assertTrue(loginPage.isOpen(),
                "После выхода должна открываться страница авторизации");
        Assert.assertFalse(driver.getCurrentUrl().contains("/inventory.html"),
                "После выхода каталог товаров недоступен без авторизации");

        AllureAttachments.screenshot(driver, "Страница входа после выхода из аккаунта");
    }
}
