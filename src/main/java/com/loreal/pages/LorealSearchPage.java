package com.loreal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LorealSearchPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By pageBody = By.tagName("body");

    // Scan for actionable login/sign-in links/buttons only to avoid matching query text
    private final By loginLikeLinksOrButtons = By.xpath(
            "//*[self::a or self::button]" +
                    "[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'login') " +
                    " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'log in') " +
                    " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign in')]"
    );

    public LorealSearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForPage() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/search"),
                ExpectedConditions.visibilityOfElementLocated(pageBody)
        ));
    }

    public boolean isOnSearchPageFor(String q) {
        String url = driver.getCurrentUrl();
        return url.contains("/search") && url.toLowerCase().contains("q=" + q.toLowerCase());
    }

    public boolean hasLoginLinksOrButtons() {
        try {
            return !driver.findElements(loginLikeLinksOrButtons).isEmpty();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
