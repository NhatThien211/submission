package server;

import com.practicalexam.student.TemplateQuestion;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
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
            "checkLoginDAO:1-" +
            "updateDAOAccount:1-" +
            "searchLastnameDAO:1-" +
            "deleteDAOAccount:1-" +
            "createUINewAccount:1-" +
            "testLoginUI:1-" +
            "updateUIAccount:1-" +
            "searchUIAccount:1-" +
            "deleteUIAccount:1";

    private static boolean isLogin;
    public static WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();
    //public static InternetExplorerOptions options;
    public static ChromeOptions options;


    public TestwebApplicationTests() {
        //  System.setProperty("webdriver.ie.driver", "src/main/resources/static/IEDriverServer.exe");
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

    @BeforeAll
    public static void cleanData() {
        try {
            DatabaseTestUtils.cleanData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    public void createDAONewAccount() {
        System.out.println("Create DAO account");
        boolean checkStudent = TemplateQuestion.insertAccount("admintest", "admintest", "admintest", true);
        boolean checkServer = DatabaseTestUtils.checkByUsername("admintest");
        Assert.assertEquals(true, checkServer && checkStudent);
    }

    @Test
    @Order(2)
    public void checkLoginDAO() {
        System.out.println("Delete DAO account");
        boolean checkStudentSuccess = TemplateQuestion.checkLogin("admintest02", "admintest02");
        boolean checkStudentFailed = TemplateQuestion.checkLogin("admintest02", "admintest02Wrong");
        isLogin = checkStudentSuccess && !checkStudentFailed;
        Assert.assertEquals(true, isLogin);
    }

    @Test
    @Order(3)
    public void updateDAOAccount() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            System.out.println("Update DAO account");
            boolean checkStudent = TemplateQuestion.updateAccount("admintest", "admintestUpdated", "admintest", true);
            boolean checkServer = DatabaseTestUtils.checkUpdate("admintest", "admintestUpdated");
            Assert.assertEquals(true, checkServer && checkStudent);
        }
    }

    @Test
    @Order(4)
    public void searchLastnameDAO() throws InterruptedException {
        if (!isLogin) {
            assertFalse(true);
        } else {
            Integer size = TemplateQuestion.searchByLastname("admintest02");
            Assert.assertEquals(Integer.valueOf("1"), size);
        }
    }

    @Test
    @Order(5)
    public void deleteDAOAccount() {
        System.out.println("Delete DAO account");
        if (!isLogin) {
            assertFalse(true);
        } else {
            boolean checkStudent = TemplateQuestion.deleteAccount("admintest");
            boolean checkServer = DatabaseTestUtils.checkByUsername("admintest");
            Assert.assertEquals(true, !checkServer && checkStudent);
        }
    }

    @Test
    @Order(6)
    public void createUINewAccount() {
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
    @Order(7)
    public void testLoginUI() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            if (driver != null) {
                driver.get("http://localhost:8080/login.html");
                driver.findElement(By.name("txtUsername")).clear();
                driver.findElement(By.name("txtUsername")).sendKeys("admintest02");
                driver.findElement(By.name("txtPassword")).clear();
                driver.findElement(By.name("txtPassword")).sendKeys("admintest02");
                driver.findElement(By.name("btAction")).click();
                try {
                    String html = driver.findElement(By.tagName("body")).getText();
                    //String html = driver.findElement(By.xpath("//text()[contains(lower-case(.), lower-case('Welcome,'))]")).getText();
                    //System.out.println("testLogin");
                    //String text = element.getText();
                    assertEquals(true, html.contains("Search Page"));
                } catch (Exception e) {
                    assertFalse(true);
                }
            }
        }
    }

    @Test
    @Order(8)
    public void updateUIAccount() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            System.out.println("Update UI account");
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
            } catch (Exception e) {
                assertFalse(true);
            }
        }
    }

    @Test
    @Order(9)
    public void searchUIAccount() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            System.out.println("Update UI account");
            driver.get("http://localhost:8080/search.html");
            driver.findElement(By.name("txtSearchValue")).clear();
            driver.findElement(By.name("txtSearchValue")).sendKeys("admintest02");
            driver.findElement(By.name("btAction")).click();
            try {
                WebElement element = driver.findElements(By.tagName("td")).get(2);
                String text = element.getAttribute("innerHTML");
                assertEquals(true, text.contains("admintest"));
                // assertTrue(text.equalsIgnoreCase("Login Page"));
            } catch (Exception e) {
                assertFalse(true);
            }
        }
    }

    @Test
    @Order(10)
    public void deleteUIAccount() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            System.out.println("Delete account");
            driver.get("http://localhost:8080/DispatchController?btAction=delete&username=admintest&lastSearchValue=admintestUpdated");
            try {
                WebElement element = driver.findElement(By.tagName("h1"));
                String text = element.getText();
                // assertTrue(text.equalsIgnoreCase("Login Page"));
                if (text.equalsIgnoreCase("Search Page")) {
                    try {
                        WebElement elementTd = driver.findElements(By.tagName("td")).get(2);
                        String textHtml = elementTd.getAttribute("innerHTML");
                        assertEquals(false, textHtml.contains("admintest"));
                    } catch (Exception e) {
                        assertTrue(true);
                    }
                } else {
                    assertFalse(true);
                }
            } catch (Exception e) {
                assertFalse(true);
            }
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
