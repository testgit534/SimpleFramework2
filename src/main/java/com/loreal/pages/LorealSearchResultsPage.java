package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

public class LorealSearchResultsPage {

    private static final Logger log = Logger.getLogger(LorealSearchResultsPage.class.getName());

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Generic, robust locators for results and empty-state
    private final By resultsListCandidates[] = new By[] {
            By.cssSelector("ul[class*='search'] li"),
            By.cssSelector("div[class*='search'] article"),
            By.cssSelector("[class*='search'] [class*='result']"),
            By.xpath("//*[contains(@class,'search') and (.//li or .//article or .//div[contains(@class,'result')])]//*[self::li or self::article or self::div]")
    };

    private final By noResultsText = By.xpath("//*[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'no results') or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'no result')]");

    private final By searchFieldOnResults = By.id("site-search");

    public LorealSearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForPage(String query) {
        log.info("Waiting for search results page to load.");
        wait.until(ExpectedConditions.urlContains("/search/"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchFieldOnResults));
        // Ensure query made it to the URL in some form
        String encodedPlus = query.trim().replace(" ", "+");
        String encodedPercent = URLEncoder.encode(query.trim(), StandardCharsets.UTF_8);
        wait.until(d -> {
            String url = d.getCurrentUrl();
            return url.contains("q=" + encodedPlus) || url.contains("q=" + encodedPercent) || url.contains("q=" + query.replace(" ", "%20"));
        });
    }

    public boolean titleIndicatesSearch() {
        String title = driver.getTitle() != null ? driver.getTitle() : "";
        log.info("Search results page title: " + title);
        return title.toLowerCase().contains("search");
    }

    public boolean hasAnyResults() {
        for (By by : resultsListCandidates) {
            List<WebElement> els = driver.findElements(by);
            if (els != null && !els.isEmpty()) {
                for (WebElement el : els) {
                    if (el.isDisplayed()) {
                        log.info("At least one search result element is visible.");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean hasNoResultsMessage() {
        try {
            WebElement empty = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(noResultsText));
            return empty != null && empty.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}
