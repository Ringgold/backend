package seleniumTest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import template.Constant;

import java.util.List;
import static org.junit.Assert.*;


public class ContactSellerTest {
    private static WebDriver driver;
    private static WebDriverWait wait;

    private String loginUsername = Constant.generateUUID();
    public String loginUsername2 = Constant.generateUUID();
    private String loginPassword = "password";
    private String book_title = Constant.generateUUID();


    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);

        driver.navigate().to("http://localhost:9000/");

        /* Test seller Account Registration */
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

        // Post a book for testing
        driver.findElement(By.id("post")).click();

        // Fill form
        wait.until(ExpectedConditions.elementToBeClickable((By.id("post_book"))));
        WebElement bookForm = driver.findElement(By.id("post_book"));

        List<WebElement> bookInputs = bookForm.findElements(By.xpath("*"));
        bookInputs.get(0).sendKeys(book_title);
        bookInputs.get(1).sendKeys("Farm Animal");
        bookInputs.get(2).sendKeys("123123-123123");
        bookInputs.get(3).sendKeys("10.99");
        bookInputs.get(4).sendKeys("Book Description");
        bookInputs.get(6).click();

        //logout as seller

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout")));
        WebElement logoutButton = driver.findElement(By.id("logout"));
        logoutButton.click();

        WebElement signUpButton2 = driver.findElement(By.id("sign_up"));
        wait.until(ExpectedConditions.visibilityOf(signUpButton2));
        signUpButton2.click();

        wait.until(ExpectedConditions.titleContains("Sign Up"));
        WebElement email2 = driver.findElement(By.name("email"));
        email2.sendKeys(loginUsername2 + "@test.test");
        WebElement name2 = driver.findElement(By.name("name"));
        name2.sendKeys(loginUsername2);
        WebElement password2 = driver.findElement(By.name("password"));
        password2.sendKeys(loginPassword);
        WebElement createAccountButton2 = driver.findElement(By.cssSelector("body > div > form > button"));
        createAccountButton2.click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("message_ok"))));
        WebElement returnHomePageButton2 = driver.findElement(By.id("return-home-button"));
        returnHomePageButton2.click();
        wait.until(ExpectedConditions.titleIs("Book Trader"));

        /* Login as test user */
        WebElement signInButton2 = driver.findElement(By.id("sign_in"));
        wait.until(ExpectedConditions.visibilityOf(signInButton2));
        signInButton2.click();
        wait.until(ExpectedConditions.titleContains("Sign In"));
        email2 = driver.findElement(By.name("email"));
        email2.sendKeys(loginUsername2 + "@test.test");
        password2 = driver.findElement(By.name("password"));
        password2.sendKeys(loginPassword);
        WebElement loginButton2= driver.findElement(By.cssSelector("body > div > form > button"));
        loginButton2.click();
    }
    @After
    public void teardown() {
       driver.close();
    }

    @Test
    public void valid_contactSeller_test1() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("contact")));
        driver.findElement(By.name("contact")).click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("content")));
        WebElement content = driver.findElement(By.id("content"));
        content.click();
        content.sendKeys("Some message");
        driver.findElement(By.id("sendButton")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        String alertMessage = driver.switchTo().alert().getText();
        assertEquals("success",alertMessage);
    }

    @Test
    public void valid_contactSeller_test2() {
        goToBookPage();

        WebElement webElement = driver.findElement(By.id("contact_seller"));
        wait.until(ExpectedConditions.visibilityOf(webElement));
        webElement.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("content")));
        WebElement content = driver.findElement(By.id("content"));
        content.click();
        content.sendKeys("Some message");
        driver.findElement(By.id("sendButton")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        String alertMessage = driver.switchTo().alert().getText();

        assertEquals("success", alertMessage);
    }


    @Test
    public void invalid_contactSeller_test1() {
        String message = "something";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("contact")));
        driver.findElement(By.name("contact")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sendButton")));
        driver.findElement(By.id("sendButton")).click();

        try{
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
        }
        catch (Exception e){
            message = "alert Present";
        }
        assertEquals("alert Present", message);

    }

    @Test
    public void invalid_contactSeller_test2() {
        String message = "something";
        goToBookPage();

        WebElement webElement = driver.findElement(By.id("contact_seller"));
        wait.until(ExpectedConditions.visibilityOf(webElement));
        webElement.click();
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sendButton")));
        driver.findElement(By.id("sendButton")).click();

        try{
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
        }
        catch (Exception e){
            message = "No alert Present";
        }

        assertEquals("No alert Present", message);

    }

    private void goToBookPage() {
        // Find the book
        driver.navigate().to("http://localhost:9000");

//        WebElement signInButton = driver.findElement(By.id("sign_in"));
//        wait.until(ExpectedConditions.visibilityOf(signInButton));
//        signInButton.click();
//        wait.until(ExpectedConditions.titleContains("Sign In"));
//        WebElement emailReLogin = driver.findElement(By.name("email"));
//        emailReLogin.sendKeys(loginUsername2 + "@test.test");
//        WebElement passwordRelogin = driver.findElement(By.name("password"));
//        passwordRelogin.sendKeys("password");
//        WebElement loginButton2= driver.findElement(By.cssSelector("body > div > form > button"));
//        loginButton2.click();

        WebElement webElement = driver.findElement(By.cssSelector("body > header > ul > li:nth-child(1) > a"));
        wait.until(ExpectedConditions.visibilityOf(webElement));
        wait.until(ExpectedConditions.elementToBeClickable(By.tagName("header")));

        List<WebElement> books = driver.findElements(By.tagName("header"));
        WebElement testBook = null;

        for (WebElement book : books) {
            if (book.getText().contains(book_title.toUpperCase())) {
                testBook = book;
            }
        }

        if (testBook == null) {
            Assert.fail();
        }

        testBook.findElement(By.tagName("a")).click();
    }


}
