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

public class SubCategoryPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private CommonUtils commonUtils;
    private CSVLogger csvLogger;

    public SubCategoryPage(WebDriver driver, CSVLogger csvLogger) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.commonUtils = new CommonUtils(driver);
        this.csvLogger = csvLogger;
        PageFactory.initElements(driver, this);
    }

    public void clickAllSubCategories(String skillsCategory) {
        try {
            String dynamicXPath = "//h2[normalize-space()='Sub-Categories in " + skillsCategory + "']";
            WebElement subCategorySection = driver.findElement(By.xpath(dynamicXPath));
            wait.until(ExpectedConditions.visibilityOf(subCategorySection));
        } catch (Exception e) {
            System.err.println("Sub-category section not found for " + skillsCategory + ": " + e.getMessage());
            return;
        }

        List<WebElement> subCategoryButtons = driver.findElements(
                By.xpath("//div[contains(@class, 'sc-iGgWBj gzqErG')]//span[contains(@class, 'sc-kAyceB')]"));

        for (WebElement subCategoryButton : subCategoryButtons) {
            String subCategoryLabel = subCategoryButton.getText().trim();
            try {
                commonUtils.scrollToElement(subCategoryButton);
                wait.until(ExpectedConditions.elementToBeClickable(subCategoryButton));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", subCategoryButton);
                System.out.println("Clicked on sub-category: " + subCategoryLabel);

                try {
                    List<WebElement> closeIcons = driver.findElements(By.xpath("//button[contains(@class, 'close-icon')]"));
                    if (!closeIcons.isEmpty()) {
                        WebElement closeIcon = closeIcons.get(0);
                        wait.until(ExpectedConditions.elementToBeClickable(closeIcon));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeIcon);
                        System.out.println("Clicked on close icon to unblock elements.");
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    System.err.println("Error handling close icons: " + e.getMessage());
                }

                SkillsPage skillsPage = new SkillsPage(driver, csvLogger);
                System.out.println("Clicking Skills block for sub-category: " + subCategoryLabel);
                skillsPage.clickSkillsInSpecificSection(skillsCategory, subCategoryLabel);
                System.out.println("Clicked Skills block");

            } catch (Exception e) {
                System.err.println("Error clicking sub-category " + subCategoryLabel + ": " + e.getMessage());
            }
        }
    }
}