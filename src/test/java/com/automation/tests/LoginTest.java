package com.automation.tests;

import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.automation.base.BaseTest;
import com.automation.base.ConfigReader;
import com.automation.pages.CareerAreasPage;
import com.automation.utils.CSVLogger;

public class LoginTest extends BaseTest {

    private CSVLogger csvLogger;

    @Test
    public void testSuccessfulLogin() {
        csvLogger = new CSVLogger();
        CareerAreasPage careerAreasPage = new CareerAreasPage(driver, csvLogger);

        driver.get(ConfigReader.getProperty("baseUrl"));
        careerAreasPage.clickAllCareerAreas();
    }

    @AfterTest
    public void tearDown() {
        if (csvLogger != null) {
            csvLogger.close();
            System.out.println("CSVLogger closed in tearDown");
        }
    }
}