package com.practicalexam;

import com.practicalexam.student.TemplateQuestion;
import com.practicalexam.utils.CheckingUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(TestResultLoggerExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JavaApplicationTests {

    public static String questionPointStr = "insertShoes:2-checkDuplicatedId:1-updateShoes:2-searchShoes:2-removeShoes:1.5-sortShoes:1.5";

    private TemplateQuestion templateQuestion = new TemplateQuestion();

    @Test
    @Order(1)
    public void insertShoes() {
        String[] data = new String[]{"1000","modelTest","10","100.0"};
        CheckingUtils.changeValueOfFile(data);
        templateQuestion.insert();
        System.out.println("insertShoes");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(data, "insertShoes"));
    }

    @Test
    @Order(2)
    public void checkDuplicatedId() {
        String[] data = new String[]{"1000","DuplicatedModel","10","100.0"};
        CheckingUtils.changeValueOfFile(data);
        templateQuestion.insert();
        System.out.println("checkDuplicatedId");
        assertEquals(false, CheckingUtils.checkConsoleLogContains(data, "checkDuplicatedId"));
    }

    @Test
    @Order(3)
    public void updateShoes() {
        String[] data = new String[]{"1001","modelUpdate","11","1001"};
        CheckingUtils.changeValueOfFile(data);
        templateQuestion.insert();
        System.out.println("==========Start Update==========");
        CheckingUtils.changeValueOfFile(data);
        templateQuestion.update();
        System.out.println("updateShoes");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(data, "updateShoes"));
    }

    @Test
    @Order(4)
    public void searchShoes() {
        String[] data = new String[]{"1002","modelSearch","12","1002"};
        CheckingUtils.changeValueOfFile(data);
        templateQuestion.insert();
        System.out.println("==========Start Search==========");
        templateQuestion.search();
        System.out.println("searchShoes");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(data, "searchShoes"));
    }

    //
    @Test
    @Order(5)
    public void sortShoes() {
        String[] data = new String[]{"1003","aModelSort","12","1003"};
        CheckingUtils.changeValueOfFile(data);
        templateQuestion.insert();
        System.out.println("==========Start Sort==========");
        templateQuestion.sort();
        System.out.println("sortShoes");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(data, "sortShoes"));
    }

    //
    @Test
    @Order(6)
    public void removeShoes() {
        String[] data = new String[]{"1004","modelRemove","14","1004"};
        CheckingUtils.changeValueOfFile(data);
        templateQuestion.insert();
        System.out.println("insertShoes");
        boolean check =  CheckingUtils.checkConsoleLogContains(data, "insertShoes");
        if(check){
            templateQuestion.remove();
            System.out.println("removeShoes");
            assertEquals(true, !CheckingUtils.checkConsoleLogContains(data, "removeShoes"));
        }else {
            assertTrue(false);
        }
    }


}
