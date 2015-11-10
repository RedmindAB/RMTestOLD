using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RMTest
{
    class logger
    {
        public static void info(String message)
        {
            Console.WriteLine(message);
        }

        public static void warn(String message)
        {
            Console.WriteLine(message);
        }

        public static void warn(String message, Exception e)
        {
            Console.WriteLine(message, e.Message);
        }
    }
}
