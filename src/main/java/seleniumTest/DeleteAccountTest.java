
 package seleniumTest;

 import org.junit.*;
 import org.openqa.selenium.*;
 import org.openqa.selenium.chrome.ChromeDriver;
 import org.openqa.selenium.support.ui.ExpectedCondition;
 import org.openqa.selenium.support.ui.ExpectedConditions;
 import org.openqa.selenium.support.ui.WebDriverWait;
 import template.Constant;

 import java.util.List;

 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertTrue;

 /**
 * Created by Vivien on 12/03/2017.
 */
public class DeleteAccountTest {

    private WebDriver driver;
    private WebDriverWait wait;

     private static String USER_ID = Constant.generateUUID();
     private static String HOSTNAME = "http://localhost:9000/";
     private static String pwd = "password";

     @BeforeClass
     public static void setUpBeforeClass() throws Exception {
         System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
         WebDriver d = new ChromeDriver();
         WebDriverWait w = new WebDriverWait(d, 5);
         d.navigate().to(HOSTNAME + "sign_up");
         WebElement webElement = d.findElement(By.name("email"));
         webElement.sendKeys(USER_ID + "@test.test");
         webElement = d.findElement(By.name("name"));
         webElement.sendKeys(USER_ID);
         webElement = d.findElement(By.name("password"));
         webElement.sendKeys(pwd);
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
     }

     private void loginSuccessful(boolean suc){
         login();
         if(suc){
             wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search_Form")));
             assertEquals(driver.getCurrentUrl(), HOSTNAME);
         }else{
             wait.until(ExpectedConditions.alertIsPresent());
             Alert alert = driver.switchTo().alert();
             assertTrue(alert.getText().contains("please try again"));
             alert.accept();
         }
     }



     private void createPost(){
         //make a post
         wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#post")));
         driver.findElement(By.cssSelector("#post")).click();
         wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#post_book")));
         WebElement formElement = driver.findElement(By.cssSelector("#post_book"));
         List<WebElement> bookFields = formElement.findElements(By.xpath("*"));
         bookFields.get(0).sendKeys("Test Book_delete_test_" + USER_ID);
         bookFields.get(1).sendKeys("Test Author_delete_test_" + USER_ID);
         bookFields.get(2).sendKeys("100");
         bookFields.get(3).sendKeys("101");
         bookFields.get(4).sendKeys("The best book_delete_test_" + USER_ID);
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
         //driver.navigate().to("http://localhost:9000/");
         loginSuccessful(true);
         createPost();
         goToProfilePage();
         //click the delete account button
         wait.until(ExpectedConditions.elementToBeClickable(By.id("delete-account-confirmation-button")));
         driver.findElement(By.id("delete-account-confirmation-button")).click();

         wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("modal-account-deletion-confirmation"))));
         driver.findElement(By.id("delete-account-button")).click();

         wait.until(ExpectedConditions.alertIsPresent());
         Alert alert = driver.switchTo().alert();
         alert.accept();

         wait.until(ExpectedConditions.urlToBe(HOSTNAME));
         loginSuccessful(false);
     }

}
