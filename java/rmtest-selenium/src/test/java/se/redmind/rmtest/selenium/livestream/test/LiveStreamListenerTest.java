package se.redmind.rmtest.selenium.livestream.test;

import org.junit.Test;
import se.redmind.rmtest.selenium.livestream.LiveStreamListener;

/**
 * Created by victormattsson on 2016-02-02.
 */
public class LiveStreamListenerTest {


    LiveStreamListener listener = new LiveStreamListener();

    @Test
    public void test() throws Exception {
        listener.testRunStarted(DescriptionMocker.mockDescription());
    }

}
