package se.redmind.rmtest.selenium.grid;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestHome {

	/**
	 * @param args
	 */
	 public static String main() {
		// TODO Auto-generated method stub
		 String testHome = null;
		 if (isWindows()) {
			 testHome = System.getenv("TESTHOME");
		 } else {
			 InputStream fis;
			 try {
				 fis = new FileInputStream(System.getenv("HOME") + "/.RmTest");

				 BufferedReader br = new BufferedReader(new InputStreamReader(fis)); 
				 String line;
				 
				 while ((line = br.readLine()) != null) {                
					 if (line.contains("TESTHOME=")) {
						 System.out.println(line);
						 testHome = line.split("=")[1];
						 System.out.println(testHome);
					 }
				 }
			 } catch (FileNotFoundException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 } catch (IOException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
		 }
		return testHome;

	 }




	 public static String getOsName()
	 {
		 String OS = null;

		 OS = System.getProperty("os.name");
		 return OS;
	 }

	 public static boolean isWindows()
	 {
		 System.out.println(getOsName());
		 return getOsName().startsWith("Windows");
	 }

}
