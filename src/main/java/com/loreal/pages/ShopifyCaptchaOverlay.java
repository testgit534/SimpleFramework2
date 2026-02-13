package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ShopifyCaptchaOverlay {

    private final WebDriver driver;

    // Primary locator from Browser-use JSON with some conservative fallbacks
    private final By overlayXpath = By.xpath("html/body/div[5]/div[2]");
    private final By overlayCss = By.cssSelector("body > div:nth-of-type(5) > div:nth-of-type(2)");

    // A generic verification iframe fallback seen on challenge UIs
    private final By verificationIframe = By.xpath("//iframe[contains(translate(@title,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'verification') or contains(translate(@aria-label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'verification') or contains(@src,'challenge')]");

    public ShopifyCaptchaOverlay(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isVisible(Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(overlayXpath));
            return true;
        } catch (TimeoutException e1) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(overlayCss));
                return true;
            } catch (TimeoutException e2) {
                try {
                    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(verificationIframe));
                    driver.switchTo().defaultContent();
                    return true;
                } catch (TimeoutException e3) {
                    return false;
                } catch (NoSuchFrameException e4) {
                    return false;
                }
            }
        }
    }
}
