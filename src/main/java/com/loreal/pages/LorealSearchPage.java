package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class LorealSearchPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // URL pattern and structural elements
    private final String expectedPathFragment = "/search/?q=";

    // Generic "No results" matchers
    private final By noResultsText = By.xpath("//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'no results') or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'no result')]");
    // Generic results items candidates
    private final By resultsItemsGeneric = By.cssSelector(
            "ol li a, ul li a, .search-results a, [data-test='search-result'], article a"
    );

    // Main container heuristic (optional wait)
    private final By mainContainer = By.cssSelector("main, section, div");

    public LorealSearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        waitForPage();
    }

    public void waitForPage() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains(expectedPathFragment),
                ExpectedConditions.visibilityOfElementLocated(mainContainer)
        ));
    }

    public boolean isOnSearchPageFor(String query) {
        return driver.getCurrentUrl().toLowerCase(Locale.ROOT).contains("/search/?q=" + query.toLowerCase(Locale.ROOT));
    }

    public boolean isNoResultsMessageDisplayed() {
        try {
            WebElement el = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(noResultsText));
            return el != null && el.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public int getVisibleResultsCount() {
        List<WebElement> items = driver.findElements(resultsItemsGeneric);
        if (items == null || items.isEmpty()) return 0;
        // Filter only displayed and unique elements
        return items.stream().filter(el -> {
            try {
                return el.isDisplayed();
            } catch (StaleElementReferenceException ex) {
                return false;
            }
        }).collect(Collectors.toList()).size();
    }

    public boolean hasAnyResults() {
        return getVisibleResultsCount() > 0;
    }
}
