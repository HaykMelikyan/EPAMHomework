package amazonpages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BasePage {
    private String pageUrl = BASE_URL;

    private final By deliverCountryLoc = By.id("glow-ingress-block");
    private final By deliverCountryNameLoc = By.id("glow-ingress-line2");
    private final By searchTypeLoc = By.id("nav-search-dropdown-card");
    private final By dropdownOptionsLoc = By.cssSelector("select[name = 'url'] > option");
    private final By searchInputFieldLoc = By.id("twotabsearchtextbox");

    private final String searchOptionTemplate = "//option[normalize-space() = '%s']";

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get(pageUrl);
        waitUntilPageLoads();
    }

    public void waitUntilPageLoads() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(deliverCountryLoc));
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchTypeLoc));
    }

    public void selectSearchOptionByString(String type) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement searchDropdownElement = driver.findElement(searchTypeLoc);
        searchDropdownElement.click();
        wait.until(ExpectedConditions.numberOfElementsToBe(dropdownOptionsLoc, 28));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dropdownOptionsLoc));

        searchDropdownElement.findElement(By.xpath(String.format(searchOptionTemplate, type))).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("nav-search-label-id"), type));
    }

    public BookSearchResultsPage searchString(String searchText) {
        driver.findElement(searchInputFieldLoc).sendKeys(searchText + Keys.ENTER);
        return new BookSearchResultsPage(driver, searchText);
    }

    public String getDeliveryCountry() {
        WebElement deliveryCountryBlock = driver.findElement(deliverCountryLoc);
        return deliveryCountryBlock.findElement(deliverCountryNameLoc).getText();
    }
}
