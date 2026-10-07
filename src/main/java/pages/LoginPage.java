package pages;

import base.BasePage;
import utils.TestConfig;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(TestConfig.BASE_URL);
        visible(username);
        return this;
    }

    public InventoryPage loginAs(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginButton);

        return new InventoryPage(driver);
    }

    public LoginPage loginExpectingError(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginButton);

        wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));

        return this;
    }

    public String getErrorText() {
        return visible(errorMessage).getText();
    }
}