package cucumber.runtime.java;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.runtime.junit.JUnitReporter;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.*;
import se.redmind.utils.Fields;

/**
 * @author Jeremy Comte
 */
public class WrapperReporter implements Formatter, Reporter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Reporter reporter;
    private final Formatter formatter;

    public WrapperReporter(JUnitReporter reporter) {
        this.formatter = Fields.getSafeValue(reporter, "formatter");
        this.reporter = Fields.getSafeValue(reporter, "reporter");
    }

    @Override
    public void syntaxError(String state, String event, List<String> legalEvents, String uri, Integer line) {
//        reporter.syntaxError(state, event, legalEvents, uri, line);
    }

    @Override
    public void uri(String uri) {
//        reporter.uri(uri);
    }

    @Override
    public void feature(Feature feature) {
//        reporter.feature(feature);
    }

    @Override
    public void scenarioOutline(ScenarioOutline scenarioOutline) {
//        reporter.scenarioOutline(scenarioOutline);
    }

    @Override
    public void examples(Examples examples) {
//        reporter.examples(examples);
    }

    @Override
    public void startOfScenarioLifeCycle(Scenario scenario) {
//        reporter.startOfScenarioLifeCycle(scenario);
    }

    @Override
    public void background(Background background) {
//        reporter.background(background);
    }

    @Override
    public void scenario(Scenario scenario) {
//        reporter.scenario(scenario);
    }

    @Override
    public void step(Step step) {
        formatter.step(step);
    }

    @Override
    public void endOfScenarioLifeCycle(Scenario scenario) {
//        reporter.endOfScenarioLifeCycle(scenario);
    }

    @Override
    public void done() {
//        reporter.done();
    }

    @Override
    public void close() {
//        reporter.done();
    }

    @Override
    public void eof() {
//        reporter.eof();
    }

    @Override
    public void before(Match match, Result result) {
//        reporter.before(match, result);
    }

    @Override
    public void result(Result result) {
//        reporter.result(result);
    }

    @Override
    public void after(Match match, Result result) {
//        reporter.after(match, result);
    }

    @Override
    public void match(Match match) {
      //  reporter.match(match);
    }

    @Override
    public void embedding(String mimeType, byte[] data) {
//        reporter.embedding(mimeType, data);
    }

    @Override
    public void write(String text) {
    }

}
