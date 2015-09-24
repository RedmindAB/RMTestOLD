package se.redmind.rmtest.selenium.livestream;

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
