package tests;

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
@Feature("Навигация и защита страниц")
@Owner("Маркин Сергей")
@Link(name = "Saucedemo", url = "https://www.saucedemo.com/")
public class NavigationTest extends BaseTest {

    @Test(description = "Негатив: прямой переход в каталог без авторизации блокируется")
    @Story("Доступ без авторизации")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("SAUCE-19")
    @Description("При открытии /inventory.html без активной сессии Saucedemo перенаправляет "
            + "на страницу входа и выводит сообщение о необходимости авторизации.")
    public void inventoryPageIsNotAccessibleWithoutLogin() {

        new InventoryPage(driver).openDirectly();
        LoginPage loginPage = new LoginPage(driver);

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Без авторизации должна появляться ошибка доступа к каталогу");

        String errorText = loginPage.getErrorText();

        Assert.assertTrue(errorText.contains("/inventory.html"),
                "Сообщение должно указывать на недоступный раздел, фактически: " + errorText);
        Assert.assertFalse(driver.getCurrentUrl().contains("/inventory.html"),
                "Каталог товаров не должен открываться без авторизации");

        AllureAttachments.screenshot(driver, "Ошибка доступа к каталогу без авторизации");
    }

    @Test(description = "Негатив: прямой переход в корзину без авторизации блокируется")
    @Story("Доступ без авторизации")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("SAUCE-20")
    @Description("Открытие /cart.html без авторизации приводит к перенаправлению на страницу входа "
            + "с сообщением о необходимости авторизации.")
    public void cartPageIsNotAccessibleWithoutLogin() {

        driver.get(TestConfig.BASE_URL + "cart.html");
        LoginPage loginPage = new LoginPage(driver);

        Assert.assertTrue(loginPage.isOpen(),
                "Пользователь должен быть возвращён на страницу авторизации");
        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Ожидалось сообщение о необходимости авторизации");
        Assert.assertTrue(loginPage.getErrorText().contains("/cart.html"),
                "Сообщение должно указывать на недоступный раздел, фактически: " + loginPage.getErrorText());

        AllureAttachments.screenshot(driver, "Ошибка доступа к корзине без авторизации");
    }

    @Test(description = "Позитив: защищённая страница открывается после авторизации")
    @Story("Доступ после авторизации")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("SAUCE-21")
    @Description("После входа прямой переход на /inventory.html открывает каталог товаров "
            + "с заголовком Products — сессия сохраняется между переходами.")
    public void inventoryPageIsAccessibleAfterLogin() {

        new LoginPage(driver)
                .open()
                .loginAs(TestConfig.STANDARD_USER, TestConfig.PASSWORD);

        InventoryPage inventory = new InventoryPage(driver).openDirectly();

        Assert.assertTrue(inventory.isOpen(),
                "После авторизации каталог должен открываться по прямой ссылке");
        Assert.assertEquals(inventory.getTitleText(), TestConfig.PRODUCTS_TITLE,
                "Ожидался заголовок каталога товаров");

        AllureAttachments.screenshot(driver, "Каталог доступен после авторизации");
    }
}
