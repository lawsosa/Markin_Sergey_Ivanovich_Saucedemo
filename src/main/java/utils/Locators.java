package utils;

import org.openqa.selenium.By;


public final class Locators {

    private Locators() {
    }

    
    public static final class Login {
        public static final By USERNAME = By.id("user-name");
        public static final By PASSWORD = By.id("password");
        public static final By LOGIN_BUTTON = By.id("login-button");
        public static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

        private Login() {
        }
    }

    
    public static final class Inventory {
        public static final By TITLE = By.cssSelector("[data-test='title']");
        public static final By ITEM = By.cssSelector("[data-test='inventory-item']");
        public static final By ITEM_NAME = By.cssSelector("[data-test='inventory-item-name']");
        public static final By ITEM_PRICE = By.cssSelector("[data-test='inventory-item-price']");
        public static final By ITEM_DESCRIPTION = By.cssSelector("[data-test='inventory-item-desc']");
        public static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");
        public static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");
        public static final By SORT_SELECT = By.cssSelector("[data-test='product-sort-container']");
        public static final By BURGER_MENU = By.id("react-burger-menu-btn");
        public static final By LOGOUT_LINK = By.id("logout_sidebar_link");
        public static final By MENU_WRAPPER = By.cssSelector(".bm-menu-wrap");

        private Inventory() {
        }
    }

    
    public static final class Cart {
        public static final By CART_ITEM = By.cssSelector("[data-test='inventory-item']");
        public static final By ITEM_NAME = By.cssSelector("[data-test='inventory-item-name']");
        public static final By ITEM_PRICE = By.cssSelector("[data-test='inventory-item-price']");
        public static final By QUANTITY = By.cssSelector("[data-test='item-quantity']");
        public static final By CHECKOUT_BUTTON = By.cssSelector("[data-test='checkout']");
        public static final By CONTINUE_SHOPPING = By.cssSelector("[data-test='continue-shopping']");

        private Cart() {
        }
    }

    
    public static final class Checkout {
        public static final By FIRST_NAME = By.id("first-name");
        public static final By LAST_NAME = By.id("last-name");
        public static final By POSTAL_CODE = By.id("postal-code");
        public static final By CONTINUE_BUTTON = By.id("continue");
        public static final By CANCEL_BUTTON = By.id("cancel");
        public static final By FINISH_BUTTON = By.id("finish");
        public static final By BACK_HOME_BUTTON = By.cssSelector("[data-test='back-to-products']");
        public static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");
        public static final By TOTAL_LABEL = By.cssSelector("[data-test='total-label']");
        public static final By SUBTOTAL_LABEL = By.cssSelector("[data-test='subtotal-label']");
        public static final By TAX_LABEL = By.cssSelector("[data-test='tax-label']");
        public static final By COMPLETE_HEADER = By.cssSelector("[data-test='complete-header']");
        public static final By COMPLETE_TEXT = By.cssSelector("[data-test='complete-text']");
        public static final By PAYMENT_INFO = By.cssSelector("[data-test='payment-info-value']");
        public static final By SHIPPING_INFO = By.cssSelector("[data-test='shipping-info-value']");

        private Checkout() {
        }
    }
}
