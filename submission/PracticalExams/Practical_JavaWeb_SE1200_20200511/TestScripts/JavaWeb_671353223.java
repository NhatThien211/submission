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


import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = {SpringbootWithWebxmlApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@ExtendWith(TestResultLoggerExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestwebApplicationTests {

    public static String questionPointStr = "checkConnection:2-" +
            "checkLoginDAOWithBoss:1-" +
            "showAllDAO:1.5-" +
            "deleteDAO:1-" +
            "testLoginUI:1-" +
            "checkWelcomeWithName:0.5-" +
            "showAllUI:1.5-" +
            "deleteUI:1-" +
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
    }

    //


    @Test
    @Order(1)
    public void checkConnection() {
        boolean check = DBUtils.checkMakeConnection();
        Assert.assertEquals(true, check);
        if (check) {
            String sql = "Insert into tbl_Weapon(amourId, description, classification, defense, timeOfCreate, status)" +
                    " Values ('AM01','AM01','AM01','AM01','2020-03-12','true')," +
                    "('AM02','AM02','AM02','AM02','2020-03-12','true'), " +
                    "('AM03','AM03','AM03','AM03','2020-03-12','true')";
            DBUtils.executeUpdate(sql);
        }
    }

    @Test
    @Order(2)
    public void checkLoginDAOWithBoss() {
        boolean checkLoginSuccess = TemplateQuestion.checkLogin("LoginSuccess", "1");
        boolean checkLoginFailed = TemplateQuestion.checkLogin("LoginFailed", "1");
        boolean checkLoginIsBoss = TemplateQuestion.checkLogin("LoginNotBoss", "1");
        isLogin = checkLoginSuccess && !checkLoginFailed;
        Assert.assertEquals(true, isLogin && !checkLoginIsBoss);
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
            String sql = "SELECT amourId FROM tbl_Weapon Where  amourId = 'A02'";
            boolean checkExisted = DBUtils.executeQuery(sql);
            assertEquals(true, checkDAO && !checkExisted);
        }
    }

    @Test
    @Order(5)
    public void testLoginUI() {
        if (driver != null) {
            if (!isLogin) {
                assertFalse(true);
            } else {

                driver.get("http://localhost:8080/login.html");
                driver.findElement(By.name("txtUsername")).clear();
                driver.findElement(By.name("txtUsername")).sendKeys("LoginSuccess");
                driver.findElement(By.name("txtPassword")).clear();
                driver.findElement(By.name("txtPassword")).sendKeys("1");
                driver.findElement(By.name("btAction")).click();
                try {
                    String html = driver.findElement(By.tagName("body")).getText();
                    assertEquals(true, html.toLowerCase().contains("search page"));
                } catch (Exception e) {
                    assertFalse(true);
                }
            }
        } else {
            assertFalse(true);
        }
    }

    @Test
    @Order(6)
    public void checkWelcomeWithName() {
        if (driver != null) {
            if (!isLogin) {
                assertFalse(true);
            } else {

                driver.get("http://localhost:8080/login.html");
                driver.findElement(By.name("txtUsername")).clear();
                driver.findElement(By.name("txtUsername")).sendKeys("LoginSuccess");
                driver.findElement(By.name("txtPassword")).clear();
                driver.findElement(By.name("txtPassword")).sendKeys("1");
                driver.findElement(By.name("btAction")).click();
                try {
                    String html = driver.findElement(By.tagName("body")).getText();
                    boolean checkWelcome = html.toLowerCase().contains("loginsuccess");
                    boolean checkFullName = html.toLowerCase().contains("1");
                    assertEquals(true, checkFullName && checkWelcome);
                } catch (Exception e) {
                    assertFalse(true);
                }
            }
        } else {
            assertFalse(true);
        }
    }

    @Test
    @Order(7)
    public void showAllUI() {
        if (driver != null) {
            if (!isLogin) {
                assertFalse(true);
            } else {
                driver.get("http://localhost:8080/login.html");
                driver.findElement(By.name("txtUsername")).clear();
                driver.findElement(By.name("txtUsername")).sendKeys("LoginSuccess");
                driver.findElement(By.name("txtPassword")).clear();
                driver.findElement(By.name("txtPassword")).sendKeys("1");
                driver.findElement(By.name("btAction")).click();
                try {
                    String html = driver.findElement(By.tagName("body")).getText().toLowerCase();
                    assertEquals(true, html.contains("search page") && html.contains("am01"));
                } catch (Exception e) {
                    assertFalse(true);
                }
            }
        } else {
            assertFalse(true);
        }
    }

    @Test
    @Order(8)
    public void deleteUI() {
        if (driver != null) {
            if (!isLogin) {
                assertFalse(true);
            } else {
                driver.get("http://localhost:8080/delete?idDelete=AM03&btAction=Delete");
                boolean checkDB = DBUtils.executeQuery("SELECT amourId FROM tbl_Weapon Where  amourId = 'AM03'");
                if (!checkDB) {
                    try {
                        String html = driver.findElement(By.tagName("body")).getText().toLowerCase();

                        assertEquals(true, html.contains("search page") && !html.contains("am03"));
                    } catch (Exception e) {
                        assertFalse(true);
                    }
                } else {
                    assertFalse(true);
                }
            }
        } else {
            assertFalse(true);
        }
    }

    @Test
    @Order(9)
    public void testLogOut() {
        if (driver != null) {
            if (!isLogin) {
                assertFalse(true);
            } else {
                driver.get("http://localhost:8080/logout");
                try {
                    String html = driver.findElement(By.tagName("body")).getText().toLowerCase();

                    assertEquals(true, html.contains("login page"));
                } catch (Exception e) {
                    assertFalse(true);
                }
            }
        } else {
            assertFalse(true);
        }
        DBUtils.executeUpdate("Delete From tbl_Weapon");
    }

}
