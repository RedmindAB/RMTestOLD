package se.redmind.rmtest.selenium.livestream;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import se.redmind.rmtest.selenium.framework.config.FrameworkConfig;

public class LiveStreamListener extends RunListener {

    private volatile RmTestResultBuilder resBuilder;
    private final RmReportConnection rmrConnection;
    private volatile HashSet<String> finishedTests;
    private final boolean parrentRunner;
    private final List<LiveStreamListener> listeners;
    private volatile HashMap<String, Long> testStartTimes;

    public LiveStreamListener() {
        resBuilder = new RmTestResultBuilder();
        finishedTests = new HashSet<>();
        parrentRunner = true;
        listeners = new ArrayList<>();
        rmrConnection = new RmReportConnection();
        this.testStartTimes = new HashMap<>();
        addShutdownHook(rmrConnection);
    }

    private LiveStreamListener(RmTestResultBuilder resBuilder, RmReportConnection connection) {
        this.resBuilder = resBuilder;
        this.finishedTests = new HashSet<>();
        this.parrentRunner = false;
        this.listeners = new ArrayList<>();
        this.rmrConnection = connection;
    }

    @Override
    public void testRunStarted(Description description) throws Exception {
        initSuite(description, 0);
        rmrConnection.connect();
        rmrConnection.sendMessage("suite", resBuilder.build());
        super.testRunStarted(description);
    }

    @Override
    public void testStarted(Description description) throws Exception {
        resBuilder.addTest(description.getDisplayName());
        String id = resBuilder.getTest(description.getDisplayName()).get("id").getAsString();
        if (parrentRunner) {
            rmrConnection.sendMessage("testStart", id);
            testStartTimes.put(description.getDisplayName(), System.currentTimeMillis());
        }
        super.testStarted(description);
    }

    @Override
    public void testFinished(Description description) throws Exception {
        String displayName = description.getDisplayName();
        resBuilder.addFinishedTest(description.getDisplayName());
        finishedTests.add(displayName);
        if (parrentRunner) {
            double runTime = (double) (System.currentTimeMillis() - testStartTimes.get(displayName)) / 1000;
            resBuilder.addRunTime(displayName, runTime);
            rmrConnection.sendMessage("test", resBuilder.getTest(description.getDisplayName()));
        }
        super.testFinished(description);
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        String description = failure.getDescription().getDisplayName();
        resBuilder.addAssumptionFailure(description, failure);
        super.testAssumptionFailure(failure);
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        String description = failure.getDescription().getDisplayName();
        resBuilder.addTestFailure(description, failure);
        super.testFailure(failure);
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        resBuilder.addIgnoredTest(description.getDisplayName());
        rmrConnection.sendMessage("test", resBuilder.getTest(description.getDisplayName()));
        super.testIgnored(description);
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        if (parrentRunner) {
            resBuilder.setResult(result);
            JsonObject results = resBuilder.build();
            rmrConnection.sendSuiteFinished();
            rmrConnection.sendClose();
            rmrConnection.close();
            saveReport();
            super.testRunFinished(result);
        }
    }

    private void addShutdownHook(RmReportConnection con) {
        Runtime.getRuntime().addShutdownHook(new Thread(new LiveTestShutdownHook(con)));
    }

    private void saveReport() {
        String suitename = resBuilder.getSuiteName();
        String timestamp = resBuilder.getTimestamp();
        String savePath = FrameworkConfig.getConfig().getJsonReportSavePath();
        new File(savePath).mkdirs();
        String filename = suitename + "-" + timestamp + ".json";
        try {
            try (PrintWriter writer = new PrintWriter(savePath + "/" + filename, "UTF-8")) {
                writer.print(new GsonBuilder().setPrettyPrinting().create().toJson(resBuilder.build()));
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void initSuite(Description desc, int level) {
        level++;
        String suitename = System.getProperty("rmt.live.suitename");
        if (suitename != null && level == 1) {
            resBuilder.setSuiteName(suitename);
        } else if (level == 1 && desc.isSuite()) {
            resBuilder.setSuiteName(desc.getClassName());
        }
        if (desc.isTest()) {
            resBuilder.addTest(desc.getDisplayName());
        }
        ArrayList<Description> children = desc.getChildren();
        for (Description description : children) {
            initSuite(description, level);
        }
    }

    public LiveStreamListener getSubListener() {
        LiveStreamListener subListener = new LiveStreamListener(resBuilder, rmrConnection);
        listeners.add(subListener);
        return subListener;
    }

}
