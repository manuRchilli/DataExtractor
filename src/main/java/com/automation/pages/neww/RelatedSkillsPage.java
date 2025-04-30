package com.automation.pages.neww;

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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.commonUtils = new CommonUtils(driver);
        this.csvLogger = csvLogger; // Use shared CSVLogger instance
        PageFactory.initElements(driver, this);
    }

    public void getRelatedSkillsAndLog(String skillsCategory, String subCategory, String skill) {
        System.out.println("Fetching Related Skills for Skill: " + skill);

        List<String> relatedSkills = new ArrayList<>();
        List<String> relatedTitles = new ArrayList<>();
        List<String> relatedOccupations = new ArrayList<>();

        try {
            // Fetch related skills
            WebElement relatedSkillsSection = driver.findElement(By.xpath("//div[contains(text(), 'Related Skills')]"));
            wait.until(ExpectedConditions.visibilityOf(relatedSkillsSection));

            List<WebElement> relatedSkillLinks = driver.findElements(
                    By.xpath("//div[@class='flex flex-col gap-y-5']//a"));

            for (WebElement skillLink : relatedSkillLinks) {
                String skillName = skillLink.getText().trim();
                relatedSkills.add(skillName);
                System.out.println("Related Skill: " + skillName);
            }

            // Fetch related titles
            clickRelatedTitlesAndFetch(relatedTitles);

            // Fetch related occupations
            clickRelatedOccupationsAndFetch(relatedOccupations);

            // Log the data to CSV
            csvLogger.writeData(skillsCategory, subCategory, skill, relatedSkills, relatedTitles, relatedOccupations);
        } catch (Exception e) {
            System.err.println("Error fetching related skills for " + skill + ": " + e.getMessage());
        }
    }

    public void clickRelatedSkill(String skillName) {
        System.out.println("Clicking on Related Skill: " + skillName);

        try {
            WebElement skillLink = driver.findElement(
                    By.xpath("//div[@class='flex flex-col gap-y-5']//a[normalize-space(text())='" + skillName + "']"));
            wait.until(ExpectedConditions.elementToBeClickable(skillLink));
            skillLink.click();
            System.out.println("Clicked on skill: " + skillName);
        } catch (Exception e) {
            System.err.println("Error clicking related skill " + skillName + ": " + e.getMessage());
        }
    }

    private void clickRelatedTitlesAndFetch(List<String> relatedTitles) {
        System.out.println("Clicking on 'Related Titles' button");

        try {
            WebElement relatedTitlesButton = driver.findElement(By.xpath("//button[@aria-label='Related Titles']"));
            wait.until(ExpectedConditions.elementToBeClickable(relatedTitlesButton));
            relatedTitlesButton.click();

            System.out.println("Fetching Related Titles...");

            List<WebElement> relatedTitleElements = driver.findElements(
                    By.xpath("//div[@class='flex flex-col items-start gap-3 w-full']//a"));

            for (WebElement title : relatedTitleElements) {
                String titleText = title.getText().trim();
                relatedTitles.add(titleText);
                System.out.println("Related Title: " + titleText);
            }
        } catch (Exception e) {
            System.err.println("Error fetching related titles: " + e.getMessage());
        }
    }

    private void clickRelatedOccupationsAndFetch(List<String> relatedOccupations) {
        System.out.println("Clicking on 'Related Occupations' button");

        try {
            WebElement relatedOccupationsButton = driver.findElement(By.xpath("//button[@aria-label='Related Occupations']"));
            wait.until(ExpectedConditions.elementToBeClickable(relatedOccupationsButton));
            relatedOccupationsButton.click();

            System.out.println("Fetching Related Occupations...");

            // Wait for the occupations section to be visible
            WebElement occupationsSection = driver.findElement(By.xpath("//h2[text()='Related Occupations']"));
            wait.until(ExpectedConditions.visibilityOf(occupationsSection));

            // Locate all occupation links within the section
            List<WebElement> relatedOccupationElements = driver.findElements(
                    By.xpath("//div[contains(@class, 'flex flex-col justify-center items-start gap-5')]//div[@class='grid gap-1']//a"));

            for (WebElement occupation : relatedOccupationElements) {
                String occupationText = occupation.getText().trim();
                relatedOccupations.add(occupationText);
                System.out.println("Related Occupation: " + occupationText);
            }
        } catch (Exception e) {
            System.err.println("Error fetching related occupations: " + e.getMessage());
        }
    }
}