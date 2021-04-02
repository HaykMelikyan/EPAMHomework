package amazonpages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HomePage extends BasePage {
    private String pageUrl = BASE_URL;

    @FindBy(id = "glow-ingress-block")
    private WebElement deliverCountry;

    @FindBy(id = "glow-ingress-line2")
    private WebElement deliverCountryName;

    @FindBy(css = "select[name = 'url'] > option")
    private List<WebElement> dropdownOptions;

    @FindBy(id = "nav-search-dropdown-card")
    private WebElement searchType;

    @FindBy(id = "nav-search-label-id")
    private WebElement dropdownText;

    @FindBy(id = "twotabsearchtextbox")
    private WebElement searchInputField;

    private By dropdownOptionsLoc = By.cssSelector("select[name = 'url'] > option");
    private String searchOptionTemplate = "//option[normalize-space() = '%s']";

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get(pageUrl);
        waitUntilPageLoads();
    }

    public void waitUntilPageLoads() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(deliverCountry));
        wait.until(ExpectedConditions.visibilityOf(searchType));
    }

    public void selectSearchOptionByString(String type) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        searchType.click();
        wait.until(ExpectedConditions.numberOfElementsToBe(dropdownOptionsLoc, 28));
        wait.until(ExpectedConditions.visibilityOfAllElements(dropdownOptions));

        searchType.findElement(By.xpath(String.format(searchOptionTemplate, type))).click();
        wait.until(ExpectedConditions.textToBePresentInElement(dropdownText, type));
    }

    public BookSearchResultsPage searchString(String searchText) {
        searchInputField.sendKeys(searchText + Keys.ENTER);
        return new BookSearchResultsPage(driver, searchText);
    }

    public String getDeliveryCountry() {
        return deliverCountryName.getText();
    }
}
