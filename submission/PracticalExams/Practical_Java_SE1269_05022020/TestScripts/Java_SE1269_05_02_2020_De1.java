package com.fpt.practical.java;

import com.fpt.practical.java.template.TemplateQuestion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(TestResultLoggerExtension.class)
class JavaApplicationTests {

    public static String questionPointStr = "checkQuestion1:2-checkQuestion2:4-checkQuestion3:2-checkQuestion4:2";

    @Autowired
    private TemplateQuestion templateQuestion;

    @Test
    public void checkQuestion1() {
        assertEquals(Integer.valueOf(5), templateQuestion.question1(3, 2));
    }

    @Test
    public void checkQuestion2() {
        assertEquals(Integer.valueOf(5), templateQuestion.question2(3, 2));
    }

    @Test
    public void checkQuestion3() {
        assertEquals("5" + "Test3", templateQuestion.question3(3, 2));
    }

    @Test
    public void checkQuestion4() {
        assertEquals("5" + "Test4", templateQuestion.question4(3, 2));
    }


}
