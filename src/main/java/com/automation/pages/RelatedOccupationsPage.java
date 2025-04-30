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
import com.automation.utils.CSVLogger;

public class RelatedOccupationsPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private CSVLogger csvLogger;

    public RelatedOccupationsPage(WebDriver driver, CSVLogger csvLogger) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        this.csvLogger = csvLogger;
        PageFactory.initElements(driver, this);
    }

    public List<String> getRelatedOccupationNames() {
        List<String> occupationNames = new ArrayList<>();
        try {
            WebElement relatedOccupationsHeader = driver.findElement(
                    By.xpath("//h2[contains(text(), 'Related Occupations')]"));
            wait.until(ExpectedConditions.visibilityOf(relatedOccupationsHeader));

            List<WebElement> occupationLinks = driver.findElements(
                    By.xpath("//h2[contains(text(), 'Related Occupations')]/following::div[contains(@class, 'flex flex-col gap-5')]//a[contains(@href, '/lot/occupations')]"));

            for (WebElement link : occupationLinks) {
                String occupationName = link.getText().trim();
                if (!occupationName.isEmpty()) {
                    occupationNames.add(occupationName);
                    System.out.println("Found related occupation: " + occupationName);
                }
            }

            System.out.println("Total related occupations found: " + occupationNames.size());
        } catch (Exception e) {
            System.err.println("Error in getRelatedOccupationNames: " + e.getMessage());
            e.printStackTrace();
        }
        return occupationNames;
    }
}