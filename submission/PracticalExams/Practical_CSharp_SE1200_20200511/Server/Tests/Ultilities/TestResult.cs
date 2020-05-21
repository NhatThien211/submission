using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Tests.Models;

namespace Tests.Ultilities
{
    public class Utils
    {
        public static System.Net.Sockets.TcpClient client;
        public static ResultSocket resultSocket { get; set; }
        public static void WriteResult(string path, string result)
        {
            if (File.Exists(path))
            {
                using (StreamWriter sw = File.AppendText(path))
                {
                    sw.WriteLine(result);
                }
            }
            else
            {
                using (StreamWriter sw = File.CreateText(path))
                {
                    sw.WriteLine(result);
                }
            }
        }
        public static double GetWarnings()
        {
            String filePath = @"E:\warning.txt";
            int numberOfWarnings = File.ReadAllLines(filePath).Count();
            return numberOfWarnings * 0.1;
        }
        public static Dictionary<String, String> splitQuestions(String questionPointStr)
        {
            Dictionary<String, String> storeQuestion = new Dictionary<string, string>();
            List<String> splited = questionPointStr.Split("-").ToList();
            foreach (var item in splited)
            {
                String[] itemSplited = item.Split(":");
                storeQuestion.Add(itemSplited[0], itemSplited[1]);
            }
            return storeQuestion;
        }

        public static void connectToServer(String ip, int port, String data)
        {
            System.Net.Sockets.TcpClient client = new System.Net.Sockets.TcpClient();
            client.Connect(ip, port);
            Thread.Sleep(500);
            sendMessage(Encoding.Unicode.GetBytes(data), client);
            client.Close();
        }
        public static String ExecutablePath()
        {
            string startupPath = System.IO.Directory.GetCurrentDirectory();
            string projectDirectory = Directory.GetParent(startupPath).Parent.FullName;
            projectDirectory = Directory.GetParent(projectDirectory).Parent.FullName;
            //projectDirectory = Directory.GetParent(projectDirectory).Parent.FullName;
            String finalPath = projectDirectory.Substring(0, projectDirectory.Length - 7); //server//
            return finalPath;

        }
        public static String GetStudentCode()
        {
            String filePath = Path.Combine(ExecutablePath() + @"\Server" + @"\Tests" + @"\Controllers\");
            String[] getAllFile = Directory.GetFiles(filePath, "*.cs");
            String getStudentcode = Path.GetFileName(getAllFile[0]);
            String split = getStudentcode.Split('_')[1];
            return split;
        }

        public static void PrintResult(String data,String studentCode)
        {
            String prefixStart = "Start" + studentCode;
            int prefixLength = studentCode.Length + 5;
            String endStart = "End" + studentCode;
            String filePath = ExecutablePath() + @"\Result.txt";
            String fullContent = data;
            String readAtIndex = File.ReadAllText(filePath);
            int startIndex = readAtIndex.IndexOf(prefixStart);
            int endIndex = readAtIndex.IndexOf(endStart);
            String firstPart = readAtIndex.Substring(0, startIndex+ prefixLength);
            String endPart = readAtIndex.Substring(endIndex);
            String appendPart = firstPart + data + endPart;
            File.WriteAllText(filePath, appendPart);
        }
        public static ResultSocket GetResultSocket(String studentCode)
        {
            String prefixStart = "Start" + studentCode;
            String endStart = "End" + studentCode;
            String filePath = ExecutablePath()+@"\Result.txt";
            String readByStudentCode = File.ReadAllText(filePath);
            int startIndex = readByStudentCode.IndexOf(prefixStart)+prefixStart.Length;
            int endIndex = readByStudentCode.IndexOf(endStart);
            int readLength = endIndex - startIndex;
            String readAtIndex = readByStudentCode.Substring(startIndex, readLength);
            var mappingObject = JsonConvert.DeserializeObject<ResultSocket>(readAtIndex);
            return mappingObject;
        }
        public static void DeleteWarning(String file)
        {
            if (File.Exists(file))
            {
                File.Delete(file);
            }
        }

        private static void sendMessage(byte[] bytes, TcpClient client)
        {
            // Client must be connected to   
            client.GetStream() // Get the stream and write the bytes to it  
                  .Write(bytes, 0,
                  bytes.Length); // Send the stream  
        }


    }
}
