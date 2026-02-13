package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.ExtentReportsDocsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class ExtentReportsDocsE2ETest extends BaseTest {

    private static final Logger log = Logger.getLogger(ExtentReportsDocsE2ETest.class.getName());

    @Test
    public void verifyExtentReportsDocsUsageAndCopyExample() {
        log.info("Step 1: Navigate directly to Extent Reports v4 Java documentation page.");
        driver.navigate().to("https://www.extentreports.com/docs/versions/4/java/index.html");

        ExtentReportsDocsPage docs = new ExtentReportsDocsPage(driver);
        log.info("Step 2: Wait for documentation page to load.");
        docs.waitForPage();
        Assert.assertTrue(driver.getTitle().toLowerCase().contains("extent framework")
                        || driver.getCurrentUrl().contains("/docs/versions/4/java/index.html"),
                "Documentation page did not load as expected.");

        log.info("Step 3: Click 'Getting started' from left navigation.");
        docs.clickGettingStarted();
        Assert.assertTrue(driver.getCurrentUrl().contains("#getting-started")
                        || driver.getPageSource().toLowerCase().contains("getting started"),
                "Getting started section was not reached.");

        log.info("Step 4: Click 'Usage' from left navigation.");
        docs.clickUsage();
        Assert.assertTrue(driver.getCurrentUrl().contains("#usage")
                        || driver.getPageSource().toLowerCase().contains("usage"),
                "Usage section was not reached.");

        log.info("Step 5: Verify 'attachReporter' code snippet is visible in Usage section.");
        Assert.assertTrue(docs.isAttachReporterSnippetVisible(),
                "Expected 'attachReporter' code snippet was not visible.");

        log.info("Step 6: Click the 'Copy' button for the code example in Usage section.");
        boolean copyClicked = docs.clickCopyOnUsageSection();
        Assert.assertTrue(copyClicked, "Failed to click the 'Copy' button in the Usage section.");

        log.info("E2E verification completed: Navigated to Extent Reports docs, accessed Getting started and Usage, validated snippet, and clicked Copy successfully.");
    }
}
