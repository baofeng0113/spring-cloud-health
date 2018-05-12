package cloud.dispatcher.midware.health.client;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ComponentMetricsInfoMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentMetricsInfoMonitor.class);

    @Autowired private ComponentInstancesDiscovery componentInstancesDiscovery;

    @Autowired private ComponentMetricsInfoCapture componentMetricsInfoCapture;

    @Autowired private ComponentMetricsInfoPublish componentMetricsInfoPublish;

    @Async
    public void reportMetricsInfo() {
        List<ServiceInstance> instanceList = componentInstancesDiscovery.getLocalizeInstanceList();
        if (instanceList.isEmpty()) {
            LOGGER.warn("No running instance on the local");
        } else {
            instanceList.forEach(item -> {
                Map<String, Object> metrics = componentMetricsInfoCapture.captureMetricsInfo(item);
                if (!metrics.isEmpty()) {
                    LOGGER.info("Collect metrics completed, id: {}, host: {}, port: {}",
                            item.getServiceId(), item.getHost(), item.getPort());
                    componentMetricsInfoPublish.publishMetricsInfo(item.getServiceId(), metrics);
                }
            });
        }
    }
}
