package com.automation.pages.neww;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.base.CommonUtils;
import com.automation.utils.CSVLogger;

public class CategoryPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private CommonUtils commonUtils;
    private CSVLogger csvLogger;

    public CategoryPage(WebDriver driver, CSVLogger csvLogger) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.commonUtils = new CommonUtils(driver);
        this.csvLogger = csvLogger;
        PageFactory.initElements(driver, this);
    }

    public void clickAllCategoryButtonsAndSubCategories() {
        List<WebElement> categoryButtons = driver.findElements(By.xpath("//div[contains(@class, 'sc-gsFSXq')]//span"));

        try {
            WebElement declineBtn = driver.findElement(By.id("hs-eu-decline-button"));
            wait.until(ExpectedConditions.elementToBeClickable(declineBtn));
            declineBtn.click();
            System.out.println("Clicked decline button");
        } catch (Exception e) {
            System.out.println("Decline button not found or already clicked: " + e.getMessage());
        }

        for (WebElement categoryButton : categoryButtons) {
            String categoryLabel = categoryButton.getText().trim();
            try {
                wait.until(ExpectedConditions.elementToBeClickable(categoryButton));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", categoryButton);
                System.out.println("Clicked on category: " + categoryLabel);

                String dynamicXPath = "//h2[normalize-space()='Sub-Categories in " + categoryLabel + "']";
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));

                WebElement subCategorySection = driver.findElement(By.xpath(dynamicXPath));
                commonUtils.scrollToElement(subCategorySection);

                SubCategoryPage subCategoryPage = new SubCategoryPage(driver, csvLogger);
                subCategoryPage.clickAllSubCategories(categoryLabel);

                Thread.sleep(1000);
            } catch (Exception e) {
                System.err.println("Error clicking category " + categoryLabel + ": " + e.getMessage());
            }
        }
    }
}