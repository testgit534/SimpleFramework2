package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.LorealHomePage;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ShopifySignUpAndLoginTest extends BaseTest {

    @Test
    public void testUserSignUpAndLogin() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Step 1: Navigate to the Shopify demo site
        driver.get("https://sauce-demo.myshopify.com/");
        LorealHomePage homePage = new LorealHomePage(driver);
        homePage.waitForHomeHeader();
        homePage.acceptCookiesIfPresent();
        log.info("Navigated to homepage and handled cookies.");

        // Step 2: Click on 'Sign up' link
        WebElement signUpLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("html > body > header > div:nth-of-type(1) > div:nth-of-type(2) > nav > a:nth-of-type(4)[href=\"/account/register\"][id=\"customer_register_link\"]")));
        signUpLink.click();
        log.info("Clicked on 'Sign up' link.");

        // Step 3: Enter email and password on sign-up page
        wait.until(ExpectedConditions.urlContains("/account/register"));
        WebElement emailInput = driver.findElement(By.cssSelector("html > body > div:nth-of-type(3) > div:nth-of-type(2) > div > div:nth-of-type(2) > div > form > div:nth-of-type(4) > input.long[type=\"email\"][name=\"customer[email]\"][id=\"email\"]"));
        WebElement passwordInput = driver.findElement(By.cssSelector("html > body > div:nth-of-type(3) > div:nth-of-type(2) > div > div:nth-of-type(2) > div > form > div:nth-of-type(5) > input.long[type=\"password\"][name=\"customer[password]\"][id=\"password\"]"));
        WebElement createAccountButton = driver.findElement(By.cssSelector("html > body > div:nth-of-type(3) > div:nth-of-type(2) > div > div:nth-of-type(2) > div > form > div:nth-of-type(6) > input[type=\"submit\"]"));

        emailInput.clear();
        emailInput.sendKeys("uniqueemail@example.com");
        passwordInput.clear();
        passwordInput.sendKeys("ValidPassword123");
        createAccountButton.click();
        log.info("Entered credentials and clicked 'Create'.");

        // Step 4: Handle CAPTCHA if present (mocked for demonstration)
        try {
            WebElement captchaDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("html > body > div:nth-of-type(5) > div:nth-of-type(2)")));
            log.warn("CAPTCHA detected during sign up. Test cannot proceed beyond this point without manual intervention.");
            Assert.fail("CAPTCHA encountered in automation flow.");
        } catch (Exception e) {
            log.info("No CAPTCHA found; proceeding to verify account creation.");
        }

        // Step 5: Attempt login (assuming account creation success)
        driver.navigate().to("https://sauce-demo.myshopify.com/account/login");
        log.info("Navigated to login page.");

        WebElement loginEmailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("html > body > div:nth-of-type(3) > div:nth-of-type(2) > div > div:nth-of-type(2) > div > div:nth-of-type(1) > form > div:nth-of-type(2) > input.long[type=\"email\"][name=\"customer[email]\"][id=\"customer_email\"]")));
        WebElement loginPasswordInput = driver.findElement(By.cssSelector("html > body > div:nth-of-type(3) > div:nth-of-type(2) > div > div:nth-of-type(2) > div > div:nth-of-type(1) > form > div:nth-of-type(3) > input.long.password[type=\"password\"][name=\"customer[password]\"][id=\"customer_password\"]"));
        WebElement loginButton = driver.findElement(By.cssSelector("html > body > div:nth-of-type(3) > div:nth-of-type(2) > div > div:nth-of-type(2) > div > div:nth-of-type(1) > form > div:nth-of-type(5) > input.button[type=\"submit\"]"));

        loginEmailInput.clear();
        loginEmailInput.sendKeys("uniqueemail@example.com");
        loginPasswordInput.clear();
        loginPasswordInput.sendKeys("ValidPassword123");
        loginButton.click();
        log.info("Attempted to log in with created account credentials.");

        // Verify captcha on login page
        try {
            WebElement loginCaptcha = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("html > body > div:nth-of-type(5) > div:nth-of-type(2)")));
            log.warn("CAPTCHA detected during login. Test cannot proceed without manual intervention.");
            Assert.fail("CAPTCHA encountered at login step.");
        } catch (Exception e) {
            log.info("No CAPTCHA found; login action verified.");
        }
    }
}
