package com.automation.pages;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.automation.utils.CSVLogger;

public class OccupationGroupPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private CSVLogger csvLogger;
    private String careerArea;

    public OccupationGroupPage(WebDriver driver, CSVLogger csvLogger, String careerArea) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.csvLogger = csvLogger;
        this.careerArea = careerArea;
        PageFactory.initElements(driver, this);
    }

    public void clickAllOccupationGroups() {
        try {
            WebElement occupationGroupHeader = driver.findElement(By.id("occupation_group"));
            wait.until(ExpectedConditions.visibilityOf(occupationGroupHeader));

            List<WebElement> occupationGroupButtons = driver.findElements(
                    By.xpath("//h2[@id='occupation_group']/following-sibling::div//button"));

            System.out.println("Found " + occupationGroupButtons.size() + " occupation group buttons");

            for (WebElement button : occupationGroupButtons) {
                String occupationGroupName = button.getText().trim();
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
                wait.until(ExpectedConditions.elementToBeClickable(button));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                System.out.println("Clicked on occupation group: " + occupationGroupName);

                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//h2[@id='occupations']/following-sibling::div//button")));

                OccupationsPage occupationsPage = new OccupationsPage(driver, csvLogger, careerArea, occupationGroupName);
                occupationsPage.clickAllOccupations();
            }
        } catch (Exception e) {
            System.err.println("Error in clickAllOccupationGroups: " + e.getMessage());
            e.printStackTrace();
        }
    }
}