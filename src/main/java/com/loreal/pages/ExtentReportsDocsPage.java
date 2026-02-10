package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ExtentReportsDocsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // From Browser-use JSON (primary absolute xpaths) with fallbacks
    private final By gettingStartedNavXpath = By.xpath("html/body/main/div/div/div[1]/aside/div/ul/li[1]/a");
    private final By usageNavXpath = By.xpath("html/body/main/div/div/div[1]/aside/div/ul/li[2]/a");
    private final By copyButtonUsageXpath = By.xpath("html/body/main/div/div/div[2]/div[3]/div/div[2]/a");

    private final By mainRoot = By.cssSelector("main");
    private final By gettingStartedAnchorId = By.cssSelector("#getting-started, a[href='#getting-started']");
    private final By usageAnchorId = By.cssSelector("#usage, a[href='#usage']");
    private final By attachReporterCode = By.xpath("//pre//code[contains(.,'attachReporter')] | //code[contains(.,'attachReporter')]");

    public ExtentReportsDocsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(mainRoot));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.titleContains("Extent Framework"),
                ExpectedConditions.urlContains("/docs/versions/4/java/index.html")
        ));
    }

    public void clickGettingStarted() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(gettingStartedNavXpath));
        safeClick(link);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("#getting-started"),
                ExpectedConditions.visibilityOfElementLocated(gettingStartedAnchorId)
        ));
    }

    public void clickUsage() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(usageNavXpath));
        safeClick(link);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("#usage"),
                ExpectedConditions.visibilityOfElementLocated(usageAnchorId)
        ));
    }

    public boolean isAttachReporterSnippetVisible() {
        try {
            WebElement code = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(attachReporterCode));
            return code.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean clickCopyOnUsageSection() {
        try {
            WebElement copyBtn = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(copyButtonUsageXpath));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", copyBtn);
            safeClick(copyBtn);
            // Best-effort validation for feedback change (optional, non-failing)
            try {
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.or(
                                ExpectedConditions.textToBePresentInElement(copyBtn, "Copied"),
                                ExpectedConditions.attributeContains(copyBtn, "class", "copied")
                        ));
            } catch (TimeoutException ignored) { }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void safeClick(WebElement el) {
        try {
            el.click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }
}
