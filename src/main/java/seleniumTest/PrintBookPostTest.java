package seleniumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import template.Constant;

import java.util.*;

import static junit.framework.TestCase.assertEquals;

public class PrintBookPostTest {
    private WebDriver driver;
    private WebDriverWait wait;

    /* Test User Account Information */
    private String loginUsername = Constant.generateUUID();
    private String loginPassword = "password";

    /* Book Posts Information*/
    private String bookTitle = "A Criminal Defense";
    private String bookAuthor = "William L. Myers Jr.";
    private String bookISBN = "1503943429";
    private String bookPrice = "8.81";
    private String bookDescription = "New";

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);

        driver.navigate().to("http://localhost:9000/");

         /* Test User Account Registration */
        WebElement signUpButton = driver.findElement(By.id("sign_up"));
        wait.until(ExpectedConditions.visibilityOf(signUpButton));
        signUpButton.click();
        wait.until(ExpectedConditions.titleContains("Sign Up"));
        WebElement email = driver.findElement(By.name("email"));
        email.sendKeys(loginUsername + "@test.test");
        WebElement name = driver.findElement(By.name("name"));
        name.sendKeys(loginUsername);
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys(loginPassword);
        WebElement createAccountButton = driver.findElement(By.cssSelector("body > div > form > button"));
        createAccountButton.click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("message_ok"))));
        WebElement returnHomePageButton = driver.findElement(By.id("return-home-button"));
        returnHomePageButton.click();
        wait.until(ExpectedConditions.titleIs("Book Trader"));

        /* Login as test user */
        WebElement signInButton = driver.findElement(By.id("sign_in"));
        wait.until(ExpectedConditions.visibilityOf(signInButton));
        signInButton.click();
        wait.until(ExpectedConditions.titleContains("Sign In"));
        email = driver.findElement(By.name("email"));
        email.sendKeys(loginUsername + "@test.test");
        password = driver.findElement(By.name("password"));
        password.sendKeys(loginPassword);
        WebElement loginButton = driver.findElement(By.cssSelector("body > div > form > button"));
        loginButton.click();
        wait.until(ExpectedConditions.titleIs("Book Trader"));
    }

    @After
    public void teardown() {
        driver.close();
    }

    @Test
    public void print_book_post() {
        /* Add a book post */
        WebElement createPostButton = driver.findElement(By.id("post"));
        wait.until(ExpectedConditions.visibilityOf(createPostButton));
        createPostButton.click();
        wait.until(ExpectedConditions.titleIs("Sign Up"));
        driver.findElement(By.name("title")).sendKeys(bookTitle);
        driver.findElement(By.name("author")).sendKeys(bookAuthor);
        driver.findElement(By.name("code")).sendKeys(bookISBN);
        driver.findElement(By.name("price")).sendKeys(bookPrice);
        driver.findElement(By.name("description")).sendKeys(bookDescription);
        driver.findElement(By.id("post_btn")).click();
        wait.until(ExpectedConditions.titleIs("Book Trader"));

        /* Find the post */
        WebElement bookList = driver.findElement(By.id("bookList"));
        wait.until(ExpectedConditions.visibilityOf(bookList));
        List<WebElement> bookResult = bookList.findElements(By.cssSelector("div > header"));
        int postNumber = 0;
        for (int i = 0; i < bookResult.size(); i++) {
            if (bookResult.get(i).getText().equals(bookTitle)) {
                postNumber = i;
            }
            break;
        }
        bookResult.get(postNumber).findElement(By.tagName("a")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("book_title"))));

        WebElement printButton = driver.findElement(By.id("print"));
        printButton.click();
        String parentHandles = driver.getWindowHandle();
        driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString());
        WebElement printPreview = driver.findElement(By.cssSelector("html"));
        boolean result = printPreview.isDisplayed();
        System.out.println(result);
        driver.switchTo().window(parentHandles);
        assertEquals(true, result);
    }
}