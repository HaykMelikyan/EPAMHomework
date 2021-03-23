import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class greetzWebsiteTest {

    @Test
    public void favoritesPageTest() {
        // Set driver property, create browser instance, create waiter for that instance
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverWait waiter = new WebDriverWait(driver, 10);

        // Login to greetz.nl
        login(driver, waiter);

        // Go to baloons' page
        driver.get("https://www.greetz.nl/ballonnen/denken-aan");
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
                "g-wall-summary.wall-options__summary > div.label-small")));

        // Get first product's name and price
        WebElement productItem = driver.findElement(By.cssSelector("div.b-products-grid__item:nth-child(1)"));
        String productNameExpected = productItem.findElement(By.className("b-products-grid__item-title")).getText();
        String productPriceExpected = "€ " + productItem.findElement(By.className("b-products-grid__item-price")).getText();

        // Add product to favorites
        productItem.findElement(By.className("b-products-grid__item-action")).click();

        // Go to favorites' page
        driver.findElement(By.cssSelector("a[class = 'page-header__navigation-item item-user']")).click();
        driver.findElement(By.cssSelector("a[ui-sref ='mygreetz.favorites']")).click();
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.className("page_title")));

        // Go to the last added item's details' page
        driver.findElement(By.cssSelector("div.favorite-item:nth-child(1)")).click();
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.className("page-detail__sidebar-container")));

        // Get page's sidebar element
        WebElement sidebar = driver.findElement(By.className("page-detail__sidebar-container"));

        // Check name and price
        Assert.assertEquals(sidebar.findElement(By.tagName("h1")).getText(), productNameExpected,
                "Name doesn't match. Make sure that the first product in the list hadn't been already added to favorites");
        Assert.assertEquals(sidebar.findElement(By.className("price-block")).getText(), productPriceExpected,
                "Price doesn't match. Make sure that the first product in the list hadn't been already added to favorites");

        // Remove the product from favorites, so the test can be repeated on this product
        driver.findElement(By.className("productdetails-favorite")).click();

        // Close the browser
        driver.quit();
    }

    @Test
    public void priceAmountTest() {
        // Set driver property, create browser instance, create waiter for that instance
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverWait waiter = new WebDriverWait(driver, 10);

        // Login to greetz.nl
        login(driver, waiter);

        // Go to cards' page
        driver.get("https://www.greetz.nl/kaarten/denken-aan");
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
                "g-wall-summary.wall-options__summary > div.label-small")));

        // Go to the first product details' page
        driver.findElement(By.cssSelector("div.b-products-grid__item:nth-child(1)")).click();
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.className("page-detail__sidebar-container")));

        // Change amount
        WebElement amountField = driver.findElement(By.name("amount"));
        amountField.clear();
        int amount = 5;
        amountField.sendKeys("" + amount);

        // Obtain prices
        WebElement priceBlock = driver.findElement(By.className("page-detail__price"));
        String singlePrice = priceBlock.findElement(By.className("price-normal")).getText().substring(2);
        String totalPrice = priceBlock.findElement(By.className("price-total")).getText();
        totalPrice = totalPrice.substring(9, totalPrice.indexOf('e') - 1);

        // Replace , with .
        singlePrice = singlePrice.replace(',', '.');
        totalPrice = totalPrice.replace(',', '.');

        // Convert to float and calculate expected total price
        float totalPriceActual = Float.parseFloat(totalPrice);
        float totalPriceExpected = Float.parseFloat(singlePrice) * amount;

        //Compare prices
        Assert.assertEquals(totalPriceActual, totalPriceExpected, "Total price is wrong");

        // Close the browser
        driver.quit();
    }

    private void login(WebDriver driver, WebDriverWait waiter) {
        // Go to the greetz.nl login page
        driver.get("https://www.greetz.nl/auth/login");
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.className("inputfields")));

        // Close the popup window
        driver.findElement(By.xpath("//span[text() = 'Akkoord']")).click();

        // Fill the fields and log in
        WebElement inputFields = driver.findElement(By.className("inputfields"));
        inputFields.findElement(By.name("email")).sendKeys("vander@vaart.com");
        inputFields.findElement(By.name("password")).sendKeys("holand5654");
        driver.findElement(By.id("login-cta")).click();
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text() = 'MyGreetz']")));
    }
}