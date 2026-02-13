package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.Normalizer;
import java.time.Duration;

public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By productsTitleXpath = By.xpath("//span[contains(@class,'title') and normalize-space()='Products']");
    private final By titleSpanCss = By.cssSelector("span.title");

    private final By cartLinkDataTest = By.cssSelector("a[data-test='shopping-cart-link']");
    private final By cartLinkClass = By.cssSelector("a.shopping_cart_link");
    private final By cartContainerId = By.id("shopping_cart_container");
    private final By cartBadgeCss = By.cssSelector("span.shopping_cart_badge");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void waitForPage() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(productsTitleXpath),
                    ExpectedConditions.visibilityOfElementLocated(titleSpanCss),
                    ExpectedConditions.urlContains("/inventory.html")
            ));
        } catch (TimeoutException e) {
            throw new TimeoutException("Inventory page did not load properly.", e);
        }
    }

    private WebElement findWithFallback(By... candidates) {
        for (By locator : candidates) {
            try {
                return new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(locator));
            } catch (TimeoutException ignored) {
            }
        }
        throw new NoSuchElementException("Unable to locate element using provided fallback locators.");
    }

    public String getTitleText() {
        try {
            WebElement titleEl = wait.until(ExpectedConditions.visibilityOfElementLocated(productsTitleXpath));
            return titleEl.getText().trim();
        } catch (TimeoutException e) {
            WebElement titleEl = wait.until(ExpectedConditions.visibilityOfElementLocated(titleSpanCss));
            return titleEl.getText().trim();
        }
    }

    public void clickCart() {
        WebElement cart = findWithFallback(cartLinkDataTest, cartLinkClass, cartContainerId);
        cart.click();
    }

    public void addProductToCartByName(String productName) {
        // Prefer data-test based add-to-cart button: add-to-cart-{slugified name}
        String slug = slugify(productName);
        By dataTestBtn = By.cssSelector("button[data-test='add-to-cart-" + slug + "']");
        // Fallback to locating by visible product name then add-to-cart button
        By xpathBtn = By.xpath("//div[contains(@class,'inventory_item_name') and normalize-space()='" + escapeXPath(productName) + "']" +
                "/ancestor::div[contains(@class,'inventory_item')]//button[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'add to cart')]");

        try {
            WebElement btn = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(dataTestBtn));
            btn.click();
        } catch (TimeoutException | NoSuchElementException e) {
            WebElement btn = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(xpathBtn));
            btn.click();
        }
    }

    public int getCartBadgeCount() {
        try {
            WebElement badge = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(cartBadgeCss));
            String text = badge.getText().trim();
            return Integer.parseInt(text);
        } catch (TimeoutException | NoSuchElementException e) {
            return 0;
        }
    }

    private String slugify(String input) {
        String nowhitespace = input.trim().toLowerCase().replaceAll("\\s+", "-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        return normalized.replaceAll("[^a-z0-9\\-]", "");
    }

    private String escapeXPath(String text) {
        if (!text.contains("'")) return text;
        // handle single quotes in XPath literal
        return "concat('" + text.replace("'", "',\"'\",'") + "')";
    }
}
