package se.redmind.rmtest.selenium.livestream;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import se.redmind.rmtest.selenium.grid.RmConfig;

public class RmReportConnection {
	
	private Socket socket;
	private String rmrLiveAddress;
	private int rmrLivePort;
	private PrintWriter out;
	private boolean isConnected;
	
	public RmReportConnection() {
		this.rmrLiveAddress = RmConfig.getRMRLiveAddress();
		this.rmrLivePort = RmConfig.getRMRLivePort();
	}
	
	public boolean connect(){
		System.out.println("Connecting to RMReport...");
		try {
			socket = new Socket(rmrLiveAddress, rmrLivePort);
			out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			System.err.println("Could not connect to RMReport with: "+rmrLiveAddress+":"+rmrLivePort);
			System.err.println("RMReport might not be online or the config is not correct...");
			return false;
		}
		System.out.println("Connection etablished.");
		isConnected = true;
		return true;
	}
	
	public boolean close(){
		System.out.println("Closing connection to RMReport...");
		try {
			socket.close();
			out.close();
			System.out.println("Connection closed");
		} catch (NullPointerException | IOException e) {
			System.err.println("Could not close socket...");
			return false;
		}
		return true;
	}
	
	public synchronized void sendMessage(String type, JsonObject message){
		send(type+"@"+new Gson().toJson(message));
	}
	
	public synchronized void sendMessage(String message){
		send("message@"+message);
	}
	
	public synchronized void sendMessage(String type, String message){
		send(type+"@"+message);
	}
	
	public synchronized void sendSuiteFinished(){
		send("!suiteFinished@");
	}
	
	public synchronized void sendClose(){
		send("!close@");
	}
	
	private synchronized void send(String message){
		try {
			out.println(message);
			out.flush();
		} catch (NullPointerException e) {
			isConnected = false;
		}
	}
	
	public boolean isConnected(){
		return isConnected;
	}
	
}
