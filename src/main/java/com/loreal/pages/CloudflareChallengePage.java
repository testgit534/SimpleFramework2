package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CloudflareChallengePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators derived from Browser-use JSON
    private final By cloudflareVendorLinkXpath = By.xpath("html/body/div[2]/div/div[2]/a");
    private final By cloudflareVendorLinkCss = By.cssSelector("a[href=\"https://www.cloudflare.com?utm_source=challenge&utm_campaign=m\"][target=\"_blank\"]");
    private final By hCaptchaOrTurnstileFrame = By.cssSelector("iframe[src*='challenge'], iframe[src*='turnstile'], iframe[src*='hcaptcha']");

    public CloudflareChallengePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public boolean waitForChallengeOrTimeout() {
        try {
            wait.until(d -> isOnChallengePage());
            return true;
        } catch (TimeoutException te) {
            return false;
        }
    }

    public boolean isOnChallengePage() {
        try {
            if (driver.getTitle() != null && driver.getTitle().toLowerCase().contains("just a moment")) {
                return true;
            }
        } catch (Exception ignored) { }

        try {
            WebElement link = waitShortForAny(cloudflareVendorLinkXpath, cloudflareVendorLinkCss);
            if (link != null && link.isDisplayed()) {
                return true;
            }
        } catch (Exception ignored) { }

        try {
            return !driver.findElements(hCaptchaOrTurnstileFrame).isEmpty();
        } catch (Exception ignored) { }

        return false;
    }

    public boolean isVendorLinkVisible() {
        try {
            WebElement link = waitShortForAny(cloudflareVendorLinkXpath, cloudflareVendorLinkCss);
            return link != null && link.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private WebElement waitShortForAny(By... locators) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        for (By by : locators) {
            try {
                return shortWait.until(ExpectedConditions.visibilityOfElementLocated(by));
            } catch (TimeoutException ignored) { }
        }
        return null;
    }
}
