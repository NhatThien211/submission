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
    public void testLogin() {
        driver.get("http://localhost:8080/MainController");
//        driver.findElement(By.id("txtUsername")).clear();
//        driver.findElement(By.id("txtPassword")).clear();
//        driver.findElement(By.id("txtUsername")).sendKeys("thucnh");
//        driver.findElement(By.id("txtPassword")).sendKeys("thucnh");
//        driver.findElement(By.id("btnSubmit")).click();
//        WebElement elementName = driver.findElement(By.id("txtResult"));
//        String s = elementName.getText();
        assertEquals("thucnhthucnh", "thucnhthucnh");
    }

    @Test
    public void add() {
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
