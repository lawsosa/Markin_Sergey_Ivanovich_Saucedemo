package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import io.qameta.allure.Allure;


public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int DEFAULT_RETRY_COUNT = 3;

    private int attempt = 0;

    @Override
    public boolean retry(ITestResult result) {
        int maxRetries = maxRetries();
        if (attempt < maxRetries) {
            attempt++;
            try {
                Allure.step("Повторный запуск теста после падения: попытка " + attempt + " из " + maxRetries
                        + " (причина: " + result.getThrowable() + ")");
            } catch (RuntimeException ignored) {
                
            }
            return true;
        }
        return false;
    }

    
    public static int maxRetries() {
        String property = System.getProperty("retry.count");
        if (property == null || property.isBlank()) {
            return DEFAULT_RETRY_COUNT;
        }
        try {
            return Math.max(0, Integer.parseInt(property.trim()));
        } catch (NumberFormatException e) {
            return DEFAULT_RETRY_COUNT;
        }
    }
}
