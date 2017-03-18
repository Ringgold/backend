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
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class SearchTest{
    private WebDriver driver;
    private WebDriverWait wait;

    /* test user account information */
    private String loginUsername = Constant.generateUUID();
    private String loginPassword = "password";

    private String bookAuthor = "Stieg Larsson";
    private String bookTitle_1 = "The Girl with the Dragon Tattoo";
    private String bookISBN_1 = "0307949486";
    private String bookPrice_1 = "8.50";
    private String bookDescription_1 = "Great book, excellent condition";

    private String bookTitle_2 = "The Girl Who Played with Fire";
    private String bookISBN_2 = "0307949508";
    private String bookPrice_2 = "9.00";
    private String bookDescription_2 = "Good Condition";

    private String bookTitle_3 = "The Girl Who Kicked the Hornet's Nest";
    private String bookISBN_3 = "0307742539";
    private String bookPrice_3 = "10.25";
    private String bookDescription_3 = "Third volume of the Millennium series";

    private String bookTitle_4 = "The Girl in the Spider's Web";
    private String bookAuthor_4 = "David Lagercrantz";
    private String bookISBN_4 = "0385354282";
    private String bookPrice_4 = "13.75";
    private String bookDescription_4 = "[On Hold] Brand New";

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);

        driver.navigate().to("http://localhost:9000/");

        /* Test User Account Registration */
        WebElement signUpButton = driver.findElement(By.cssSelector("body > header > ul > li:nth-child(5) > a"));
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
        WebElement signInButton = driver.findElement(By.cssSelector("body > header > ul > li:nth-child(6) > a"));
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
    public void valid_search_by_all_categories_test() {
        /* Add book posts */
        WebElement createPostButton = driver.findElement(By.cssSelector("body > header > ul > li:nth-child(3) > a"));
        wait.until(ExpectedConditions.visibilityOf(createPostButton));
        createPostButton.click();
        wait.until(ExpectedConditions.titleIs("Sign Up"));
        driver.findElement(By.name("title")).sendKeys(bookTitle_1);
        driver.findElement(By.name("author")).sendKeys(bookAuthor);
        driver.findElement(By.name("code")).sendKeys(bookISBN_1);
        driver.findElement(By.name("price")).sendKeys(bookPrice_1);
        driver.findElement(By.name("description")).sendKeys(bookDescription_1);
        driver.findElement(By.id("post_btn")).click();
        wait.until(ExpectedConditions.titleIs("Book Trader"));

        /* Enter Search Key input */
        WebElement searchInput = driver.findElement(By.id("search_text"));
        searchInput.sendKeys("dragon tattoo");
        WebElement searchButton = driver.findElement(By.tagName("button"));
        wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        searchButton.click();
        WebElement searchList = driver.findElement(By.id("searchList"));
        wait.until(ExpectedConditions.visibilityOf(searchList));
        List<WebElement> searchResult = searchList.findElements(By.cssSelector("div > header"));

        assertEquals(1, searchResult.size());
    }

}
