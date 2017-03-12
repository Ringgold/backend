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

import static junit.framework.TestCase.assertEquals;

public class ProfileTest{
    private WebDriver driver;
    private WebDriverWait wait;

    /* test user account information */
    private String loginUsername = "test";
    private String loginPassword = "password";

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);

        driver.navigate().to("http://localhost:9000/");

        /* Login as test user */
        WebElement signInButton = driver.findElement(By.cssSelector("body > header > ul > li:nth-child(6) > a"));
        wait.until(ExpectedConditions.visibilityOf(signInButton));
        signInButton.click();
        wait.until(ExpectedConditions.titleContains("Sign In"));
        WebElement email = driver.findElement(By.name("email"));
        email.sendKeys(loginUsername + "@test.test");
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys(loginPassword);
        WebElement loginButton = driver.findElement(By.cssSelector("body > div > form > button"));
        loginButton.click();

        /* Enter Profile Page*/
        WebElement username = driver.findElement(By.id("welcome"));
        username.click();
        wait.until(ExpectedConditions.titleIs("User Profile"));
    }

    @After
    public void teardown() {
        driver.close();
        driver.quit();
    }

    @Test
    public void valid_about_me_test() {
        driver.findElement(By.id("about-me-edit")).click();

        WebDriverWait wait2 = new WebDriverWait(driver, 10);
        wait2.until(ExpectedConditions.elementToBeClickable(By.name("about_me")));
        driver.findElement(By.name("about_me")).sendKeys("I am a good seller");
        driver.findElement(By.id("save-aboutme")).click();

        WebElement webElement = driver.findElement(By.id("about_me"));
        String about_me = webElement.getText();
        assertEquals(about_me, "I am a good seller");
    }

    @Test
    public void invalid_about_me_test(){
        driver.findElement(By.id("about-me-edit")).click();

        WebDriverWait wait2 = new WebDriverWait(driver, 10);
        wait2.until(ExpectedConditions.elementToBeClickable(By.name("about_me")));
        driver.findElement(By.id("save-aboutme")).click();

        WebElement webElement = driver.findElement(By.id("about_me"));
        String about_me = webElement.getText();
        assertEquals("I am a good seller", about_me);
    }

    @Test
    public void valid_phone_number_test() {
        /* Edit Phone Number */
        WebElement phoneEdit = driver.findElement(By.id("phone-edit"));
        phoneEdit.click();
        WebElement modalPhoneEdit = driver.findElement(By.id("modal-phone-edit"));
        wait.until(ExpectedConditions.visibilityOf(modalPhoneEdit));
        WebElement phoneInput = driver.findElement(By.name("phone"));
        // New 10-digit phone number as a valid input
        phoneInput.sendKeys("0987654321");
        WebElement saveButton = driver.findElement(By.id("save-phone"));
        saveButton.click();
        WebElement phoneNumber = driver.findElement(By.id("phone_number"));
        wait.until(ExpectedConditions.visibilityOf(phoneNumber));
        String currentPhoneNumber = phoneNumber.getText();
        assertEquals(currentPhoneNumber, "0987654321");
    }

    @Test
    public void invalid_phone_number_test() {
        /* Edit Phone Number */
        WebElement phoneNumber = driver.findElement(By.id("phone_number"));
        wait.until(ExpectedConditions.visibilityOf(phoneNumber));
        String oldPhoneNumber = phoneNumber.getText();
        WebElement phoneEdit = driver.findElement(By.id("phone-edit"));
        phoneEdit.click();
        WebElement modalPhoneEdit = driver.findElement(By.id("modal-phone-edit"));
        wait.until(ExpectedConditions.visibilityOf(modalPhoneEdit));
        WebElement phoneInput = driver.findElement(By.name("phone"));
        // Empty phone number as an invalid input
        phoneInput.sendKeys("");
        WebElement saveButton = driver.findElement(By.id("save-phone"));
        saveButton.click();
        phoneNumber = driver.findElement(By.id("phone_number"));
        wait.until(ExpectedConditions.visibilityOf(phoneNumber));
        String currentPhoneNumber = phoneNumber.getText();
        assertEquals(currentPhoneNumber, oldPhoneNumber);
    }

}
