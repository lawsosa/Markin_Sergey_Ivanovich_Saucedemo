package utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicBoolean;

import org.openqa.selenium.WebDriver;
import org.testng.TestNG;

import io.qameta.allure.Allure;

/**
 * Формирование блока Environment и классификации дефектов Allure.
 * <p>
 * environment.properties и categories.json должны лежать в каталоге результатов
 * (target/allure-results) до генерации HTML-отчёта.
 */
public final class AllureEnvironment {

    /** Каталог результатов; должен совпадать с allure.results.directory в allure.properties. */
    public static final Path RESULTS_DIRECTORY = Path.of("target", "allure-results");

    private static final AtomicBoolean WRITTEN = new AtomicBoolean(false);

    private AllureEnvironment() {
    }

    /**
     * Однократно записывает environment.properties и categories.json в каталог результатов.
     * Гард нужен из-за параллельного прогона (parallel="tests"): метод вызывается
     * из @BeforeSuite каждого тестового класса.
     */
    public static void write() {
        if (!WRITTEN.compareAndSet(false, true)) {
            return;
        }
        try {
            Files.createDirectories(RESULTS_DIRECTORY);
            Files.writeString(RESULTS_DIRECTORY.resolve("environment.properties"), environment());
            copyCategories();
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось записать файлы окружения Allure", e);
        }
    }

    private static String environment() {
        return """
                os.name=%s
                os.version=%s
                os.arch=%s
                java.version=%s
                java.vendor=%s
                selenium=%s
                testng=%s
                allure=%s
                browser=Chrome; Firefox; Edge
                driver.manager=Selenium Manager
                application=SauceDemo
                base.url=%s
                """.formatted(
                System.getProperty("os.name"),
                System.getProperty("os.version"),
                System.getProperty("os.arch"),
                System.getProperty("java.version"),
                System.getProperty("java.vendor"),
                libraryVersion(WebDriver.class, "4.48.0"),
                libraryVersion(TestNG.class, "7.10.2"),
                libraryVersion(Allure.class, "2.25.0"),
                TestConfig.BASE_URL
        );
    }

    /** Версия библиотеки из манифеста jar-файла с откатом на версию из pom.xml. */
    private static String libraryVersion(Class<?> type, String fallback) {
        Package libraryPackage = type.getPackage();
        String version = (libraryPackage == null) ? null : libraryPackage.getImplementationVersion();
        return (version == null || version.isBlank()) ? fallback : version;
    }

    private static void copyCategories() throws IOException {
        try (InputStream source = AllureEnvironment.class.getResourceAsStream("/categories.json")) {
            if (source == null) {
                return;
            }
            Files.copy(
                    source,
                    RESULTS_DIRECTORY.resolve("categories.json"),
                    StandardCopyOption.REPLACE_EXISTING
            );
        }
    }
}
