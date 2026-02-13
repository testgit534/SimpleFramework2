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

    public boolean isItemPresent(String productName) {
        By nameXpath = By.xpath("//div[contains(@class,'cart_item')]//div[contains(@class,'inventory_item_name') and normalize-space()='" + escapeXPath(productName) + "']");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(nameXpath));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public int getItemQuantity(String productName) {
        By rowXpath = By.xpath("//div[contains(@class,'cart_item')]" +
                "[.//div[contains(@class,'inventory_item_name') and normalize-space()='" + escapeXPath(productName) + "']]" +
                "//div[contains(@class,'cart_quantity')]");
        try {
            WebElement qtyEl = wait.until(ExpectedConditions.visibilityOfElementLocated(rowXpath));
            String qty = qtyEl.getText().trim();
            return Integer.parseInt(qty);
        } catch (Exception e) {
            return 0;
        }
    }

    private String escapeXPath(String text) {
        if (!text.contains("'")) return text;
        return "concat('" + text.replace("'", "',\"'\",'") + "')";
    }
}
