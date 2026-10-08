package base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import drivers.BrowserFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Parameter;
import utils.AllureAttachments;
import utils.BrowserConfig;
import utils.EnvironmentWriter;
import utils.TestConfig;


public abstract class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String browser;
    protected boolean headless;

    
    @BeforeSuite(alwaysRun = true)
    public void prepareAllureEnvironment() {
        EnvironmentWriter.writeAll();
    }

    @Parameters({"browser", "headless"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional String browserParameter,
                      @Optional String headlessParameter) {

        browser = resolveBrowser(browserParameter);
        headless = resolveHeadless(headlessParameter);

        driver = BrowserFactory.create(browser, headless);

        wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.EXPLICIT_WAIT_SECONDS));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        String testName = result.getMethod().getMethodName();
        boolean failed = !result.isSuccess();

        if (failed) {
            AllureAttachments.screenshotAndPageSource(driver,
                    "Падение теста " + testName + " [" + browser + "]");
        }

        addBrowserParametersToReport();

        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    
    private void addBrowserParametersToReport() {
        AllureLifecycle lifecycle = Allure.getLifecycle();
        lifecycle.getCurrentTestCase().ifPresent(uuid -> {
            try {
                lifecycle.updateTestCase(uuid, testResult -> {
                    testResult.getParameters().add(new Parameter()
                            .setName("browser").setValue(browser).setMode(Parameter.Mode.DEFAULT));
                    testResult.getParameters().add(new Parameter()
                            .setName("headless").setValue(String.valueOf(headless)).setMode(Parameter.Mode.DEFAULT));
                });
            } catch (RuntimeException ignored) {
                
            }
        });
    }

    private String resolveBrowser(String browserParameter) {
        
        if (browserParameter != null && !browserParameter.isBlank()) {
            return BrowserConfig.normalize(browserParameter);
        }
        return BrowserConfig.browserFromSystemProperty();
    }

    private boolean resolveHeadless(String headlessParameter) {
        if (headlessParameter != null && !headlessParameter.isBlank()) {
            return BrowserConfig.parseHeadless(headlessParameter);
        }
        return BrowserConfig.headlessFromSystemProperty();
    }
}