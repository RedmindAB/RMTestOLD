package se.redmind.rmtest.selenium.grid;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestHome {

    private static final Logger LOG = LoggerFactory.getLogger(TestHome.class);

    /**
     * @param args
     */
    public static String main() {
        String testHome = null;
        if(isWindows()) {
            LOG.debug("We're on windows");
            testHome = System.getenv("TESTHOME");
        }
        else {
            LOG.debug("We're on a unixy system");
            InputStream fis;
            try {
                fis = new FileInputStream(System.getenv("HOME") + "/.RmTest");
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String line;
                while((line = br.readLine()) != null) {
                    if(line.contains("TESTHOME=")) {
                        LOG.debug(line);
                        testHome = line.split("=")[1];
                        LOG.debug(testHome);
                    }
                }
            }
            catch(FileNotFoundException e) {
                LOG.debug("", e);
            }
            catch(IOException e) {
                LOG.debug("", e);
            }
        }
        if(testHome == null) {
            LOG.debug("ERROR: We where not able to find a testhome folder");
            LOG.debug("On windows, set your TESTHOME system variable");
            LOG.debug("On Unixy systems, create your .RmTest file in your home folder");
        }
        return testHome;
    }

    public static String getOsName() {
        String OS = null;
        OS = System.getProperty("os.name");
        return OS;
    }

    public static boolean isWindows() {
        LOG.debug(getOsName());
        return getOsName().startsWith("Windows");
    }
}
