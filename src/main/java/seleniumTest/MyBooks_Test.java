package seleniumTest;
import api.BookApi;
import api.UserApi;
import com.google.gson.Gson;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import template.Book.BookDao;
import template.Constant;
import template.Profile.ProfileDao;
import template.User.User;
import template.User.UserDao;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class MyBooks_Test{
    private WebDriver driver = null;
    private WebDriverWait wait = null;
    private static String USER_ID = Constant.generateUUID();
    private static String HOSTNAME = "http://localhost:9000/";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        DBI dbi = new DBI("jdbc:mysql://localhost/booktrader?useUnicode=true&characterEncoding=UTF-8&useSSL=false", "root", "delivery");
        Gson gson = new Gson();
        Handle handle = dbi.open();
        handle.execute("DELETE FROM book");
        handle.execute("DELETE FROM user");
        handle.execute("DELETE FROM profile");
        handle.close();

        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver d = new ChromeDriver();
        WebDriverWait w = new WebDriverWait(d, 5);
        d.navigate().to(HOSTNAME + "sign_up");
        WebElement webElement = d.findElement(By.name("email"));
        webElement.sendKeys(USER_ID + "@test.test");
        webElement = d.findElement(By.name("name"));
        webElement.sendKeys(USER_ID);
        webElement = d.findElement(By.name("password"));
        webElement.sendKeys("password");
        webElement = d.findElement(By.id("create-account-button"));
        webElement.click();
        d.close();
    }

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);
    }

    @After
    public void teardown() {
        driver.close();
        driver.quit();
    }


    private void loginAndCreatePost(){
        driver.navigate().to(HOSTNAME);
        WebElement webElement = driver.findElement(By.cssSelector("body > header > ul > li:nth-child(1) > a"));
        wait.until(ExpectedConditions.visibilityOf(webElement));
        //sign in
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#sign_in")));
        driver.findElement(By.cssSelector("#sign_in")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#login_form")));
        WebElement formElement = driver.findElement(By.cssSelector("#login_form"));
        List<WebElement> loginFields = formElement.findElements(By.xpath("*"));
        loginFields.get(0).sendKeys(USER_ID + "@test.test");
        loginFields.get(1).sendKeys("password");
        loginFields.get(2).click();

        //make a post
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#post")));
        driver.findElement(By.cssSelector("#post")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#post_book")));
        formElement = driver.findElement(By.cssSelector("#post_book"));
        List<WebElement> bookFields = formElement.findElements(By.xpath("*"));
        bookFields.get(0).sendKeys(USER_ID);
        bookFields.get(1).sendKeys("Test Author");
        bookFields.get(2).sendKeys("1234");
        bookFields.get(3).sendKeys("99.99");
        bookFields.get(4).sendKeys("The best book");
        driver.findElement(By.cssSelector("#post_btn")).click();
    }
    private void goToProfilePage(){
        //go to profile page
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#welcome")));
        driver.findElement(By.cssSelector("#welcome")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#my_books")));
        driver.findElement(By.cssSelector("#my_books")).click();
    }

    @Test
    public void NormalScenario_S1(){
        loginAndCreatePost();
        driver.navigate().to(HOSTNAME);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#bookList")));
        WebElement bookWrapper = driver.findElement(By.cssSelector("#bookList"));
        List<WebElement> bookList = bookWrapper.findElements(By.xpath("*"));
        WebElement firstBook = bookWrapper.findElements(By.xpath("*")).get(0);
        WebElement bookTitle = firstBook.findElement(By.cssSelector("div > header > ul > li > a"));
        Assert.assertEquals(USER_ID.toLowerCase(), bookTitle.getText().toLowerCase());
    }
    @Test
    public void AlternateScenario_S1(){
        driver.navigate().to("https://www.google.ca");
        NormalScenario_S1();
    }

    @Test
    public void NormalScenario_S39(){
        loginAndCreatePost();
        goToProfilePage();
        //go to book list
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#bookList")));
        WebElement bookWrapper = driver.findElement(By.cssSelector("#bookList"));
        List<WebElement> bookList = bookWrapper.findElements(By.xpath("*"));
        //check the first book to match what is posted
        WebElement firstBook = bookWrapper.findElements(By.xpath("*")).get(0);
        WebElement bookTitle = firstBook.findElement(By.cssSelector("div > header > ul > li > a"));
        Assert.assertEquals(USER_ID.toLowerCase(), bookTitle.getText().toLowerCase());
        int curSize = bookList.size();
        WebElement bookHeader = firstBook.findElement(By.cssSelector("div > header > ul"));
        bookHeader.findElement(By.cssSelector("li[id^='delete']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#confirm-deletion")));
        WebElement confirmDelete = driver.findElement(By.id("confirm-deletion"));
        boolean clicked = false;
        while(!clicked){ // NEED this loop or else sometimes selenium will fail to click due to some weird bug...
            try {
                confirmDelete.click();
            } catch (WebDriverException e) {
                continue;
            }
            clicked = true;
        }
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("#bookList > *"),curSize - 1));
    }

    @Test
    public void AlternateScenario_S39(){
        //driver.navigate().to("https://silentdoor.net");
        loginAndCreatePost();
        goToProfilePage();
        //go to book list
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#bookList")));
        WebElement bookWrapper = driver.findElement(By.cssSelector("#bookList"));
        List<WebElement> bookList = bookWrapper.findElements(By.xpath("*"));
        //check the first book to match what is posted
        WebElement firstBook = bookWrapper.findElements(By.xpath("*")).get(0);
        WebElement bookTitle = firstBook.findElement(By.cssSelector("div > header > ul > li > a"));
        Assert.assertEquals(USER_ID.toLowerCase(), bookTitle.getText().toLowerCase());
        //see details page of the first book
        bookTitle.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#title")));
        Assert.assertEquals(USER_ID.toLowerCase(), driver.findElement(By.cssSelector("#title")).getText().toLowerCase());
    }
}
