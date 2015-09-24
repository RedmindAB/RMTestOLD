package se.redmind.rmtest.selenium.livestream;

import se.redmind.rmtest.selenium.framework.config.FrameworkConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class RmReportConnection {

    private static final Logger LOG = LoggerFactory.getLogger(RmReportConnection.class);

    private Socket socket;
    private String rmrLiveAddress;
    private int rmrLivePort;
    private PrintWriter out;
    private boolean isConnected;

    public RmReportConnection() {
        final FrameworkConfig config = FrameworkConfig.getConfig();
        this.rmrLiveAddress = config.getRMRLiveAddress();
        this.rmrLivePort = config.getRMRLivePort();
    }

    public boolean connect() {
        LOG.debug("Connecting to RMReport...");
        try {
            socket = new Socket(rmrLiveAddress, rmrLivePort);
            out = new PrintWriter(socket.getOutputStream());
        }
        catch(IOException e) {
            LOG.warn("Could not connect to RMReport with: " + rmrLiveAddress + ":" + rmrLivePort);
            LOG.warn("RMReport might not be online or the config is not correct...");
            return false;
        }
        LOG.debug("Connection etablished.");
        isConnected = true;
        return true;
    }

    public boolean close() {
        LOG.debug("Closing connection to RMReport...");
        try {
            socket.close();
            out.close();
            LOG.debug("Connection closed");
        }
        catch(NullPointerException | IOException e) {
            LOG.warn("Could not close socket...");
            return false;
        }
        return true;
    }

    public synchronized void sendMessage(String type, JsonObject message) {
        send(type + "@" + new Gson().toJson(message));
    }

    public synchronized void sendMessage(String message) {
        send("message@" + message);
    }

    public synchronized void sendMessage(String type, String message) {
        send(type + "@" + message);
    }

    public synchronized void sendSuiteFinished() {
        send("!suiteFinished@");
    }

    public synchronized void sendClose() {
        send("!close@");
    }

    private synchronized void send(String message) {
        try {
            out.println(message);
            out.flush();
        }
        catch(NullPointerException e) {
            isConnected = false;
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}
