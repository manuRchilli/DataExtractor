package com.automation.pages;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.automation.utils.CSVLogger;

public class SpecializedOccupationsPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private CSVLogger csvLogger;
    private String careerArea;
    private String occupationGroup;
    private String occupation;

    public SpecializedOccupationsPage(WebDriver driver, CSVLogger csvLogger, String careerArea,
                                      String occupationGroup, String occupation) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        this.csvLogger = csvLogger;
        this.careerArea = careerArea;
        this.occupationGroup = occupationGroup;
        this.occupation = occupation;
        PageFactory.initElements(driver, this);
    }

    public void clickAllSpecializedOccupations() {
        try {
            WebElement specializedOccupationsHeader = driver.findElement(By.id("specialized_occupations"));
            wait.until(ExpectedConditions.visibilityOf(specializedOccupationsHeader));

            List<WebElement> specializedOccupationButtons = driver.findElements(
                    By.xpath("//h2[@id='specialized_occupations']/following-sibling::div//button"));

            System.out.println("Found " + specializedOccupationButtons.size() + " specialized occupation buttons");

            String originalWindow = driver.getWindowHandle();

            for (WebElement button : specializedOccupationButtons) {
                String specializedOccupationName = button.getText().trim();
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
                wait.until(ExpectedConditions.elementToBeClickable(button));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                System.out.println("Clicked on specialized occupation: " + specializedOccupationName);

                wait.until(ExpectedConditions.numberOfWindowsToBe(2));
                Set<String> allWindows = driver.getWindowHandles();

                for (String windowHandle : allWindows) {
                    if (!windowHandle.equals(originalWindow)) {
                        driver.switchTo().window(windowHandle);
                        System.out.println("Switched to new tab: " + driver.getTitle());
                        break;
                    }
                }

                Thread.sleep(2000); // Wait for page to stabilize

                RelatedOccupationsPage relatedOccupationsPage = new RelatedOccupationsPage(driver, csvLogger);
                List<String> relatedOccupations = relatedOccupationsPage.getRelatedOccupationNames();

                RelatedSkillsPage relatedSkillsPage = new RelatedSkillsPage(driver, csvLogger);
                List<String> relatedSkills = relatedSkillsPage.getRelatedSkillNames();

                RelatedTitlesPage relatedTitlesPage = new RelatedTitlesPage(driver, csvLogger);
                List<String> relatedTitles = relatedTitlesPage.getRelatedTitleNames();

                // Log the combined data
                csvLogger.writeData(
                        careerArea,
                        occupationGroup,
                        occupation,
                        specializedOccupationName,
                        relatedSkills,
                        relatedTitles,
                        relatedOccupations
                );

                driver.close();
                System.out.println("Closed new tab");

                driver.switchTo().window(originalWindow);
                System.out.println("Switched back to original window: " + driver.getTitle());

                wait.until(ExpectedConditions.visibilityOf(specializedOccupationsHeader));
            }
        } catch (Exception e) {
            System.err.println("Error in clickAllSpecializedOccupations: " + e.getMessage());
            e.printStackTrace();
        }
    }
}