package com.thucnh.azuredevops;

import com.thucnh.azuredevops.template.TemplateQuestion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ExtendWith(TestResultLoggerExtension.class)
class TestJavaWebApplication {

    @Autowired
    private TemplateQuestion templateQuestion;

    @Test
    public void checkQuestion1() {
        assertEquals(Integer.valueOf(5), templateQuestion.question1(3, 1));
    }

    @Test
    public void checkQuestion2() {
        assertEquals(Integer.valueOf(5), templateQuestion.question2(1, 2));
    }

    @Test
    public void checkQuestion3() {
        assertEquals("5" + "Test3", templateQuestion.question3(3, 2));
    }

    @Test
    public void checkQuestion4() {
        assertEquals("5" + "Test42", templateQuestion.question4(3, 2));
    }

}
