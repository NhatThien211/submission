/*
 * File:   newcunittest.c
 * Author: HP
 *
 * Created on Feb 10, 2020, 10:36:19 AM
 */

#include <stdio.h>
#include <stdlib.h>
#include <CUnit/Basic.h>
#include <CUnit/TestRun.h>
#include <CUnit/Automated.h>
/*
 * CUnit Test Suite
 */

int init_suite(void) {
    return 0;
}

int clean_suite(void) {
    return 0;
}

void question1() {
    CU_ASSERT(2 * 2 == 4);
}

void question2() {
    CU_ASSERT(2 * 2 == 4);
}

int main() {
    CU_pSuite pSuite = NULL;

    /* Initialize the CUnit test registry */
    if (CUE_SUCCESS != CU_initialize_registry())
        return CU_get_error();

    /* Add a suite to the registry */
    pSuite = CU_add_suite("C_SE1269_05_02_2020_De1", init_suite, clean_suite);
    if (NULL == pSuite) {
        CU_cleanup_registry();
        return CU_get_error();
    }

    /* Add the tests to the suite */
    if ((NULL == CU_add_test(pSuite, "question1", question1)) ||
            (NULL == CU_add_test(pSuite, "question2", question2))) {
        CU_cleanup_registry();
        return CU_get_error();
    }

    /* Run all tests using the CUnit Basic interface */
    CU_basic_set_mode(CU_BRM_VERBOSE);
    CU_basic_run_tests();
//         CU_pRunSummary c = CU_get_run_summary();
    CU_automated_run_tests();
    CU_list_tests_to_file();

    CU_cleanup_registry();

    return CU_get_error();
}
