using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RMTest
{
    class TestHome
    {
        	/**
	 * @param args
	 */
	 public static String main() {
		// TODO Auto-generated method stub
		 String testHome = null;
		 if (isWindows()) {
			 Console.WriteLine("We're on windows");
             testHome = Environment.GetEnvironmentVariable("TESTHOME", EnvironmentVariableTarget.Machine);
             Console.WriteLine("TESTHOMESTRINGTHINGY: " + testHome);
		 } else {
			 Console.WriteLine("Strange, We are running dotNet on a non windows system?");

		 }

		if (testHome == null) {
			Console.WriteLine("ERROR: We where not able to find a testhome folder");
			Console.WriteLine("On windows, set your TESTHOME system variable");
			Console.WriteLine("On Unixy systems, create your .RmTest file in your home folder");
		}
		return testHome;

    }
        
	 

	 public static bool isWindows()
	 {
         Console.WriteLine("Assuming windows since I'm .Net.");
		 return true;
	 }
}
}
    
