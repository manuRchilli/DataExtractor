package com.automation.base;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.JavascriptExecutor;

public class CommonUtils {

	WebDriver driver;
	WebDriverWait wait;

	public CommonUtils(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	/**
	 * Scrolls the top of the page.
	 *
	 * @author Manu Sharma
	 */
	public void scrollToTop() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 0)");
	}

	/**
	 * Scrolls the page to the bottom.
	 * 
	 * @author Manu Sharma
	 */
	public void scrollToBottom() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	}

	/**
	 * Scrolls the page to bring the specified element into view.
	 *
	 * @param element The WebElement to scroll to.
	 * @author Manu Sharma
	 * @throws InterruptedException
	 */
	public void scrollToElement(WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));

		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Scroll to bring the element into view
		js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);

		// Wait for any potential animations or page reflows to complete
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Double-check if the element is in the viewport
		boolean isInViewport = (boolean) js.executeScript(
				"var rect = arguments[0].getBoundingClientRect();" + "return (rect.top >= 0 && rect.left >= 0 && "
						+ "rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && "
						+ "rect.right <= (window.innerWidth || document.documentElement.clientWidth));",
				element);

		// If not in viewport, try scrolling again with an offset
		if (!isInViewport) {
			js.executeScript("window.scrollBy(0, -100);");

			// Wait again for any animations
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Final check to ensure element is clickable
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public void scrollToElements(List<WebElement> elements) {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		for (WebElement element : elements) {
			try {
				wait.until(ExpectedConditions.visibilityOf(element));

				// Scroll to bring each element into view
				js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);

				// Wait for animations or page reflows
				Thread.sleep(500);

				// Check if element is in viewport
				boolean isInViewport = (boolean) js.executeScript(
						"var rect = arguments[0].getBoundingClientRect();"
								+ "return (rect.top >= 0 && rect.left >= 0 && "
								+ "rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && "
								+ "rect.right <= (window.innerWidth || document.documentElement.clientWidth));",
						element);

				// If not in viewport, try adjusting scroll
				if (!isInViewport) {
					js.executeScript("window.scrollBy(0, -100);");
					Thread.sleep(300);
				}

				// Ensure element is clickable
				wait.until(ExpectedConditions.elementToBeClickable(element));

			} catch (Exception e) {
				System.out.println("Failed to scroll to element: " + element + " - " + e.getMessage());
			}
		}
	}
	
	/**
     * Switches to the browser tab with the specified title.
     *
     * @param tabTitle The title of the tab to switch to.
     */
	public void switchToTab(String tabTitle) {
        Set<String> windows = driver.getWindowHandles();
        for (String window : windows) {
            driver.switchTo().window(window);
            if (tabTitle.equals(driver.getTitle())) {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
                wait.until((ExpectedCondition<Boolean>) d -> tabTitle.equals(d.getTitle()));
                break;
            }
        }
    }
	
	/**
	 * Closes all browser tabs except the one with the specified title.
	 * 
	 * @param tabTitle The title of the tab to remain open.
	 */
	public void closeAllTabsExcept(String tabTitle) {
		Set<String> windows = driver.getWindowHandles();

		@SuppressWarnings("unused")
		String originalWindow = driver.getWindowHandle();
		String targetWindow = null;

		for (String window : windows) {
			driver.switchTo().window(window);

			if (tabTitle.equals(driver.getTitle())) {
				targetWindow = window;
				break;
			}
		}

		if (targetWindow == null) {
			return;
		}

		for (String window : windows) {
			if (!window.equals(targetWindow)) {
				driver.switchTo().window(window);
				driver.close();
			}
		}

		driver.switchTo().window(targetWindow);
	}
	

	public void switchToNewTab() {
		Set<String> windows = driver.getWindowHandles();
		windows.forEach(driver.switchTo()::window);
	}

}
