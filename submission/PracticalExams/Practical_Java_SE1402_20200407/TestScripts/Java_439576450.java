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

@SpringBootTest
@ExtendWith(TestResultLoggerExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JavaApplicationTests {

    public static String questionPointStr = "insertWatch:2-updateWatch:2-search:2-remove:2-sort:2";

    private TemplateQuestion templateQuestion = new TemplateQuestion();



    @Test
    @Order(1)
    public void insertWatch() {
        Map<String, String> maps = new HashMap<>();
        maps.put("id","1000");
        maps.put("name","nameTest");
        maps.put("manufacturer","manufactorTest");
        maps.put("price","1000");
        CheckingUtils.changeValueOfFile(maps);
        templateQuestion.insert();
        String msg = String.format("SE1267|%-5s|%10s|%5s|%5f", 1000, "nameTest", "manufactorTest", 1000.0);
        System.out.println("insertWatch");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(msg, "insertWatch"));
    }

    @Test
    @Order(2)
    public void updateWatch() {
        Map<String, String> maps = new HashMap<>();
        maps.put("id","1000");
        maps.put("name","nameTestChanged");
        maps.put("manufacturer","manufactorTest");
        maps.put("price","1000");
        CheckingUtils.changeValueOfFile(maps);
        templateQuestion.update();
        String msg = String.format("SE1267|%-5s|%10s|%5s|%5f", 1000, "nameTestChanged", "manufactorTest", 1000.0);
        System.out.println("updateWatch");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(msg, "updateWatch"));
    }

    @Test
    @Order(3)
    public void search() {
        templateQuestion.search();
        String msg = String.format("SE1267|%-5s|%10s|%5s|%5f", 1000, "nameTestChanged", "manufactorTest", 1000.0);
        System.out.println("search");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(msg, "search"));
    }

    //
    @Test
    @Order(4)
    public void sort() {
        Map<String, String> maps = new HashMap<>();
        maps.put("id","1001");
        maps.put("name","nameTest1001");
        maps.put("manufacturer","manufactorTest");
        maps.put("price","1000");
        CheckingUtils.changeValueOfFile(maps);
        templateQuestion.insert();
        templateQuestion.sort();
        String msg = String.format("SE1267|%-5s|%10s|%5s|%5f", 1000, "nameTestChanged", "manufactorTest", 1000.0);
        System.out.println("sort");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(msg, "sort"));
    }

    //
    @Test
    @Order(5)
    public void remove() {
        templateQuestion.remove();
        String msg = String.format("SE1267|%-5s|%10s|%5s|%5f", 1001, "nameTest1001", "manufactorTest", 1000.0);
        System.out.println("remove");
        assertEquals(true, !CheckingUtils.checkConsoleLogContains(msg, "remove"));
    }


}
