package seleniumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import template.Constant;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

/**
 * Created by Vivien on 31/03/2017.
 */
public class DeleteBookPostTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static String USER_ID = Constant.generateUUID();
    private static String HOSTNAME = "http://localhost:9000/";
    private static String pwd = "password";

    @BeforeClass
    public static void setupBeforeClass() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver d = new ChromeDriver();
        WebDriverWait w = new WebDriverWait(d, 6);
        d.navigate().to(HOSTNAME + "sign_up");
        WebElement webElement = d.findElement(By.name("email"));
        webElement.sendKeys(USER_ID + "@test.test");
        webElement = d.findElement(By.name("name"));
        webElement.sendKeys(USER_ID);
        webElement = d.findElement(By.name("password"));
        webElement.sendKeys(pwd);
        webElement = d.findElement(By.id("create-account-button"));
        webElement.click();
        try {sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
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


    private void login() {
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
        loginFields.get(1).sendKeys(pwd);
        loginFields.get(2).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search_Form")));
    }


    private void createPost(){
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#post")));
        driver.findElement(By.cssSelector("#post")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#post_book")));
        WebElement formElement = driver.findElement(By.cssSelector("#post_book"));
        List<WebElement> bookFields = formElement.findElements(By.xpath("*"));
        bookFields.get(0).sendKeys("Test Book_delete_posts_test_" + USER_ID);
        bookFields.get(1).sendKeys("Test Author_delete_test_" + USER_ID);
        bookFields.get(2).sendKeys("454783");
        bookFields.get(3).sendKeys("130");
        bookFields.get(4).sendKeys("The best book to delete your posts_test" + USER_ID);
        driver.findElement(By.cssSelector("#post_btn")).click();
    }


    private void goToProfilePage(){
        //go to profile page
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#welcome")));
        driver.findElement(By.cssSelector("#welcome")).click();
    }

    @Test
    //Need to have the user and profile for test@test.test in the local database to run
    public void NormalScenario(){
        /*
        * Logs in
        * Create the post
        * Goes to profile page
        * Clicks on "See my book posts"
        * Delete the first book post (that was just created)
        * Makes sure no book posts are present in "My book posts"*/
        login();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        createPost();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        goToProfilePage();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //click the delete account button
        wait.until(ExpectedConditions.elementToBeClickable(By.id("my_books")));
        driver.findElement(By.id("my_books")).click();
        try {sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"bookList\"]/div/header/ul/li[2]/a"))));
        driver.findElement(By.xpath("//*[@id=\"bookList\"]/div/header/ul/li[2]/a")).click();
        try {sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("modal-confirm-deletion"))));
        driver.findElement(By.id("confirm-deletion")).click();
        try {sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("bookList"))));
        assertEquals(driver.findElements(By.xpath("//*[@id=\"bookList\"]/div/header/ul/li[2]/a")).size(), 0);
    }


    @Test
    //Need to have the user and profile for test@test.test in the local database to run
    public void ErrorScenario(){
        /*
        * Logs in
        * Goes to the main page
        * Counts the number of delete buttons
        * Makes sure there are none of them*/
        login();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //click the delete account button
        List<WebElement> deleteElements = driver.findElements(By.cssSelector("[id^=delete_]"));
        int deleteElementsSize = 0;
        for(int i=0; i< deleteElements.size(); i++){
            if(deleteElements.get(i).isDisplayed()){
                deleteElementsSize++;
            }
        }
        try {sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        assertEquals(deleteElementsSize, 0);

    }
}
