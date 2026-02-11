package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ShopifyProductPage {
    
    private WebDriver driver;
    private WebDriverWait wait;

    // Product page elements
    private By productLinkSelector = By.cssSelector("a.animated.fadeInUpBig[href=\"/collections/frontpage/products/grey-jacket\"][id=\"product-1\"]");
    private By addToCartButtonSelector = By.cssSelector("input.btn.add-to-cart[type=\"submit\"][id=\"add\"]");
    private By cartButtonSelector = By.cssSelector("a.toggle-drawer.cart.desktop[href=\"#\"]");

    public ShopifyProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void selectProduct() {
        wait.until(ExpectedConditions.elementToBeClickable(productLinkSelector)).click();
    }

    public void addToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButtonSelector)).click();
    }

    public void gotoCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartButtonSelector)).click();
    }
}
