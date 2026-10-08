package base;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.TestConfig;


public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final WebDriverWait shortWait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.EXPLICIT_WAIT_SECONDS));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    

    
    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    
    protected WebElement present(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    
    protected WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    
    protected boolean invisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    
    protected boolean textPresent(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    
    protected boolean textEquals(By locator, String expectedText) {
        return wait.until(ExpectedConditions.textToBe(locator, expectedText));
    }

    
    protected boolean urlContains(String fragment) {
        return wait.until(ExpectedConditions.urlContains(fragment));
    }

    
    protected List<WebElement> elements(By locator) {
        return driver.findElements(locator);
    }

    

    
    protected void click(By locator) {
        clickable(locator).click();
    }

    
    protected void jsClick(By locator) {
        WebElement element = clickable(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    
    protected void type(By locator, String value) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(value);
    }

    
    protected void selectByValue(By locator, String value) {
        new Select(visible(locator)).selectByValue(value);
    }

    
    protected String text(By locator) {
        return visible(locator).getText();
    }

    
    protected String attribute(By locator, String name) {
        return present(locator).getAttribute(name);
    }

    
    protected boolean isVisible(By locator) {
        try {
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
            } catch (Exception ignored) {
                return false;
            }
        }
    }

    
    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    
    protected void scrollTo(By locator) {
        WebElement element = present(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    
    protected void refresh() {
        driver.navigate().refresh();
    }
}
