package se.redmind.rmtest.selenium.grid;




 
public class RegisterNode {
    
//    static String hubHost = "localhost";
//    static int hubPort = 4444;
 
    public static void main(String[] args)  {
        String nodeHost = "10.12.11.225";
        String hubHost = RMTestConfig.getHubHost();
        
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-hub")) {
                hubHost = args[i+1];
            } else if(args[i].contains("-node")){
                nodeHost = args[i+1];
            } else if (args[i].contains("-help")) {
                System.out.println("Usage: ");
                System.out.println("-help : prints this message");
                System.out.println("-hub : ip for the selenium hub");
                System.out.println("-node : ip for the selenium nodes(default 10.12.11.225)");
                return;
            }
        }
//        String nodeHost = GridConstatants.hubHost;
//        String nodePort = "localhost";
        AdbNodeFileImporter nodeInfo = new AdbNodeFileImporter();
//        ArrayList<AndroidNodeRegistrator> nodes = new ArrayList<AndroidNodeRegistrator>();
        AndroidNodeRegistrator currentNode;
        for (int i = 0; i < nodeInfo.getNodeList().size(); i++) {
            currentNode = new AndroidNodeRegistrator();
            try {
                currentNode.regNodeInfo(hubHost, nodeHost, nodeInfo.getNodeList().get(i));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            currentNode.sendRegistrationReq();
        }
    }
}