package com.automation.pages.neww;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.base.CommonUtils;
import com.automation.utils.CSVLogger;

public class SkillsPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private CommonUtils commonUtils;
    private CSVLogger csvLogger;

    public SkillsPage(WebDriver driver, CSVLogger csvLogger) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.commonUtils = new CommonUtils(driver);
        this.csvLogger = csvLogger;
        PageFactory.initElements(driver, this);
    }

    public void clickAllSkillsInSection(String skillsCategory, String subCategory) {
        System.out.println("Clicking Skills in Sub-Category: " + subCategory);

        String mainWindowHandle = driver.getWindowHandle();

        try {
            String dynamicXPath = "//h2[normalize-space()='Skills in " + subCategory + "']";
            WebElement skillsSection = driver.findElement(By.xpath(dynamicXPath));
            wait.until(ExpectedConditions.visibilityOf(skillsSection));
            commonUtils.scrollToElement(skillsSection);
        } catch (Exception e) {
            System.err.println("Skills section not found for " + subCategory + ": " + e.getMessage());
            return;
        }

        List<WebElement> skillButtons = driver.findElements(
                By.xpath("//h2[normalize-space()='Skills in " + subCategory + "']/ancestor::div[contains(@class, 'sc-dAlyuH')]//div[contains(@class, 'sc-iGgWBj')]//div[contains(@class, 'sc-gsFSXq')]//span[contains(@class, 'sc-kAyceB')]"));

        System.out.println("Found " + skillButtons.size() + " skill buttons in sub-category: " + subCategory);

        List<String> skillLabels = new ArrayList<>();
        List<String> skillXPaths = new ArrayList<>();

        for (WebElement skillButton : skillButtons) {
            skillLabels.add(skillButton.getText().trim());
            String skillId = skillButton.findElement(By.xpath("./parent::div")).getAttribute("id");
            if (skillId != null && !skillId.isEmpty()) {
                skillXPaths.add("//div[@id='" + skillId + "']//span[contains(@class, 'sc-kAyceB')]");
            } else {
                skillXPaths.add("//span[contains(@class, 'sc-kAyceB') and normalize-space()='" + skillButton.getText().trim() + "']");
            }
        }

        for (int i = 0; i < skillLabels.size(); i++) {
            String skillLabel = skillLabels.get(i);
            String skillXPath = skillXPaths.get(i);

            try {
                driver.switchTo().window(mainWindowHandle);
                WebElement skillButton = driver.findElement(By.xpath(skillXPath));
                wait.until(ExpectedConditions.elementToBeClickable(skillButton));
                commonUtils.scrollToElement(skillButton);

                System.out.println("Clicking on skill: " + skillLabel);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", skillButton);

                wait.until(driver -> driver.getWindowHandles().size() > 1);
                Set<String> windowHandles = driver.getWindowHandles();

                for (String handle : windowHandles) {
                    if (!handle.equals(mainWindowHandle)) {
                        driver.switchTo().window(handle);
                        System.out.println("Switched to new tab for skill: " + skillLabel);

                        RelatedSkillsPage relatedSkillsPage = new RelatedSkillsPage(driver, csvLogger);
                        relatedSkillsPage.getRelatedSkillsAndLog(skillsCategory, subCategory, skillLabel);

                        Thread.sleep(2000);
                        driver.close();
                        System.out.println("Closed tab for skill: " + skillLabel);
                        break;
                    }
                }

                driver.switchTo().window(mainWindowHandle);
                System.out.println("Switched back to main window");
                Thread.sleep(1000);

            } catch (Exception e) {
                System.err.println("Error processing skill " + skillLabel + ": " + e.getMessage());
                try {
                    driver.switchTo().window(mainWindowHandle);
                } catch (Exception e2) {
                    System.err.println("Could not switch back to main window: " + e2.getMessage());
                }
            }
        }
    }

    public void clickSkillsInSpecificSection(String skillsCategory, String subCategory) {
        clickAllSkillsInSection(skillsCategory, subCategory);
    }
}