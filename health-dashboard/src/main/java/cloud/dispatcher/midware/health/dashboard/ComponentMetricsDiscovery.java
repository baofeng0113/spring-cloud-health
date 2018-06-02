package cloud.dispatcher.midware.health.dashboard;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class ComponentMetricsDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentMetricsDiscovery.class);

    @Autowired private DiscoveryClient discoveryClient;

    public List<String> getServiceIdList() {
        List<String> serviceIdList = discoveryClient.getServices();
        if (CollectionUtils.isEmpty(serviceIdList)) {
            return Collections.emptyList();
        } else {
            Collections.sort(serviceIdList, String::compareTo);
            return serviceIdList;
        }
    }

    public List<ServiceInstance> getInstanceList(String serviceId) {
        List<ServiceInstance> result = discoveryClient.getInstances(serviceId);
        return CollectionUtils.isEmpty(result) ? Collections.emptyList() : result;
    }
}
