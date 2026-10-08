package utils;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.qameta.allure.Allure;


public final class AllureAttachments {

    private AllureAttachments() {
    }

    
    public static void screenshot(WebDriver driver, String name) {
        if (driver == null) {
            return;
        }
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(bytes), ".png");
        } catch (Exception e) {
            Allure.addAttachment(name + " (не удалось снять скриншот)", "text/plain", e.toString());
        }
    }

    
    public static void pageSource(WebDriver driver, String name) {
        if (driver == null) {
            return;
        }
        try {
            Allure.addAttachment(name, "text/html", driver.getPageSource(), ".html");
        } catch (Exception e) {
            Allure.addAttachment(name + " (не удалось получить page source)", "text/plain", e.toString());
        }
    }

    
    public static void pageUrl(WebDriver driver, String name) {
        if (driver == null) {
            return;
        }
        try {
            String info = "URL: " + driver.getCurrentUrl() + System.lineSeparator()
                    + "Title: " + driver.getTitle();
            Allure.addAttachment(name, "text/plain", info, ".txt");
        } catch (Exception e) {
            Allure.addAttachment(name + " (не удалось получить URL)", "text/plain", e.toString());
        }
    }

    
    public static void text(String name, String content) {
        Allure.addAttachment(name, "text/plain",
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), ".txt");
    }

    
    public static void json(String name, String json) {
        Allure.addAttachment(name, "application/json", json, ".json");
    }

    
    public static void screenshotAndPageSource(WebDriver driver, String prefix) {
        screenshot(driver, prefix + ": скриншот");
        pageSource(driver, prefix + ": HTML страницы");
        pageUrl(driver, prefix + ": URL страницы");
    }
}
