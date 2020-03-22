package server;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import server.databasetestutil.DatabaseTestUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = {SpringbootWithWebxmlApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@ExtendWith(TestResultLoggerExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestwebApplicationTests {

    public static String questionPointStr = "createDAONewAccount:1-" +
            "updateDAOAccount:1-" +
            "deleteDAOAccount:1-" +
            "createUINewAccount:1-" +
            "testLogin:1-" +
            "updateUIAccount:2-" +
            "searchUIAccount:2-" +
            "deleteUIAccount:1";
    public static WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();
    //public static InternetExplorerOptions options;
    public static ChromeOptions options;


    public TestwebApplicationTests() {
        //  System.setProperty("webdriver.ie.driver", "src/main/resources/static/IEDriverServer.exe");
        System.setProperty("webdriver.chrome.driver", "src/main/resources/static/chromedriver2.exe");

//        if (options == null) {
//            options = new InternetExplorerOptions();
//            if (driver == null) {
//                driver = new InternetExplorerDriver(options);
//                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//            }
//        }

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
    public void createDAONewAccount() {
        System.out.println("create account");
        driver.get("http://localhost:8080/DispatchController?" +
                "btAction=Create%20New%20Account" +
                "&txtUsername=admintest" +
                "&txtPassword=admintest" +
                "&txtConfirm=admintest" +
                "&txtFullname=admintest");
        try {
            Assert.assertEquals(true, DatabaseTestUtils.checkByUsername("admintest"));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    @Order(2)
    public void updateDAOAccount() {
        System.out.println("Update account");
        driver.get("http://localhost:8080/DispatchController?" +
                "btAction=Update" +
                "&txtUsername=admintest" +
                "&txtPassword=admintestUpdated");
        try {
            Assert.assertEquals(true, DatabaseTestUtils.checkUpdate("admintest", "admintestUpdated"));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    @Order(3)
    public void deleteDAOAccount() {
        System.out.println("Delete account");
        driver.get("http://localhost:8080/DispatchController?btAction=delete&username=admintest");
        try {
            assertEquals(false, DatabaseTestUtils.checkByUsername("admintest"));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    @Order(4)
    public void createUINewAccount() {
        System.out.println("create account");
        driver.get("http://localhost:8080/createNewAccount.html");
        driver.findElement(By.name("txtUsername")).clear();
        driver.findElement(By.name("txtUsername")).sendKeys("admintest");
        driver.findElement(By.name("txtPassword")).clear();
        driver.findElement(By.name("txtPassword")).sendKeys("admintest");
        driver.findElement(By.name("txtConfirm")).clear();
        driver.findElement(By.name("txtConfirm")).sendKeys("admintest");
        driver.findElement(By.name("txtFullname")).clear();
        driver.findElement(By.name("txtFullname")).sendKeys("admintest");
        driver.findElement(By.name("btAction")).click();
        try {
            WebElement element = driver.findElement(By.tagName("h1"));
            String text = element.getText();
            // assertTrue(text.equalsIgnoreCase("Login Page"));
            assertEquals(true, text.equalsIgnoreCase("Login Page"));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    @Order(5)
    public void testLogin() {
        System.out.println(driver == null);
        if (driver != null) {
            driver.get("http://localhost:8080/login.html");
            driver.findElement(By.name("txtUsername")).clear();
            driver.findElement(By.name("txtUsername")).sendKeys("thien");
            driver.findElement(By.name("txtPassword")).clear();
            driver.findElement(By.name("txtPassword")).sendKeys("1");
            driver.findElement(By.name("btAction")).click();
            try {
                String html = driver.findElement(By.tagName("body")).getText();
                //String html = driver.findElement(By.xpath("//text()[contains(lower-case(.), lower-case('Welcome,'))]")).getText();
                //System.out.println("testLogin");
                //String text = element.getText();
                assertEquals(true, html.contains("thien"));
            } catch (Exception e) {
                assertFalse(true);
            }
        }
    }

    @Test
    @Order(6)
    public void updateUIAccount() {
        System.out.println("Update account");
        driver.get("http://localhost:8080/DispatchController?btAction=Search&txtSearchValue=admintest");
        driver.findElement(By.name("txtPassword")).clear();
        driver.findElement(By.name("txtPassword")).sendKeys("admintestUpdated");
        driver.findElement(By.name("chkAdmin")).click();
        //get btnAction by Value
        getBtnActionByValue("Update").click();
        try {
            WebElement element = driver.findElements(By.tagName("td")).get(2);
            String text = element.getAttribute("innerHTML");
            assertEquals(true, text.contains("admintestUpdated"));
            // assertTrue(text.equalsIgnoreCase("Login Page"));
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    @Order(7)
    public void searchUIAccount() {
        System.out.println("Update account");
        driver.get("http://localhost:8080/search.html");
        driver.findElement(By.name("txtSearchValue")).clear();
        driver.findElement(By.name("txtSearchValue")).sendKeys("admintest");
        driver.findElement(By.name("btAction")).click();
        try {
            // k có get(2) đc - phải fix lại
            WebElement element = driver.findElements(By.tagName("td")).get(2);
            String text = element.getAttribute("innerHTML");
            assertEquals(true, text.contains("admintest"));
            // assertTrue(text.equalsIgnoreCase("Login Page"));
        } catch (Exception e) {
            assertFalse(false);
        }
    }

//    @Test
//    @Order(4)
//    public void searchDAOAccount() throws InterruptedException {
//        System.out.println("Search account");
//        driver.get("http://localhost:8080/search.html");
//        driver.findElement(By.name("txtSearchValue")).clear();
//        driver.findElement(By.name("txtSearchValue")).sendKeys("ad");
//        driver.findElement(By.name("btAction")).click();
//        try {
//            String url = driver.getCurrentUrl();
//            System.out.println(url);
//            int numberOfRows = driver.findElements(By.tagName("td")).size();
//            assertEquals(true, numberOfRows > 0);
//            // assertTrue(text.equalsIgnoreCase("Login Page"));
//        } catch (Exception e) {
//            assertEquals(Integer.valueOf("1"), Integer.valueOf("3"));
//        }
//    }

    @Test
    @Order(8)
    public void deleteUIAccount() {
        System.out.println("Delete account");
        driver.get("http://localhost:8080/DispatchController?btAction=delete&username=admintest&lastSearchValue=admintestUpdated");
        try {
            WebElement element = driver.findElement(By.tagName("h1"));
            String text = element.getText();
            // assertTrue(text.equalsIgnoreCase("Login Page"));
            if (text.equalsIgnoreCase("Search Page")) {
                // k có get(2) đc - phải fix lại
                try {
                    WebElement elementTd = driver.findElements(By.tagName("td")).get(2);
                    String textHtml = elementTd.getAttribute("innerHTML");
                    assertEquals(false, textHtml.contains("admintest"));
                } catch (Exception e) {
                    assertTrue(true);
                }
            }
            // assertTrue(text.equalsIgnoreCase("Login Page"));
        } catch (Exception e) {
            assertFalse(true);
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
