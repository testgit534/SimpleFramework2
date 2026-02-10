package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.NoSuchElementException;

public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By productsTitleXpath = By.xpath("//span[contains(@class,'title') and normalize-space()='Products']");
    private final By titleSpanCss = By.cssSelector("span.title");

    private final By cartLinkDataTest = By.cssSelector("a[data-test='shopping-cart-link']");
    private final By cartLinkClass = By.cssSelector("a.shopping_cart_link");
    private final By cartContainerId = By.id("shopping_cart_container");

    // Added robust locators for inventory items and add-to-cart buttons
    private final By inventoryItemContainer = By.cssSelector(".inventory_item");
    private final By inventoryItemName = By.cssSelector(".inventory_item_name");
    private final By addToCartButtonByText = By.xpath(".//button[contains(normalize-space(.),'Add to cart')]");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");

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

    // New methods

    public void addProductToCartByName(String productName) {
        // Locate specific inventory item card by product name and click its Add to cart button
        By itemCardByName = By.xpath("//div[contains(@class,'inventory_item')][.//div[contains(@class,'inventory_item_name') and normalize-space()='" + productName + "']]");
        WebElement itemCard = wait.until(ExpectedConditions.visibilityOfElementLocated(itemCardByName));

        // Primary: button with text "Add to cart" within the item card
        try {
            WebElement addBtn = itemCard.findElement(addToCartButtonByText);
            wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();
            return;
        } catch (NoSuchElementException ignored) {
        }

        // Fallback: known data-test or id for some products (Swag Labs common)
        String slug = productName.toLowerCase().replace(" ", "-");
        By addBtnById = By.id("add-to-cart-" + slug);
        By addBtnByDataTest = By.cssSelector("button[data-test='add-to-cart-" + slug + "']");
        try {
            WebElement addBtn = itemCard.findElement(addBtnById);
            wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();
        } catch (NoSuchElementException e) {
            WebElement addBtn = itemCard.findElement(addBtnByDataTest);
            wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();
        }
    }

    public int getCartBadgeCount() {
        try {
            WebElement badge = wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge));
            String text = badge.getText().trim();
            return Integer.parseInt(text);
        } catch (TimeoutException | NoSuchElementException e) {
            return 0;
        }
    }
}
