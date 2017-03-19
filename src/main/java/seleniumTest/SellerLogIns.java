package seleniumTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import junit.framework.Assert;
import template.Constant;

import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SellerLogIns {
	
	 private WebDriver driver;
	 private String username;
	 private WebDriverWait wait;
	 private String password;

	    @Before
	    public void setup() {
	        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
	        driver = new ChromeDriver();
	        wait = new WebDriverWait(driver, 5);

	        String hostName = "https://www.silentdoor.net";
	        this.username = Constant.generateUUID();
	        password = "password";
	        

	        driver.navigate().to(hostName + "/sign_up");
	        WebElement webElement = driver.findElement(By.name("email"));
	        webElement.sendKeys(username + "@test.test");
	        webElement = driver.findElement(By.name("name"));
	        webElement.sendKeys(username);
	        webElement = driver.findElement(By.name("password"));
	        webElement.sendKeys(password);
	        webElement = driver.findElement(By.id("create-account-button"));
	        webElement.click();
	       

	        

	    }

	    @After
	    public void teardown() {
	        driver.close();
	    }

}
