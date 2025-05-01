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

public class RelatedTitlesPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private CSVLogger csvLogger;
    private CommonUtils commonUtils;

    public RelatedTitlesPage(WebDriver driver, CSVLogger csvLogger) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        this.csvLogger = csvLogger;
        this.commonUtils = new CommonUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public List<String> getRelatedTitleNames() {
        List<String> titleNames = new ArrayList<>();
        try {
            commonUtils.scrollToTop();
            WebElement relatedTitlesTab = driver.findElement(
                    By.xpath("//*[@id=\"Related Titles\"]"));
            commonUtils.scrollToElement(relatedTitlesTab);
            wait.until(ExpectedConditions.elementToBeClickable(relatedTitlesTab));
            relatedTitlesTab.click();
            System.out.println("Clicked on Related Titles tab");

            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//h2[contains(text(), 'Related Titles')] | //h2[contains(text(), 'related titles')]")));

            WebElement relatedTitlesHeader = driver.findElement(
                    By.xpath("//h2[contains(text(), 'Related Titles')] | //h2[contains(text(), 'related titles')]"));
            wait.until(ExpectedConditions.visibilityOf(relatedTitlesHeader));

            List<WebElement> titleLinks = driver.findElements(
                    By.xpath("//h2[contains(text(), 'Related Titles') or contains(text(), 'related titles')]/following::div[contains(@class, 'flex flex-col items-start gap-3')]//a[contains(@href, '/open-titles')]"));

            for (WebElement link : titleLinks) {
                String titleName = link.getText().trim();
                if (!titleName.isEmpty()) {
                    titleNames.add(titleName);
                    System.out.println("Found related title: " + titleName);
                }
            }

            System.out.println("Total related titles found: " + titleNames.size());
        } catch (Exception e) {
            System.err.println("Error in getRelatedTitleNames: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Page source snippet: " + driver.getPageSource().substring(0, Math.min(500, driver.getPageSource().length())));
        }
        return titleNames;
    }
}