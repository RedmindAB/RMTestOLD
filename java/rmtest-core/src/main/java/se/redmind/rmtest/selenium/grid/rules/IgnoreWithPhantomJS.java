package se.redmind.rmtest.selenium.grid.rules;

import se.redmind.rmtest.selenium.framework.config.FrameworkConfig;

public class IgnoreWithPhantomJS implements ConditionalRule.IgnoreCondition{
	 
		@Override
	    public boolean isSatisfied() {
	        return FrameworkConfig.getConfig().usePhantomJS();
	    }
}
