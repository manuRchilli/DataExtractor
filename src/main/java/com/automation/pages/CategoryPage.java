package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class CategoryPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public CategoryPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    // Method to click all category buttons
    public void clickAllCategoryButtons() {
        // Find all buttons based on their label text
        List<WebElement> categoryButtons = driver.findElements(By.xpath("//div[contains(@class, 'sc-gsFSXq')]//span"));

        // Iterate over each button and click
        for (WebElement button : categoryButtons) {
            String buttonLabel = button.getText().trim();
            try {
                // Wait for the button to be clickable
                wait.until(ExpectedConditions.elementToBeClickable(button));

                // Click the button
                button.click();

                // Print the label to confirm
                System.out.println("Clicked on button: " + buttonLabel);

                // Optional: Wait a bit to ensure the page can handle the click
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Error clicking on button: " + button.getText() + " - " + e.getMessage());
            }
        }
    }
}
