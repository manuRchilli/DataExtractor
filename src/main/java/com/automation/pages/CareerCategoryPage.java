package com.automation.pages;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.base.BasePage;
import com.automation.base.CommonUtils;
import com.automation.utils.CSVLogger;

public class CareerCategoryPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(CareerCategoryPage.class);
    private  WebDriverWait wait;
    private  CommonUtils commonUtils;
    private final String basePageTitle = "Lightcast Occupation Taxonomy | Lightcast"; 
    private WebDriver driver;
    	
    public CareerCategoryPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        commonUtils = new CommonUtils(driver);
    }
    
	 /**
     * Get list of Career Area buttons dynamically
     */
    private List<WebElement> getCareerAreaButtons() {
        return driver.findElements(By.xpath("//h2[@id='career_areas']/following-sibling::div//button"));
    }

    /**
     * Get list of Occupation Group buttons dynamically
     */
    private List<WebElement> getOccupationGroupButtons() {
        return driver.findElements(By.xpath("//h2[@id='occupation_group']/following-sibling::div//button"));
    }

    /**
     * Get list of Occupations buttons dynamically
     */
    private List<WebElement> getOccupationsButtons() {
        return driver.findElements(By.xpath("//h2[@id='occupations']/following-sibling::div//button"));
    }

    /**
     * Get list of Specialized Occupations buttons dynamically
     */
    private List<WebElement> getSpecializedOccupationsButtons() {
        return driver.findElements(By.xpath("//h2[@id='specialized_occupations']/following-sibling::div//button"));
    }
    
    public void clickCareerAndOccupationGroupButtons() {
        CSVLogger csvLogger = new CSVLogger();
        String targetCareerArea = "Military"; // Define target Career Area

        List<WebElement> careerButtons = getCareerAreaButtons();
        boolean startClicking = false; // Flag to start clicking from target button

        for (WebElement careerButton : careerButtons) {
            commonUtils.scrollToElement(careerButton);
            wait.until(ExpectedConditions.elementToBeClickable(careerButton));
            String careerName = careerButton.getText().trim();

            // Start clicking only when we find the target Career Area
            if (careerName.equalsIgnoreCase(targetCareerArea)) {
                startClicking = true;
            }

            if (startClicking) {
                careerButton.click();
                waitForElementVisibility(By.id("occupation_group"));

                List<WebElement> occupationGroupButtons = getOccupationGroupButtons();
                for (WebElement occupationGroupButton : occupationGroupButtons) {
                    commonUtils.scrollToElement(occupationGroupButton);
                    wait.until(ExpectedConditions.elementToBeClickable(occupationGroupButton));
                    String occupationGroupName = occupationGroupButton.getText();
                    occupationGroupButton.click();

                    waitForElementVisibility(By.id("occupations"));

                    List<WebElement> occupationsButtons = getOccupationsButtons();
                    for (WebElement occupationButton : occupationsButtons) {
                        commonUtils.scrollToElement(occupationButton);
                        wait.until(ExpectedConditions.elementToBeClickable(occupationButton));
                        String occupationName = occupationButton.getText();
                        occupationButton.click();

                        waitForElementVisibility(By.id("specialized_occupations"));

                        List<WebElement> specializedButtons = getSpecializedOccupationsButtons();
                        for (WebElement specializedButton : specializedButtons) {
                            commonUtils.scrollToElement(specializedButton);
                            wait.until(ExpectedConditions.elementToBeClickable(specializedButton));
                            String specializedName = specializedButton.getText();
                            specializedButton.click();

                            // Wait for content to load
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            commonUtils.switchToNewTab();

                            // Write data to CSV including Related Occupations

                            commonUtils.switchToTab(basePageTitle);
                            commonUtils.closeAllTabsExcept(basePageTitle);

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        csvLogger.close();
    }

    
//    public void clickCareerAndOccupationGroupButtons() {
//        CSVLogger csvLogger = new CSVLogger();
//
//        List<WebElement> careerButtons = getCareerAreaButtons();
//        for (WebElement careerButton : careerButtons) {
//            commonUtils.scrollToElement(careerButton);
//            wait.until(ExpectedConditions.elementToBeClickable(careerButton));
//            String careerName = careerButton.getText();
//            careerButton.click();
//
//            waitForElementVisibility(By.id("occupation_group"));
//
//            List<WebElement> occupationGroupButtons = getOccupationGroupButtons();
//            for (WebElement occupationGroupButton : occupationGroupButtons) {
//                commonUtils.scrollToElement(occupationGroupButton);
//                wait.until(ExpectedConditions.elementToBeClickable(occupationGroupButton));
//                String occupationGroupName = occupationGroupButton.getText();
//                occupationGroupButton.click();
//
//                waitForElementVisibility(By.id("occupations"));
//
//                List<WebElement> occupationsButtons = getOccupationsButtons();
//                for (WebElement occupationButton : occupationsButtons) {
//                    commonUtils.scrollToElement(occupationButton);
//                    wait.until(ExpectedConditions.elementToBeClickable(occupationButton));
//                    String occupationName = occupationButton.getText();
//                    occupationButton.click();
//
//                    waitForElementVisibility(By.id("specialized_occupations"));
//
//                    List<WebElement> specializedButtons = getSpecializedOccupationsButtons();
//                    for (WebElement specializedButton : specializedButtons) {
//                        commonUtils.scrollToElement(specializedButton);
//                        wait.until(ExpectedConditions.elementToBeClickable(specializedButton));
//                        String specializedName = specializedButton.getText();
//                        specializedButton.click();
//
//                        // Wait for content to load
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        commonUtils.switchToNewTab();
//                        careerPage.scrollToOccupationTab();
//
//                        // Fetch Skills, Titles, and Related Occupations
//                        List<String> skills = careerPage.getSkills();
//                        List<String> titles = careerPage.getTitles();
//                        List<String> relatedOccupations = careerPage.getRelatedOccupations();  // Fetch Related Occupations
//
//                        // Write data to CSV in real-time including Related Occupations
//                        csvLogger.writeData(careerName, occupationGroupName, occupationName, specializedName, skills, titles, relatedOccupations);
//
//                        commonUtils.switchToTab(basePageTitle);
//                        commonUtils.closeAllTabsExcept(basePageTitle);
//                        
//                        try {
//                            Thread.sleep(2000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }
//
//        csvLogger.close();
//    }


    /**
     * Wait for a section to become visible
     */
    private void waitForElementVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

}