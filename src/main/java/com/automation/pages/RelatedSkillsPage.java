package com.automation.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.automation.base.CommonUtils;
import com.automation.utils.CSVLogger;

public class RelatedSkillsPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private CommonUtils commonUtils;
    private CSVLogger csvLogger;

    public RelatedSkillsPage(WebDriver driver, CSVLogger csvLogger) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        this.csvLogger = csvLogger;
        this.commonUtils = new CommonUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public List<String> getRelatedSkillNames() {
        List<String> skillNames = new ArrayList<>();
        try {
            commonUtils.scrollToBottom();
            WebElement relatedSkillsTab = driver.findElement(
                    By.xpath("//button[@id='Related Skills']"));
            commonUtils.scrollToElement(relatedSkillsTab);
            wait.until(ExpectedConditions.elementToBeClickable(relatedSkillsTab));
            relatedSkillsTab.click();
            System.out.println("Clicked on Related Skills tab");

            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//h2[contains(text(), 'Related Skills')] | //h2[contains(text(), 'related skills')]")));

            WebElement relatedSkillsHeader = driver.findElement(
                    By.xpath("//h2[contains(text(), 'Related Skills')] | //h2[contains(text(), 'related skills')]"));
            wait.until(ExpectedConditions.visibilityOf(relatedSkillsHeader));

            List<WebElement> skillLinks = driver.findElements(
                    By.xpath("//h2[contains(text(), 'Related Skills') or contains(text(), 'related skills')]/following::div[contains(@class, 'flex flex-col gap-5')]//a[contains(@href, '/open-skills')]"));

            for (WebElement link : skillLinks) {
                String skillName = link.getText().trim();
                if (!skillName.isEmpty()) {
                    skillNames.add(skillName);
                    System.out.println("Found related skill: " + skillName);
                }
            }

            System.out.println("Total related skills found: " + skillNames.size());
        } catch (Exception e) {
            System.err.println("Error in getRelatedSkillNames: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Page source snippet: " + driver.getPageSource().substring(0, Math.min(500, driver.getPageSource().length())));
        }
        return skillNames;
    }
}