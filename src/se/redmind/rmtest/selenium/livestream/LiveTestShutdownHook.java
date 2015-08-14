package se.redmind.rmtest.selenium.livestream;

import se.redmind.rmtest.selenium.grid.DriverProvider;

public class LiveTestShutdownHook implements Runnable {

	private RmReportConnection con;

	public LiveTestShutdownHook(RmReportConnection con) {
		this.con = con;
	}
	
	@Override
	public void run() {
		con.sendSuiteFinished();
		con.sendClose();
		con.close();
	}

}
