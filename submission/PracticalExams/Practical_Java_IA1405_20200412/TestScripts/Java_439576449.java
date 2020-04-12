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

    public static String questionPointStr = "insertJewelry:2-updateJewelry:1.5-searchJewelry:1.5-removeJewelry:1.5-sortJewelry:1.5";

    private TemplateQuestion templateQuestion = new TemplateQuestion();

    @Test
    @Order(1)
    public void insertJewelry() {
        String[] data = new String[]{"1000","nameTest","brandTest","1000"};
        changeData(data);
        templateQuestion.insert();
        System.out.println("insertJewelry");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(data, "insertJewelry"));
    }

    @Test
    @Order(2)
    public void checkDuplicatedId() {
        String[] data = new String[]{"1000","DuplicatedName","DuplicatedBrandTest","1000"};
        changeData(data);
        templateQuestion.insert();
        System.out.println("checkDuplicatedId");
        assertEquals(false, CheckingUtils.checkConsoleLogContains(data, "checkDuplicatedId"));
    }

    @Test
    @Order(3)
    public void updateJewelry() {
        String[] data = new String[]{"1001","nameUpdate","brandUpdate","1001"};
        changeData(data);
        templateQuestion.insert();
        System.out.println("==========Start Update==========");
        changeData(new String[]{"1001","nameUpdateChanged","brandUpdateChanged","1002"});
        templateQuestion.update();
        System.out.println("updateJewelry");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(data, "updateJewelry"));
    }

    @Test
    @Order(4)
    public void searchJewelry() {
        String[] data = new String[]{"1002","nameSearch","brandSearch","1002"};
        changeData(data);
        templateQuestion.insert();
        System.out.println("==========Start Search==========");
        templateQuestion.search();
        System.out.println("searchJewelry");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(data, "searchJewelry"));
    }

    //
    @Test
    @Order(5)
    public void sortJewelry() {
        String[] data = new String[]{"1003","zNameSort","zBrandSort","1003"};
        changeData(data);
        templateQuestion.insert();
        System.out.println("==========Start Sort==========");
        templateQuestion.sort();
        System.out.println("sortJewelry");
        assertEquals(true, CheckingUtils.checkConsoleLogContains(data, "sortJewelry"));
    }

    //
    @Test
    @Order(6)
    public void removeJewelry() {
        String[] data = new String[]{"1004","nameRemove","brandRemove","1004"};
        changeData(data);
        templateQuestion.insert();
        System.out.println("insertJewelry");
        boolean check =  CheckingUtils.checkConsoleLogContains(data, "insertJewelry");
        if(check){
            templateQuestion.remove();
            System.out.println("removeJewelry");
            assertEquals(true, !CheckingUtils.checkConsoleLogContains(data, "removeJewelry"));
        }else {
            assertTrue(false);
        }
    }

    private void changeData(String[] param){
        Map<String, String> maps = new HashMap<>();
        maps.put("id",param[0]);
        maps.put("name",param[1]);
        maps.put("manufacturer",param[2]);
        maps.put("price",param[3]);
        CheckingUtils.changeValueOfFile(maps);
    }
}
