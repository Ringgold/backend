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

    private String loginUsername_2 = Constant.generateUUID();

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

    @Test
    public void from_profile_page() {
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

        /* Enter Profile Page*/
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("welcome")));
        WebElement username = driver.findElement(By.id("welcome"));
        username.click();
        wait.until(ExpectedConditions.titleIs("User Profile"));

        WebElement mainPageTitle = driver.findElement(By.cssSelector("body > header > ul > li > a"));
        mainPageTitle.click();
        wait.until(ExpectedConditions.titleIs(webPageTitle));
        String pageTitle = driver.getTitle();
        assertEquals(webPageTitle, pageTitle);
    }

    @Test
    public void from_post_detail_page() {
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

        /* Add a book post */
        WebElement createPostButton = driver.findElement(By.id("post"));
        wait.until(ExpectedConditions.visibilityOf(createPostButton));
        createPostButton.click();
        wait.until(ExpectedConditions.titleIs("Sign Up"));
        driver.findElement(By.name("title")).sendKeys(bookTitle_3);
        driver.findElement(By.name("author")).sendKeys(bookAuthor_3);
        driver.findElement(By.name("code")).sendKeys(bookISBN_3);
        driver.findElement(By.name("price")).sendKeys(bookPrice_3);
        driver.findElement(By.name("description")).sendKeys(bookDescription_3);
        driver.findElement(By.id("post_btn")).click();
        wait.until(ExpectedConditions.titleIs("Book Trader"));

        WebElement bookList = driver.findElement(By.id("bookList"));
        wait.until(ExpectedConditions.visibilityOf(bookList));
        List<WebElement> bookResult = bookList.findElements(By.cssSelector("div > header"));
        int postNumber = 0;
        for (int i = 0; i < bookResult.size(); i++) {
            if (bookResult.get(i).getText().equals(bookTitle_3)) {
                postNumber = i;
            }
            break;
        }
        bookResult.get(postNumber).findElement(By.tagName("a")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("book_title"))));

        WebElement mainPageTitle = driver.findElement(By.cssSelector("body > header > ul > li > a"));
        mainPageTitle.click();
        wait.until(ExpectedConditions.titleIs(webPageTitle));
        String pageTitle = driver.getTitle();
        assertEquals(webPageTitle, pageTitle);
    }

    @Test
    public void from_contact_page() {
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

        /* Add a book post */
        WebElement createPostButton = driver.findElement(By.id("post"));
        wait.until(ExpectedConditions.visibilityOf(createPostButton));
        createPostButton.click();
        wait.until(ExpectedConditions.titleIs("Sign Up"));
        driver.findElement(By.name("title")).sendKeys(bookTitle_4);
        driver.findElement(By.name("author")).sendKeys(bookAuthor_4);
        driver.findElement(By.name("code")).sendKeys(bookISBN_4);
        driver.findElement(By.name("price")).sendKeys(bookPrice_4);
        driver.findElement(By.name("description")).sendKeys(bookDescription_4);
        driver.findElement(By.id("post_btn")).click();
        wait.until(ExpectedConditions.titleIs("Book Trader"));

        /* Seller logouts */
        WebElement logoutButton = driver.findElement(By.id("logout"));
        logoutButton.click();
        wait.until(ExpectedConditions.titleIs("Book Trader"));

        /* Test User Account Registration */
        signUpButton = driver.findElement(By.id("sign_up"));
        wait.until(ExpectedConditions.visibilityOf(signUpButton));
        signUpButton.click();
        wait.until(ExpectedConditions.titleContains("Sign Up"));
        email = driver.findElement(By.name("email"));
        email.sendKeys(loginUsername_2 + "@test.test");
        name = driver.findElement(By.name("name"));
        name.sendKeys(loginUsername_2);
        password = driver.findElement(By.name("password"));
        password.sendKeys(loginPassword);
        createAccountButton = driver.findElement(By.cssSelector("body > div > form > button"));
        createAccountButton.click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("message_ok"))));
        returnHomePageButton = driver.findElement(By.id("return-home-button"));
        returnHomePageButton.click();
        wait.until(ExpectedConditions.titleIs("Book Trader"));

        /* Login as test user */
        signInButton = driver.findElement(By.id("sign_in"));
        wait.until(ExpectedConditions.visibilityOf(signInButton));
        signInButton.click();
        wait.until(ExpectedConditions.titleContains("Sign In"));
        email = driver.findElement(By.name("email"));
        email.sendKeys(loginUsername + "@test.test");
        password = driver.findElement(By.name("password"));
        password.sendKeys(loginPassword);
        loginButton = driver.findElement(By.cssSelector("body > div > form > button"));
        loginButton.click();

        WebElement bookList = driver.findElement(By.id("bookList"));
        wait.until(ExpectedConditions.visibilityOf(bookList));
        List<WebElement> bookResult = bookList.findElements(By.cssSelector("div > header"));
        int postNumber = 0;
        for (int i = 0; i < bookResult.size(); i++) {
            if (bookResult.get(i).getText().equals(bookTitle_4)) {
                postNumber = i;
            }
            break;
        }
        bookResult.get(postNumber).findElement(By.tagName("a")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("book_title"))));

        /* Contact seller */
        WebElement contactButton = driver.findElement(By.id("contact_seller"));
        wait.until(ExpectedConditions.elementToBeClickable(contactButton));
        contactButton.click();
        WebElement contactPageMessage = driver.findElement(By.cssSelector("body > div > div > h2"));
        wait.until(ExpectedConditions.textToBePresentInElement(contactPageMessage, "Send seller an email"));

        WebElement mainPageTitle = driver.findElement(By.cssSelector("body > header > ul > li > a"));
        mainPageTitle.click();
        wait.until(ExpectedConditions.titleIs(webPageTitle));
        String pageTitle = driver.getTitle();
        assertEquals(webPageTitle, pageTitle);
    }
}