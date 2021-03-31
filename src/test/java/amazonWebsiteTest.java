import amazonpages.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class amazonWebsiteTest {
    WebDriver driver;

    @BeforeMethod
    public void setupDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
    }

    @Test
    public void booksByAuthor() {
        SoftAssert assertion = new SoftAssert();
        String writerName = "Andrzej Sapkowski";

        // Open amazon.com
        HomePage homePage = new HomePage(driver);
        homePage.open();

        // Check delivery country
        String expectedDeliveryCountry = "Armenia";
        String actualDeliveryCountry = homePage.getDeliveryCountry();
        assertion.assertEquals(actualDeliveryCountry, expectedDeliveryCountry, "Delivery country is not Armenia.");

        // Choose books option and search for writer
        homePage.selectSearchOptionByString("Books");
        BookSearchResultsPage bookSearchResultsPage = homePage.searchString(writerName);

        // Check writer in results
        assertion.assertTrue(bookSearchResultsPage.checkWriterInResults(writerName),
                "Not in all displayed results writer is " + writerName + ".");

        // Go to writer's page
        WritersPage writersPage = bookSearchResultsPage.goToFirstWritersPage();

        // Check if there is "Books by " + {searched_writer} text
        String expectedWriter = "Books By " + writerName;
        String actualWriter = writersPage.getBooksByWriter();
        assertion.assertEquals(actualWriter, expectedWriter, "There is no \"Books by " + writerName + "\" text in the page.");

        // Check sorting by price from low to high
        writersPage.selectSortOptionByString("Price: Low to High");
        float prices[] = writersPage.obtainPrices();
        boolean pricesSorted = true;
        for (int i = 0; i < prices.length - 1; i++) {
            if (prices[i] > prices[i + 1]) {
                pricesSorted = false;
                break;
            }
        }
        assertion.assertTrue(pricesSorted, "Prices are not sorted from low to high.");

        // Perform assertions
        assertion.assertAll();
    }

    @AfterMethod
    public void closeBrowser() {
        driver.quit();
    }
}
