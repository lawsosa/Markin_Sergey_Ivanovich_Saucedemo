package utils;

import java.io.ByteArrayInputStream;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.qameta.allure.Allure;

/**
 * Вложения Allure: скриншоты, HTML-исходник страницы и текстовые логи.
 * <p>
 * Используется программный API {@code Allure.addAttachment(...)} — он не требует
 * инструментирования AspectJ, поэтому вложения попадают в отчёт всегда.
 */
public final class AllureAttachments {

    private AllureAttachments() {
    }

    /** Скриншот текущего состояния страницы (PNG). */
    public static void screenshot(WebDriver driver, String name) {
        if (driver == null) {
            return;
        }
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(bytes), "png");
        } catch (Exception e) {
            text(name + " (скриншот недоступен)", e.toString());
        }
    }

    /** HTML-исходник страницы (полезен для разбора падений по локаторам). */
    public static void pageSource(WebDriver driver, String name) {
        if (driver == null) {
            return;
        }
        try {
            Allure.addAttachment(name, "text/html", driver.getPageSource(), "html");
        } catch (Exception e) {
            text(name + " (page source недоступен)", e.toString());
        }
    }

    /** Произвольное текстовое вложение (значения, списки, результат проверки). */
    public static void text(String name, String content) {
        Allure.addAttachment(name, "text/plain", content);
    }
}
