import amazonpages.BookSearchResultsPage;
import amazonpages.HomePage;
import amazonpages.WritersPage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.FileReader;
import java.io.IOException;

public class amazonWebsiteTest {
    WebDriver driver;

    @BeforeMethod
    public void setupDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
    }

    @AfterMethod
    public void closeBrowser() {
        driver.quit();
    }

    @Test(dataProvider = "testParams")
    public void booksByAuthor(String writerName, String sortOption) {
        SoftAssert assertion = new SoftAssert();

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
        bookSearchResultsPage.waitUntilPageLoads();

        // Check writer in results
        assertion.assertTrue(bookSearchResultsPage.checkWriterInResults(writerName),
                "Not in all displayed results writer is " + writerName + ".");

        // Go to writer's page
        WritersPage writersPage = bookSearchResultsPage.goToFirstWritersPage();
        writersPage.waitUntilPageLoads();

        // Check if there is "Books by " + {searched_writer} text
        String expectedWriter = "Titles By " + writerName;
        String actualWriter = writersPage.getBooksByWriter();
        assertion.assertEquals(actualWriter, expectedWriter, "There is no \"Titles by " + writerName + "\" text in the page.");

        // Check sorting by price from low to high
        writersPage.selectSortOptionByString(sortOption);
        float prices[] = writersPage.obtainPrices();
        boolean pricesSorted = false;
        switch (sortOption) {
            case "Price: Low to High":
                pricesSorted = fromLowToHigh(prices);
                break;
            case "Price: High to Low":
                pricesSorted = fromHighToLow(prices);
        }
        assertion.assertTrue(pricesSorted, "Prices are not sorted with \"" + sortOption + "\" pattern.");

        // Perform assertions
        assertion.assertAll();
    }

    private boolean fromLowToHigh(float sortedNums[]) {
        for (int i = 0; i < sortedNums.length - 1; i++) {
            if (sortedNums[i] > sortedNums[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private boolean fromHighToLow(float sortedNums[]) {
        for (int i = 0; i < sortedNums.length - 1; i++) {
            if (sortedNums[i] < sortedNums[i + 1]) {
                return false;
            }
        }
        return true;
    }

    @DataProvider(name = "testParams")
    public static Object[][] nameAndSort() throws IOException, ParseException {
        Object[][] testParams = new Object[4][2];

        JSONArray paramsJSON = (JSONArray) (new JSONParser().parse(
                new FileReader("src/test/resources/amazonTestParams.json")));
        for (int i = 0; i < paramsJSON.size(); i++) {
            testParams[i][0] = ((JSONObject) paramsJSON.get(i)).get("writerName");
            testParams[i][1] = ((JSONObject) paramsJSON.get(i)).get("sortOption");
        }

        return testParams;
    }
}