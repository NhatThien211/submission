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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = {SpringbootWithWebxmlApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@ExtendWith(TestResultLoggerExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestwebApplicationTests {

    //check
    public static String questionPointStr = "checkConnection:2-" +
            "checkLoginDAOWithBoss:1-" +
            "showAllDAO:1.5-" +
            "deleteDAO:1-" +
            "testLoginUI:1-" +
            "checkWelcomeWithName:0.5-" +
            "showAllUI:1.5-"+
            "deleteUI:1-"+
            "testLogOut:0.5";

    private static boolean isLogin;
    public static WebDriver driver;
    //public static InternetExplorerOptions options;
    public static ChromeOptions options;


    public TestwebApplicationTests() {
        //  System.setProperty("webdriver.ie.driver", "src/main/resources/static/IEDriverServer.exe");
        System.setProperty("webdriver.chrome.driver", "src/main/resources/static/chromedriver2.exe");

        if (options == null) {
            options = new ChromeOptions();
            if (driver == null) {
                options.addArguments("--headless");
                driver = new ChromeDriver(options);
                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            }
        }
//
    }



    @Test
    @Order(1)
    public void checkConnection() {
        System.out.println("Check make connection");
        boolean check = DatabaseTestUtils.checkMakeConnection();
        Assert.assertEquals(true, check);
        if(check){
            DatabaseTestUtils.preparedData();
        }
    }

    @Test
    @Order(2)
    public void checkLoginDAOWithBoss() {
        System.out.println("Check login DAO");
        boolean checkStudentSuccess = TemplateQuestion.checkLogin("t02", "2");
        System.out.println("Check login DAO1");
        boolean checkStudentFailed = TemplateQuestion.checkLogin("t02", "20");
        System.out.println("Check login DAO2");
        boolean checkStudentIsBoss = TemplateQuestion.checkLogin("t03", "3");
        System.out.println("Check login DAO3");
        isLogin = checkStudentSuccess && !checkStudentFailed;
        boolean check = isLogin && !checkStudentIsBoss;
        System.out.println(isLogin);
        Assert.assertEquals(true, check);
    }


    @Test
    @Order(3)
    public void showAllDAO() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            Integer size = TemplateQuestion.showAll();
            Assert.assertEquals(Integer.valueOf("3"), size);
        }
    }

    @Test
    @Order(4)
    public void deleteDAO() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            boolean checkDAO = TemplateQuestion.delete("AM02");
            boolean checkDB = DatabaseTestUtils.checkExistAfterDelete("AM02");
            boolean check = checkDAO && !checkDB;
            assertEquals(true, check);
        }
    }

    @Test
    @Order(5)
    public void testLoginUI() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            if (driver != null) {
                driver.get("http://localhost:8080/login.html");
                driver.findElement(By.name("txtUsername")).clear();
                driver.findElement(By.name("txtUsername")).sendKeys("t02");
                driver.findElement(By.name("txtPassword")).clear();
                driver.findElement(By.name("txtPassword")).sendKeys("2");
                driver.findElement(By.name("btAction")).click();
                try {
                    String html = driver.findElement(By.tagName("body")).getText();
                    assertEquals(true, html.toLowerCase().contains("search page"));
                } catch (Exception e) {
                    assertFalse(true);
                }
            }
        }
    }

    @Test
    @Order(6)
    public void checkWelcomeWithName() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            if (driver != null) {
                driver.get("http://localhost:8080/login.html");
                driver.findElement(By.name("txtUsername")).clear();
                driver.findElement(By.name("txtUsername")).sendKeys("t02");
                driver.findElement(By.name("txtPassword")).clear();
                driver.findElement(By.name("txtPassword")).sendKeys("2");
                driver.findElement(By.name("btAction")).click();
                try {
                    String html = driver.findElement(By.tagName("body")).getText();
                    boolean checkWelcome = html.toLowerCase().contains("welcome");
                    boolean checkFullName = html.toLowerCase().contains("t02");
                    assertEquals(true, checkFullName && checkWelcome);
                } catch (Exception e) {
                    assertFalse(true);
                }
            }
        }
    }

    @Test
    @Order(7)
    public void showAllUI() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            if (driver != null) {
                driver.get("http://localhost:8080/login.html");
                driver.findElement(By.name("txtUsername")).clear();
                driver.findElement(By.name("txtUsername")).sendKeys("t02");
                driver.findElement(By.name("txtPassword")).clear();
                driver.findElement(By.name("txtPassword")).sendKeys("2");
                driver.findElement(By.name("btAction")).click();
                try {
                    String html = driver.findElement(By.tagName("body")).getText().toLowerCase();
                    boolean check = true;
                    if (!html.contains("search page")) {
                        check = false;
                    }
                    if (!html.contains("am01")) {
                        check = false;
                    }
                    assertEquals(true, check);
                } catch (Exception e) {
                    assertFalse(true);
                }
            }
        }
    }

    @Test
    @Order(8)
    public void deleteUI() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            driver.get("http://localhost:8080/DispatcherController?btAction=Delete&amourId=AM03");
            boolean checkDB = DatabaseTestUtils.checkExistAfterDelete("AM03");
            if (!checkDB) {
                try {
                    String html = driver.findElement(By.tagName("body")).getText().toLowerCase();
                    boolean check = true;
                    if (!html.contains("search page")) {
                        check = false;
                    }
                    if (html.contains("am03")) {
                        check = false;
                    }
                    assertEquals(true, check);
                } catch (Exception e) {
                    assertFalse(true);
                }
            }
        }
    }

    @Test
    @Order(9)
    public void testLogOut() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            driver.get("http://localhost:8080/DispatcherController?btAction=Logout");
            try {
                String html = driver.findElement(By.tagName("body")).getText().toLowerCase();
                boolean check = false;
                if (html.contains("login page")) {
                    check = true;
                }
                assertEquals(true, check);
            } catch (Exception e) {
                assertFalse(true);
            }
        }
    }
    @AfterAll
    public static void cleanData(){
        DatabaseTestUtils.cleanData();
    }
}
