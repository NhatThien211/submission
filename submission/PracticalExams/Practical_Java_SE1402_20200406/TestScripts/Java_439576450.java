package com.practicalexam;

import com.practicalexam.student.TemplateQuestion;
import com.practicalexam.utils.CheckingUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(TestResultLoggerExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JavaApplicationTests {

    public static String questionPointStr = "insertWatch:2-updateWatch:2-search:2-remove:2-sort:2";

    private TemplateQuestion templateQuestion = new TemplateQuestion();

    @BeforeAll
    public static void pre() {
        CheckingUtils.changeScannerToValue("2020", 2020, true);
    }

    @Test
    @Order(1)
    public void insertWatch() {
        templateQuestion.insertWatch();
        String msg = String.format("SE1267|%-5s|%10s|%5s|%5f", 2020, "2020", "2020", 2020.0);
        System.out.println("insertWatch");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(msg,"insertWatch"));
    }

    @Test
    @Order(2)
    public void updateWatch() {
        templateQuestion.updateWatch();
        String msg = String.format("SE1267|%-5s|%10s|%5s|%5f", 2020, "2020", "2020", 2020.0);
        System.out.println("updateWatch");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(msg,"updateWatch"));
    }

    @Test
    @Order(3)
    public void search() {
        templateQuestion.search();
        String msg = String.format("SE1267|%-5s|%10s|%5s|%5f", 2020, "2020", "2020", 2020.0);
        System.out.println("search");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(msg,"search"));
    }
    //
    @Test
    @Order(5)
    public void remove() {
        templateQuestion.remove();
        String msg = String.format("SE1267|%-5s|%10s|%5s|%5f", 2020, "2020", "2020", 2020.0);
        System.out.println("remove");
        assertEquals(true, !CheckingUtils.checkConsoleLogContains(msg,"remove"));
    }
    //
    @Test
    @Order(4)
    public void sort() {
        templateQuestion.sort();
        String msg = String.format("SE1267|%-5s|%10s|%5s|%5f", 2020, "2020", "2020", 2020.0);
        System.out.println("sort");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(msg,"sort"));
    }
}
