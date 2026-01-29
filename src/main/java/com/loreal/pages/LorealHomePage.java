package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

public class LorealHomePage {

    private static final Logger log = Logger.getLogger(LorealHomePage.class.getName());

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Cookie banner (from Browser-use JSON)
    private final By cookieAcceptBtnXpath = By.xpath("html/body/div[3]/div[2]/div/div/div[2]/div/div/button");
    private final By cookieAcceptBtnId = By.id("onetrust-accept-btn-handler");

    // Burger / Main Navigation (from Browser-use JSON)
    private final By burgerButtonXpath = By.xpath("html/body/div[2]/header/div/div[1]/button");
    private final By burgerButtonCss = By.cssSelector("header button.header__burger[aria-label='Open menu']");
    private final By mainNavigationId = By.id("main-navigation");
    private final By mainNavigationRole = By.cssSelector("header nav[role='navigation']");

    // "Group" top navigation link (from Browser-use JSON)
    private final By groupLinkXpath = By.xpath("html/body/div[2]/header/div/div[3]/nav/div/ul/li[1]/a");
    private final By groupLinkCss = By.cssSelector("header nav a[href='/en/group/']");

    // Header root / logo fallbacks
    private final By headerRoot = By.tagName("header");
    private final By logoByClass = By.xpath("//header//a[contains(@class,'logo')]");
    private final By logoSvg = By.xpath("//header//*[name()='svg' and contains(@class,'logo')]");
    private final By logoImgAlt = By.xpath("//header//img[contains(@alt,\"L'Or\") or contains(@alt,'L’Or')]");
    private final By logoHrefEn = By.cssSelector("header a[href='/en/']");

    // Potential login/sign-in elements (assert non-existence)
    private final By loginOrSignIn = By.xpath(
            "//*[self::a or self::button]" +
            "[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'login') " +
            " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'log in') " +
            " or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign in')]"
    );

    public LorealHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForHomeHeader() {
        log.info("Waiting for L'Oréal home header to be visible.");
        wait.until(ExpectedConditions.visibilityOfElementLocated(headerRoot));
    }

    public void acceptCookiesIfPresent() {
        log.info("Attempting to accept cookies if banner is present.");
        try {
            WebElement btn = waitShortForAny(cookieAcceptBtnXpath, cookieAcceptBtnId);
            if (btn != null && btn.isDisplayed()) {
                btn.click();
                log.info("Cookie consent accepted.");
                try {
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("onetrust-banner-sdk")));
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            log.info("Cookie banner not present or already handled.");
        }
    }

    public boolean isLogoDisplayed() {
        log.info("Verifying L'Oréal logo is displayed in the header.");
        try {
            WebElement logo = waitShortForAny(logoByClass, logoSvg, logoImgAlt, logoHrefEn);
            if (logo != null) {
                return logo.isDisplayed();
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    public void openMainMenu() {
        log.info("Opening main navigation menu (burger).");
        WebElement burger = wait.until(ExpectedConditions.visibilityOfElementLocated(burgerButtonXpath));
        try {
            burger.click();
        } catch (ElementClickInterceptedException e) {
            WebElement burgerCssEl = wait.until(ExpectedConditions.elementToBeClickable(burgerButtonCss));
            burgerCssEl.click();
        }

        wait.until(anyOf(
                ExpectedConditions.attributeContains(burgerButtonCss, "aria-expanded", "true"),
                ExpectedConditions.visibilityOfElementLocated(mainNavigationId),
                ExpectedConditions.visibilityOfElementLocated(mainNavigationRole)
        ));
        log.info("Main navigation appears opened.");
    }

    public boolean isMainMenuOpen() {
        try {
            // If aria-expanded attribute becomes true or the nav is visible, consider open
            WebElement burger = wait.until(ExpectedConditions.visibilityOfElementLocated(burgerButtonCss));
            String expanded = burger.getAttribute("aria-expanded");
            if ("true".equalsIgnoreCase(expanded)) return true;

            WebElement nav = driver.findElement(mainNavigationRole);
            return nav.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickGroupLink() {
        log.info("Clicking on 'Group' link in the opened main navigation.");
        WebElement groupEl = waitShortForAny(groupLinkXpath, groupLinkCss);
        if (groupEl == null) {
            throw new NoSuchElementException("Unable to locate 'Group' link in main navigation.");
        }
        try {
            groupEl.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", groupEl);
        }
    }

    public boolean isLoginOptionPresent() {
        log.info("Checking for presence of any Login/Sign in options.");
        try {
            List<WebElement> elems = driver.findElements(loginOrSignIn);
            for (WebElement el : elems) {
                if (el.isDisplayed()) return true;
            }
        } catch (NoSuchElementException ignored) { }
        return false;
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

    @SafeVarargs
    private final ExpectedCondition<Boolean> anyOf(ExpectedCondition<?>... conditions) {
        return driver -> {
            for (ExpectedCondition<?> condition : conditions) {
                try {
                    Object result = condition.apply(driver);
                    if (result instanceof Boolean) {
                        if (Boolean.TRUE.equals(result)) return true;
                    } else if (result != null) {
                        return true;
                    }
                } catch (Exception ignored) { }
            }
            return false;
        };
    }
}
