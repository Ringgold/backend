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

import static junit.framework.TestCase.assertEquals;

public class ProfileTest{
    private WebDriver driver;
    private WebDriverWait wait;

    /* test user account information */
    private String loginUsername = Constant.generateUUID();
    private String loginPassword = "password";

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);

        driver.navigate().to("https://www.silentdoor.net");

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
    }

    @After
    public void teardown() {
        driver.close();
    }

    @Test
    public void valid_about_me_test() {
        driver.findElement(By.id("about-me-edit")).click();

        WebDriverWait wait2 = new WebDriverWait(driver, 10);
        wait2.until(ExpectedConditions.elementToBeClickable(By.name("about_me")));
        driver.findElement(By.name("about_me")).sendKeys("I am a good seller");
        driver.findElement(By.id("save-aboutme")).click();

        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        WebElement aboutMe = driver.findElement(By.id("about_me"));
        wait.until(ExpectedConditions.visibilityOf(aboutMe));
        String about_me = aboutMe.getText();
        assertEquals(about_me, "I am a good seller");
    }

    @Test
    public void invalid_about_me_test() throws Exception {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("about-me-edit")));
        driver.findElement(By.id("about-me-edit")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.name("about_me")));
        driver.findElement(By.id("save-aboutme")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("about_me")));
        WebElement webElement = driver.findElement(By.id("about_me"));
        String about_me = webElement.getText();
        assertEquals("who am I?", about_me);
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
        phoneInput.sendKeys("1234567890");
        WebElement saveButton = driver.findElement(By.id("save-phone"));
        wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        saveButton.click();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        WebElement phoneNumber = driver.findElement(By.id("phone_number"));
        wait.until(ExpectedConditions.visibilityOf(phoneNumber));
        String currentPhoneNumber = phoneNumber.getText();
        assertEquals("1234567890", currentPhoneNumber);
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
        wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        saveButton.click();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        phoneNumber = driver.findElement(By.id("phone_number"));
        wait.until(ExpectedConditions.visibilityOf(phoneNumber));
        String currentPhoneNumber = phoneNumber.getText();
        assertEquals(currentPhoneNumber, oldPhoneNumber);
    }

}
