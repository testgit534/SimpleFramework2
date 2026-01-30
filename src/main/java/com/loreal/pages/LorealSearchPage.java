package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LorealSearchPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Search bar locators (from Browser-use JSON) with fallbacks
    private final By searchInputXpath = By.xpath("html/body/div[2]/main/div[1]/div/form/section/div[2]/div[1]/input[1]");
    private final By searchInputId = By.id("site-search");
    private final By searchInputCss = By.cssSelector("input.search-box__input");

    private final By searchButtonXpath = By.xpath("html/body/div[2]/main/div[1]/div/form/section/div[2]/div[1]/button");
    private final By searchButtonCss = By.cssSelector("button.btn.search-box__submit");

    // Results page signalers
    private final By resultsContainerHeuristic = By.cssSelector("main"); // generic but present post navigation

    public LorealSearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private WebElement findWithFallback(By... candidates) {
        for (By locator : candidates) {
            try {
                return new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (TimeoutException ignored) {
            }
        }
        throw new NoSuchElementException("Unable to locate element using provided fallback locators.");
    }

    public void waitForSearchInput() {
        findWithFallback(searchInputXpath, searchInputId, searchInputCss);
    }

    public void typeQuery(String query) {
        WebElement input = findWithFallback(searchInputXpath, searchInputId, searchInputCss);
        input.click();
        input.clear();
        input.sendKeys(query);
    }

    public void submitSearch() {
        try {
            WebElement submitBtn = findWithFallback(searchButtonXpath, searchButtonCss);
            submitBtn.click();
        } catch (NoSuchElementException e) {
            // Fallback to ENTER if button not clickable
            WebElement input = findWithFallback(searchInputXpath, searchInputId, searchInputCss);
            input.sendKeys(Keys.ENTER);
        }
    }

    public void search(String query) {
        waitForSearchInput();
        typeQuery(query);
        submitSearch();
    }

    public void waitForResults() {
        // Wait for URL change to include /search and the main content to be visible
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/search"),
                ExpectedConditions.titleContains("Search")
        ));
        wait.until(ExpectedConditions.visibilityOfElementLocated(resultsContainerHeuristic));
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
