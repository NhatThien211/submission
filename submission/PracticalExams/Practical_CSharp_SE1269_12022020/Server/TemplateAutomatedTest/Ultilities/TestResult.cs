using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;

namespace TemplateAutomatedTest.Ultilities
{
    public class TestResult
    {       
        public static void WriteResult(string path,string result)
        {
            if(File.Exists(path))
            {
                using(StreamWriter sw = File.AppendText(path))
                {
                    sw.WriteLine(result);
                }
            }else
            {
               using (StreamWriter sw = File.CreateText(path))
                {
                    sw.WriteLine(result);
                }
            }
        }
    }
}
