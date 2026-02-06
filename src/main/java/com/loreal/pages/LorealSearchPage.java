package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class LorealSearchPage {

    private static final Logger log = Logger.getLogger(LorealSearchPage.class.getName());

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By pageBody = By.tagName("body");

    // Search URL indicator
    private final String searchUrlFragment = "/search";

    // Country accordion (prefer JSON XPaths, fallback to id/text)
    private final By countryAccordionXpath = By.xpath("html/body/div[2]/main/div/div/form/div/div[2]/div[1]/ul/li[1]/div/div/h3/button");
    private final By countryAccordionId = By.id("accordion_0");
    private final By countryAccordionByText = By.xpath("//ul/li//h3/button[contains(.,'Country') or contains(.,'COUNTRY')]");

    // Country options (labels use 'for' attribute as per JSON)
    private By countryOptionLabel(String code) {
        return By.xpath("//label[@for='country-" + code + "']");
    }
    private final By countryOptionGlobal = By.xpath("//label[@for='country-global']");
    private final By countryOptionUSA = By.xpath("//label[@for='country-usa']");

    // Language accordion (varies: accordion_4 or accordion_7 in JSON)
    private final By languageAccordionXpathAltA = By.xpath("html/body/div[2]/main/div/div/form/div/div[2]/div[1]/ul/li[5]/div/div/h3/button");
    private final By languageAccordionXpathAltB = By.xpath("html/body/div[2]/main/div/div/form/div/div[2]/div[1]/ul/li[8]/div/div/h3/button");
    private final By languageAccordionId4 = By.id("accordion_4");
    private final By languageAccordionId7 = By.id("accordion_7");
    private final By languageAccordionByText = By.xpath("//ul/li//h3/button[contains(.,'Language') or contains(.,'LANGUAGE')]");

    // Language options (labels use 'for' attribute as per JSON)
    private final By languageEnglishLabel = By.xpath("//label[@for='language-en' or @lang='en']");

    // Sort dropdown (seen in JSON; used as a stability indicator)
    private final By sortBySelect = By.xpath("//select[@id='sortBy' or @name='sortby']");

    // Footer Brands link (from JSON)
    private final By footerBrandsLinkXpath = By.xpath("html/body/div[2]/div/div[1]/div[1]/ul/li[3]/a[@href='/en/our-global-brands-portfolio/']");
    private final By footerBrandsLinkCss = By.cssSelector("a[href='/en/our-global-brands-portfolio/']");

    public LorealSearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForPage() {
        log.info("Waiting for search results page to load.");
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains(searchUrlFragment),
                ExpectedConditions.visibilityOfElementLocated(pageBody)
        ));
    }

    public boolean isOnSearchPageFor(String q) {
        String url = driver.getCurrentUrl();
        return url.contains(searchUrlFragment) && url.toLowerCase().contains("q=" + q.toLowerCase());
    }

    public void waitForFilters() {
        log.info("Waiting for filter accordions to be present.");
        waitShortForAny(countryAccordionXpath, countryAccordionId, countryAccordionByText);
    }

    public void openCountryFilter() {
        log.info("Opening Country filter accordion.");
        WebElement accordion = waitShortForAny(countryAccordionXpath, countryAccordionId, countryAccordionByText);
        clickWithRetry(accordion);
        // Wait for at least one country option to be visible
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(countryOptionUSA),
                ExpectedConditions.visibilityOfElementLocated(countryOptionGlobal)
        ));
    }

    public void selectCountry(String countryCode) {
        log.info("Selecting country: " + countryCode);
        By option = countryOptionLabel(countryCode);
        WebElement el = waitShortForAny(option);
        clickWithRetry(el);
    }

    public void openLanguageFilter() {
        log.info("Opening Language filter accordion.");
        WebElement accordion = waitShortForAny(languageAccordionXpathAltA, languageAccordionXpathAltB, languageAccordionId4, languageAccordionId7, languageAccordionByText);
        clickWithRetry(accordion);
        wait.until(ExpectedConditions.visibilityOfElementLocated(languageEnglishLabel));
    }

    public void selectLanguageEnglish() {
        log.info("Selecting language: English");
        WebElement el = waitShortForAny(languageEnglishLabel);
        clickWithRetry(el);
    }

    public void waitForUrlParam(String key, String value) {
        log.info("Waiting for URL to contain " + key + "=" + value);
        wait.until(ExpectedConditions.urlContains(key + "=" + value));
    }

    public LorealBrandsPage clickFooterBrands() {
        log.info("Scrolling to footer and clicking 'Brands' link.");
        scrollToBottom();
        WebElement link = waitShortForAny(footerBrandsLinkXpath, footerBrandsLinkCss);
        clickWithRetry(link);
        return new LorealBrandsPage(driver);
    }

    private void scrollToBottom() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        } catch (Exception ignored) { }
    }

    private WebElement waitShortForAny(By... locators) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(8));
        for (By by : locators) {
            try {
                return shortWait.until(ExpectedConditions.visibilityOfElementLocated(by));
            } catch (TimeoutException ignored) { }
        }
        throw new NoSuchElementException("Unable to locate element using provided locators.");
    }

    private void clickWithRetry(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (Exception e1) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            } catch (Exception e2) {
                element.click();
            }
        }
    }
}
