package se.redmind.rmtest.selenium.grid.rules;

import se.redmind.rmtest.config.Configuration;
import se.redmind.rmtest.config.LocalConfiguration;

public class IgnoreWithPhantomJS implements ConditionalRule.IgnoreCondition {

    @Override
    public boolean isSatisfied() {
        Configuration configuration = Configuration.current();
        return configuration.runner instanceof LocalConfiguration && configuration.runner.as(LocalConfiguration.class).usePhantomJS;
    }

}
