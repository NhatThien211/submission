package server;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = {SpringbootWithWebxmlApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@ExtendWith(TestResultLoggerExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestwebApplicationTests {

    public static String questionPointStr = "createNewAccount:2-testLogin:2-updateAccount:2-searchAccount:2-deleteAccount:2";

    public static WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();
    public static ChromeOptions options;

    public TestwebApplicationTests() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/static/chromedriver2.exe");
        if (options == null) {
            options = new ChromeOptions();
            if (driver == null) {
                driver = new ChromeDriver(options);
                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            }
        }
//        options.addArguments("--headless");
    }

    @Test
    @Order(1)
    public void createNewAccount() {
        System.out.println("create account");
        driver.get("http://localhost:8080/createNewAccount.html");
        driver.findElement(By.name("txtUsername")).clear();
        driver.findElement(By.name("txtUsername")).sendKeys("adminTest");
        driver.findElement(By.name("txtPassword")).clear();
        driver.findElement(By.name("txtPassword")).sendKeys("adminTest");
        driver.findElement(By.name("txtConfirm")).clear();
        driver.findElement(By.name("txtConfirm")).sendKeys("adminTest");
        driver.findElement(By.name("txtFullname")).clear();
        driver.findElement(By.name("txtFullname")).sendKeys("adminTest");
        driver.findElement(By.name("btAction")).click();
        try {
            WebElement element = driver.findElement(By.tagName("h1"));
            String text = element.getText();
            // assertTrue(text.equalsIgnoreCase("Login Page"));
            assertEquals(true, text.equalsIgnoreCase("Login Page"));
        } catch (Exception e) {
            assertEquals(Integer.valueOf("1"), Integer.valueOf("2"));
        }
    }


    @Test
    @Order(2)
    public void testLogin() {

        System.out.println("Vô đây rồi nha");
        System.out.println(driver == null);
        if (driver != null) {
            driver.get("http://localhost:8080/login.html");
            driver.findElement(By.name("txtUsername")).clear();
            driver.findElement(By.name("txtUsername")).sendKeys("adminTest");
            driver.findElement(By.name("txtPassword")).clear();
            driver.findElement(By.name("txtPassword")).sendKeys("adminTest");
            driver.findElement(By.name("btAction")).click();
            try {
                String html = driver.findElement(By.tagName("body")).getText();
                if (html.contains("adminTest")) {
                    assertTrue(true);
                } else {
                    assertTrue(false);
                }
            } catch (Exception e) {
                assertTrue(false);
            }
        }
    }

    @Test
    @Order(4)
    public void updateAccount() throws InterruptedException {
        System.out.println("Update account");
        driver.get("http://localhost:8080/DispatchController?btAction=Search&txtSearchValue=adminTest");
        driver.findElement(By.name("txtPassword")).clear();
        driver.findElement(By.name("txtPassword")).sendKeys("1234567");
        //get btnAction by Value
        getBtnActionByValue("Update").click();
        try {
            String html = driver.findElement(By.tagName("body")).getText();
            if (html.contains("adminTest")) {
                assertTrue(true);
            } else {
                assertTrue(false);
            }
            // assertTrue(text.equalsIgnoreCase("Login Page"));
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    @Order(3)
    public void searchAccount() throws InterruptedException {
        System.out.println("Update account");
        driver.get("http://localhost:8080/search.html");
        driver.findElement(By.name("txtSearchValue")).clear();
        driver.findElement(By.name("txtSearchValue")).sendKeys("adminTest");
        driver.findElement(By.name("btAction")).click();
        try {
            String html = driver.findElement(By.tagName("body")).getText();
            if (html.contains("adminTest")) {
                assertTrue(true);
            } else {
                assertTrue(false);
            }
            // assertTrue(text.equalsIgnoreCase("Login Page"));
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    @Order(5)
    public void deleteAccount() throws InterruptedException {
        try {
            System.out.println("Update account");
            driver.get("http://localhost:8080/DispatchController?btAction=delete&username=adminTest&lastSearchValue=adminTest");
            String html = driver.findElement(By.tagName("body")).getText();
            assertTrue(html.contains("Search Page"));
        } catch (Exception e) {
            assertTrue(false);
        }
    }


    public static WebElement getBtnActionByValue(String value) {

        List<WebElement> listElement = driver.findElements(By.name("btAction"));
        for (WebElement element : listElement) {
            if (element.getAttribute("value").equalsIgnoreCase(value)) {
                return element;
            }
        }
        return null;
    }


}
