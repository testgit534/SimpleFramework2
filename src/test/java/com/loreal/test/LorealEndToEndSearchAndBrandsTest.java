package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.LorealBrandsPage;
import com.loreal.pages.LorealHomePage;
import com.loreal.pages.LorealSearchPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.logging.Logger;

public class LorealEndToEndSearchAndBrandsTest extends BaseTest {

    private static final Logger log = Logger.getLogger(LorealEndToEndSearchAndBrandsTest.class.getName());

    @Override
    @BeforeMethod
    public void setUp() {
        // Override BaseTest to open L'Oréal website per test scenario
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://www.loreal.com/en/");
    }

    @Test
    public void testLorealSearchFiltersAndNavigateToBrands() {
        log.info("Step 1: Load L'Oréal home, accept cookies and verify header/logo.");
        LorealHomePage home = new LorealHomePage(driver);
        home.waitForHomeHeader();
        home.acceptCookiesIfPresent();
        Assert.assertTrue(home.isLogoDisplayed(), "Expected L'Oréal logo to be visible on the home page header.");

        log.info("Step 2: Perform site search for 'products'.");
        LorealSearchPage searchPage = home.search("products");
        searchPage.waitForPage();
        Assert.assertTrue(searchPage.isOnSearchPageFor("products"), "Expected to be on search page for query 'products'.");

        log.info("Step 3: Open Country filter and select 'USA'.");
        searchPage.waitForFilters();
        searchPage.openCountryFilter();
        searchPage.selectCountry("usa");
        searchPage.waitForUrlParam("country", "usa");
        Assert.assertTrue(driver.getCurrentUrl().contains("country=usa"), "URL should contain country=usa after selection.");

        log.info("Step 4: Open Language filter and select 'English'.");
        searchPage.openLanguageFilter();
        searchPage.selectLanguageEnglish();
        searchPage.waitForUrlParam("language", "en");
        Assert.assertTrue(driver.getCurrentUrl().contains("language=en"), "URL should contain language=en after selection.");

        log.info("Step 5: Scroll to footer and click 'Brands' to open the brands portfolio page.");
        LorealBrandsPage brandsPage = searchPage.clickFooterBrands();
        brandsPage.waitForPage();

        log.info("Step 6: Validate we navigated to 'Our Global Brands Portfolio'.");
        Assert.assertTrue(brandsPage.isOnBrandsPage(), "Expected URL to contain '/our-global-brands-portfolio/'.");
        Assert.assertTrue(brandsPage.getTitle().toLowerCase().contains("brands portfolio"),
                "Expected page title to contain 'Brands Portfolio'.");
    }
}
