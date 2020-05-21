package com.practicalexam;

import com.practicalexam.student.TemplateQuestion;
import com.practicalexam.utils.CheckingUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(TestResultLoggerExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JavaApplicationTests {
    public static String questionPointStr = "insertShoes:2-checkDuplicatedId:1-updateShoes:1.5-searchShoes:1.5-removeShoes:1.5-sortShoes:1.5";
    private TemplateQuestion templateQuestion = new TemplateQuestion();

    @Test
    @Order(1)
    public void insertShoes() {
        String[] data = new String[]{"codeAdd","modelAdd","10","1000.0"};
        String start = "==========Start Insert==========";
        String end = "==========End Insert==========";
        System.out.println(start);
        templateQuestion.insert();
        System.out.println(end);
        assertEquals(true, CheckingUtils.checkConsoleLogContains(data, end,start));
    }

    @Test
    @Order(3)
    public void updateShoes() {
        String[] data = new String[]{"codeAdd2","modelUpdate","11","1001"};
        String start = "==========Start Update==========";
        String end = "==========End Update==========";
        System.out.println(start);
        templateQuestion.update();
        System.out.println(end);
        assertEquals(true, CheckingUtils.checkConsoleLogContains(data, end,start));

    }

    @Test
    @Order(4)
    public void searchShoes() {
        String[] data = new String[]{"codeAdd","modelAdd","10","1000"};
        String start = "==========Start Search==========";
        String end = "==========End Search==========";
        System.out.println(start);
        templateQuestion.search();
        System.out.println(end);
        assertEquals(true, CheckingUtils.checkConsoleLogContains(data, end,start));
    }

    @Test
    @Order(5)
    public void sortShoes() {
        try{
            String[] data = new String[]{"codeSort","zmodelSort","14","1004"};
            String start = "==========Start Sort==========";
            String end = "==========End Sort==========";
            System.out.println(start);
            templateQuestion.sort();
            System.out.println(end);
            assertEquals(true, CheckingUtils.checkConsoleLogContains(data, end,start));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    @Order(6)
    public void removeShoes() {
        String[] data = new String[]{"codeAdd","modelAdd","10","1000"};
        String start = "==========Start Remove==========";
        String end = "==========End Remove==========";
        System.out.println(start);
        templateQuestion.remove();
        System.out.println(end);
        assertEquals(true, !CheckingUtils.checkConsoleLogContains(data, end,start));
    }


}
