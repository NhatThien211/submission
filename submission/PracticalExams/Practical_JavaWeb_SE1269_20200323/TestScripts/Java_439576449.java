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

    public static String questionPointStr = "checkLoginDAO:1-" +
            "getAllDoctorSchedule:2-" +
            "searchDoctorSchedule:2-" +
            "testLoginUI:1-" +
            "getAllDoctorScheduleUI:2-" +
            "searchDoctorScheduleUI:2-";

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


    @Test
    @Order(1)
    public void checkLoginDAO() {
        System.out.println("Check login DAO");
        boolean checkStudentSuccess = TemplateQuestion.checkLogin("t02", "t02");
        boolean checkStudentFailed = TemplateQuestion.checkLogin("t02", "t02Wr");
        boolean checkStudentIsLeader = TemplateQuestion.checkLogin("t03", "t03");

        isLogin = checkStudentSuccess && !checkStudentFailed && !checkStudentIsLeader;
        Assert.assertEquals(true, isLogin);
    }

    @Test
    @Order(2)
    public void getAllDoctorSchedule() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            Integer size = TemplateQuestion.getAllDoctorSchedule();
            Assert.assertEquals(Integer.valueOf("3"), size);
        }
    }

    @Test
    @Order(3)
    public void searchDoctorSchedule() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            try {
                Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-03-20");
                Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-04-26");
                Integer size = TemplateQuestion.searchDoctorSchedule(fromDate, toDate, "t02");
                assertEquals(true, size == 2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    @Order(4)
    public void testLoginUI() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            if (driver != null) {
                driver.get("http://localhost:8080/login.html");
                driver.findElement(By.name("txtUsername")).clear();
                driver.findElement(By.name("txtUsername")).sendKeys("t02");
                driver.findElement(By.name("txtPassword")).clear();
                driver.findElement(By.name("txtPassword")).sendKeys("t02");
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
    @Order(5)
    public void getAllDoctorScheduleUI() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            if (driver != null) {
                driver.get("http://localhost:8080/login.html");
                driver.findElement(By.name("txtUsername")).clear();
                driver.findElement(By.name("txtUsername")).sendKeys("t02");
                driver.findElement(By.name("txtPassword")).clear();
                driver.findElement(By.name("txtPassword")).sendKeys("t02");
                driver.findElement(By.name("btAction")).click();
                try {
                    String html = driver.findElement(By.tagName("body")).getText().toLowerCase();
                    boolean check = true;
                    if (!html.contains("search page")) {
                        check = false;
                    }
                    if (!html.contains("t02")) {
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
    @Order(6)
    public void searchDoctorScheduleUI() {
        if (!isLogin) {
            assertFalse(true);
        } else {
            System.out.println("Update UI account");
            driver.get("http://localhost:8080/search.jsp");
            driver.findElement(By.name("txtFrom")).clear();
            driver.findElement(By.name("txtFrom")).sendKeys("2020-03-20");
            driver.findElement(By.name("txtTo")).clear();
            driver.findElement(By.name("txtTo")).sendKeys("2020-04-26");
            driver.findElement(By.name("txtId")).clear();
            driver.findElement(By.name("txtId")).sendKeys("t02");
            driver.findElement(By.name("btAction")).click();

            try {
                String html = driver.findElement(By.tagName("body")).getText().toLowerCase();
                boolean check = true;
                if (!html.contains("search page")) {
                    check = false;
                }
                if (!html.contains("t02")) {
                    check = false;
                }
                assertEquals(true, check);
            } catch (Exception e) {
                assertFalse(true);
            }
        }
    }

}
