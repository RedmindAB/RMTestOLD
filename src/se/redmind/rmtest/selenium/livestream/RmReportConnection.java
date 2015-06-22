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
		return true;
	}
	
	public boolean close(){
		System.out.println("Closing connection to RMReport...");
		try {
			socket.close();
			out.close();
			System.out.println("Connection closed");
		} catch (IOException e) {
			System.err.println("Could not close socket...");
			return false;
		}
		return true;
	}
	
	public synchronized void sendMessage(String type, JsonObject message){
		out.println(type+"@"+new Gson().toJson(message));
		out.flush();
	}
	
	public synchronized void sendMessage(String message){
		out.println("message@"+message);
		out.flush();
	}
	
	public synchronized void sendMessage(String type, String message){
		out.println(type+"@"+message);
		out.flush();
	}
	
	public synchronized void sendSuiteFinished(){
		out.println("!suiteFinished@");
		out.flush();
	}
	
	public synchronized void sendClose(){
		out.println("!close@");
		out.flush();
	}
	
}
