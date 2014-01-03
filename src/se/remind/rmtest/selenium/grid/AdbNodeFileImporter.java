package se.remind.rmtest.selenium.grid;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AdbNodeFileImporter {
    private static ArrayList <AdbNodeContainer> nodeList = new ArrayList<AdbNodeContainer>();
    public AdbNodeFileImporter() {
        InputStream    fis;
        BufferedReader br;
        String         line;

        try {
            fis = new FileInputStream(GridConstatants.androidNodes);

            br = new BufferedReader(new InputStreamReader(fis));
            AdbNodeContainer currentNode;
            while ((line = br.readLine()) != null) {
                currentNode = new AdbNodeContainer(line);
                
                nodeList.add(currentNode);
                System.out.println(currentNode.toString());
            }
            // Done with the file
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        br = null;
        fis = null;
    }

    public static ArrayList<AdbNodeContainer> getNodeList() {
        return nodeList;
    }

}
