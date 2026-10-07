package pages;

import base.BasePage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPage extends BasePage {

    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");

    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");

    private final By completeHeader = By.cssSelector("[data-test='complete-header']");

    private final By error = By.cssSelector("[data-test='error']");

    private final By total = By.cssSelector("[data-test='total-label']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage fillCustomerInfo(String first, String last, String zip) {
        type(firstName, first);
        type(lastName, last);
        type(postalCode, zip);

        return this;
    }

    public CheckoutPage continueToOverview() {
        click(continueButton);
        return this;
    }

    public CheckoutPage continueExpectingError() {
        click(continueButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(error));

        return this;
    }

    public String getErrorText() {
        return visible(error).getText();
    }

    public String getTotalText() {
        return visible(total).getText();
    }

    public CheckoutPage finish() {
        click(finishButton);
        return this;
    }

    public String getCompleteHeader() {
        return visible(completeHeader).getText();
    }
}