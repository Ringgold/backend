package seleniumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import template.Constant;

import static org.junit.Assert.assertTrue;

public class RateSellerTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);

        String hostName = "http://localhost:9000";
        String userName = Constant.generateUUID();
        String password = "password";
        String profileUrl;

        driver.navigate().to(hostName + "/sign_up");
        WebElement webElement = driver.findElement(By.name("email"));
        webElement.sendKeys(userName + "@test.test");
        webElement = driver.findElement(By.name("name"));
        webElement.sendKeys(userName);
        webElement = driver.findElement(By.name("password"));
        webElement.sendKeys(password);
        webElement = driver.findElement(By.id("create-account-button"));
        webElement.click();
        driver.navigate().to(hostName + "/login");
        webElement = driver.findElement(By.name("email"));
        webElement.sendKeys(userName + "@test.test");
        webElement = driver.findElement(By.name("password"));
        webElement.sendKeys(password);
        webElement = driver.findElement(By.className("w3-green"));
        webElement.click();

        //get profile page url
        webElement = driver.findElement(By.id("welcome"));
        webElement.click();
        profileUrl = driver.getCurrentUrl();

        driver.navigate().back();
        webElement = driver.findElement(By.id("logout"));
        webElement.click();

        driver.navigate().to(profileUrl);

    }

    @After
    public void teardown() {
        driver.close();
    }

    @Test
    public void normalScenario() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rating")));
        String ratingBefore = driver.findElement(By.id("rating")).getText();

        Select select = new Select(driver.findElement(By.id("rate_val")));
        select.selectByIndex(0);

        driver.findElement(By.cssSelector("a[onclick*='rateSeller']")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[onclick*='rateSeller']")));
        String ratingAfter = driver.findElement(By.id("rating")).getText();

        Boolean ratingSame = ratingBefore.equals(ratingAfter);
        assertTrue(!ratingSame);
    }

    @Test
    public void errorlScenario() {
        String ratingBefore = driver.findElement(By.id("rating")).getText();

        Select select = new Select(driver.findElement(By.id("rate_val")));
        select.selectByIndex(1);

        driver.findElement(By.cssSelector("a[onclick*='rateSeller']")).click();
        driver.switchTo().alert().dismiss();

        String ratingAfter = driver.findElement(By.id("rating")).getText();

        Boolean ratingSame = ratingBefore.equals(ratingAfter);
        assertTrue(ratingSame);
    }
}
