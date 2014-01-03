package se.remind.rmtest.selenium.grid;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RMTestConfig {
    private static boolean isInitialized = false;
    private static String hubHost; 
    private static String localNodeHost;
    
    public static String getHubHost() {
        if (!isInitialized) {
            main();
        }
        return hubHost;
    }
    
    public static String getLocalNodeHost() {
        if (!isInitialized) {
            main();
        }
        return localNodeHost;
    }
    
    /**
     * @param args
     */
    public static void main() {
        // TODO Auto-generated method stub
        try {
            InputStream fis = new FileInputStream(System.getenv("HOME") + "/.bashrc");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis)); 
            
            String line;
            String testHome=null;
            while ((line = br.readLine()) != null) {                
                if (line.contains("testHome=")) {
                    System.out.println(line);
                    testHome = System.getenv("HOME") + line.split("HOME")[1].replace("\"", "");
                    System.out.println(testHome);
                }
            }

//            System.out.println(testHome);
            fis = new FileInputStream(testHome + "/etc/RMTestLocal.conf");
            br = new BufferedReader(new InputStreamReader(fis)); 
            while ((line = br.readLine()) != null) { 
                if (line.startsWith("#")) {
                    //Do nothing
                } else if (line.contains("RMTestHubIp=")) {
                    hubHost = line.split("\"")[1];
                } else if (line.contains("RMTestLocalNodeIp=")) {
                    localNodeHost = line.split("\"")[1];
                }
            }
            System.out.println(hubHost + " : " + localNodeHost);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        File bashrc = new File(System.getenv("HOME") + "/.bashrc");
//        System.out.println(System.getenv("HOME"));
        
    }

}
