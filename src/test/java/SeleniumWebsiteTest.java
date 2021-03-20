import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class SeleniumWebsiteTest {

    @Test
    public void seleniumWebsiteTest() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        WebDriver browser = new ChromeDriver();

        browser.get("https://www.selenium.dev/");
        browser.findElement(By.cssSelector("a.nav-item[href = '/downloads']")).click();
        String latestVersion = browser.findElement(By.xpath(
                "//p[contains(normalize-space(),'Latest stable version')]/a")).getText();
        Assert.assertEquals(latestVersion, "3.141.59", "Latest version is 3.141.59.");

        String searchText = "selenium webdriver";
        browser.findElement(By.name("search")).sendKeys(searchText + Keys.ENTER);
        List<WebElement> resultLinks = browser.findElements(By.cssSelector(
                "a.lc_.styleable-title, div.gsc-thumbnail-inside > div.gs-title > a.gs-title"));
        boolean containsSearch = false;
        for (WebElement link : resultLinks) {
            if (link.getText().toLowerCase().contains(searchText)) {
                containsSearch = true;
                break;
            }
        }
        Assert.assertTrue(containsSearch, "Search results contain searched text.");

        browser.quit();
    }
}
