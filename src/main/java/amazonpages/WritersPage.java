package amazonpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class WritersPage extends BasePage {
    private String writerName;

    @FindBy(id = "formatSelectorHeader")
    private WebElement booksByWriter;

    @FindBy(id = "sortBySelectors")
    private WebElement sortBy;

    @FindBy(css = "ul[role = 'listbox']")
    private WebElement sortByOptions;

    @FindBy(css = "span[class = 'a-size-base-plus a-color-price a-text-bold']")
    private List<WebElement> pricesElements;

    private By pricesElementsLoc = By.cssSelector("span[class = 'a-size-base-plus a-color-price a-text-bold']");
    private String sortOptionTemplate = "//a[contains(normalize-space(),'%s')]";

    public WritersPage(WebDriver driver, String writerName) {
        this.driver = driver;
        this.writerName = writerName;
        PageFactory.initElements(driver, this);
    }

    public void waitUntilPageLoads() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(booksByWriter));
        wait.until(ExpectedConditions.visibilityOf(sortBy));
    }

    public void waitForPaginationUpdate() {
        new WebDriverWait(driver, 10).until(ExpectedConditions
                .numberOfElementsToBe(pricesElementsLoc, 12));
    }

    public String getBooksByWriter() {
        return booksByWriter.getText();
    }

    public void selectSortOptionByString(String sortOption) {
        sortBy.click();
        sortByOptions.findElement(By.xpath(String.format(sortOptionTemplate, sortOption))).click();
        waitForPaginationUpdate();
    }

    public float[] obtainPrices() {
        int i = 0;
        float prices[] = new float[12];
        for (WebElement priceElement : pricesElements) {
            String priceString = priceElement.getText().substring(1);
            prices[i++] = Float.parseFloat(priceString);
        }
        return prices;
    }
}
