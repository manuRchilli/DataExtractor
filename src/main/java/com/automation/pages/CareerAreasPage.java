package com.automation.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.automation.base.CommonUtils;
import com.automation.utils.CSVLogger;

public class CareerAreasPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private CommonUtils commonUtils;
    private CSVLogger csvLogger;

    public CareerAreasPage(WebDriver driver, CSVLogger csvLogger) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.commonUtils = new CommonUtils(driver);
        this.csvLogger = csvLogger;
    }

    public void clickAllCareerAreas() {
        try {
            WebElement careerAreasHeader = driver.findElement(By.id("career_areas"));
            commonUtils.scrollToElement(careerAreasHeader);
            wait.until(ExpectedConditions.visibilityOf(careerAreasHeader));

            List<WebElement> careerAreaButtons = driver.findElements(
                    By.xpath("//h2[@id='career_areas']/following-sibling::div//button"));

            List<String> careerAreaNames = new ArrayList<>();
            for (WebElement button : careerAreaButtons) {
                careerAreaNames.add(button.getText().trim());
            }

            System.out.println("Found " + careerAreaNames.size() + " career area buttons");

            for (String name : careerAreaNames) {
                WebElement button = driver.findElement(
                        By.xpath("//button[normalize-space(text())='" + name + "']"));
                commonUtils.scrollToElement(button);
                wait.until(ExpectedConditions.elementToBeClickable(button));
                button.click();
                System.out.println("Clicked on career area: " + name);

                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//h2[@id='occupation_group']/following-sibling::div//button")));

                OccupationGroupPage occupationGroupPage = new OccupationGroupPage(driver, csvLogger, name);
                occupationGroupPage.clickAllOccupationGroups();
            }
        } catch (Exception e) {
            System.err.println("Error in clickAllCareerAreas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}