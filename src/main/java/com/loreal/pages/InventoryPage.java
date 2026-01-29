package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By productsTitleXpath = By.xpath("//span[contains(@class,'title') and normalize-space()='Products']");
    private final By titleSpanCss = By.cssSelector("span.title");

    private final By cartLinkDataTest = By.cssSelector("a[data-test='shopping-cart-link']");
    private final By cartLinkClass = By.cssSelector("a.shopping_cart_link");
    private final By cartContainerId = By.id("shopping_cart_container");

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
}
