package server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = {SpringbootWithWebxmlApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@ExtendWith(TestResultLoggerExtension.class)
class TestwebApplicationTests {

    public static String questionPointStr = "checkQuestion1:2-checkQuestion2:4-checkQuestion3:2-checkQuestion4:2";

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
    public void checkQuestion1() {
        driver.get("http://localhost:8080/index.jsp");
        driver.findElement(By.name("txtUsername")).clear();
        driver.findElement(By.name("txtPassword")).clear();
        driver.findElement(By.name("txtUsername")).sendKeys("admin");
        driver.findElement(By.name("txtPassword")).sendKeys("admin");
        driver.findElement(By.name("btnLogin")).click();
        try {
            WebElement elementUsername = driver.findElement(By.name("txtUsername"));
            assertEquals(1, 2);
        }catch (Exception e){
            assertEquals(1, 1);
        }
    }

    @Test
    public void checkQuestion2() {
        assertEquals(1, 1);
    }
    @Test
    public void checkQuestion3() {
        assertEquals(1, 1);
    }
    @Test
    public void checkQuestion4() {
        assertEquals(1, 1);
    }
    private boolean checkElement(WebDriver driver) {
        if (driver.findElement(By.id("viewport")) != null) {
            return true;
        }
        return false;
    }

    private void close() {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);

        }
    }


}
