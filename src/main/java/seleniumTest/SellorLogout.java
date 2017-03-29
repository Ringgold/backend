package seleniumTest;

import com.google.gson.Gson;
import junit.framework.Assert;
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
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import template.Constant;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by User on 3/29/2017.
 */
public class SellorLogout {
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

    private void login(){
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
    }

    private void logout(){
        driver.navigate().to(HOSTNAME);
        WebElement webElement = driver.findElement(By.cssSelector("body > header > ul > li:nth-child(1) > a"));
        wait.until(ExpectedConditions.visibilityOf(webElement));
        //logout
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#logout")));
        driver.findElement(By.cssSelector("#logout")).click();
    }

    @Test
    public void testLogout(){
        login();
        logout();
        driver.navigate().to(HOSTNAME);
        WebElement webElement = driver.findElement(By.cssSelector("body > header > ul > li:nth-child(1) > a"));
        wait.until(ExpectedConditions.visibilityOf(webElement));
        //check that sign_in is available
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#sign_in")));
    }
}
