package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cartTitleXpath = By.xpath("//span[contains(@class,'title') and normalize-space()='Your Cart']");
    private final By titleSpanCss = By.cssSelector("span.title");

    // Added locators to validate items in the cart
    private final By cartItem = By.cssSelector(".cart_item");
    private final By cartItemName = By.cssSelector(".inventory_item_name");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void waitForPage() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(cartTitleXpath),
                    ExpectedConditions.urlContains("/cart.html")
            ));
        } catch (TimeoutException e) {
            throw new TimeoutException("Cart page did not load properly.", e);
        }
    }

    public String getTitleText() {
        try {
            WebElement titleEl = wait.until(ExpectedConditions.visibilityOfElementLocated(cartTitleXpath));
            return titleEl.getText().trim();
        } catch (TimeoutException e) {
            WebElement titleEl = wait.until(ExpectedConditions.visibilityOfElementLocated(titleSpanCss));
            return titleEl.getText().trim();
        }
    }

    public boolean isItemPresentByName(String productName) {
        By itemByName = By.xpath("//div[contains(@class,'cart_item')]//div[contains(@class,'inventory_item_name') and normalize-space()='" + productName + "']");
        try {
            WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(itemByName));
            return item.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public int getCartItemsCount() {
        try {
            return driver.findElements(cartItem).size();
        } catch (NoSuchElementException e) {
            return 0;
        }
    }
}
