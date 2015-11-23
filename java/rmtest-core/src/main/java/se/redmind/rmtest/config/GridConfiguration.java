package se.redmind.rmtest.config;

import se.redmind.rmtest.DriverWrapper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import se.redmind.rmtest.selenium.grid.DescriptionBuilder;
import se.redmind.rmtest.selenium.grid.GridConstants;
import se.redmind.rmtest.selenium.grid.HubNodesStatus;

/**
 * @author Jeremy Comte
 */
@JsonTypeName("grid")
public class GridConfiguration extends DriverConfiguration<RemoteWebDriver> {

    @JsonProperty
    @NotNull
    public String hubIp;

    @JsonProperty
    public boolean enableLiveStream;

    public GridConfiguration() {
        super(new DesiredCapabilities());
    }

    @Override
    protected List<DriverWrapper<RemoteWebDriver>> createDrivers() {
        List<DriverWrapper<RemoteWebDriver>> instances = new ArrayList<>();
        HubNodesStatus nodeInfo = new HubNodesStatus(hubIp, GridConstants.hubPort);
        nodeInfo.getNodesAsRegReqs().forEach(nodeReq -> {
            nodeReq.getCapabilities().stream()
                .map(capabilities -> new DesiredCapabilities(capabilities))
                .forEach(capabilities -> {
                    try {
                        String driverDescription = DescriptionBuilder.buildDescriptionFromCapabilities(capabilities);
                        URL driverUrl = new URL("http://" + nodeReq.getConfigAsString("host") + ":" + nodeReq.getConfigAsString("port") + "/wd/hub");
                        instances.add(new DriverWrapper<>(generateCapabilities(), driverDescription, (otherCapabilities) -> {
                            otherCapabilities.asMap().forEach((key, value) -> capabilities.setCapability(key, value));
                            return new RemoteWebDriver(driverUrl, capabilities);
                        }));
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                });
        });
        return instances;
    }
}
