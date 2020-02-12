using Microsoft.VisualStudio.TestTools.UnitTesting;
using TemplateAutomatedTest.Controllers;
using System;
using System.Collections.Generic;
using System.Text;
using TemplateAutomatedTest.Template;
using System.IO;

namespace AutomatedTests
{
    [TestClass()]
    public class AutomatedTests
    {
        public TestContext TestContext { get; set; }
        //start
        [TestMethod()]
        public void Question1()
        {
            Assert.AreEqual(5, TemplateQuestion.Question1(2, 3));
        }
        [TestMethod()]
        public void Question2()
        {
            Assert.AreEqual(6, TemplateQuestion.Question2(5, 1));
        }
        //end
        [TestCleanup]
        public void TestCleanup()
        {           
            String testResult = TestContext.CurrentTestOutcome.ToString();
            String testName = TestContext.TestName;
            String result = testName + " : " + testResult;
            String path = System.AppDomain.CurrentDomain.BaseDirectory+@"Result.txt";
            Console.WriteLine(path);
            TemplateAutomatedTest.Ultilities.TestResult.WriteResult(path, result);
        }
    }
}


