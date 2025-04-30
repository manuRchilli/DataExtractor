package com.automation.tests;

import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.automation.base.BaseTest;
import com.automation.base.ConfigReader;
import com.automation.pages.neww.CategoryPage;
import com.automation.utils.CSVLogger;

public class LoginTest extends BaseTest {

    private CSVLogger csvLogger;

    @Test
    public void testSuccessfulLogin() {
        csvLogger = new CSVLogger();
        CategoryPage categoryPage = new CategoryPage(driver, csvLogger);

        driver.get(ConfigReader.getProperty("baseUrl"));
        categoryPage.clickAllCategoryButtonsAndSubCategories();
    }

    @AfterTest
    public void tearDown() {
        if (csvLogger != null) {
            csvLogger.close();
            System.out.println("CSVLogger closed in tearDown");
        }
        if (driver != null) {
            driver.quit();
        }
    }
}