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

public class ReturnToMainPageTest{
    private WebDriver driver;
    private WebDriverWait wait;

    /* Website Title - BookTrader */
    private String webPageTitle = "Book Trader";

    /* Test User Account Information */
    private String loginUsername = Constant.generateUUID();
    private String loginPassword = "password";

    /* Book Posts Information*/
    private String bookTitle_3 = "The Girl Who Kicked the Hornet's Nest";
    private String bookAuthor_3 = "Stieg Larsson";
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
    }

    @After
    public void teardown() {
        driver.close();
    }

    @Test
    public void from_sign_up_page() {
        /* Registration Page*/
        WebElement signUpButton = driver.findElement(By.id("sign_up"));
        wait.until(ExpectedConditions.visibilityOf(signUpButton));
        signUpButton.click();
        wait.until(ExpectedConditions.titleContains("Sign Up"));

        WebElement mainPageTitle = driver.findElement(By.cssSelector("body > header > ul > li > a"));
        mainPageTitle.click();
        wait.until(ExpectedConditions.titleIs(webPageTitle));
        String pageTitle = driver.getTitle();
        assertEquals(webPageTitle, pageTitle);
    }

    @Test
    public void from_sign_in_page() {
        /* Login Page*/
        WebElement signInButton = driver.findElement(By.id("sign_in"));
        wait.until(ExpectedConditions.visibilityOf(signInButton));
        signInButton.click();
        wait.until(ExpectedConditions.titleContains("Sign In"));

        WebElement mainPageTitle = driver.findElement(By.cssSelector("body > header > ul > li > a"));
        mainPageTitle.click();
        wait.until(ExpectedConditions.titleIs(webPageTitle));
        String pageTitle = driver.getTitle();
        assertEquals(webPageTitle, pageTitle);
    }

    @Test
    public void from_help_page() {
        /* Login Page*/
        WebElement helpButton = driver.findElement(By.id("help"));
        wait.until(ExpectedConditions.visibilityOf(helpButton));
        helpButton.click();
        wait.until(ExpectedConditions.titleContains("Help Page"));

        WebElement mainPageTitle = driver.findElement(By.cssSelector("body > header > ul > li > a"));
        mainPageTitle.click();
        wait.until(ExpectedConditions.titleIs(webPageTitle));
        String pageTitle = driver.getTitle();
        assertEquals(webPageTitle, pageTitle);
    }
}