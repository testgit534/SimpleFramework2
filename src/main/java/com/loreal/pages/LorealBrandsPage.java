package com.loreal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.logging.Logger;

public class LorealBrandsPage {

    private static final Logger log = Logger.getLogger(LorealBrandsPage.class.getName());

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final String brandsUrlFragment = "/our-global-brands-portfolio/";
    private final By h1Heading = By.xpath("//h1[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'brands portfolio')]");

    public LorealBrandsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForPage() {
        log.info("Waiting for 'Our Global Brands Portfolio' page to load.");
        try {
            wait.until(ExpectedConditions.urlContains(brandsUrlFragment));
            wait.until(ExpectedConditions.visibilityOfElementLocated(h1Heading));
        } catch (TimeoutException e) {
            throw new TimeoutException("Brands portfolio page did not load properly.", e);
        }
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public boolean isOnBrandsPage() {
        return driver.getCurrentUrl().contains(brandsUrlFragment);
    }
}
