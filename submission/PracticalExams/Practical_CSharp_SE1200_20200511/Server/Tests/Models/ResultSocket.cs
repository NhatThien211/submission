using System;
using System.Collections.Generic;
using System.Text;

namespace Tests.Models
{
   public class ResultSocket
    {
        public String StudentCode { get; set; }
        public Dictionary<String,String> ListQuestions { get; set; }
        public Double TotalPoint { get; set; }
        public String SubmitTime { get; set; }
        public String EvaluateTime { get; set; }
        public String CodingConvention { get; set; }
        public String ErrorMsg { get; set; }
        public String Result { get; set; }

    }
}
