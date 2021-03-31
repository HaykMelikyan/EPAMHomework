package amazonpages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class WritersPage {
    private WebDriver driver;
    private String writerName;

    private final By booksByWriterLoc = By.id("formatSelectorHeader");
    private final By sortByLoc = By.id("sortBySelectors");
    private final By sortByOptionsLoc = By.cssSelector("ul[role = 'listbox']");
    private final By paginationPricesLoc = By.cssSelector("span[class = 'a-size-base-plus a-color-price a-text-bold']");

    private String sortOptionTemplate = "//a[contains(normalize-space(),'%s')]";

    public WritersPage(WebDriver driver, String writerName) {
        this.driver = driver;
        this.writerName = writerName;
    }

    public void open() {
        BookSearchResultsPage bookSearchResultsPage = new BookSearchResultsPage(driver, writerName);
        bookSearchResultsPage.open();
        bookSearchResultsPage.goToFirstWritersPage();
    }

    public void waitUntilPageLoads() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(booksByWriterLoc));
        wait.until(ExpectedConditions.visibilityOfElementLocated(sortByLoc));
    }

    public void waitForPaginationUpdate() {
        new WebDriverWait(driver, 10).until(ExpectedConditions
                .numberOfElementsToBe(paginationPricesLoc, 12));
    }

    public String getBooksByWriter() {
        return driver.findElement(booksByWriterLoc).getText();
    }

    public void selectSortOptionByString(String sortOption) {
        driver.findElement(sortByLoc).click();
        WebElement optionsBlock = driver.findElement(sortByOptionsLoc);
        optionsBlock.findElement(By.xpath(String.format(sortOptionTemplate, sortOption))).click();
        waitForPaginationUpdate();
    }

    public float[] obtainPrices() {
        int i = 0;
        float prices[] = new float[12];
        List<WebElement> pricesElements = driver.findElements(paginationPricesLoc);
        for (WebElement priceElement : pricesElements) {
            String priceString = priceElement.getText().substring(1);
            prices[i++] = Float.parseFloat(priceString);
        }
        return prices;
    }
}
