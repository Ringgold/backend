package seleniumTest;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import template.Constant;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class BookEditablesTest {
    private static WebDriver driver;
    private static WebDriverWait wait;

    private static String TEST_EMAIL;

    private final static String BOOK_TITLE = "Learn Java in 21 Days";
    private final static String[] PRICE_TESTS_NORMAL = {"1.00", "1", "3.50", "199.99", "200", "200.01"};
    private final static String[] PRICE_TESTS_ERROR = {"-10", "-0.5", "-0", "abc", "5.5.5", ""};

    @BeforeClass
    public static void setUpBeforeClass() {
        System.setProperty("webdriver.gecko.driver", "C:\\geckodriver-v0.14.0-win32\\geckodriver.exe");
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, 5);

        // Register an account
        driver.navigate().to("http://localhost:9000");

        // Go to register form
        WebElement webElement = driver.findElement(By.cssSelector("body > header > ul > li:nth-child(1) > a"));
        wait.until(ExpectedConditions.visibilityOf(webElement));

        wait.until(ExpectedConditions.elementToBeClickable(By.id("sign_up")));
        driver.findElement(By.id("sign_up")).click();

        // Fill out register form and click submit
        wait.until(ExpectedConditions.elementToBeClickable(By.id("register_form")));
        WebElement registerForm = driver.findElement(By.id("register_form"));

        TEST_EMAIL = Constant.generateUUID() + "@test.test";
        List<WebElement> registerInputs = registerForm.findElements(By.xpath("*"));
        registerInputs.get(0).sendKeys(TEST_EMAIL);
        registerInputs.get(1).sendKeys(Constant.generateUUID());
        registerInputs.get(2).sendKeys("password");
        registerInputs.get(3).click();

        // Log in
        driver.navigate().to("http://localhost:9000");
        WebElement loginButton = driver.findElement(By.cssSelector("body > header > ul > li:nth-child(1) > a"));
        wait.until(ExpectedConditions.visibilityOf(loginButton));

        wait.until(ExpectedConditions.elementToBeClickable(By.id("sign_in")));
        driver.findElement(By.id("sign_in")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("login_form")));
        WebElement loginForm = driver.findElement(By.id("login_form"));

        List<WebElement> loginInputs = loginForm.findElements(By.xpath("*"));
        loginInputs.get(0).sendKeys(TEST_EMAIL);
        loginInputs.get(1).sendKeys("password");
        loginInputs.get(2).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.tagName("header")));

        // Verify logged in
        WebElement webElement2 = driver.findElement(By.cssSelector("body > header > ul > li:nth-child(1) > a"));
        wait.until(ExpectedConditions.visibilityOf(webElement2));

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#welcome_pre > a:nth-child(1)")));
        assertTrue(driver.findElement(By.cssSelector("#welcome_pre > a:nth-child(1)")).getText().equals("Welcome"));

        // Post a book for testing
        driver.findElement(By.id("post")).click();

        // Fill form
        wait.until(ExpectedConditions.elementToBeClickable((By.id("post_book"))));
        WebElement bookForm = driver.findElement(By.id("post_book"));

        List<WebElement> bookInputs = bookForm.findElements(By.xpath("*"));
        bookInputs.get(0).sendKeys(BOOK_TITLE);
        bookInputs.get(1).sendKeys("Farm Animal");
        bookInputs.get(2).sendKeys("123123-123123");
        bookInputs.get(3).sendKeys("10.99");
        bookInputs.get(4).sendKeys("Book Description");
        bookInputs.get(6).click();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        driver.quit();
    }

    @Test
    public void editPriceTest() {
        goToBookPage();

        for (String testPrice : PRICE_TESTS_NORMAL) {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("price-edit")));
            WebElement priceEditButton = driver.findElement(By.id("price-edit"));
            priceEditButton.click();

            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#modal-price-edit > div:nth-child(1) > div:nth-child(2) > p:nth-child(1) > input:nth-child(1)")));
            WebElement priceInput = driver.findElement(By.cssSelector("#modal-price-edit > div:nth-child(1) > div:nth-child(2) > p:nth-child(1) > input:nth-child(1)"));
            WebElement savePriceButton = driver.findElement(By.id("save-price"));

            priceInput.clear();

            priceInput.sendKeys(testPrice);
            savePriceButton.click();

            wait.until(ExpectedConditions.elementToBeClickable(By.tagName("header")));
            wait.until(ExpectedConditions.elementToBeClickable(By.id("price")));
            WebElement priceElement = driver.findElement(By.id("price"));
            wait.until(ExpectedConditions.visibilityOf(priceElement));

            String expectedPrice = testPrice.length() < 4 ? testPrice + ".00" : testPrice.charAt(testPrice.length() - 3) == '.' ? testPrice : testPrice + ".00";

            assertTrue(priceElement.getText().equals(expectedPrice));
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.id("price")));
        WebElement priceElement = driver.findElement(By.id("price"));

        for (String testPrice : PRICE_TESTS_ERROR) {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("price-edit")));
            WebElement priceEditButton = driver.findElement(By.id("price-edit"));
            priceEditButton.click();

            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#modal-price-edit > div:nth-child(1) > div:nth-child(2) > p:nth-child(1) > input:nth-child(1)")));
            WebElement priceInput = driver.findElement(By.cssSelector("#modal-price-edit > div:nth-child(1) > div:nth-child(2) > p:nth-child(1) > input:nth-child(1)"));
            WebElement savePriceButton = driver.findElement(By.id("save-price"));

            priceInput.clear();

            priceInput.sendKeys(testPrice);
            savePriceButton.click();

            if (!testPrice.equals("")) {
                Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                alert.accept();
            }

            wait.until(ExpectedConditions.visibilityOf(priceElement));

            assertTrue(priceElement.getText().equals(PRICE_TESTS_NORMAL[PRICE_TESTS_NORMAL.length - 1]));
        }
    }

    @Test
    public void editDescriptionTest() {
        goToBookPage();
    }

    private void goToBookPage() {
        // Find the book
        driver.navigate().to("http://localhost:9000");

        WebElement webElement = driver.findElement(By.cssSelector("body > header > ul > li:nth-child(1) > a"));
        wait.until(ExpectedConditions.visibilityOf(webElement));
        wait.until(ExpectedConditions.elementToBeClickable(By.tagName("header")));

        List<WebElement> books = driver.findElements(By.tagName("header"));
        WebElement testBook = null;

        for (WebElement book : books) {
            if (book.getText().contains(BOOK_TITLE.toUpperCase())) {
                testBook = book;
            }
        }

        if (testBook == null) {
            Assert.fail();
        }

        testBook.findElement(By.tagName("a")).click();
    }
}
