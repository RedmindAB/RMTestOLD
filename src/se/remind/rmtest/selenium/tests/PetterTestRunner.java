package se.remind.rmtest.selenium.tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class PetterTestRunner {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
//        AbTestConfig.main();
        JUnitCore junit = new JUnitCore();
        Result result = junit.run(AllTests.class);   
        System.out.println(result.getFailureCount());
        }

}
