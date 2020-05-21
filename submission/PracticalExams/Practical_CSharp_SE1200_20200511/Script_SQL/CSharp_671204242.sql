using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Practical_CSharp.Student.DBUtil
{
    public class Connection
    {
        public static SqlConnection GetConnection()
        {
            SqlConnection con = new SqlConnection("Server=THUCNHSE63155;" +
                  "Initial Catalog=BookManagement;" +
                   "User Id=sa;Password=12");
            return con;
        }
    }
}
