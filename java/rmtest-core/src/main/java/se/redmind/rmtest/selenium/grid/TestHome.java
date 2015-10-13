package se.redmind.rmtest.selenium.grid;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestHome {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestHome.class);

    /**
     * @param args
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static String main() {
        String testHome = null;
        if (isWindows()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("We are on windows");
            }
            testHome = System.getenv("TESTHOME");
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("We are on a POSIX system");
            }
            try (InputStream fis = new FileInputStream(System.getenv("HOME") + "/.RmTest")) {
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String line;

                while ((line = br.readLine()) != null) {
                    if (line.contains("TESTHOME=")) {
                        testHome = line.split("=")[1];
                    }
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        if (testHome == null) {
            LOGGER.error("We where not able to find a testhome folder");
            LOGGER.error("On windows, set your TESTHOME system variable");
            LOGGER.error("On Unixy systems, create your .RmTest file in your home folder");
        }
        return testHome;

    }

    public static String getOsName() {
        return System.getProperty("os.name");
    }

    public static boolean isWindows() {
        return getOsName().startsWith("Windows");
    }

}
