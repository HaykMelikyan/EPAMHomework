import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Random;

public class SixpmWebsiteTest {

    private WebDriver driver;
    private WebDriverWait wait;

    /**
     * Create web driver and wait objects
     */
    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 15);
    }

    /**
     * Clear the bag and close the browser
     */
    @AfterMethod
    public void cleareBag() {
        driver.findElement(By.cssSelector("select[name = 'quantity'] > option[value = '0']")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("uz-z")));

        driver.quit();
    }

    @Test
    public void shoppingBagTest() {
        By loc;

        // open 6pm.com
        driver.get("https://www.6pm.com/");
        loc = By.cssSelector("a[href = '/c/accessories']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(loc));

        // hover accessories
        WebElement navBarAccessories = driver.findElement(loc);
        Actions builder = new Actions(driver);
        Action mouseHover = builder.moveToElement(navBarAccessories).build();
        mouseHover.perform();
        loc = By.xpath("//a[normalize-space() = 'Aviators']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(loc));

        // go to Aviators page
        driver.findElement(loc).click();
        loc = By.cssSelector("div.js-z > article");
        wait.until(ExpectedConditions.numberOfElementsToBe(loc, 100));

        // go to random aviator page
        Random random = new Random();
        int randomProductNumber = random.nextInt(100) + 1;
        driver.findElement(By.cssSelector("div.js-z > article:nth-child(" + randomProductNumber + ")")).click();
        loc = By.id("productRecap");
        wait.until(ExpectedConditions.visibilityOfElementLocated(loc));

        // obtain product expected characteristics
        //
        // obtain product expected brand and name
        WebElement productOverview = driver.findElement(By.id("overview"));
        String expectedBrand = productOverview.findElement(By.cssSelector("#overview span[itemprop = 'name']")).getText();
        String expectedModel = productOverview.findElement(By.className("eN-z")).getText();

        // obtain product expected price and MSRP
        WebElement priceBlock = driver.findElement(By.cssSelector("div.Ib-z div.rK-z"));
        String expectedPrice = priceBlock.findElement(By.className("tK-z")).getText();
        String expectedMSRP = priceBlock.findElement(By.className("DK-z")).getText();

        // obtain product expected color
        WebElement colorOption;
        try {
            // try to get selected color, if product have color picking option
            Select select = new Select(driver.findElement(By.id("pdp-color-select")));
            colorOption = select.getFirstSelectedOption();
        } catch (NoSuchElementException e) {
            colorOption = driver.findElement(By.className("CR-z"));
        }
        String expectedColor = colorOption.getText();


        // add product to the shopping bag
        driver.findElement(By.cssSelector("#buyBox button[type = 'submit']")).click();
        loc = By.className("ph-z");
        wait.until(ExpectedConditions.visibilityOfElementLocated(loc));


        // obtain product actual characteristics
        //
        // obtain the block with product info
        WebElement productInfoBlock = driver.findElement(loc);

        // obtain product actual brand and model
        WebElement productNameBlock = productInfoBlock.findElement(By.cssSelector("a[data-te = 'TE_CART_PRODUCTCLICKED']"));
        String actualBrand = productNameBlock.findElement(By.cssSelector("span:nth-child(1)")).getText();
        String actualModel = productNameBlock.findElement(By.cssSelector("span:nth-child(2)")).getText();

        // obtain product actual price and MSRP
        WebElement productPriceBlock = productInfoBlock.findElement(By.className("rh-z"));
        String actualPrice = productPriceBlock.findElement(By.tagName("em")).getText();
        String actualMSRP = productPriceBlock.findElement(By.tagName("span")).getText();
        actualMSRP = actualMSRP.replaceAll("[^0-9.$]", "");

        // obtain product actual color
        String actualColor = productInfoBlock.findElement(By.xpath("//dt[contains(text(),'Color')]/following-sibling::dd[1]")).getText();


        // check if product in the bag is one we added
        SoftAssert assertion = new SoftAssert();
        assertion.assertEquals(actualBrand, expectedBrand);
        assertion.assertEquals(actualModel, expectedModel);
        assertion.assertEquals(actualPrice, expectedPrice);
        assertion.assertEquals(actualMSRP, expectedMSRP);
        assertion.assertEquals(actualColor, expectedColor);

        assertion.assertAll();
    }
}