package base;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.qameta.allure.Step;
import utils.TestConfig;

/**
 * Базовый класс для всех Page Object.
 * <p>
 * Здесь собраны единые обёртки над явными ожиданиями (WebDriverWait + ExpectedConditions)
 * и типовыми действиями. Наследники работают только с локаторами и бизнес-логикой страницы.
 * <p>
 * Шаги помечены аннотацией {@link Step}; их отображение в отчёте Allure обеспечивает
 * AspectJ LTW (javaagent aspectjweaver + META-INF/aop.xml).
 */
public abstract class BasePage {

    /** Локатор карточки товара, внутри которой находится элемент с указанным текстом названия. */
    private static final String ITEM_BY_NAME =
            "//div[@data-test='inventory-item']"
                    + "[.//div[@data-test='inventory-item-name' and normalize-space()=%s]]";

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = (wait != null)
                ? wait
                : new WebDriverWait(driver, Duration.ofSeconds(TestConfig.EXPLICIT_WAIT_SECONDS));
    }

    // ------------------------------------------------------------------ ожидания

    /** Ожидание видимости элемента. */
    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Ожидание присутствия элемента в DOM (без проверки видимости). */
    protected WebElement present(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /** Ожидание кликабельности элемента. */
    protected WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Проверка видимости без выбрасывания исключения (для негативных проверок). */
    protected boolean isVisible(By locator) {
        try {
            return visible(locator).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /** Ожидание исчезновения элемента из DOM. */
    protected void waitForAbsence(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /** Ожидание перехода на страницу, URL которой содержит указанный фрагмент. */
    protected void waitForUrl(String urlPart) {
        wait.until(ExpectedConditions.urlContains(urlPart));
    }

    protected String currentUrl() {
        return driver.getCurrentUrl();
    }

    // ------------------------------------------------------------------ действия

    @Step("Клик по элементу {locator}")
    protected void click(By locator) {
        clickable(locator).click();
    }

    @Step("Ввод значения \"{value}\" в поле {locator}")
    protected void type(By locator, String value) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(value);
    }

    @Step("Получение текста элемента {locator}")
    protected String text(By locator) {
        return visible(locator).getText();
    }

    @Step("Прокрутка страницы к элементу {locator}")
    protected void scrollTo(By locator) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", present(locator));
    }

    /**
     * Тексты дочерних элементов всех найденных контейнеров (в порядке отображения).
     * <p>
     * Метод не ожидает появления контейнеров, так как список может быть пустым
     * (например, пустая корзина). Ожидание нужного состояния страницы выполняет вызывающий код.
     */
    protected List<String> textsOf(By container, By child) {
        return driver.findElements(container).stream()
                .map(element -> element.findElement(child).getText())
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------------ утилиты локаторов SauceDemo

    /** Локатор карточки товара по его названию. */
    protected By itemContainer(String itemName) {
        return By.xpath(ITEM_BY_NAME.formatted(xpathLiteral(itemName)));
    }

    /**
     * Локатор кнопки внутри карточки товара (SauceDemo меняет data-test кнопки
     * на add-to-cart / remove, поэтому ищем по началу значения атрибута).
     */
    protected By itemButton(String itemName, String dataTestPrefix) {
        return By.xpath(
                ITEM_BY_NAME.formatted(xpathLiteral(itemName))
                        + "//button[contains(@data-test,'" + dataTestPrefix + "')]"
        );
    }

    /** Безопасная подстановка произвольной строки в XPath-выражение. */
    protected static String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        StringBuilder result = new StringBuilder("concat(");
        String[] parts = value.split("'", -1);
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                result.append(", \"'\", ");
            }
            result.append('\'').append(parts[i]).append('\'');
        }
        return result.append(')').toString();
    }
}
