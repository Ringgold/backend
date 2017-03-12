package seleniumTest;

import com.google.common.base.Function;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class UserActivationTest {
    private WebDriver driver;
    private WebDriverWait wait;

    private final static String TEST_EMAIL = "regis123@test.test";
    private final static String TEST_ACCT = "actTest123123";

    private class User {
        public String id;
        public String email;
        public String password;
        public String name;
        public String activationCode;
        public int status;
    }

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);
    }

    @After
    public void teardown() {
        driver.quit();
    }

    @Test
    public void registerAccount() {
        driver.navigate().to("http://localhost:9000");

        // Go to register form
        WebElement webElement = driver.findElement(By.cssSelector("body > header > ul > li:nth-child(1) > a"));
        wait.until(ExpectedConditions.visibilityOf(webElement));

        wait.until(ExpectedConditions.elementToBeClickable(By.id("sign_up")));
        driver.findElement(By.id("sign_up")).click();

        // Fill out register form and click submit
        wait.until(ExpectedConditions.elementToBeClickable(By.id("register_form")));
        WebElement registerForm = driver.findElement(By.id("register_form"));

        List<WebElement> registerInputs = registerForm.findElements(By.xpath("*"));
        registerInputs.get(0).sendKeys(TEST_EMAIL);
        registerInputs.get(1).sendKeys(TEST_ACCT);
        registerInputs.get(2).sendKeys("password");
        registerInputs.get(3).click();

        // Verify OK message
        wait.until(ExpectedConditions.elementToBeClickable(By.id("message_ok")));
        Assert.assertTrue(driver.findElement(By.id("message_ok")).getText().contains("created"));
    }

    @Test
    public void activationTest() {
        driver.navigate().to("http://localhost:9000/api/user/all");
        String response = driver.findElement(By.tagName("body")).getText();

        // Find the user that we have created
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<User>>(){}.getType();
        List<User> userList = gson.fromJson(response, collectionType);

        User user = null;
        for (User u : userList) {
            if (u.email.equals(TEST_EMAIL)) {
                user = u;
            }
        }

        Assert.assertNotNull(user);

        // Go to activation page
        driver.navigate().to("http://localhost:9000/activate/?user=" + user.id + "&code=" + "null");

        // Verify OK message
        wait.until(ExpectedConditions.elementToBeClickable(By.id("message_ok")));
        Assert.assertTrue(driver.findElement(By.id("message_ok")).getText().contains("activated!"));
    }
}
