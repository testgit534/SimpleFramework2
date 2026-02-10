package com.loreal.pages;

public class ShopifyHomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Header root (for readiness)
    private final By header = By.tagName("header");

    // "Sign up" link from Browser-use JSON and robust fallback
    private final By signUpLinkXpath = By.xpath("html/body/header/div[1]/div[2]/nav/a[4]");
    private final By signUpLinkId = By.id("customer_register_link");
    private final By signUpLinkHref = By.cssSelector("a[href='/account/register']");

    public ShopifyHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForHome() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(header));
        // Also ensure the Sign up link becomes present/visible
        waitShortForAny(signUpLinkXpath, signUpLinkId, signUpLinkHref);
    }

    public void clickSignUp() {
        WebElement el = findClickableWithFallback(signUpLinkXpath, signUpLinkId, signUpLinkHref);
        el.click();
    }

    private WebElement waitShortForAny(By... locators) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        for (By by : locators) {
            try {
                return shortWait.until(ExpectedConditions.visibilityOfElementLocated(by));
            } catch (TimeoutException ignored) { }
        }
        throw new NoSuchElementException("Expected element was not found using provided locators.");
    }

    private WebElement findClickableWithFallback(By... candidates) {
        for (By locator : candidates) {
            try {
                return new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(locator));
            } catch (TimeoutException ignored) { }
        }
        throw new NoSuchElementException("Unable to locate clickable element using provided fallback locators.");
    }
}
