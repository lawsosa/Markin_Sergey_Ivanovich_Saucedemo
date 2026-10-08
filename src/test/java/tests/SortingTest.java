package tests;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import utils.AllureAttachments;

@Epic("SauceDemo UI")
@Feature("Сортировка каталога")
@Owner("Izteleuev Daniel")
@Link(name = "SauceDemo", url = "https://www.saucedemo.com/")
public class SortingTest extends BaseTest {

    @Test(description = "Позитивный сценарий: сортировка товаров по цене (Low to High)")
    @Story("Сортировка по цене")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("SD-SORT-1")
    @Description("После выбора сортировки lohi цены товаров идут по возрастанию.")
    public void productsCanBeSortedByPriceAscending() {

        InventoryPage inventory = loginAsStandardUser();
        inventory.selectSort("lohi");

        List<Double> actual = inventory.getProductPrices();
        List<Double> expected = new ArrayList<>(actual);
        Collections.sort(expected);

        AllureAttachments.text("Фактический порядок цен", actual.toString());

        assertEquals(
                actual,
                expected,
                "Цены должны быть отсортированы по возрастанию"
        );
    }

    @Test(description = "Позитивный сценарий: сортировка товаров по названию (Z to A)")
    @Story("Сортировка по названию")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("SD-SORT-2")
    @Description("После выбора сортировки za названия товаров идут в обратном алфавитном порядке.")
    public void productsCanBeSortedByNameDescending() {

        InventoryPage inventory = loginAsStandardUser();
        inventory.selectSort("za");

        List<String> actual = inventory.getProductNames();
        List<String> expected = new ArrayList<>(actual);
        Collections.sort(expected);
        Collections.reverse(expected);

        AllureAttachments.text("Фактический порядок названий", String.join("\n", actual));

        assertEquals(
                actual,
                expected,
                "Названия товаров должны быть отсортированы от Z к A"
        );
    }
}
