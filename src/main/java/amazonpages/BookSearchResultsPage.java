package amazonpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class BookSearchResultsPage {
    private WebDriver driver;
    private String pageUrl;
    private String writerName;

    private final By resultsCountLoc = By.cssSelector("div[class='a-section a-spacing-small a-spacing-top-small']>span:nth-child(1)");
    private final By writersNamesLoc = By.cssSelector("div[class='a-section a-spacing-none'] " +
            "div[class='a-row a-size-base a-color-secondary']");
    private final String firstResultWriterNameXpath = "//div[@data-component-type='s-search-result'][1]";

    public BookSearchResultsPage(WebDriver driver, String writerName) {
        this.driver = driver;
        setWriterName(writerName);
    }

    public void open() {
        driver.get(HomePage.BASE_URL + pageUrl);
        waitUntilPageLoads();
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
        pageUrl = "s?k=" + writerName + "&i=stripbooks-intl-ship&ref=nb_sb_noss";
    }

    public void waitUntilPageLoads() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.visibilityOfElementLocated(resultsCountLoc));
        String resultsCountString = driver.findElement(resultsCountLoc).getText();
        int numberOfResults = Integer.parseInt(resultsCountString.substring(2, resultsCountString.indexOf(' ')));
        wait.until(ExpectedConditions.numberOfElementsToBe(writersNamesLoc, numberOfResults));
    }

    public boolean checkWriterInResults(String writerName) {
        List<WebElement> foundNamesList = driver.findElements(writersNamesLoc);
        for (WebElement foundName : foundNamesList) {
            if (!foundName.getText().contains(writerName)) {
                return false;
            }
        }
        return true;
    }

    public WritersPage goToFirstWritersPage() {
        driver.findElement(By.xpath(firstResultWriterNameXpath
                + "//a[contains(normalize-space(),'" + writerName + "')]")).click();
        return new WritersPage(driver, writerName);
    }
}
