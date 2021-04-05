package amazonpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class BookSearchResultsPage extends BasePage {
    private String pageUrl;
    private String writerName;

    @FindBy(css = "div[class='a-section a-spacing-small a-spacing-top-small']>span:nth-child(1)")
    private WebElement resultsCount;

    @FindBy(css = "div[class='a-section a-spacing-none'] div[class='a-row a-size-base a-color-secondary']")
    private List<WebElement> foundNames;

    @FindBy(xpath = "//div[@data-component-type='s-search-result'][1]")
    private WebElement firstResult;

    private By foundNamesLoc = By.cssSelector("div[class='a-section a-spacing-none'] " +
            "div[class='a-row a-size-base a-color-secondary']");

    public BookSearchResultsPage(WebDriver driver, String writerName) {
        this.driver = driver;
        setWriterName(writerName);
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get(BASE_URL + pageUrl);
        waitUntilPageLoads();
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
        pageUrl = "s?k=" + writerName + "&i=stripbooks-intl-ship&ref=nb_sb_noss";
    }

    public void waitUntilPageLoads() {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.visibilityOf(resultsCount));
        String resultsCountString = resultsCount.getText();
        int numberOfResults = Integer.parseInt(resultsCountString.substring(2, resultsCountString.indexOf(' ')));
        wait.until(ExpectedConditions.numberOfElementsToBe(foundNamesLoc, numberOfResults));
    }

    public boolean checkWriterInResults(String writerName) {
        for (WebElement foundName : foundNames) {
            if (!foundName.getText().contains(writerName)) {
                return false;
            }
        }
        return true;
    }

    public WritersPage goToFirstWritersPage() {
        firstResult.findElement(By.xpath(".//a[contains(normalize-space(),'" + writerName + "')]")).click();
        return new WritersPage(driver, writerName);
    }
}
