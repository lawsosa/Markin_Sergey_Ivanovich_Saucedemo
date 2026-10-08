package utils;


public final class BrowserConfig {

    public static final String DEFAULT_BROWSER = "chrome";
    public static final boolean DEFAULT_HEADLESS = false;

    
    public static final String BROWSER_PROPERTY = "browser";
    
    public static final String HEADLESS_PROPERTY = "headless";

    private BrowserConfig() {
    }

    
    public static String browserFromSystemProperty() {
        return normalize(System.getProperty(BROWSER_PROPERTY, DEFAULT_BROWSER));
    }

    
    public static boolean headlessFromSystemProperty() {
        return parseHeadless(System.getProperty(HEADLESS_PROPERTY, String.valueOf(DEFAULT_HEADLESS)));
    }

    
    public static String normalize(String browser) {
        if (browser == null || browser.isBlank()) {
            return DEFAULT_BROWSER;
        }
        return browser.trim().toLowerCase();
    }

    
    public static boolean parseHeadless(String value) {
        if (value == null || value.isBlank()) {
            return DEFAULT_HEADLESS;
        }
        String normalized = value.trim().toLowerCase();
        return "true".equals(normalized) || "1".equals(normalized) || "yes".equals(normalized);
    }
}
