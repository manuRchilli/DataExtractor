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

public class OccupationsPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private CSVLogger csvLogger;
    private String careerArea;
    private String occupationGroup;

    public OccupationsPage(WebDriver driver, CSVLogger csvLogger, String careerArea, String occupationGroup) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.csvLogger = csvLogger;
        this.careerArea = careerArea;
        this.occupationGroup = occupationGroup;
        PageFactory.initElements(driver, this);
    }

    public void clickAllOccupations() {
        try {
            WebElement occupationsHeader = driver.findElement(By.id("occupations"));
            wait.until(ExpectedConditions.visibilityOf(occupationsHeader));

            List<WebElement> occupationButtons = driver.findElements(
                    By.xpath("//h2[@id='occupations']/following-sibling::div//button"));

            System.out.println("Found " + occupationButtons.size() + " occupation buttons");

            for (WebElement button : occupationButtons) {
                String occupationName = button.getText().trim();
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
                wait.until(ExpectedConditions.elementToBeClickable(button));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                System.out.println("Clicked on occupation: " + occupationName);

                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//h2[@id='specialized_occupations']/following-sibling::div//button")));

                SpecializedOccupationsPage specializedOccupationsPage = new SpecializedOccupationsPage(
                        driver, csvLogger, careerArea, occupationGroup, occupationName);
                specializedOccupationsPage.clickAllSpecializedOccupations();
            }
        } catch (Exception e) {
            System.err.println("Error in clickAllOccupations: " + e.getMessage());
            e.printStackTrace();
        }
    }
}